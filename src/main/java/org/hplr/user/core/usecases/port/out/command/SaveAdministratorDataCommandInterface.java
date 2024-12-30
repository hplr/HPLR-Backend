package org.hplr.user.core.usecases.port.out.command;

import org.hplr.user.core.model.AdministratorSnapshot;

public interface SaveAdministratorDataCommandInterface {
    void saveAdministrator(AdministratorSnapshot administratorSnapshot);
}
