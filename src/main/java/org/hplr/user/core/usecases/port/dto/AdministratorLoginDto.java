package org.hplr.user.core.usecases.port.dto;

public record AdministratorLoginDto(
        String email,
        String passwordPlain
) {
}
