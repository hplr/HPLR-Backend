package org.hplr.core.usecases.port.out.command;

import org.hplr.core.model.GameSnapshot;

public interface StartGameCommandInterface {
    void startGame(GameSnapshot gameSnapshot);
}
