package org.hplr.game.infrastructure.dbadapter.repositories;

import org.hplr.game.infrastructure.dbadapter.entities.GameSideEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameSideRepository extends CrudRepository<GameSideEntity, Long> {

    @Query("select g from GameSideEntity g where g.sideId =:sideId")
    Optional<GameSideEntity> findBySideId(UUID sideId);
}
