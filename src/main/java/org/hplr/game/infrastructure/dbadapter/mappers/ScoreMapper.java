package org.hplr.game.infrastructure.dbadapter.mappers;

import org.hplr.elo.core.model.vo.Score;
import org.hplr.elo.core.usecases.port.dto.ScoreDto;
import org.hplr.game.infrastructure.dbadapter.entities.GameTurnScoreEntity;

public class ScoreMapper {

    private ScoreMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static GameTurnScoreEntity fromSnapshot(Score score){
        return new GameTurnScoreEntity(
                null,
                score.turnNumber(),
                score.turnScore(),
                score.tabled()
        );
    }

    public static ScoreDto fromEntity(GameTurnScoreEntity score){
        return new ScoreDto(
                score.getTurnNumber(),
                score.getTurnScore(),
                score.getTabled()
        );
    }
}
