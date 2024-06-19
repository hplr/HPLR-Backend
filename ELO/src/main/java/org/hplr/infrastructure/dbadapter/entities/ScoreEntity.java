package org.hplr.infrastructure.dbadapter.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hplr.core.util.ConstDatabaseNames;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name= ConstDatabaseNames.SCORE_TABLE)
public class ScoreEntity {
    @Id
    private Long id;
    private UUID playerId;
    private Long score;
}
