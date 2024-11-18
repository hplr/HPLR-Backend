package org.hplr.tournament.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.game.core.model.Game;
import org.hplr.game.core.model.vo.GameDeployment;
import org.hplr.game.core.model.vo.GameMission;
import org.hplr.game.core.usecases.port.out.command.SaveAllGamesCommandInterface;
import org.hplr.game.core.usecases.port.out.query.SelectAllGameDeploymentsQueryInterface;
import org.hplr.game.core.usecases.port.out.query.SelectAllGameMissionsQueryInterface;
import org.hplr.tournament.core.model.Tournament;
import org.hplr.tournament.core.model.TournamentRound;
import org.hplr.tournament.core.model.dto.TournamentGameDto;
import org.hplr.tournament.core.model.vo.TournamentPairing;
import org.hplr.tournament.core.usecases.port.StartTournamentUseCaseInterface;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StartTournamentUseCaseService implements StartTournamentUseCaseInterface {
//todo: implement
//    private final SelectTournamentByTournamentIdQueryInterface selectTournamentByTournamentIdQueryInterface;
    private final SelectAllGameDeploymentsQueryInterface selectAllGameDeploymentsQueryInterface;
    private final SelectAllGameMissionsQueryInterface selectAllGameMissionsQueryInterface;
    private final SaveAllGamesCommandInterface saveAllGamesCommandInterface;
    @Override
    public UUID startTournament(UUID tournamentId) {
        Tournament tournament = null;
        tournament.setClosed(true);
        List<TournamentPairing> usedParings = new ArrayList<>();
        List<Game> gameList = new ArrayList<>();
        List<TournamentPairing> currentPairings = getTournamentPairings(usedParings, tournament);
        usedParings.addAll(currentPairings);
        Random random = new Random();
        List<GameMission> gameMissionList = selectAllGameMissionsQueryInterface.getAllGameMissions();
        List<GameDeployment> gameDeploymentList = selectAllGameDeploymentsQueryInterface.getAllGameDeployments();
        currentPairings.forEach(pairing ->{
            TournamentGameDto tournamentGameDto = new TournamentGameDto(
                    //todo: refactor and validate
                    pairing.firstPlayer(),
                    pairing.secondPlayer(),
                    tournament.getTournamentData().pointSize(),
                    tournament.getTournamentData().gameLength(),
                    tournament.getTournamentData().tournamentStart().toString(),
                    tournament.getTournamentData().tournamentStart().toString(),
                    0,
                    tournament.getTournamentLocation().location(),
                    gameMissionList.get(random.nextInt(gameMissionList.size())),
                    gameDeploymentList.get(random.nextInt(gameDeploymentList.size()))
            );
            gameList.add(Game.fromTournamentDto(tournamentGameDto));
        });
        saveAllGamesCommandInterface.saveAllGamesCommandInterface(gameList.stream().map(Game::toSnapshot).toList());
        return tournament.getTournamentId().tournamentId();
    }

    private static List<TournamentPairing> getTournamentPairings(List<TournamentPairing> usedParings, Tournament tournament) {
        List<TournamentPairing> currentPairings = new ArrayList<>();

        while(!Collections.disjoint(usedParings, currentPairings)){
            currentPairings.clear();
            for(int i = 0; i< tournament.getPlayerList().size(); i=i+2){
                currentPairings.add(
                        new TournamentPairing(
                                tournament.getPlayerList().get(i),
                                tournament.getPlayerList().get(i + 1)
                        )
                );
            }
        }
        return currentPairings;
    }


}
