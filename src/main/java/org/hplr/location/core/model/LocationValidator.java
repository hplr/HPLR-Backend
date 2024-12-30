package org.hplr.location.core.model;

import org.hplr.library.exception.HPLRIllegalStateException;

import java.util.Objects;

public class LocationValidator {

    private LocationValidator(){
        throw new HPLRIllegalStateException("Utility class");
    }
    public static void validateLocation(Location location) {
        if (Boolean.TRUE.equals(!location.getPrivateLocation()) && Objects.isNull(location.getLocationGeoData())) {
            throw new IllegalArgumentException();

        }
    }
}
