package org.hplr.user.core.usecases.port.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PlayerSelectDto(
        UUID playerId,
        String name,
        String nickname,
        String email,
        String motto,
        Long score,
        String pwHash,
        LocalDateTime registrationTime,
        LocalDateTime lastLogin
) {
}
