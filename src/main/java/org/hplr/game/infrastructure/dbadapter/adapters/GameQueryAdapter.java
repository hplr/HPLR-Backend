package org.hplr.game.infrastructure.dbadapter.adapters;

import lombok.extern.slf4j.Slf4j;
import org.hplr.game.core.enums.Status;
import org.hplr.game.core.usecases.port.dto.GameSelectDto;
import org.hplr.game.core.usecases.port.out.query.SelectAllGamesQueryInterface;
import org.hplr.game.core.usecases.port.out.query.SelectCreatedGamesByPlayerIdNotMatchingQueryInterface;
import org.hplr.game.core.usecases.port.out.query.SelectGameByGameIdQueryInterface;
import org.hplr.game.core.usecases.port.out.query.SelectGamesByStatusAndPlayerIdQueryInterface;
import org.hplr.game.infrastructure.dbadapter.entities.GameEntity;
import org.hplr.game.infrastructure.dbadapter.mappers.GameDatabaseMapper;
import org.hplr.game.infrastructure.dbadapter.repositories.GameRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class GameQueryAdapter implements
        SelectGameByGameIdQueryInterface,
        SelectAllGamesQueryInterface,
        SelectGamesByStatusAndPlayerIdQueryInterface,
        SelectCreatedGamesByPlayerIdNotMatchingQueryInterface {
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

    @Override
    public List<GameSelectDto> selectGamesByStatusAndPlayerId(Status status, UUID playerId) {
        ArrayList<GameEntity> gameEntityFilteredList;
        List<GameEntity> gameEntityList = gameRepository.findAllByStatus(status);
        List<GameEntity> gameEntityListFirstSide = gameEntityList.stream().filter(gameEntity -> gameEntity
                .getFirstGameSide()
                .getGamePlayerDataEntityList()
                .stream()
                .anyMatch(playerData -> Objects.equals(playerId, playerData.getPlayerEntity().getUserId())))
                .toList();
        List<GameEntity> gameEntityListSecondSide = gameEntityList.stream().filter(gameEntity -> gameEntity
                        .getSecondGameSide()
                        .getGamePlayerDataEntityList()
                        .stream()
                        .anyMatch(playerData -> Objects.equals(playerId, playerData.getPlayerEntity().getUserId())))
                .toList();
        gameEntityFilteredList = new ArrayList<>(gameEntityListFirstSide);
        gameEntityFilteredList.addAll(gameEntityListSecondSide);
        List<GameSelectDto> gameSelectDtoList = new ArrayList<>();
        gameEntityFilteredList.forEach(
                game -> {
                    try{
                        gameSelectDtoList.add(GameDatabaseMapper.toDto(game));

                    } catch (Exception e){
                        log.error("Game with id {} is not valid", game.getGameId());
                    }
                });
        return  gameSelectDtoList;
    }

    @Override
    public List<GameSelectDto> selectCreatedGamesByPlayerIdNotMatching(UUID playerId) {
        List<GameEntity> gameEntityList = gameRepository.findAllByStatus(Status.CREATED);
        List<GameEntity> gameEntityFiltered = gameEntityList
                .stream()
                .filter(gameEntity -> gameEntity.getFirstGameSide().getGamePlayerDataEntityList()
                        .stream()
                        .noneMatch(gamePlayerDataEntity -> Objects.equals(playerId,gamePlayerDataEntity.getPlayerEntity().getUserId()))).toList()
                ;
        return gameEntityFiltered.stream().map(GameDatabaseMapper::toDto).toList();

    }
}
