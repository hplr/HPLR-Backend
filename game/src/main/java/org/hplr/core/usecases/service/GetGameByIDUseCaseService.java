package org.hplr.core.usecases.service;

import lombok.AllArgsConstructor;
import org.hplr.core.model.Game;
import org.hplr.core.model.GameSnapshot;
import org.hplr.core.usecases.port.dto.GameSelectDto;
import org.hplr.core.usecases.port.in.GetGameByIDUseCaseInterface;
import org.hplr.core.usecases.port.out.query.SelectGameByGameIdQueryInterface;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class GetGameByIDUseCaseService implements GetGameByIDUseCaseInterface {

    SelectGameByGameIdQueryInterface selectGameByGameIdQueryInterface;

    @Override
    public GameSnapshot getGameByID(UUID gameId) {
        Optional<GameSelectDto> gameSelectDtoOptional = selectGameByGameIdQueryInterface.selectGameByGameId(gameId);
        //todo: secure
        Game game = null;
        try{
            game = Game.fromDto(gameSelectDtoOptional.get());
        }
        catch (Exception e){

        }
        //todo catch validation error
        return game.toSnapshot();
    }
}
