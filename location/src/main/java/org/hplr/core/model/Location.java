package org.hplr.core.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hplr.core.model.vo.LocationGeoData;
import org.hplr.core.model.vo.LocationId;
import org.hplr.core.model.vo.LocationMapPoint;
import org.hplr.core.usecases.port.dto.LocationSaveDto;
import org.hplr.exception.LocationCalculationException;
import org.hplr.infrastructure.external.OSMCoordinatesCalculator;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Location {
    @Setter(AccessLevel.PRIVATE)
    private LocationId locationId;
    private final String name;
    private final Boolean privateLocation;
    @Setter(AccessLevel.PRIVATE)
    private LocationGeoData locationGeoData;

    private Location(String name, Boolean privateLocation, LocationGeoData locationGeoData) {
        this.name = name;
        this.privateLocation = privateLocation;
        this.locationGeoData = locationGeoData;
    }

    public static Location fromDto(LocationSaveDto locationSaveDto) throws LocationCalculationException {
        if (locationSaveDto.isPrivate()) {
            Location location = new Location(
                    locationSaveDto.name(),
                    true,
                    null
            );
            location.setLocationId(new LocationId(UUID.randomUUID()));
            try {
                Map<String, Double> apiResponse = OSMCoordinatesCalculator.getCoordinatesFromAddress(
                        location.getLocationGeoData().country(),
                        location.getLocationGeoData().city(),
                        location.getLocationGeoData().street(),
                        location.getLocationGeoData().houseNumber());
                location.setLocationGeoData(new LocationGeoData(
                        locationSaveDto.country(),
                        locationSaveDto.city(),
                        locationSaveDto.street(),
                        locationSaveDto.houseNumber(),
                        new LocationMapPoint(apiResponse.get("lon"), apiResponse.get("lat"))
                ));
            } catch (LocationCalculationException | IOException | InterruptedException exception) {
                throw new LocationCalculationException("Error during location calculation!");
            }
            validateLocation(location);

            return location;
        } else {
            return new Location(
                    locationSaveDto.name(),
                    false,
                    null);
        }
    }

    private static void validateLocation(Location location) {
        if (!location.getPrivateLocation() && Objects.isNull(location.getLocationGeoData())) {
            throw new IllegalArgumentException();

        }
    }
}
