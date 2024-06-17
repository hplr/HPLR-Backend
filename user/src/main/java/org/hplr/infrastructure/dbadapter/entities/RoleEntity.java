package org.hplr.infrastructure.dbadapter.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hplr.dbadapter.entites.GeneralEntity;
import org.hplr.util.ConstDatabaseNames;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name= ConstDatabaseNames.ROLE_TABLE)
public class RoleEntity extends GeneralEntity {
    private String roleName;
}
