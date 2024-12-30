package org.hplr.user.infrastructure.dbadapter.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hplr.library.infrastructure.dbadapter.entities.GeneralEntity;

import java.time.LocalDateTime;
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
    private String pwHash;
    private LocalDateTime registrationTime;
    private LocalDateTime lastLogin;

}
