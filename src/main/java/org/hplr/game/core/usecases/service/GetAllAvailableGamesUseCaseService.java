package org.hplr.game.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.game.core.model.Game;
import org.hplr.game.core.model.GameSnapshot;
import org.hplr.game.core.usecases.port.dto.GameSelectDto;
import org.hplr.game.core.usecases.port.in.GetAllAvailableGamesUseCaseInterface;
import org.hplr.game.core.usecases.port.out.query.SelectCreatedGamesByPlayerIdNotMatchingQueryInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetAllAvailableGamesUseCaseService implements GetAllAvailableGamesUseCaseInterface {

    private final SelectCreatedGamesByPlayerIdNotMatchingQueryInterface selectCreatedGamesByPlayerIdNotMatchingQueryInterface;
    @Override
    public List<GameSnapshot> getAllAvailableGames(UUID playerId) {
        List<GameSelectDto> gameSelectDtoList = selectCreatedGamesByPlayerIdNotMatchingQueryInterface.selectCreatedGamesByPlayerIdNotMatching(playerId);
        return gameSelectDtoList.stream().map(Game::fromDto).map(Game::toSnapshot).toList();
    }
}
