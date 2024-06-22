package org.hplr.core.model;

import org.hplr.core.model.vo.LocationGeoData;
import org.hplr.core.model.vo.LocationId;

public record LocationSnapshot(
        LocationId locationId,
        String name,
        Boolean privateLocation,
        LocationGeoData locationGeoData
) {
    public LocationSnapshot(Location location){
        this(
                location.getLocationId(),
                location.getName(),
                location.getPrivateLocation(),
                location.getLocationGeoData()
        );
    }
}
