package org.hplr.core.usecases.port.in;

import org.hplr.core.usecases.port.dto.InitialGameSaveDataDto;
import org.hplr.exception.LocationCalculationException;

import java.util.UUID;

public interface SaveGameUseCaseInterface {
    UUID saveGame(InitialGameSaveDataDto initialGameSaveDataDto) throws LocationCalculationException;
}
