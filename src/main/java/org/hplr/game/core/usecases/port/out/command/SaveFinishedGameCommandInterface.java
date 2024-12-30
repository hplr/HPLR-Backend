package org.hplr.game.core.usecases.port.out.command;

import org.hplr.game.core.model.GameSnapshot;
import org.hplr.game.core.model.vo.GameHistoricalElo;

import java.util.List;

public interface SaveFinishedGameCommandInterface {
    List<Integer> saveFinishedGame(GameSnapshot gameSnapshot, GameHistoricalElo gameHistoricalElo);
}
