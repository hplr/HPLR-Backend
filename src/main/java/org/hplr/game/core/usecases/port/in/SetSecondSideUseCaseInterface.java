package org.hplr.game.core.usecases.port.in;

import org.hplr.game.core.usecases.port.dto.CreatedGameSaveSecondSideDto;

import java.util.UUID;

public interface SetSecondSideUseCaseInterface {
    UUID setSecondSideForGame(CreatedGameSaveSecondSideDto createdGameSaveSecondSideDto);
}
