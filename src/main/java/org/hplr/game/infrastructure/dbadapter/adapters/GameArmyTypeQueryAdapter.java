package org.hplr.game.infrastructure.dbadapter.adapters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hplr.game.core.model.vo.GameArmyType;
import org.hplr.game.core.usecases.port.out.query.SelectAllGameArmyTypesQueryInterface;
import org.hplr.game.infrastructure.dbadapter.repositories.GameArmyTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameArmyTypeQueryAdapter implements SelectAllGameArmyTypesQueryInterface {
    final GameArmyTypeRepository gameArmyTypeRepository;


    @Override
    public List<GameArmyType> getAllGameArmyTypes() {
        return gameArmyTypeRepository
                .findAll()
                .stream()
                .map(gameArmyType -> new GameArmyType(
                        gameArmyType.getName()
                )).toList();
    }
}
