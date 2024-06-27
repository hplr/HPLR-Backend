package org.hplr.infrastructure.dbadapter.adapters;

import org.hplr.core.usecases.port.dto.GameSelectDto;
import org.hplr.core.usecases.port.out.query.SelectGameByGameIdQueryInterface;
import org.hplr.infrastructure.dbadapter.entities.GameEntity;
import org.hplr.infrastructure.dbadapter.mappers.GameDatabaseMapper;
import org.hplr.infrastructure.dbadapter.repositories.GameRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class GameQueryAdapter implements SelectGameByGameIdQueryInterface {
    final GameRepository gameRepository;

    public GameQueryAdapter(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Optional<GameSelectDto> selectGameByGameId(UUID gameId) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findByGameId(gameId);
        if(gameEntityOptional.isEmpty()){
            throw new NoSuchElementException("Game not found");
        }
        return gameEntityOptional.map(GameDatabaseMapper::toDto);
    }
}
