package org.hplr.location.core.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hplr.location.core.model.vo.LocationGeoData;
import org.hplr.location.core.model.vo.LocationId;
import org.hplr.location.core.model.vo.LocationMapPoint;
import org.hplr.location.core.usecases.port.dto.LocationSaveDto;
import org.hplr.location.core.usecases.port.dto.LocationSelectDto;
import org.hplr.library.exception.LocationCalculationException;
import org.hplr.location.infrastructure.external.OSMCoordinatesCalculator;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Getter
@Slf4j
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
                log.error("Error during location calculation!");
                location.setLocationGeoData(new LocationGeoData(
                        locationSaveDto.country(),
                        locationSaveDto.city(),
                        locationSaveDto.street(),
                        locationSaveDto.houseNumber(),
                        new LocationMapPoint(0.0,0.0)
                ));
            }
            LocationValidator.validateLocation(location);
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


    public LocationSnapshot toSnapshot(){
        return new LocationSnapshot(this);
    }
}
