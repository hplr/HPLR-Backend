package org.hplr.core.usecases.service;

import lombok.AllArgsConstructor;
import org.hplr.core.model.Game;
import org.hplr.core.usecases.port.dto.InitialGameSaveDataDto;
import org.hplr.core.usecases.port.in.SaveGameUseCaseInterface;
import org.hplr.exception.LocationCalculationException;
import org.hplr.infrastructure.dbadapter.adapters.GameCommandAdapter;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class SaveGameUseCaseService implements SaveGameUseCaseInterface {

    GameCommandAdapter gameCommandAdapter;
    @Override
    public UUID saveGame(InitialGameSaveDataDto initialGameSaveDataDto) throws LocationCalculationException {
        Game game;
        try {
            game = Game.fromDto(initialGameSaveDataDto, null, null);
            gameCommandAdapter.saveGame(game.toSnapshot());
        } catch (LocationCalculationException e) {
            throw new LocationCalculationException(e.getMessage());
        }
        return game.getGameId().gameId();
    }
}
