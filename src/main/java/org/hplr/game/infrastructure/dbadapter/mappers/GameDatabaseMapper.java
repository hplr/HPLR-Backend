package org.hplr.game.infrastructure.dbadapter.mappers;

import org.hplr.game.core.model.GameSnapshot;
import org.hplr.game.core.model.vo.GameDeployment;
import org.hplr.game.core.model.vo.GameMission;
import org.hplr.game.core.model.vo.GameSideSnapshot;
import org.hplr.game.core.usecases.port.dto.GameSelectDto;
import org.hplr.game.core.usecases.port.dto.GameSideSelectDto;
import org.hplr.game.infrastructure.dbadapter.entities.*;
import org.hplr.location.infrastructure.dbadapter.mapper.LocationMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameDatabaseMapper {

    public static GameEntity fromSnapshot(GameSnapshot gameSnapshot) {
        return new GameEntity(
                null,
                null,
                null,
                gameSnapshot.gameId().gameId(),
                null,
                null,
                null,
                gameSnapshot.gameData().gamePointSize(),
                gameSnapshot.gameData().gameTurnLength(),
                gameSnapshot.gameData().gameTimeLength().toHoursPart(),
                gameSnapshot.gameData().gameStartTime(),
                gameSnapshot.gameData().gameEndTime(),
                gameSnapshot.gameData().ranking(),
                gameSnapshot.gameStatus(),
                null,
                null
        );


    }

    public static GameSelectDto toDto(GameEntity gameEntity) {
        GameSideSelectDto secondSide = null;
        if (Objects.nonNull(gameEntity.getSecondGameSide())) {
            secondSide = GameSideDatabaseMapper.fromEntity(gameEntity.getSecondGameSide());

        }
        return new GameSelectDto(
                gameEntity.getGameId(),
                LocationMapper.fromEntity(gameEntity.getLocationEntity()),
                new GameMission(gameEntity.getGameMissionEntity().getName()),
                new GameDeployment(gameEntity.getGameDeploymentEntity().getName()),
                gameEntity.getGamePointSize(),
                gameEntity.getGameTurnLength(),
                gameEntity.getGameHoursDuration(),
                gameEntity.getGameStartTime(),
                gameEntity.getGameEndTime(),
                gameEntity.getRanking(),
                gameEntity.getStatus(),
                GameSideDatabaseMapper.fromEntity(gameEntity.getFirstGameSide()),
                secondSide
                );
    }

    public static List<GameTurnScoreEntity> mapScore(GameSideSnapshot gameSide) {
        List<GameTurnScoreEntity> turnScoreEntityList = new ArrayList<>();
        gameSide.scorePerTurnList().forEach(
                score -> turnScoreEntityList.add(
                    ScoreMapper.fromSnapshot(score)
                )
        );
        return turnScoreEntityList;
    }


    private GameDatabaseMapper() {
        throw new IllegalStateException("Utility class");
    }
}
