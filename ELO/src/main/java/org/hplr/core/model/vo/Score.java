package org.hplr.core.model.vo;

import org.hplr.core.usecases.port.dto.ScoreDto;

public record Score(
        Long turnNumber,
        Long turnScore,
        Boolean tabled
) {
    public static Score fromDto(ScoreDto scoreDto){
        return new Score(
                scoreDto.turnNumber(),
                scoreDto.turnScore(),
                scoreDto.tabled()
        );
    }
}
