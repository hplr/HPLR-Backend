package org.hplr.game.core.usecases.service;

import lombok.AllArgsConstructor;

import org.hplr.game.core.model.Game;
import org.hplr.game.core.model.GameSnapshot;
import org.hplr.game.core.usecases.port.dto.GameSelectDto;
import org.hplr.game.core.usecases.port.in.GetGameByIDUseCaseInterface;
import org.hplr.game.core.usecases.port.out.query.SelectGameByGameIdQueryInterface;

import org.hplr.library.exception.HPLRIllegalArgumentException;
import org.hplr.library.exception.HPLRIllegalStateException;

import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class GetGameByIDUseCaseService implements GetGameByIDUseCaseInterface {

    SelectGameByGameIdQueryInterface selectGameByGameIdQueryInterface;

    @Override
    public GameSnapshot getGameByID(UUID gameId) throws NoSuchElementException, HPLRIllegalStateException, HPLRIllegalArgumentException {
        Optional<GameSelectDto> gameSelectDtoOptional =
                selectGameByGameIdQueryInterface.selectGameByGameId(gameId);

        Game game = Game.fromDto(gameSelectDtoOptional.orElseThrow(NoSuchElementException::new));

        return game.toSnapshot();
    }
}
