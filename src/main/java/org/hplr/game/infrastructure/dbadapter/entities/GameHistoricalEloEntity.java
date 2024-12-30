package org.hplr.game.infrastructure.dbadapter.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@Table(name= ConstDatabaseNames.ARMY_TABLE)
public class GameHistoricalEloEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private GameEntity game;
    private Long firstElo;
    private Long secondSideElo;
}
