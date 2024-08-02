package org.hplr.core.usecases.port.out.command;

import org.hplr.core.model.GameSnapshot;

import java.util.List;

public interface StartAllDueGamesCommandInterface {
    void startAllDueGames(List<GameSnapshot> gameToStartList);
}
