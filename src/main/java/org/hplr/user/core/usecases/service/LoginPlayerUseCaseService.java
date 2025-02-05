package org.hplr.user.core.usecases.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hplr.library.core.model.StringValidator;
import org.hplr.user.core.model.Player;
import org.hplr.user.core.usecases.port.dto.GetTokenResponseDto;
import org.hplr.user.core.usecases.port.dto.PlayerLoginDto;
import org.hplr.user.core.usecases.port.dto.PlayerSelectDto;
import org.hplr.user.core.usecases.port.in.LoginPlayerUseCaseInterface;
import org.hplr.user.core.usecases.port.out.command.SaveLastLoginDateCommandInterface;
import org.hplr.user.core.usecases.port.out.query.SelectPlayerByEmailQueryInterface;
import org.hplr.library.core.util.ConstValues;
import org.hplr.library.exception.HPLRIllegalStateException;
import org.hplr.library.exception.HPLRValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LoginPlayerUseCaseService implements LoginPlayerUseCaseInterface {

    final SelectPlayerByEmailQueryInterface selectPlayerByEmailQueryInterface;
    final SaveLastLoginDateCommandInterface saveLastLoginDateCommandInterface;

    @Value("${jwt.secret}")
    @Getter
    private String secret;

    @Override
    public GetTokenResponseDto loginPlayer(PlayerLoginDto playerLoginDto) throws NoSuchElementException, HPLRValidationException, HPLRIllegalStateException {
        StringValidator.validateString(secret);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Optional<PlayerSelectDto> playerSelectDtoOptional =
                selectPlayerByEmailQueryInterface.selectPlayerByEmail(playerLoginDto.email());

        Player player = Player.fromDto(playerSelectDtoOptional.orElseThrow(NoSuchElementException::new));


        if(bCryptPasswordEncoder.matches(playerLoginDto.passwordPlain(), player.getSecurity().pwHash())){
            saveLastLoginDateCommandInterface.saveLastLoginDate(LocalDateTime.now(), player.getUserId().id());
            Date currentDate = Date.from(Instant.now());
            return new GetTokenResponseDto(Jwts.builder()
                    .claim("id", player.getUserId().id())
                    .claim("role", "PLAYER")
                    .setSubject("hplr")
                    .setIssuedAt(currentDate)
                    .setExpiration(Date.from(Instant.now().plus(ConstValues.TOKEN_DURATION, ChronoUnit.MINUTES)))
                    .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                    .compact());
        }
        else throw new HPLRValidationException("Wrong password!");

    }
}
