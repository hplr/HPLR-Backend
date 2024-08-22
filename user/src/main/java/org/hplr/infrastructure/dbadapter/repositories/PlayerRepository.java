package org.hplr.infrastructure.dbadapter.repositories;

import lombok.NonNull;
import org.hplr.infrastructure.dbadapter.entities.PlayerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerRepository extends CrudRepository<PlayerEntity, Long> {
    @NonNull
    List<PlayerEntity> findAll();

    Optional<PlayerEntity> findByUserId(UUID userId);

    List<PlayerEntity> findAllByUserIdIn(List<UUID> userIdList);
}
