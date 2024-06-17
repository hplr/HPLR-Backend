package org.hplr.core.model;

import lombok.Getter;
import org.hplr.core.model.vo.UserData;
import org.hplr.core.model.vo.UserId;
import org.hplr.core.model.vo.PlayerRanking;
import org.hplr.core.model.vo.PlayerSecurity;

@Getter
public class Player extends User{


    private PlayerRanking ranking;
    private PlayerSecurity security;

    //todo: private this, make fromDto
    private Player(UserId userId, PlayerRanking ranking, PlayerSecurity security, UserData userData) {
        super(userId, userData);
        this.ranking = ranking;
        this.security = security;
    }
}
