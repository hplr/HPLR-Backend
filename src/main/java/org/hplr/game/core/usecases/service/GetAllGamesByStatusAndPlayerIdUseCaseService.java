package org.hplr.game.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.game.core.enums.Status;
import org.hplr.game.core.model.Game;
import org.hplr.game.core.model.GameSnapshot;
import org.hplr.game.core.usecases.port.in.GetAllGamesByStatusAndPlayerIdUseCaseInterface;
import org.hplr.game.core.usecases.port.out.query.SelectGamesByStatusAndPlayerIdQueryInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GetAllGamesByStatusAndPlayerIdUseCaseService implements GetAllGamesByStatusAndPlayerIdUseCaseInterface {

    final SelectGamesByStatusAndPlayerIdQueryInterface selectGamesByStatusAndPlayerIdQueryInterface;
    @Override
    public List<GameSnapshot> getAllGamesByStatusAndPlayerId(Status status, UUID playerId) {
        return selectGamesByStatusAndPlayerIdQueryInterface
                .selectGamesByStatusAndPlayerId(status, playerId)
                .stream()
                .map(Game::fromDto)
                .map(Game::toSnapshot)
                .toList();
    }
}
