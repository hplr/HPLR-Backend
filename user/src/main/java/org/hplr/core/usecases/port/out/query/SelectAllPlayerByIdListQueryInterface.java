package org.hplr.core.usecases.port.out.query;

import org.hplr.core.usecases.port.dto.PlayerSelectDto;

import java.util.List;
import java.util.UUID;

public interface SelectAllPlayerByIdListQueryInterface {
    List<PlayerSelectDto> selectAllPlayerByIdList(List<UUID> idList);
}
