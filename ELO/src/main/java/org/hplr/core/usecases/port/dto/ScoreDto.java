package org.hplr.core.usecases.port.dto;

public record ScoreDto(
        Long turnNumber,
        Long turnScore,
        Boolean tabled
) {
}
