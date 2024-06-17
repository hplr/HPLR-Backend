package org.hplr.infrastructure.dbadapter.repositories;

import org.hplr.infrastructure.dbadapter.entities.LocationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface LocationRepository extends CrudRepository<LocationEntity, Long> {
     Optional<LocationEntity> findByLocationId(UUID locationId);
}
