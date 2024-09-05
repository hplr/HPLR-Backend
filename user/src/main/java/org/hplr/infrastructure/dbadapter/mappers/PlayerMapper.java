package org.hplr.infrastructure.dbadapter.mappers;

import org.hplr.core.model.PlayerFullDataSnapshot;
import org.hplr.core.usecases.port.dto.PlayerSelectDto;
import org.hplr.infrastructure.dbadapter.entities.PlayerEntity;

public class PlayerMapper {
    public static PlayerSelectDto toDto(PlayerEntity playerEntity){
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

    public static PlayerEntity toEntity(PlayerFullDataSnapshot playerFullDataSnapshot){
        return new PlayerEntity(
                playerFullDataSnapshot.userId().id(),
                playerFullDataSnapshot.userData().name(),
                playerFullDataSnapshot.userData().email(),
                playerFullDataSnapshot.playerSecurity().pwHash(),
                playerFullDataSnapshot.playerSecurity().registrationTime(),
                playerFullDataSnapshot.playerSecurity().lastLogin(),
                playerFullDataSnapshot.userData().nickname(),
                playerFullDataSnapshot.userData().motto(),
                playerFullDataSnapshot.playerRanking().score()
        );
    }

    private PlayerMapper() {
        throw new IllegalStateException("Utility class");
    }
}
