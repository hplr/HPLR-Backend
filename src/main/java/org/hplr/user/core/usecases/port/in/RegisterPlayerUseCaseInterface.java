package org.hplr.user.core.usecases.port.in;

import org.hplr.user.core.usecases.port.dto.InitialPlayerSaveDataDto;

import java.util.UUID;

public interface RegisterPlayerUseCaseInterface {
    UUID registerPlayer (InitialPlayerSaveDataDto initialPlayerSaveDataDto);
}
