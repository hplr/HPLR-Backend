package org.hplr.core.usecases.port.out.command;

import org.hplr.core.model.PlayerFullDataSnapshot;

public interface SavePlayerDataCommandInterface {
        void savePlayer(PlayerFullDataSnapshot playerFullDataSnapshot);
}
