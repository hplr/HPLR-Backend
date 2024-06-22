package org.hplr.core.usecases.port.out.query;

import org.hplr.core.usecases.port.dto.PlayerSelectDto;

import java.util.List;

public interface SelectAllPlayerListQueryInterface {
    List<PlayerSelectDto> selectAllPlayerList();
}
