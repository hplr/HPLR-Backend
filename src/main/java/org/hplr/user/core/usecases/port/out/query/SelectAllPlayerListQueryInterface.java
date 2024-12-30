package org.hplr.user.core.usecases.port.out.query;

import org.hplr.user.core.usecases.port.dto.PlayerSelectDto;

import java.util.List;

public interface SelectAllPlayerListQueryInterface {
    List<PlayerSelectDto> selectAllPlayerList();
}
