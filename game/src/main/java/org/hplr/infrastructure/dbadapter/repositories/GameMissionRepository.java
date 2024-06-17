package org.hplr.infrastructure.dbadapter.repositories;

import org.hplr.infrastructure.dbadapter.entities.GameMissionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameMissionRepository extends CrudRepository<GameMissionEntity, Long> {
    Optional<GameMissionEntity> findByName(String name);

}
