package org.hplr.infrastructure.dbadapter.mappers;

import org.hplr.core.model.Player;
import org.hplr.core.model.vo.*;
import org.hplr.core.usecases.port.dto.GameSideSelectDto;
import org.hplr.infrastructure.dbadapter.entities.GameSideEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameSideDatabaseMapper {

    public static GameSideSelectDto fromEntity(GameSideEntity gameSideEntity) {
        List<GameSidePlayerData> gameSidePlayerDataList = new ArrayList<>();

        gameSideEntity.getGamePlayerDataEntityList().forEach(gamePlayerDataEntity -> {
                    List<GameArmy> gameArmyList = new ArrayList<>();
                    gamePlayerDataEntity.getAllyArmyEntityList().forEach(gameArmyEntity ->
                            gameArmyList.add(new GameArmy(
                                    new GameArmyType(gameArmyEntity.getGameArmyTypeEntity().getName()),
                                    gameArmyEntity.getName(),
                                    gameArmyEntity.getPointValue()
                            ))
                    );
                    gameSidePlayerDataList.add(new GameSidePlayerData(
                            Optional.of(PlayerMapper.fromEntity(gamePlayerDataEntity.getPlayerEntity())).map(Player::fromDto).get(),
                            new ELO(gamePlayerDataEntity.getELOScore()),
                            new GameArmy(
                                    new GameArmyType(gamePlayerDataEntity.getPrimaryArmyEntity().getGameArmyTypeEntity().getName()),
                                    gamePlayerDataEntity.getPrimaryArmyEntity().getName(),
                                    gamePlayerDataEntity.getPrimaryArmyEntity().getPointValue()
                            ),
                            gameArmyList

                    ));
                }
        );
        List<Score> scorePerTurnList = new ArrayList<>(gameSideEntity.getTurnScoreEntityList().size());
        return new GameSideSelectDto(
                gameSideEntity.getSideId(),
                gameSideEntity.getAllegiance(),
                gameSidePlayerDataList,
                gameSideEntity.getFirst(),
                scorePerTurnList
        );
    }
}
