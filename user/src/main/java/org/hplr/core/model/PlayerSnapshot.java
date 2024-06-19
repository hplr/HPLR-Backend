package org.hplr.core.model;

import org.hplr.core.model.vo.PlayerRanking;
import org.hplr.core.model.vo.PlayerSecurity;
import org.hplr.core.model.vo.UserData;
import org.hplr.core.model.vo.UserId;

public record PlayerSnapshot(
        UserId userId,
        UserData userData,
        PlayerRanking playerRanking,
        PlayerSecurity playerSecurity
) {
    public PlayerSnapshot(Player player){
        this(player.getUserId(),
                player.getUserData(),
                player.getRanking(),
                player.getSecurity()
        );
    }
}
