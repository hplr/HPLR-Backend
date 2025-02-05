package org.hplr.tournament.infrastructure.dbadapter.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hplr.game.core.model.GameSnapshot;
import org.hplr.game.infrastructure.dbadapter.entities.GameEntity;
import org.hplr.library.core.util.ConstDatabaseNames;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name= ConstDatabaseNames.TOURNAMENT_ROUND_TABLE)

public class TournamentRoundEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL)
    private List<GameEntity> gameEntityList;

    @Transient
    List<GameSnapshot> gameSnapshotList;
}
