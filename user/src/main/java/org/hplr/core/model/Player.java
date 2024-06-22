package org.hplr.core.model;

import lombok.Getter;
import org.hplr.core.model.vo.UserData;
import org.hplr.core.model.vo.UserId;
import org.hplr.core.model.vo.PlayerRanking;
import org.hplr.core.model.vo.PlayerSecurity;
import org.hplr.core.usecases.port.dto.PlayerSelectDto;
import org.hplr.exception.HPLRValidationException;

@Getter
public class Player extends User {

    private PlayerRanking ranking;
    private PlayerSecurity security;

    private Player(UserId userId, PlayerRanking ranking, PlayerSecurity security, UserData userData) {
        super(userId, userData);
        this.ranking = ranking;
        this.security = security;
    }

    public static Player fromDto(PlayerSelectDto playerSelectDto) throws HPLRValidationException {
        Player player =  new Player(
                new UserId(playerSelectDto.playerId()),
                new PlayerRanking(
                        playerSelectDto.score()
                ),
                new PlayerSecurity(
                        playerSelectDto.pwHash(),
                        playerSelectDto.registrationTime(),
                        playerSelectDto.lastLogin()
                ),
                new UserData(
                        playerSelectDto.name(),
                        playerSelectDto.nickname(),
                        playerSelectDto.email(),
                        playerSelectDto.motto()
                )
        );
        PlayerValidator.validatePlayer(player);
        return player;
    }

    public PlayerSnapshot toSnapshot(){
        return new PlayerSnapshot(this);
    }
}
