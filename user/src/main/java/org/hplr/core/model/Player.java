package org.hplr.core.model;

import lombok.Getter;
import org.hplr.core.model.vo.UserData;
import org.hplr.core.model.vo.UserId;
import org.hplr.core.model.vo.PlayerRanking;
import org.hplr.core.model.vo.PlayerSecurity;
import org.hplr.core.usecases.service.dto.PlayerSelectDto;

@Getter
public class Player extends User {


    private PlayerRanking ranking;
    private PlayerSecurity security;

    //todo: private this, make fromDto
    private Player(UserId userId, PlayerRanking ranking, PlayerSecurity security, UserData userData) {
        super(userId, userData);
        this.ranking = ranking;
        this.security = security;
    }

    public static Player fromDto(PlayerSelectDto playerSelectDto) {
        //todo: refactor this method
        return new Player(
                new UserId(playerSelectDto.playerId()),
                new PlayerRanking(0L, 1L),
                new PlayerSecurity("email"),
                new UserData(playerSelectDto.name())
        );
    }
}
