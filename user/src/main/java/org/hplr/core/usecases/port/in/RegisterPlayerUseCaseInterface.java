package org.hplr.core.usecases.port.in;

import org.hplr.core.usecases.port.dto.InitialPlayerSaveDataDto;

import java.util.UUID;

public interface RegisterPlayerUseCaseInterface {
    UUID registerPlayer (InitialPlayerSaveDataDto initialPlayerSaveDataDto);
}
