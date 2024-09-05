package org.hplr.core.usecases.port.dto;

public record InitialPlayerSaveDataDto(
        String name,
        String nickname,
        String email,
        String motto,
        String password,
        String repeatedPassword
) {
}
