package org.hplr.infrastructure.dbadapter.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hplr.util.ConstDatabaseNames;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name= ConstDatabaseNames.PLAYER_TABLE)
public class PlayerEntity extends UserEntity {

    private Long score;

    public PlayerEntity(UUID userId, String name, String email, Long score) {
        super(userId, name, email);
        this.score = score;
    }
}
