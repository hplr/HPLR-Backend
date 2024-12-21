package org.hplr.user.infrastructure.dbadapter.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hplr.library.core.util.ConstDatabaseNames;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name=ConstDatabaseNames.PLAYER_TABLE)
public class PlayerEntity extends UserEntity {

   private String nickname;
   private String motto;
   private Long score;

    public PlayerEntity(UUID userId, String name, String email, String pwHash, LocalDateTime registrationTime, LocalDateTime lastLogin, String nickname, String motto, Long score) {
        super(userId, name, email, pwHash, registrationTime, lastLogin);
        this.nickname = nickname;
        this.motto = motto;
        this.score = score;
    }
}
