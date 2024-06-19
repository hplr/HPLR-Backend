package org.hplr.infrastructure.dbadapter.mappers;

import org.hplr.core.usecases.service.dto.PlayerSelectDto;
import org.hplr.infrastructure.dbadapter.entities.PlayerEntity;

public class PlayerMapper {
    public static PlayerSelectDto fromEntity(PlayerEntity playerEntity){
        return new PlayerSelectDto(
                //todo: finish it
                playerEntity.getUserId(),
                playerEntity.getName()
        );
    }
}
