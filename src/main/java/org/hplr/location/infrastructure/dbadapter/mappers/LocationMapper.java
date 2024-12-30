package org.hplr.location.infrastructure.dbadapter.mappers;

import org.hplr.location.core.model.LocationSnapshot;
import org.hplr.location.core.usecases.port.dto.LocationSelectDto;
import org.hplr.location.infrastructure.dbadapter.entities.LocationEntity;

import java.util.Objects;

public class LocationMapper {

    public static LocationSelectDto fromEntity(LocationEntity locationEntity) {
        if(Objects.nonNull(locationEntity.getLocationGeoData())){
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

        else return new LocationSelectDto(
                locationEntity.getLocationId(),
                locationEntity.getName(),
                locationEntity.getPrivateLocation(),
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public static LocationEntity fromSnapshot(LocationSnapshot locationSnapshot) {
        return new LocationEntity(
                null,
                locationSnapshot.locationId().locationId(),
                locationSnapshot.name(),
                locationSnapshot.privateLocation(),
                Objects.nonNull(locationSnapshot.locationGeoData()) ?
                        LocationGeoDataMapper.fromRecord(locationSnapshot.locationGeoData())
                        : null
        );
    }

    private LocationMapper() {
        throw new IllegalArgumentException("Utility class");
    }
}
