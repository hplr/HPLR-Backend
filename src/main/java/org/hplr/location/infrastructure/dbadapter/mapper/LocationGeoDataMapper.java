package org.hplr.location.infrastructure.dbadapter.mapper;

import org.hplr.location.core.model.vo.LocationGeoData;
import org.hplr.location.infrastructure.dbadapter.entities.LocationGeoDataEntity;

public class LocationGeoDataMapper {

    private LocationGeoDataMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static LocationGeoDataEntity fromRecord(LocationGeoData locationGeoData){
        return new LocationGeoDataEntity(
                null,
                locationGeoData.country(),
                locationGeoData.city(),
                locationGeoData.street(),
                locationGeoData.houseNumber(),
                locationGeoData.locationMapPoint().longitude(),
                locationGeoData.locationMapPoint().latitude()
        );
    }
}
