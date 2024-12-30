package org.hplr.user.infrastructure.dbadapter.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hplr.library.core.util.ConstDatabaseNames;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name= ConstDatabaseNames.ADMINISTRATOR_TABLE)
public class AdministratorEntity extends UserEntity{

    private String nickname;
    private String motto;
    @OneToMany(cascade = CascadeType.ALL)
    private List<RoleEntity> permissions;

    public AdministratorEntity(UUID userId, String name, String email, String pwHash, LocalDateTime registrationTime, LocalDateTime lastLogin, String nickname, String motto, List<RoleEntity> permissions) {
        super(userId, name, email, pwHash, registrationTime, lastLogin);
        this.nickname = nickname;
        this.motto = motto;
        this.permissions = permissions;
    }
}
