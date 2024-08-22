package org.hplr.infrastructure.dbadapter.repositories;

import lombok.NonNull;
import org.hplr.infrastructure.dbadapter.entities.GameMissionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameMissionRepository extends CrudRepository<GameMissionEntity, Long> {
    Optional<GameMissionEntity> findByName(String name);

    @NonNull
    List<GameMissionEntity> findAll();

}
