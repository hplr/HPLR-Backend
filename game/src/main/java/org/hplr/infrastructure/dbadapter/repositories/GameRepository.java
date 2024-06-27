package org.hplr.infrastructure.dbadapter.repositories;

import org.hplr.infrastructure.dbadapter.entities.GameEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameRepository extends CrudRepository<GameEntity, Long> {
    Optional<GameEntity> findByGameId(UUID gameId);
}
