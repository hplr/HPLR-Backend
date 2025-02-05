package org.hplr.game.core.usecases.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.hplr.game.core.enums.Status;
import org.hplr.game.core.model.Game;
import org.hplr.game.core.model.GameSnapshot;
import org.hplr.game.core.model.GameValidator;
import org.hplr.game.core.usecases.port.dto.GameSelectDto;
import org.hplr.game.core.usecases.port.in.StartAllDueGamesAutomaticallyUseCaseInterface;
import org.hplr.game.core.usecases.port.out.command.StartAllDueGamesCommandInterface;
import org.hplr.game.core.usecases.port.out.query.SelectAllGamesQueryInterface;

import org.hplr.library.exception.HPLRValidationException;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StartAllDueGamesAutomaticallyUseCaseService implements StartAllDueGamesAutomaticallyUseCaseInterface {
    final SelectAllGamesQueryInterface selectAllGamesQueryInterface;
    final StartAllDueGamesCommandInterface startAllDueGamesCommandInterface;
    @Override
    public void startGameAutomatically() {
        List<GameSelectDto> gameSelectDtoList = selectAllGamesQueryInterface.selectAllGames();
        List<GameSnapshot> gameToStartIdList = gameSelectDtoList
                .stream()
                .map(Game::fromDto)
                .filter(game -> {
                    try{
                        GameValidator.validateStartingGame(game);
                        return true;
                    }
                    catch(HPLRValidationException e){
                        log.error("game {} could not be started", game.getGameId().gameId());
                        return false;
                    }
                })
                .filter(game -> Status.AWAITING.equals(game.getGameStatus()))
                .filter(game -> game.getGameData().gameStartTime().isBefore(LocalDateTime.now()))
                .map(Game::toSnapshot)
                .toList();
        startAllDueGamesCommandInterface.startAllDueGames(gameToStartIdList);
    }
}
