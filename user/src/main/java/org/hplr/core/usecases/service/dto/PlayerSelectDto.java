package org.hplr.core.usecases.service.dto;

import java.util.UUID;

public record PlayerSelectDto(
        UUID playerId,
        String name
) {
}
