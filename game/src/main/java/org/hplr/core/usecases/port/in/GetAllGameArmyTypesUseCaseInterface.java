package org.hplr.core.usecases.port.in;

import org.hplr.core.model.vo.GameArmyType;

import java.util.List;

public interface GetAllGameArmyTypesUseCaseInterface {
    List<GameArmyType> getAllGameArmyTypes();
}
