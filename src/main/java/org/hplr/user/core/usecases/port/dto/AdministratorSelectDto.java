package org.hplr.user.core.usecases.port.dto;

import org.hplr.user.core.model.vo.AdministratorRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AdministratorSelectDto(
        UUID administratorId,
        String name,
        String nickname,
        String email,
        String motto,
        String pwHash,
        LocalDateTime registrationTime,
        LocalDateTime lastLogin,
        List<AdministratorRole> roleList
) {
}
