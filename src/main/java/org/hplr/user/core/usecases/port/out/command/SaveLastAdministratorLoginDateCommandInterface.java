package org.hplr.user.core.usecases.port.out.command;

import java.time.LocalDateTime;
import java.util.UUID;

public interface SaveLastAdministratorLoginDateCommandInterface {
    void saveLastLoginDate(LocalDateTime lastLoginDate, UUID userId);
}
