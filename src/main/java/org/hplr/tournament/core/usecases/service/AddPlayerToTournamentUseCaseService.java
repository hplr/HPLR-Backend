package org.hplr.tournament.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.game.core.model.vo.GameArmy;
import org.hplr.game.core.model.vo.GameSidePlayerData;
import org.hplr.library.exception.HPLRIllegalArgumentException;
import org.hplr.library.exception.HPLRIllegalStateException;
import org.hplr.library.exception.HPLRValidationException;
import org.hplr.tournament.core.model.Tournament;
import org.hplr.tournament.core.model.TournamentSnapshot;
import org.hplr.tournament.core.model.TournamentValidator;
import org.hplr.tournament.core.model.vo.TournamentPlayer;
import org.hplr.tournament.core.usecases.port.in.AddPlayerToTournamentUseCaseInterface;
import org.hplr.tournament.core.usecases.port.out.command.UpdateTournamentQueryInterface;
import org.hplr.tournament.core.usecases.port.out.query.SelectTournamentByTournamentIdQueryInterface;
import org.hplr.tournament.core.usecases.service.dto.AddPlayerToTournamentDto;
import org.hplr.user.core.model.Player;
import org.hplr.user.core.usecases.port.out.query.SelectPlayerByUserIdQueryInterface;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddPlayerToTournamentUseCaseService implements AddPlayerToTournamentUseCaseInterface {
    private final SelectPlayerByUserIdQueryInterface selectPlayerByUserId;
    private final SelectTournamentByTournamentIdQueryInterface selectTournamentByTournamentId;
    private final UpdateTournamentQueryInterface updateTournamentQueryInterface;

    @Override
    public UUID addPlayerToTournament(AddPlayerToTournamentDto addPlayerToTournamentDto) {

        Player player = Player.fromDto(selectPlayerByUserId
                .selectPlayerByUserId(
                        addPlayerToTournamentDto.playerId()).orElseThrow(NoSuchElementException::new)
        );
        Tournament tournament = Tournament.fromSelectDto(selectTournamentByTournamentId
                .selectTournamentByTournamentId(addPlayerToTournamentDto.tournamentId())
                .orElseThrow(NoSuchElementException::new)
        );
        try {
            TournamentValidator.validateIfPlayerIsNotInTheTournament(tournament, player);
            TournamentValidator.validateIfTournamentSizeIsNotReached(tournament);
        } catch (HPLRIllegalArgumentException ex) {
            throw new HPLRValidationException(ex.getMessage());
        }
        TournamentPlayer tournamentPlayer = new TournamentPlayer(
                addPlayerToTournamentDto.allegiance(),
                new GameSidePlayerData(
                        player,
                        GameArmy.fromDto(addPlayerToTournamentDto.primaryArmy()),
                        addPlayerToTournamentDto.allyArmyList().stream().map(GameArmy::fromDto).toList()
                )
        );
        try {
            TournamentValidator.validateIfPointSizeIsLegal(tournament, tournamentPlayer);
        } catch (HPLRIllegalStateException ex) {
            throw new HPLRValidationException(ex.getMessage());
        }        tournament.getPlayerList().add(tournamentPlayer);
        TournamentSnapshot tournamentSnapshot = tournament.toSnapshot();
        updateTournamentQueryInterface.updateTournament(tournamentSnapshot);
        return tournamentSnapshot.tournamentId().tournamentId();
    }
}
