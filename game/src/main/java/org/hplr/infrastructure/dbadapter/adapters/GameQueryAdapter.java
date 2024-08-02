package org.hplr.infrastructure.dbadapter.adapters;

import lombok.extern.slf4j.Slf4j;
import org.hplr.core.usecases.port.dto.GameSelectDto;
import org.hplr.core.usecases.port.out.query.SelectAllGamesQueryInterface;
import org.hplr.core.usecases.port.out.query.SelectGameByGameIdQueryInterface;
import org.hplr.infrastructure.dbadapter.entities.GameEntity;
import org.hplr.infrastructure.dbadapter.mappers.GameDatabaseMapper;
import org.hplr.infrastructure.dbadapter.repositories.GameRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class GameQueryAdapter implements SelectGameByGameIdQueryInterface, SelectAllGamesQueryInterface {
    final GameRepository gameRepository;

    public GameQueryAdapter(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Optional<GameSelectDto> selectGameByGameId(UUID gameId) {
        GameEntity gameEntity = gameRepository.findByGameId(gameId).orElseThrow(NoSuchElementException::new);
        return Optional.of(GameDatabaseMapper.toDto(gameEntity));
    }

    @Override
    public List<GameSelectDto> selectAllGames() {
        List<GameEntity> gameEntityList = gameRepository.findAll();
        List<GameSelectDto> gameSelectDtoList = new ArrayList<>();
        gameEntityList.forEach(
                game -> {
                    try{
                        gameSelectDtoList.add(GameDatabaseMapper.toDto(game));

                    } catch (Exception e){
                       log.error("Game with id {} is not valid", game.getGameId());
                    }
                });
        return  gameSelectDtoList;

    }
}
