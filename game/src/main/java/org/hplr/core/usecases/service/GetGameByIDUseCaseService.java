package org.hplr.core.usecases.service;

import org.hplr.core.model.Game;
import org.hplr.core.model.GameSnapshot;
import org.hplr.core.usecases.port.dto.GameSelectDto;
import org.hplr.core.usecases.port.in.GetGameByIDUseCaseInterface;
import org.hplr.core.usecases.port.out.query.SelectGameByGameIdQueryInterface;

import java.util.Optional;
import java.util.UUID;

public class GetGameByIDUseCaseService implements GetGameByIDUseCaseInterface {

    SelectGameByGameIdQueryInterface selectGameByGameIdQueryInterface;

    @Override
    public GameSnapshot getGameByID(UUID gameId) {
        Optional<GameSelectDto> gameSelectDtoOptional = selectGameByGameIdQueryInterface.selectGameByGameId(gameId);
        Game game = null;
        try{
            game = Game.fromDto(gameSelectDtoOptional);
        }
        catch (Exception e){

        }
        //todo catch validation error
        return game.toSnapshot();
    }
}
