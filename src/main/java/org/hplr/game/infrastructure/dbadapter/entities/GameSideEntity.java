package org.hplr.game.infrastructure.dbadapter.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hplr.game.core.enums.Allegiance;
import org.hplr.library.core.util.ConstDatabaseNames;

import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name= ConstDatabaseNames.GAME_SIDE_TABLE)
public class GameSideEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID sideId;
    @Enumerated
    private Allegiance allegiance;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<GamePlayerDataEntity> gamePlayerDataEntityList;
    private Boolean first;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<GameTurnScoreEntity> turnScoreEntityList;
}
