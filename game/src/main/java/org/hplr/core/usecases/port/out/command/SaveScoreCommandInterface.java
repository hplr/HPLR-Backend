package org.hplr.core.usecases.port.out.command;

import org.hplr.core.model.GameSnapshot;
import org.hplr.core.model.vo.Score;

public interface SaveScoreCommandInterface {
    void saveScore(GameSnapshot gameSnapshot, Score score, Boolean firstSide);
}
