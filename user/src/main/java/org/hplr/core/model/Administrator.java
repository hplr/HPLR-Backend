package org.hplr.core.model;

import org.hplr.core.model.vo.AdministratorSecurity;
import org.hplr.core.model.vo.UserData;
import org.hplr.core.model.vo.UserId;

public class Administrator extends User{

    private AdministratorSecurity administratorSecurity;
    private Administrator(UserId userId, UserData userData, AdministratorSecurity administratorSecurity) {
        super(userId, userData);
        this.administratorSecurity = administratorSecurity;
    }
}
