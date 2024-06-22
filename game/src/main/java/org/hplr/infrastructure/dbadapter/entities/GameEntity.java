package org.hplr.infrastructure.dbadapter.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hplr.core.enums.Status;
import org.hplr.core.util.ConstDatabaseNames;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name= ConstDatabaseNames.GAME_TABLE)
public class GameEntity extends GeneralEntity {
    
    private UUID gameId;
    @OneToOne
    private LocationEntity locationEntity;
    @ManyToOne
    private GameMissionEntity gameMissionEntity;
    @ManyToOne
    private GameDeploymentEntity gameDeploymentEntity;
    private Long gamePointSize;
    private Integer gameTurnLength;
    private Integer gameHoursDuration;
    private LocalDateTime gameStartTime;
    private LocalDateTime gameEndTime;
    private Boolean ranking;
    @Enumerated
    private Status status;
    @OneToOne
    private GameSideEntity firstGameSide;
    @OneToOne
    private GameSideEntity secondGameSide;

}
