package org.hplr.location.core.model;

import org.hplr.location.core.model.vo.LocationGeoData;
import org.hplr.location.core.model.vo.LocationId;

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
