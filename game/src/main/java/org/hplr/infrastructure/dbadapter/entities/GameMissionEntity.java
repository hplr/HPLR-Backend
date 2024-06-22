package org.hplr.infrastructure.dbadapter.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hplr.core.util.ConstDatabaseNames;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name= ConstDatabaseNames.MISSION_TABLE)
public class GameMissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
