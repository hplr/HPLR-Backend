package org.hplr.infrastructure.dbadapter.mappers;

import org.hplr.core.model.GameSide;
import org.hplr.core.model.GameSnapshot;
import org.hplr.core.model.vo.GameDeployment;
import org.hplr.core.model.vo.GameMission;
import org.hplr.core.usecases.port.dto.GameSelectDto;
import org.hplr.infrastructure.dbadapter.entities.*;
import org.hplr.infrastructure.dbadapter.mapper.LocationMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class GameDatabaseMapper {

    public static GameEntity toEntity(GameSnapshot gameSnapshot, LocationEntity locationEntity, GameMissionEntity gameMissionEntity, GameDeploymentEntity gameDeploymentEntity, List<PlayerEntity> playerEntityList, List<GameArmyTypeEntity> gameArmyTypeEntityList) {

        List<GameTurnScoreEntity> firstSideTurnScoreEntityList = mapScore(gameSnapshot.firstGameSide());
        List<GameTurnScoreEntity> secondSideTurnScoreEntityList = mapScore(gameSnapshot.secondGameSide());
        List<GamePlayerDataEntity> firstSideGamePlayerDataEntityList = mapGamePlayerDataEntityList(gameSnapshot.firstGameSide(), playerEntityList, gameArmyTypeEntityList);
        List<GamePlayerDataEntity> secondSideGamePlayerDataEntityList = mapGamePlayerDataEntityList(gameSnapshot.secondGameSide(), playerEntityList, gameArmyTypeEntityList);

        return new GameEntity(
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
                        gameSnapshot.firstGameSide().getSideId().sideId(),
                        gameSnapshot.firstGameSide().getAllegiance(),
                        firstSideGamePlayerDataEntityList,
                        gameSnapshot.firstGameSide().getIsFirst(),
                        firstSideTurnScoreEntityList
                ),
                new GameSideEntity(
                        null,
                        gameSnapshot.secondGameSide().getSideId().sideId(),
                        gameSnapshot.secondGameSide().getAllegiance(),
                        secondSideGamePlayerDataEntityList,
                        gameSnapshot.secondGameSide().getIsFirst(),
                        secondSideTurnScoreEntityList
                )

        );
    }

    public static GameSelectDto fromEntity(GameEntity gameEntity) {
        return new GameSelectDto(
                gameEntity.getGameId(),
                LocationMapper.fromEntity(gameEntity.getLocationEntity()),
                new GameMission(gameEntity.getGameMissionEntity().getName()),
                new GameDeployment(gameEntity.getGameDeploymentEntity().getName()),
                gameEntity.getGameTurnLength(),
                gameEntity.getGameHoursDuration(),
                gameEntity.getGameStartTime(),
                gameEntity.getGameEndTime(),
                gameEntity.getRanking(),
                gameEntity.getStatus(),
                GameSideDatabaseMapper.fromEntity(gameEntity.getFirstGameSide()),
                GameSideDatabaseMapper.fromEntity(gameEntity.getSecondGameSide())

        );
    }

    private static List<GameTurnScoreEntity> mapScore(GameSide gameSide) {
        List<GameTurnScoreEntity> turnScoreEntityList = new ArrayList<>();
        gameSide.getScorePerTurnList().forEach(
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

    private static List<GamePlayerDataEntity> mapGamePlayerDataEntityList(GameSide gameSide, List<PlayerEntity> playerEntityList, List<GameArmyTypeEntity> gameArmyTypeEntityList) {
        List<GamePlayerDataEntity> gamePlayerDataEntityList = new ArrayList<>();
        gameSide.getGameSidePlayerDataList().forEach(gameSidePlayerData ->
                {
                    PlayerEntity playerEntity = playerEntityList.stream().filter(playerEntity1 -> playerEntity1.getUserId().equals(gameSidePlayerData.player().getUserId().id())).findFirst().orElseThrow(NoSuchElementException::new);
                    GameArmyTypeEntity primaryGameArmyTypeEntity = gameArmyTypeEntityList.stream().filter(gameArmyTypeEntity -> gameArmyTypeEntity.getName().equals(gameSidePlayerData.armyPrimary().name())).findFirst().orElseThrow(NoSuchElementException::new);
                    List<GameArmyEntity> allyArmyEntityList = new ArrayList<>();
                    gameSidePlayerData.allyArmyList().forEach(allyArmy -> {
                        GameArmyTypeEntity allyArmyTypeEntity = gameArmyTypeEntityList.stream().filter(gameArmyTypeEntity -> gameArmyTypeEntity.getName().equals(allyArmy.name())).findFirst().orElseThrow(NoSuchElementException::new);
                        allyArmyEntityList.add(new GameArmyEntity(
                                null,
                                allyArmyTypeEntity,
                                allyArmy.name(),
                                allyArmy.pointValue()

                        ));
                    });
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
}
