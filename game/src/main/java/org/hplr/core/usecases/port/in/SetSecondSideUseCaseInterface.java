package org.hplr.core.usecases.port.in;

import org.hplr.core.usecases.port.dto.CreatedGameSaveSecondSideDto;

import java.util.UUID;

public interface SetSecondSideUseCaseInterface {
    UUID setSecondSideForGame(CreatedGameSaveSecondSideDto createdGameSaveSecondSideDto);
}
