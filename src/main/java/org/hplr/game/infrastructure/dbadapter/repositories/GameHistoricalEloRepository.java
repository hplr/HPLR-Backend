package org.hplr.game.infrastructure.dbadapter.repositories;

import org.hplr.game.infrastructure.dbadapter.entities.GameHistoricalEloEntity;
import org.springframework.data.repository.CrudRepository;

public interface GameHistoricalEloRepository extends CrudRepository<GameHistoricalEloEntity, Long> {
}
