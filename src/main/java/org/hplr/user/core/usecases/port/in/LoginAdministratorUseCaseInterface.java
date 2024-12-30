package org.hplr.user.core.usecases.port.in;

import org.hplr.library.exception.HPLRIllegalStateException;
import org.hplr.library.exception.HPLRValidationException;
import org.hplr.user.core.usecases.port.dto.AdministratorLoginDto;
import org.hplr.user.core.usecases.port.dto.GetTokenResponseDto;

import java.util.NoSuchElementException;

public interface LoginAdministratorUseCaseInterface {
    GetTokenResponseDto loginAdministrator(AdministratorLoginDto administratorLoginDto) throws NoSuchElementException, HPLRValidationException, HPLRIllegalStateException;
}
