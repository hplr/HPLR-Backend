package org.hplr.user.infrastructure.dbadapter.repositories;

import jakarta.transaction.Transactional;
import org.hplr.user.infrastructure.dbadapter.entities.AdministratorEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface AdministratorCommandRepository extends CrudRepository<AdministratorEntity, Long> {
    @Query(value="UPDATE AdministratorEntity a set a.lastLogin = :lastLogin where a.userId = :userId")
    @Transactional
    @Modifying
    void updateLastLoginDate(LocalDateTime lastLogin, UUID userId);
}
