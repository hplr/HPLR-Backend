package org.hplr.infrastructure.dbadapter.mappers;

import org.hplr.core.model.vo.*;
import org.hplr.core.usecases.port.dto.*;
import org.hplr.infrastructure.dbadapter.entities.GameSideEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
                            new ELODto(gamePlayerDataEntity.getEloScore()),
                            new GameArmy(
                                    new GameArmyType(gamePlayerDataEntity.getPrimaryArmyEntity().getGameArmyTypeEntity().getName()),
                                    gamePlayerDataEntity.getPrimaryArmyEntity().getName(),
                                    gamePlayerDataEntity.getPrimaryArmyEntity().getPointValue()
                            ),
                            gameArmyList

                    ));

                }
        );
        List<ScoreDto> scorePerTurnList = new ArrayList<>(gameSideEntity.getTurnScoreEntityList().size());
        return new GameSideSelectDto(
                gameSideEntity.getSideId(),
                gameSideEntity.getAllegiance(),
                gameSidePlayerDataList,
                gameSideEntity.getFirst(),
                scorePerTurnList
        );
    }

    private GameSideDatabaseMapper() {
        throw new IllegalStateException("Utility class");
    }


}
