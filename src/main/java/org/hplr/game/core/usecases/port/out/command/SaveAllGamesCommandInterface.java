package org.hplr.game.core.usecases.port.out.command;

import org.hplr.game.core.model.GameSnapshot;

import java.util.List;

public interface SaveAllGamesCommandInterface {
    void saveAllGamesCommandInterface(List<GameSnapshot> gameSnapshotList);

}
