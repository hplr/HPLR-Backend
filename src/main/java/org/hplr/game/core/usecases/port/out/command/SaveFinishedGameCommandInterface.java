package org.hplr.game.core.usecases.port.out.command;

import org.hplr.game.core.model.GameSnapshot;
import org.hplr.game.core.model.vo.GameHistoricalElo;

public interface SaveFinishedGameCommandInterface {
    void saveFinishedGame(GameSnapshot gameSnapshot, GameHistoricalElo gameHistoricalElo);
}
