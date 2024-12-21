package org.hplr.game.infrastructure.dbadapter.mappers;


import org.hplr.elo.core.usecases.port.dto.ScoreDto;
import org.hplr.game.core.model.vo.*;
import org.hplr.game.core.usecases.port.dto.*;
import org.hplr.game.infrastructure.dbadapter.entities.*;
import org.hplr.user.infrastructure.dbadapter.mappers.PlayerMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hplr.game.infrastructure.dbadapter.mappers.GameDatabaseMapper.mapScore;

public class GameSideDatabaseMapper {

    public static GameSideSelectDto fromEntity(GameSideEntity gameSideEntity) {
        List<GameSidePlayerDataDto> gameSidePlayerDataList = new ArrayList<>();

        gameSideEntity.getGamePlayerDataEntityList().forEach(gamePlayerDataEntity -> {
                    List<GameArmy> gameArmyList = new ArrayList<>();
                    if(Objects.nonNull(gamePlayerDataEntity.getAllyArmyEntityList())) {
                        gamePlayerDataEntity.getAllyArmyEntityList().forEach(gameArmyEntity ->
                                gameArmyList.add(new GameArmy(
                                        new GameArmyType(gameArmyEntity.getGameArmyTypeEntity().getName()),
                                        gameArmyEntity.getName(),
                                        gameArmyEntity.getPointValue()
                                ))
                        );
                    }

                    gameSidePlayerDataList.add(new GameSidePlayerDataDto(
                            PlayerMapper.toDto(gamePlayerDataEntity.getPlayerEntity()),
                            new GameArmy(
                                    new GameArmyType(gamePlayerDataEntity.getPrimaryArmyEntity().getGameArmyTypeEntity().getName()),
                                    gamePlayerDataEntity.getPrimaryArmyEntity().getName(),
                                    gamePlayerDataEntity.getPrimaryArmyEntity().getPointValue()
                            ),
                            gameArmyList

                    ));

                }
        );
        List<ScoreDto> scorePerTurnList = gameSideEntity.getTurnScoreEntityList().stream().map(ScoreMapper::fromEntity).toList();

        return new GameSideSelectDto(
                gameSideEntity.getSideId(),
                gameSideEntity.getAllegiance(),
                gameSidePlayerDataList,
                gameSideEntity.getFirst(),
                scorePerTurnList
        );
    }

    public static GameSideEntity fromSnapshot(GameSideSnapshot gameSideSnapshot){
        return new GameSideEntity(
                null,
                gameSideSnapshot.sideId().sideId(),
                gameSideSnapshot.allegiance(),
                null,
                gameSideSnapshot.isFirst(),
                mapScore(gameSideSnapshot)
        );
    }






    private GameSideDatabaseMapper() {
        throw new IllegalStateException("Utility class");
    }


}
