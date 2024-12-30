package org.hplr.user.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.user.core.model.Administrator;
import org.hplr.user.core.model.AdministratorSnapshot;
import org.hplr.user.core.model.vo.AdministratorSecurity;
import org.hplr.user.core.usecases.port.dto.InitialAdministratorSaveDataDto;
import org.hplr.user.core.usecases.port.in.RegisterAdministratorUseCaseInterface;
import org.hplr.user.core.usecases.port.out.command.SaveAdministratorDataCommandInterface;
import org.hplr.user.core.usecases.service.dto.InitialAdministratorSaveDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RegisterAdministratorUseCaseService implements RegisterAdministratorUseCaseInterface {

    private final SaveAdministratorDataCommandInterface saveAdministratorDataCommandInterface;
    @Override
    public UUID registerAdministrator(InitialAdministratorSaveDataDto initialAdministratorSaveDataDto) {
        String pwHash = generatePasswordHash(initialAdministratorSaveDataDto.password());
        AdministratorSecurity administratorSecurity = new AdministratorSecurity(
                        pwHash,
                        LocalDateTime.now(),
                        null,
                        initialAdministratorSaveDataDto.roleList()

        );
        InitialAdministratorSaveDto initialAdministratorSaveDto = new InitialAdministratorSaveDto(
                initialAdministratorSaveDataDto,
                administratorSecurity
        );
        Administrator administrator = Administrator.fromInitialDto(initialAdministratorSaveDto);
        AdministratorSnapshot administratorSnapshot = administrator.toSnapshot();
        saveAdministratorDataCommandInterface.saveAdministrator(administratorSnapshot);
        return administratorSnapshot.userId().id();
    }

    private String generatePasswordHash(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }
}
