package org.hplr.tournament.core.usecases.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hplr.game.core.model.Game;
import org.hplr.game.core.model.vo.GameDeployment;
import org.hplr.game.core.model.vo.GameMission;
import org.hplr.game.core.usecases.port.out.query.SelectAllGameDeploymentsQueryInterface;
import org.hplr.game.core.usecases.port.out.query.SelectAllGameMissionsQueryInterface;
import org.hplr.library.core.util.ConstValues;
import org.hplr.library.exception.HPLRIllegalStateException;
import org.hplr.library.exception.HPLRValidationException;
import org.hplr.tournament.core.model.Tournament;
import org.hplr.tournament.core.model.TournamentRound;
import org.hplr.tournament.core.model.TournamentValidator;
import org.hplr.tournament.core.model.dto.TournamentGameDto;
import org.hplr.tournament.core.model.vo.TournamentPairing;
import org.hplr.tournament.core.model.vo.TournamentPlayer;
import org.hplr.tournament.core.usecases.port.dto.TournamentSelectDto;
import org.hplr.tournament.core.usecases.port.in.StartTournamentUseCaseInterface;
import org.hplr.tournament.core.usecases.port.out.command.StartTournamentCommandInterface;
import org.hplr.tournament.core.usecases.port.out.query.SelectTournamentByTournamentIdQueryInterface;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class StartTournamentUseCaseService implements StartTournamentUseCaseInterface {
    private final SelectTournamentByTournamentIdQueryInterface selectTournamentByTournamentIdQueryInterface;
    private final SelectAllGameDeploymentsQueryInterface selectAllGameDeploymentsQueryInterface;
    private final SelectAllGameMissionsQueryInterface selectAllGameMissionsQueryInterface;
    private final StartTournamentCommandInterface startTournamentCommandInterface;
    private final Random random = new Random();
    @Override
    public UUID startTournament(UUID tournamentId) {
        Optional<TournamentSelectDto> tournamentSelectDtoOptional
                = selectTournamentByTournamentIdQueryInterface
                .selectTournamentByTournamentIdQueryInterface(tournamentId);

        Tournament tournament = Tournament.fromSelectDto(tournamentSelectDtoOptional
                .orElseThrow(NoSuchElementException::new));
        try{
            TournamentValidator.validateIfTournamentCanBeStarted(tournament);
        } catch (HPLRIllegalStateException e){
            throw new HPLRValidationException(e.getMessage());
        }
        tournament.setClosed(true);
        List<TournamentPairing> usedParings = new ArrayList<>();
        List<TournamentRound> roundList = new ArrayList<>();

        for(int i=0;i<3;i++){
            TournamentRound tournamentRound = new TournamentRound(new ArrayList<>());
            List<TournamentPairing> currentPairings = getTournamentPairings(usedParings, tournament);
            usedParings.addAll(currentPairings);

            List<GameMission> gameMissionList = selectAllGameMissionsQueryInterface.getAllGameMissions();
            List<GameDeployment> gameDeploymentList = selectAllGameDeploymentsQueryInterface.getAllGameDeployments();
            int finalI = i;
            currentPairings.forEach(pairing ->{
                TournamentGameDto tournamentGameDto = new TournamentGameDto(
                        pairing.firstPlayer(),
                        pairing.secondPlayer(),
                        tournament.getTournamentData().pointSize(),
                        tournament.getTournamentData().gameTurnAmount(),
                        tournament.getTournamentData().tournamentStart().plusHours((long) finalI * tournament.getTournamentData().gameLength()).format(DateTimeFormatter.ofPattern(ConstValues.DATE_PATTERN)),
                        tournament.getTournamentData().tournamentStart().plusHours((long) (1 + finalI) * tournament.getTournamentData().gameLength()).format(DateTimeFormatter.ofPattern(ConstValues.DATE_PATTERN)),
                        tournament.getTournamentData().gameLength(),
                        tournament.getTournamentLocation().location(),
                        gameMissionList.get(random.nextInt(gameMissionList.size())),
                        gameDeploymentList.get(random.nextInt(gameDeploymentList.size()))
                );
                tournamentRound.getGameList().add(Game.fromTournamentDto(tournamentGameDto));
            });
            roundList.add(tournamentRound);
        }
        tournament.setTournamentRoundList(roundList);
        return startTournamentCommandInterface.startTournament(tournament.toSnapshot());
    }

    private static List<TournamentPairing> getTournamentPairings(List<TournamentPairing> usedParings, Tournament tournament) {
        List<TournamentPairing> currentPairings = new ArrayList<>();

        while(currentPairings.isEmpty() || !Collections.disjoint(usedParings, currentPairings)){
            log.info("Reshuffling");
            currentPairings.clear();
            List<TournamentPlayer> tournamentPlayerList = tournament.getPlayerList();
            Collections.shuffle(tournamentPlayerList);
            for(int i = 0; i< tournament.getPlayerList().size(); i=i+2){
                currentPairings.add(
                        new TournamentPairing(
                                tournamentPlayerList.get(i),
                                tournamentPlayerList.get(i + 1)
                        )
                );
            }
        }
        return currentPairings;
    }


}
