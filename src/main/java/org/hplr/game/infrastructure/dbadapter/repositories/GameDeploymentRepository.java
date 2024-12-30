package org.hplr.game.infrastructure.dbadapter.repositories;

import lombok.NonNull;
import org.hplr.game.infrastructure.dbadapter.entities.GameDeploymentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameDeploymentRepository extends CrudRepository<GameDeploymentEntity, Long> {
    Optional<GameDeploymentEntity> findByName(String name);

    @NonNull
    List<GameDeploymentEntity> findAll();
}
