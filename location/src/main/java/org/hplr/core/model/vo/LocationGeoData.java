package org.hplr.core.model.vo;

public record LocationGeoData(
        String country,
        String city,
        String street,
        String houseNumber,
        LocationMapPoint locationMapPoint
) {
}
