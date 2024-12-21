package org.hplr.user.core.model.vo;

import java.time.LocalDateTime;
import java.util.List;

public record AdministratorSecurity(
        String pwHash,
        LocalDateTime registrationTime,
        LocalDateTime lastLoginTime,
        List<AdministratorRole> roleList
) {
}
