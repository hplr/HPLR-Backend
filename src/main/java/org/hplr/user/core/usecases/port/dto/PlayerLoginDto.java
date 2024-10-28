package org.hplr.user.core.usecases.port.dto;

public record PlayerLoginDto(
        String email,
        String passwordPlain
) {
}
