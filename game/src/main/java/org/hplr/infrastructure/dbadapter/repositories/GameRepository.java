package org.hplr.infrastructure.dbadapter.repositories;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.hplr.core.enums.Status;
import org.hplr.infrastructure.dbadapter.entities.GameEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameRepository extends CrudRepository<GameEntity, Long> {
    Optional<GameEntity> findByGameId(UUID gameId);
    @NonNull
    List<GameEntity> findAll();

    @Transactional
    @Modifying
    @Query(value="update GameEntity g set g.status=org.hplr.core.enums.Status.IN_PROGRESS where g.gameId in :gameToStartIdList")
    void startAllDueGames(List<UUID> gameToStartIdList);

    @NonNull
    @Query(value = "select ge from GameEntity ge WHERE ge.status = :status")
    List<GameEntity> findAllByStatus(Status status);
}
