package org.hplr.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.core.model.vo.GameArmyType;
import org.hplr.core.usecases.port.in.GetAllGameArmyTypesUseCaseInterface;
import org.hplr.core.usecases.port.out.query.SelectAllGameArmyTypesQueryInterface;
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
