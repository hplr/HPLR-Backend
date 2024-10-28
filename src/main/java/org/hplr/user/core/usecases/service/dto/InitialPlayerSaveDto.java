package org.hplr.user.core.usecases.service.dto;

import org.hplr.user.core.model.vo.PlayerSecurity;
import org.hplr.user.core.model.vo.UserId;
import org.hplr.user.core.usecases.port.dto.InitialPlayerSaveDataDto;

public record InitialPlayerSaveDto(
        InitialPlayerSaveDataDto initialPlayerSaveDataDto,
        UserId userId,
        PlayerSecurity playerSecurity

) {
}
