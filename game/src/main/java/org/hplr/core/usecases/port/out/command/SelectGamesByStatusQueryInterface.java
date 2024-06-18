package org.hplr.core.usecases.port.out.command;

import org.hplr.core.enums.Status;
import org.hplr.core.usecases.port.dto.GameSelectDto;
import org.hplr.infrastructure.dbadapter.entities.GameEntity;

import java.util.List;

public interface SelectGamesByStatusQueryInterface {
    List<GameSelectDto> selectGamesByStatus(Status status);
}
