package org.hplr.user.infrastructure.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.hplr.user.core.usecases.port.dto.*;
import org.hplr.user.core.usecases.port.in.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/administrator")
@Tag(name = "Administrator")
public class RESTAdministratorController {

    final RegisterAdministratorUseCaseInterface administratorUseCase;
    final LoginAdministratorUseCaseInterface loginAdministratorUseCaseInterface;


    @PostMapping(path="/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UUID> registerAdministrator(@RequestBody InitialAdministratorSaveDataDto initialAdministratorSaveDataDto) {
        return new ResponseEntity<>(administratorUseCase.registerAdministrator(initialAdministratorSaveDataDto), HttpStatus.OK);
    }

    @PostMapping(path="/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetTokenResponseDto> loginPlayer(@RequestBody AdministratorLoginDto administratorLoginDto) throws NoSuchElementException {
        return new ResponseEntity<>(loginAdministratorUseCaseInterface.loginAdministrator(administratorLoginDto), HttpStatus.OK);
    }
}
