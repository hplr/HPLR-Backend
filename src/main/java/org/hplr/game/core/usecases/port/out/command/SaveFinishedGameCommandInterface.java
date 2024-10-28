package org.hplr.game.core.usecases.port.out.command;

import org.hplr.game.core.model.GameSnapshot;

public interface SaveFinishedGameCommandInterface {
    void saveFinishedGame(GameSnapshot gameSnapshot);
}
