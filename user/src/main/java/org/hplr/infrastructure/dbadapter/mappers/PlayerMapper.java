package org.hplr.infrastructure.dbadapter.mappers;

import org.hplr.core.usecases.port.dto.PlayerSelectDto;
import org.hplr.infrastructure.dbadapter.entities.PlayerEntity;

public class PlayerMapper {
    public static PlayerSelectDto fromEntity(PlayerEntity playerEntity){
        return new PlayerSelectDto(
                playerEntity.getUserId(),
                playerEntity.getName(),
                playerEntity.getNickname(),
                playerEntity.getEmail(),
                playerEntity.getMotto(),
                playerEntity.getScore(),
                playerEntity.getPwHash(),
                playerEntity.getRegistrationTime(),
                playerEntity.getLastLogin()
        );
    }

    private PlayerMapper() {
        throw new IllegalStateException("Utility class");
    }
}
