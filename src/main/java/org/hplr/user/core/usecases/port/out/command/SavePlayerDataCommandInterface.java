package org.hplr.user.core.usecases.port.out.command;

import org.hplr.user.core.model.PlayerFullDataSnapshot;

public interface SavePlayerDataCommandInterface {
        void savePlayer(PlayerFullDataSnapshot playerFullDataSnapshot);
}
