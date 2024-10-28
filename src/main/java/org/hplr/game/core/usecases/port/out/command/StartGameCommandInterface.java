package org.hplr.game.core.usecases.port.out.command;

import org.hplr.game.core.model.GameSnapshot;

public interface StartGameCommandInterface {
    void startGame(GameSnapshot gameSnapshot);
}
