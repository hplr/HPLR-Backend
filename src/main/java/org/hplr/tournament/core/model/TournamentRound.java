package org.hplr.tournament.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hplr.game.core.model.Game;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TournamentRound {
    private List<Game> gameList;
}
