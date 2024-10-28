package org.hplr.user.core.usecases.port.out.query;

import org.hplr.user.core.usecases.port.dto.PlayerSelectDto;
import org.hplr.library.exception.HPLRValidationException;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public interface SelectPlayerByUserIdQueryInterface {
    Optional<PlayerSelectDto> selectPlayerByUserId(UUID userId) throws NoSuchElementException, HPLRValidationException;
}
