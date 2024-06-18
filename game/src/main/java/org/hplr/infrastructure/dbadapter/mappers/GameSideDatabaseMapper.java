package org.hplr.infrastructure.dbadapter.mappers;

import org.hplr.core.usecases.port.dto.GameSideSelectDto;
import org.hplr.infrastructure.dbadapter.entities.GameSideEntity;

public class GameSideDatabaseMapper {

    public static GameSideSelectDto fromEntity(GameSideEntity gameSideEntity){
        return new GameSideSelectDto(
                gameSideEntity.getSideId(),
                gameSideEntity.getAllegiance(),
                gameSideEntity.getGamePlayerDataEntityList(),
                gameSideEntity.getFirst(),
                gameSideEntity.getTurnScoreEntityList()
        )
    }
}
