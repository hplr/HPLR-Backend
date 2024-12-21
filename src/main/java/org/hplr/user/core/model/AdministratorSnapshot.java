package org.hplr.user.core.model;

import org.hplr.user.core.model.vo.AdministratorSecurity;
import org.hplr.user.core.model.vo.UserData;
import org.hplr.user.core.model.vo.UserId;

public record AdministratorSnapshot(
        UserId userId,
        UserData userData,
        AdministratorSecurity administratorSecurity
){

    public AdministratorSnapshot(Administrator administrator) {
       this(administrator.getUserId(),
               administrator.getUserData(),
               administrator.getAdministratorSecurity());
    }
}
