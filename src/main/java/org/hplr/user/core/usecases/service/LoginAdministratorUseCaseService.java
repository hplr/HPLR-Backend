package org.hplr.user.core.usecases.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hplr.library.core.model.StringValidator;
import org.hplr.library.core.util.ConstValues;
import org.hplr.library.exception.HPLRIllegalStateException;
import org.hplr.library.exception.HPLRValidationException;
import org.hplr.user.core.model.Administrator;
import org.hplr.user.core.usecases.port.dto.*;
import org.hplr.user.core.usecases.port.in.LoginAdministratorUseCaseInterface;
import org.hplr.user.core.usecases.port.out.command.SaveLastAdministratorLoginDateCommandInterface;
import org.hplr.user.core.usecases.port.out.query.SelectAdministratorByEmailQueryInterface;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LoginAdministratorUseCaseService implements LoginAdministratorUseCaseInterface {

    final SelectAdministratorByEmailQueryInterface selectAdministratorByEmailQueryInterface;
    final SaveLastAdministratorLoginDateCommandInterface saveLastAdministratorLoginDateCommandInterface;

    @Value("${jwt.secret}")
    @Getter
    private String secret;

    @Override
    public GetTokenResponseDto loginAdministrator(AdministratorLoginDto administratorLoginDto) throws NoSuchElementException, HPLRValidationException, HPLRIllegalStateException {
        StringValidator.validateString(secret);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Optional<AdministratorSelectDto> administratorSelectDtoOptional =
                selectAdministratorByEmailQueryInterface.selectAdministratorByEmail(administratorLoginDto.email());

        Administrator administrator = Administrator.fromSelectDto(administratorSelectDtoOptional.orElseThrow(NoSuchElementException::new));


        if(bCryptPasswordEncoder.matches(administratorLoginDto.passwordPlain(), administrator.getAdministratorSecurity().pwHash())){
            saveLastAdministratorLoginDateCommandInterface.saveLastLoginDate(LocalDateTime.now(), administrator.getUserId().id());
            Date currentDate = Date.from(Instant.now());
            return new GetTokenResponseDto(Jwts.builder()
                    .claim("id", administrator.getUserId().id())
                    .claim("role", "ADMINISTRATOR")
                    .claim("permissions", administrator.getAdministratorSecurity()
                            .roleList()
                            .stream()
                            .map(role->role.name()+",")
                            .collect(Collectors.joining()))
                    .setSubject("hplr")
                    .setIssuedAt(currentDate)
                    .setExpiration(Date.from(Instant.now().plus(ConstValues.TOKEN_DURATION, ChronoUnit.MINUTES)))
                    .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                    .compact());
        }
        else throw new HPLRValidationException("Wrong password!");

    }
}
