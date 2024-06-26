package org.hplr.infrastructure.dbadapter.repositories;

import org.hplr.infrastructure.dbadapter.entities.GameArmyTypeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameArmyTypeRepository extends CrudRepository<GameArmyTypeEntity, Long> {
    List<GameArmyTypeEntity> findAll();
}
