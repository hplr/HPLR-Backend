package org.hplr.game.infrastructure.dbadapter.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hplr.library.core.util.ConstDatabaseNames;
import org.hplr.user.infrastructure.dbadapter.entities.PlayerEntity;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name= ConstDatabaseNames.GAME_PLAYER_TABLE)
public class GamePlayerDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private PlayerEntity playerEntity;
    private Long eloScore;
    @OneToOne(cascade = CascadeType.ALL)
    private GameArmyEntity primaryArmyEntity;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<GameArmyEntity> allyArmyEntityList;

}
