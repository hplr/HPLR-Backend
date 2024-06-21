package org.hplr.infrastructure.dbadapter.mapper;

import org.hplr.core.model.LocationSnapshot;
import org.hplr.core.model.vo.LocationGeoData;
import org.hplr.infrastructure.dbadapter.entities.LocationEntity;
import org.hplr.infrastructure.dbadapter.entities.LocationGeoDataEntity;

public class LocationGeoDataMapper {
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
