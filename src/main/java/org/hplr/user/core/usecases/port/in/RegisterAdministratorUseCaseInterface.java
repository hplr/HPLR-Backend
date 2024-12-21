package org.hplr.user.core.usecases.port.in;

import org.hplr.user.core.usecases.port.dto.InitialAdministratorSaveDataDto;

import java.util.UUID;

public interface RegisterAdministratorUseCaseInterface {
    UUID registerAdministrator(InitialAdministratorSaveDataDto initialAdministratorSaveDataDto);
}
