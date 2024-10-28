package org.hplr.game.core.usecases.port.in;

import org.hplr.game.core.usecases.port.dto.InitialGameSaveDataDto;
import org.hplr.library.exception.HPLRIllegalStateException;
import org.hplr.library.exception.LocationCalculationException;

import java.util.UUID;

public interface SaveGameUseCaseInterface {
    UUID saveGame(InitialGameSaveDataDto initialGameSaveDataDto) throws LocationCalculationException, IllegalArgumentException, HPLRIllegalStateException;
}
