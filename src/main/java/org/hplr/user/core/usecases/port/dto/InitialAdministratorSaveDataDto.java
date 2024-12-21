package org.hplr.user.core.usecases.port.dto;

import org.hplr.user.core.model.vo.AdministratorRole;

import java.util.List;

public record InitialAdministratorSaveDataDto(
        String name,
        String nickname,
        String email,
        String motto,
        String password,
        List<AdministratorRole> roleList
) {
}
