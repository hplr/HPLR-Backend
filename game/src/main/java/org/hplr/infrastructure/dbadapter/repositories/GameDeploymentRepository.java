package org.hplr.infrastructure.dbadapter.repositories;

import org.hplr.infrastructure.dbadapter.entities.GameDeploymentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameDeploymentRepository extends CrudRepository<GameDeploymentEntity, Long> {
    Optional<GameDeploymentEntity> findByName(String name);
}
