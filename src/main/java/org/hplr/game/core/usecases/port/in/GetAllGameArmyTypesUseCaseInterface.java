package org.hplr.game.core.usecases.port.in;

import org.hplr.game.core.model.vo.GameArmyType;

import java.util.List;

public interface GetAllGameArmyTypesUseCaseInterface {
    List<GameArmyType> getAllGameArmyTypes();
}
