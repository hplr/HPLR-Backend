package org.hplr.infrastructure.dbadapter.mapper;

import org.hplr.core.usecases.port.dto.LocationSelectDto;
import org.hplr.infrastructure.dbadapter.entities.LocationEntity;

public class LocationMapper {

    public static LocationSelectDto fromEntity(LocationEntity locationEntity){
        //todo: mapping
        return new LocationSelectDto();
    }
}
