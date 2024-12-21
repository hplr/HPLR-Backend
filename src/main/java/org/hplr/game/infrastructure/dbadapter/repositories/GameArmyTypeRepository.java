package org.hplr.game.infrastructure.dbadapter.repositories;

import lombok.NonNull;
import org.hplr.game.infrastructure.dbadapter.entities.GameArmyTypeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameArmyTypeRepository extends CrudRepository<GameArmyTypeEntity, Long> {
    @NonNull
    List<GameArmyTypeEntity> findAll();
    Optional<GameArmyTypeEntity> findByName(String name);
}
