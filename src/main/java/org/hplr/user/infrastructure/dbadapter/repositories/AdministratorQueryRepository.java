package org.hplr.user.infrastructure.dbadapter.repositories;

import org.hplr.user.infrastructure.dbadapter.entities.AdministratorEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministratorQueryRepository extends CrudRepository<AdministratorEntity, Long> {

    Optional<AdministratorEntity> findByEmail(String email);

}
