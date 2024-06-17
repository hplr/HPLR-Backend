package org.hplr.infrastructure.dbadapter.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hplr.dbadapter.entites.GeneralEntity;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public abstract class UserEntity extends GeneralEntity {
    private UUID userId;
    private String name;
    private String email;

}
