package org.hplr.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.core.enums.Status;
import org.hplr.core.model.Game;
import org.hplr.core.model.GameSnapshot;
import org.hplr.core.usecases.port.in.GetAllGamesByStatusAndPlayerIdUseCaseInterface;
import org.hplr.core.usecases.port.out.query.SelectGamesByStatusAndPlayerIdQueryInterface;
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
