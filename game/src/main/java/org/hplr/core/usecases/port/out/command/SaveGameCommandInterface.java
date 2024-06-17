package org.hplr.core.usecases.port.out.command;

import org.hplr.core.model.GameSnapshot;

public interface SaveGameCommandInterface {
    void saveGame(GameSnapshot gameSnapshot);
}
