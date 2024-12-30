package org.hplr.tournament.infrastructure.dbadapter.repositories;

import org.hplr.tournament.infrastructure.dbadapter.entities.TournamentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TournamentRepository extends CrudRepository<TournamentEntity, Long> {

    Optional<TournamentEntity> findByTournamentId(UUID tournamentId);

    @Query(value = "select t from TournamentEntity t where t.closed =:closed")
    List<TournamentEntity> findAllByClosed(Boolean closed);
}
