package org.hplr.infrastructure.dbadapter.mapper;

import org.hplr.core.model.LocationSnapshot;
import org.hplr.core.model.vo.LocationGeoData;
import org.hplr.core.usecases.port.dto.LocationSelectDto;
import org.hplr.infrastructure.dbadapter.entities.LocationEntity;

public class LocationMapper {

    public static LocationSelectDto fromEntity(LocationEntity locationEntity) {
        //todo: mapping
        return new LocationSelectDto();
    }

    public static LocationEntity fromSnapshot(LocationSnapshot locationSnapshot){
        return new LocationEntity(
                null,
                locationSnapshot.locationId().locationId(),
                locationSnapshot.name(),
                LocationGeoDataMapper.fromRecord(locationSnapshot.locationGeoData())
        );
    }
}
