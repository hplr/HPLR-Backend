package org.hplr.infrastructure.dbadapter.mappers;

import org.hplr.core.model.GameSnapshot;
import org.hplr.core.model.vo.GameDeployment;
import org.hplr.core.model.vo.GameMission;
import org.hplr.core.model.vo.GameSideSnapshot;
import org.hplr.core.usecases.port.dto.GameSelectDto;
import org.hplr.core.usecases.port.dto.GameSideSelectDto;
import org.hplr.infrastructure.dbadapter.entities.*;
import org.hplr.infrastructure.dbadapter.mapper.LocationMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class GameDatabaseMapper {

    public static GameEntity fromSnapshot(GameSnapshot gameSnapshot, LocationEntity locationEntity, GameMissionEntity gameMissionEntity, GameDeploymentEntity gameDeploymentEntity, List<PlayerEntity> playerEntityList, List<GameArmyTypeEntity> gameArmyTypeEntityList) {


        List<GameTurnScoreEntity> firstSideTurnScoreEntityList = mapScore(gameSnapshot.firstGameSide());
        List<GamePlayerDataEntity> firstSideGamePlayerDataEntityList = mapGamePlayerDataEntityList(gameSnapshot.firstGameSide(), playerEntityList, gameArmyTypeEntityList);
        GameEntity gameEntity = new GameEntity(
                null,
                null,
                null,
                gameSnapshot.gameId().gameId(),
                locationEntity,
                gameMissionEntity,
                gameDeploymentEntity,
                gameSnapshot.gameData().gamePointSize(),
                gameSnapshot.gameData().gameTurnLength(),
                gameSnapshot.gameData().gameTimeLength().toHoursPart(),
                gameSnapshot.gameData().gameStartTime(),
                gameSnapshot.gameData().gameEndTime(),
                gameSnapshot.gameData().ranking(),
                gameSnapshot.gameStatus(),
                new GameSideEntity(
                        null,
                        gameSnapshot.firstGameSide().sideId().sideId(),
                        gameSnapshot.firstGameSide().allegiance(),
                        firstSideGamePlayerDataEntityList,
                        gameSnapshot.firstGameSide().isFirst(),
                        firstSideTurnScoreEntityList
                ),
                null
        );
        if (Objects.nonNull(gameSnapshot.secondGameSide())) {
            List<GameTurnScoreEntity> secondSideTurnScoreEntityList = mapScore(gameSnapshot.secondGameSide());
            List<GamePlayerDataEntity> secondSideGamePlayerDataEntityList = mapGamePlayerDataEntityList(gameSnapshot.secondGameSide(), playerEntityList, gameArmyTypeEntityList);
            GameSideEntity secondGameSideEntity = new GameSideEntity(
                    null,
                    gameSnapshot.secondGameSide().sideId().sideId(),
                    gameSnapshot.secondGameSide().allegiance(),
                    secondSideGamePlayerDataEntityList,
                    gameSnapshot.secondGameSide().isFirst(),
                    secondSideTurnScoreEntityList
            );
            gameEntity.setSecondGameSide(secondGameSideEntity);
        }
        return gameEntity;


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

    private static List<GameTurnScoreEntity> mapScore(GameSideSnapshot gameSide) {
        List<GameTurnScoreEntity> turnScoreEntityList = new ArrayList<>();
        gameSide.scorePerTurnList().forEach(
                score -> turnScoreEntityList.add(
                        new GameTurnScoreEntity(
                                null,
                                score.index(),
                                score.scoreValue()

                        )
                )
        );
        return turnScoreEntityList;
    }

    private static List<GamePlayerDataEntity> mapGamePlayerDataEntityList(GameSideSnapshot gameSide, List<PlayerEntity> playerEntityList, List<GameArmyTypeEntity> gameArmyTypeEntityList) {
        List<GamePlayerDataEntity> gamePlayerDataEntityList = new ArrayList<>();
        gameSide.gameSidePlayerDataList().forEach(gameSidePlayerData ->
                {
                    PlayerEntity playerEntity = playerEntityList.stream().filter(playerEntity1 -> playerEntity1.getUserId().equals(gameSidePlayerData.player().userId().id())).findFirst().orElseThrow(NoSuchElementException::new);
                    GameArmyTypeEntity primaryGameArmyTypeEntity = gameArmyTypeEntityList.stream().filter(gameArmyTypeEntity -> gameArmyTypeEntity.getName().equals(gameSidePlayerData.armyPrimary().army().name())).findFirst().orElseThrow(NoSuchElementException::new);
                    List<GameArmyEntity> allyArmyEntityList;
                    if (Objects.nonNull(gameSidePlayerData.allyArmyList())) {
                        allyArmyEntityList = new ArrayList<>();
                        gameSidePlayerData.allyArmyList().forEach(allyArmy -> {
                            GameArmyTypeEntity allyArmyTypeEntity = gameArmyTypeEntityList.stream().filter(gameArmyTypeEntity -> gameArmyTypeEntity.getName().equals(allyArmy.name())).findFirst().orElseThrow(NoSuchElementException::new);
                            allyArmyEntityList.add(new GameArmyEntity(
                                    null,
                                    allyArmyTypeEntity,
                                    allyArmy.name(),
                                    allyArmy.pointValue()

                            ));
                        });
                    } else {
                        allyArmyEntityList = null;
                    }
                    gamePlayerDataEntityList.add(
                            new GamePlayerDataEntity(
                                    null,
                                    playerEntity,
                                    playerEntity.getScore(),
                                    new GameArmyEntity(
                                            null,
                                            primaryGameArmyTypeEntity,
                                            gameSidePlayerData.armyPrimary().name(),
                                            gameSidePlayerData.armyPrimary().pointValue()
                                    ),
                                    allyArmyEntityList

                            )
                    );
                }
        );
        return gamePlayerDataEntityList;
    }

    private GameDatabaseMapper() {
        throw new IllegalStateException("Utility class");
    }
}
