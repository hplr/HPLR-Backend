package org.hplr.core.model.vo;

import java.time.LocalDateTime;

public record PlayerSecuritySnapshot(
        LocalDateTime registrationTime,
        LocalDateTime lastLogin
) {
    public PlayerSecuritySnapshot(PlayerSecurity playerSecurity) {
        this(playerSecurity.registrationTime(),
                playerSecurity.lastLogin());
    }
}
