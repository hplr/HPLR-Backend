package org.hplr.core.usecases.port.dto;

import java.util.UUID;

public record LocationSelectDto(
        UUID locationId,
        String name,
        Boolean privateLocation,
        String country,
        String city,
        String street,
        String houseNumber,
        Double longitude,
        Double latitude
) {
}
