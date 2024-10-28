package org.hplr.user.core.usecases.port.in;

import org.hplr.user.core.model.PlayerSnapshot;
import org.hplr.library.exception.HPLRValidationException;

import java.util.NoSuchElementException;
import java.util.UUID;

public interface GetPlayerUseCaseInterface {
    PlayerSnapshot getPlayerByUserId(UUID userId) throws NoSuchElementException, HPLRValidationException;
}
