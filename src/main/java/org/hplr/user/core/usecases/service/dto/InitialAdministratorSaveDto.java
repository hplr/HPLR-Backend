package org.hplr.user.core.usecases.service.dto;

import org.hplr.user.core.model.vo.AdministratorSecurity;
import org.hplr.user.core.usecases.port.dto.InitialAdministratorSaveDataDto;

public record InitialAdministratorSaveDto(
        InitialAdministratorSaveDataDto initialPlayerSaveDataDto,
        AdministratorSecurity administratorSecurity

) {
}
