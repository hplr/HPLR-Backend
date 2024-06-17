package org.hplr.core.usecases.port.dto;

public record LocationSaveDto
        (
                String name,
                String country,
                String city,
                String street,
                String houseNumber,
                Boolean isPrivate
        ){
}
