package org.hplr.infrastructure.dbadapter.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name= ConstDatabaseNames.ADMINISTRATOR_TABLE)
public class AdministratorEntity extends UserEntity{
    @ManyToMany
    private List<RoleEntity> permissions;
}
