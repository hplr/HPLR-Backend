package org.hplr.game.core.usecases.port.out.command;

import org.hplr.game.core.model.GameSnapshot;
import org.hplr.elo.core.model.vo.Score;

public interface SaveScoreCommandInterface {
    void saveScore(GameSnapshot gameSnapshot, Score score, Boolean firstSide);
}
