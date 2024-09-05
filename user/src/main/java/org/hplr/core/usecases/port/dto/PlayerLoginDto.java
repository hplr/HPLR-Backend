package org.hplr.core.usecases.port.dto;

public record PlayerLoginDto(
        String email,
        String passwordPlain
) {
}
