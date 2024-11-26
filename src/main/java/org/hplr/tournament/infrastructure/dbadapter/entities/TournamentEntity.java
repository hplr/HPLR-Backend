package org.hplr.tournament.infrastructure.dbadapter.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hplr.game.core.model.GameSnapshot;
import org.hplr.game.infrastructure.dbadapter.entities.GameSideEntity;
import org.hplr.library.core.util.ConstDatabaseNames;
import org.hplr.location.infrastructure.dbadapter.entities.LocationEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name= ConstDatabaseNames.TOURNAMENT_TABLE)
public class TournamentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID tournamentId;
    String name;
    LocalDateTime tournamentStart;
    Long pointSize;
    Integer gameLength;
    Integer gameTurnAmount;
    Integer maxPlayers;
    @ManyToOne
    LocationEntity locationEntity;
    @OneToMany
    List<TournamentRoundEntity> tournamentRoundEntityList;
    @OneToMany
    List<GameSideEntity> gameSideEntityList;
    Boolean closed;


}
