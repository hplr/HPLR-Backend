package org.hplr.core.usecases.port.in;

import org.hplr.core.model.PlayerSnapshot;
import org.hplr.exception.HPLRValidationException;

import java.util.NoSuchElementException;
import java.util.UUID;

public interface GetPlayerUseCaseInterface {
    PlayerSnapshot getPlayerByUserId(UUID userId) throws NoSuchElementException, HPLRValidationException;
}
