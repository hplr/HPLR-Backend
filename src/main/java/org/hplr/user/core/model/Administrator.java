package org.hplr.user.core.model;

import lombok.Getter;
import org.hplr.user.core.model.vo.AdministratorSecurity;
import org.hplr.user.core.model.vo.UserData;
import org.hplr.user.core.model.vo.UserId;
import org.hplr.user.core.usecases.port.dto.AdministratorSelectDto;
import org.hplr.user.core.usecases.service.dto.InitialAdministratorSaveDto;

import java.util.UUID;

@Getter
public class Administrator extends User {

    private final AdministratorSecurity administratorSecurity;

    private Administrator(UserId userId, UserData userData, AdministratorSecurity administratorSecurity) {
        super(userId, userData);
        this.administratorSecurity = administratorSecurity;
    }

    public static Administrator fromInitialDto(InitialAdministratorSaveDto initialAdministratorSaveDto) {
        return new Administrator(
                new UserId(UUID.randomUUID()),
                new UserData(
                        initialAdministratorSaveDto.initialPlayerSaveDataDto().name(),
                        initialAdministratorSaveDto.initialPlayerSaveDataDto().nickname(),
                        initialAdministratorSaveDto.initialPlayerSaveDataDto().email(),
                        initialAdministratorSaveDto.initialPlayerSaveDataDto().motto()
                ),
                initialAdministratorSaveDto.administratorSecurity()
        );
    }

    public static Administrator fromSelectDto(AdministratorSelectDto administratorSelectDto){
        return new Administrator(
                new UserId(administratorSelectDto.administratorId()),
                new UserData(
                        administratorSelectDto.name(),
                        administratorSelectDto.nickname(),
                        administratorSelectDto.email(),
                        administratorSelectDto.motto()
                ),
                new AdministratorSecurity(
                        administratorSelectDto.pwHash(),
                        administratorSelectDto.registrationTime(),
                        administratorSelectDto.lastLogin(),
                        administratorSelectDto.roleList()
                )
        );
    }

    public AdministratorSnapshot toSnapshot(){
        return new AdministratorSnapshot(this);
    }
}
