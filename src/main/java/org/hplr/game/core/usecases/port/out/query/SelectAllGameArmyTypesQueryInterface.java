package org.hplr.game.core.usecases.port.out.query;

import org.hplr.game.core.model.vo.GameArmyType;

import java.util.List;

public interface SelectAllGameArmyTypesQueryInterface {
    List<GameArmyType> getAllGameArmyTypes();
}
