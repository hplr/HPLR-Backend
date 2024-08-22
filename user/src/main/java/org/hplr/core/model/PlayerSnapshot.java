package org.hplr.core.model;

import org.hplr.core.model.vo.*;

public record PlayerSnapshot(
        UserId userId,
        UserData userData,
        PlayerRanking playerRanking,
        PlayerSecuritySnapshot playerSecuritySnapshot
) {
    public PlayerSnapshot(Player player){
        this(player.getUserId(),
                player.getUserData(),
                player.getRanking(),
                new PlayerSecuritySnapshot(player.getSecurity())
        );
    }
}
