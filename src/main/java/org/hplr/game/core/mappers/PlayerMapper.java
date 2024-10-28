package org.hplr.game.core.mappers;

import lombok.extern.slf4j.Slf4j;
import org.hplr.user.core.model.Player;
import org.hplr.user.core.usecases.port.dto.PlayerSelectDto;
import org.hplr.library.exception.HPLRValidationException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PlayerMapper {

    private PlayerMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static List<Player> getPlayerListForSide( List<PlayerSelectDto> selectedSidePlayerSelectDtoList){
        List<Player> playerList = new ArrayList<>();
        selectedSidePlayerSelectDtoList.forEach(player -> {
            try {
                Player playerCreated = Player.fromDto(player);
                playerList.add(playerCreated);
            }
            catch(HPLRValidationException e) {
                log.error("Validation Error for user: {}", player.playerId());
            }
        });
        return playerList;
    }
}
