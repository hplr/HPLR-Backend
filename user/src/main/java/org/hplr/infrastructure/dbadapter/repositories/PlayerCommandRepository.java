package org.hplr.infrastructure.dbadapter.repositories;

import jakarta.transaction.Transactional;
import org.hplr.infrastructure.dbadapter.entities.PlayerEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface PlayerCommandRepository  extends CrudRepository<PlayerEntity, Long> {

    @Query(value="UPDATE PlayerEntity p set p.lastLogin = :lastLogin where p.userId = :userId")
    @Transactional
    @Modifying
    void updateLastLoginDate(LocalDateTime lastLogin, UUID userId);
}
