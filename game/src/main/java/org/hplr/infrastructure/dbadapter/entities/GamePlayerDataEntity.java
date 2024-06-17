package org.hplr.infrastructure.dbadapter.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hplr.util.ConstDatabaseNames;

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
    private Long ELOScore;
    @OneToOne
    private GameArmyEntity primaryArmyEntity;
    @OneToMany
    private List<GameArmyEntity> allyArmyEntityList;

}
