package org.hplr.core.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hplr.core.model.vo.LocationGeoData;
import org.hplr.core.model.vo.LocationId;
import org.hplr.core.model.vo.LocationMapPoint;
import org.hplr.core.usecases.port.dto.LocationSaveDto;
import org.hplr.core.usecases.port.dto.LocationSelectDto;
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

    private Location(LocationId locationId, String name, Boolean privateLocation, LocationGeoData locationGeoData) {
        this.locationId = locationId;
        this.name = name;
        this.privateLocation = privateLocation;
        this.locationGeoData = locationGeoData;
    }

    public static Location fromDto(LocationSaveDto locationSaveDto) throws LocationCalculationException {
        if (Boolean.TRUE.equals(locationSaveDto.isPrivate())) {
            return new Location(
                    new LocationId(UUID.randomUUID()),
                    locationSaveDto.name(),
                    true,
                    null
            );

        } else {
            Location location = new Location(
                    new LocationId(UUID.randomUUID()),
                    locationSaveDto.name(),
                    false,
                    null);
            location.setLocationId(new LocationId(UUID.randomUUID()));
            try {
                Map<String, Double> apiResponse = OSMCoordinatesCalculator.getCoordinatesFromAddress(
                         locationSaveDto.country(),
                         locationSaveDto.city(),
                         locationSaveDto.street(),
                         locationSaveDto.houseNumber());
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
        }
    }

    public static Location fromDto(LocationSelectDto locationSelectDto){
        return new Location(
                new LocationId(locationSelectDto.locationId()),
                locationSelectDto.name(),
                locationSelectDto.privateLocation(),
                new LocationGeoData(
                        locationSelectDto.country(),
                        locationSelectDto.city(),
                        locationSelectDto.street(),
                        locationSelectDto.houseNumber(),
                        new LocationMapPoint(
                                locationSelectDto.longitude(),
                                locationSelectDto.latitude()
                        )
                )

        );
    }
    //todo: move it to class
    private static void validateLocation(Location location) {
        if (!location.getPrivateLocation() && Objects.isNull(location.getLocationGeoData())) {
            throw new IllegalArgumentException();

        }
    }

    public LocationSnapshot toSnapshot(){
        return new LocationSnapshot(this);
    }
}
