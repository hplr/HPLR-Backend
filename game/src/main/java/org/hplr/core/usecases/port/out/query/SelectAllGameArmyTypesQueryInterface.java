package org.hplr.core.usecases.port.out.query;

import org.hplr.core.model.vo.GameArmyType;

import java.util.List;

public interface SelectAllGameArmyTypesQueryInterface {
    List<GameArmyType> getAllGameArmyTypes();
}
