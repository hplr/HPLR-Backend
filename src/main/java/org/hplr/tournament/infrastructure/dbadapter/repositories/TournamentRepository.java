package org.hplr.tournament.infrastructure.dbadapter.repositories;

import org.hplr.tournament.infrastructure.dbadapter.entities.TournamentEntity;
import org.springframework.data.repository.CrudRepository;

public interface TournamentRepository extends CrudRepository<TournamentEntity, Long> {
}
