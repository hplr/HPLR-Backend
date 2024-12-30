package org.hplr.user.core.model;

import org.hplr.user.core.model.vo.*;

public record PlayerFullDataSnapshot(
        UserId userId,
        UserData userData,
        PlayerRanking playerRanking,
        PlayerSecurity playerSecurity
) {
    public PlayerFullDataSnapshot(Player player) {
        this(player.getUserId(),
                player.getUserData(),
                player.getRanking(),
                player.getSecurity()
        );
    }
}