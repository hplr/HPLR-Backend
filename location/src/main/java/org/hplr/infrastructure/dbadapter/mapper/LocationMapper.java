package org.hplr.infrastructure.dbadapter.mapper;

import org.hplr.core.model.LocationSnapshot;
import org.hplr.core.usecases.port.dto.LocationSelectDto;
import org.hplr.infrastructure.dbadapter.entities.LocationEntity;

public class LocationMapper {

    public static LocationSelectDto fromEntity(LocationEntity locationEntity) {
        return new LocationSelectDto(
                locationEntity.getLocationId(),
                locationEntity.getName(),
                locationEntity.getPrivateLocation(),
                locationEntity.getLocationGeoData().getCountry(),
                locationEntity.getLocationGeoData().getCity(),
                locationEntity.getLocationGeoData().getStreet(),
                locationEntity.getLocationGeoData().getHouseNumber(),
                locationEntity.getLocationGeoData().getLongitude(),
                locationEntity.getLocationGeoData().getLatitude()
        );
    }

    public static LocationEntity fromSnapshot(LocationSnapshot locationSnapshot){
        return new LocationEntity(
                null,
                locationSnapshot.locationId().locationId(),
                locationSnapshot.name(),
                locationSnapshot.privateLocation(),
                LocationGeoDataMapper.fromRecord(locationSnapshot.locationGeoData())
        );
    }

    private LocationMapper(){
        throw new IllegalArgumentException("Utility class");
    }
}
