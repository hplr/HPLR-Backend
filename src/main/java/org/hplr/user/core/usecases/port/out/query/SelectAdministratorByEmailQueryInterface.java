package org.hplr.user.core.usecases.port.out.query;

import org.hplr.user.core.usecases.port.dto.AdministratorSelectDto;

import java.util.Optional;

public interface SelectAdministratorByEmailQueryInterface {
    Optional<AdministratorSelectDto> selectAdministratorByEmail(String email);
}
