package org.hplr.user.infrastructure.dbadapter.mappers;

import org.hplr.user.core.model.AdministratorSnapshot;
import org.hplr.user.core.usecases.port.dto.AdministratorSelectDto;
import org.hplr.user.infrastructure.dbadapter.entities.AdministratorEntity;
import org.hplr.user.infrastructure.dbadapter.entities.RoleEntity;


public class AdministratorMapper {
    private AdministratorMapper() {
        throw new IllegalStateException("Utility class");
    }
    public static AdministratorSelectDto toDto(AdministratorEntity administratorEntity){
        return new AdministratorSelectDto(
                administratorEntity.getUserId(),
                administratorEntity.getName(),
                administratorEntity.getNickname(),
                administratorEntity.getEmail(),
                administratorEntity.getMotto(),
                administratorEntity.getPwHash(),
                administratorEntity.getRegistrationTime(),
                administratorEntity.getLastLogin(),
                administratorEntity.getPermissions().stream().map(RoleEntity::getRole).toList()
        );
    }


    public static AdministratorEntity toEntity(AdministratorSnapshot administratorSnapshot) {
        return new AdministratorEntity(
                administratorSnapshot.userId().id(),
                administratorSnapshot.userData().name(),
                administratorSnapshot.userData().email(),
                administratorSnapshot.administratorSecurity().pwHash(),
                administratorSnapshot.administratorSecurity().registrationTime(),
                administratorSnapshot.administratorSecurity().lastLoginTime(),
                administratorSnapshot.userData().nickname(),
                administratorSnapshot.userData().motto(),
                administratorSnapshot.administratorSecurity().roleList().stream().map(RoleEntity::new).toList()

        );
    }
}
