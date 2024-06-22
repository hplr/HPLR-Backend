package org.hplr.core.model.vo;


import java.time.LocalDateTime;

public record PlayerSecurity(
        String pwHash,
        LocalDateTime registrationTime,
        LocalDateTime lastLogin
) {

}
