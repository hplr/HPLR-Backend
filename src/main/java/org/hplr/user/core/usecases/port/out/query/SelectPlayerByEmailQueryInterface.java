package org.hplr.user.core.usecases.port.out.query;

import org.hplr.user.core.usecases.port.dto.PlayerSelectDto;

import java.util.Optional;

public interface SelectPlayerByEmailQueryInterface {
    Optional<PlayerSelectDto> selectPlayerByEmail(String email);
}
