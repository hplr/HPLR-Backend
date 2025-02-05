package org.hplr.tournament.infrastructure.dbadapter.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hplr.game.infrastructure.dbadapter.entities.GameSideEntity;
import org.hplr.library.core.util.ConstDatabaseNames;
import org.hplr.location.infrastructure.dbadapter.entities.LocationEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
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
    Integer currentPlayers;
    Integer maxPlayers;
    @ManyToOne(cascade = CascadeType.ALL)
    LocationEntity locationEntity;
    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    List<TournamentRoundEntity> tournamentRoundEntityList;
    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    List<GameSideEntity> gameSideEntityList;
    Boolean closed;
    @Version
    Long version = 0L;

    public TournamentEntity(UUID tournamentId, String name, LocalDateTime tournamentStart, Long pointSize, Integer gameLength, Integer gameTurnAmount, Integer maxPlayers, LocationEntity locationEntity, List<TournamentRoundEntity> tournamentRoundEntityList, List<GameSideEntity> gameSideEntityList, Boolean closed) {
        this.tournamentId = tournamentId;
        this.name = name;
        this.tournamentStart = tournamentStart;
        this.pointSize = pointSize;
        this.gameLength = gameLength;
        this.gameTurnAmount = gameTurnAmount;
        this.maxPlayers = maxPlayers;
        this.locationEntity = locationEntity;
        this.tournamentRoundEntityList = tournamentRoundEntityList;
        this.gameSideEntityList = gameSideEntityList;
        this.closed = closed;
    }

    public TournamentEntity(Long id, UUID tournamentId, String name, LocalDateTime tournamentStart, Long pointSize, Integer gameLength, Integer gameTurnAmount, Integer maxPlayers, LocationEntity locationEntity, List<TournamentRoundEntity> tournamentRoundEntityList, List<GameSideEntity> gameSideEntityList, Boolean closed) {
        this.id = id;
        this.tournamentId = tournamentId;
        this.name = name;
        this.tournamentStart = tournamentStart;
        this.pointSize = pointSize;
        this.gameLength = gameLength;
        this.gameTurnAmount = gameTurnAmount;
        this.maxPlayers = maxPlayers;
        this.locationEntity = locationEntity;
        this.tournamentRoundEntityList = tournamentRoundEntityList;
        this.gameSideEntityList = gameSideEntityList;
        this.closed = closed;
    }


}
