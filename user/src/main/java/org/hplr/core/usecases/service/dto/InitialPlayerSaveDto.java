package org.hplr.core.usecases.service.dto;

import org.hplr.core.model.vo.PlayerSecurity;
import org.hplr.core.model.vo.UserId;
import org.hplr.core.usecases.port.dto.InitialPlayerSaveDataDto;

public record InitialPlayerSaveDto(
        InitialPlayerSaveDataDto initialPlayerSaveDataDto,
        UserId userId,
        PlayerSecurity playerSecurity

) {
}
