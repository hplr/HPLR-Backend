package org.hplr.game.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.game.core.model.vo.GameArmyType;
import org.hplr.game.core.usecases.port.in.GetAllGameArmyTypesUseCaseInterface;
import org.hplr.game.core.usecases.port.out.query.SelectAllGameArmyTypesQueryInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetAllGameArmyTypesUseCaseService implements GetAllGameArmyTypesUseCaseInterface {

    final SelectAllGameArmyTypesQueryInterface selectAllGameArmyTypesQueryInterface;
    @Override
    public List<GameArmyType> getAllGameArmyTypes() {
        return selectAllGameArmyTypesQueryInterface.getAllGameArmyTypes();
    }
}
