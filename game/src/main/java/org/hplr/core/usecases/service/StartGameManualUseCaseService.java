package org.hplr.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.core.model.Game;
import org.hplr.core.model.GameSnapshot;
import org.hplr.core.model.GameValidator;
import org.hplr.core.usecases.port.dto.GameSelectDto;
import org.hplr.core.usecases.port.in.StartGameManualUseCaseInterface;
import org.hplr.core.usecases.port.out.command.StartGameCommandInterface;
import org.hplr.core.usecases.port.out.query.SelectGameByGameIdQueryInterface;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StartGameManualUseCaseService implements StartGameManualUseCaseInterface {

    private final SelectGameByGameIdQueryInterface selectGameByGameIdQueryInterface;
    private final StartGameCommandInterface startGameCommandInterface;
    @Override
    public UUID startGameManual(UUID gameId) {
        Optional<GameSelectDto> gameSelectDto = selectGameByGameIdQueryInterface.selectGameByGameId(gameId);
        Game game = gameSelectDto.map(Game::fromDto).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        GameValidator.validateStartingGame(game);
        GameSnapshot gameSnapshot = game.toSnapshot();
        startGameCommandInterface.startGame(gameSnapshot);
        return gameSnapshot.gameId().gameId();
    }
}