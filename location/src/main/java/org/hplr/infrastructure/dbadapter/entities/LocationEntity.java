package org.hplr.infrastructure.dbadapter.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hplr.core.util.ConstDatabaseNames;

import java.util.UUID;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = ConstDatabaseNames.LOCATION_TABLE)

public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID locationId;
    private String name;
    @OneToOne
    private LocationGeoDataEntity locationGeoData;
}
