package org.hplr.game.infrastructure.dbadapter.adapters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.hplr.game.core.model.GameSnapshot;
import org.hplr.game.core.usecases.port.out.command.SaveGameSecondSideCommandInterface;
import org.hplr.game.core.usecases.port.out.command.SaveScoreCommandInterface;
import org.hplr.game.infrastructure.dbadapter.entities.GameArmyTypeEntity;
import org.hplr.game.infrastructure.dbadapter.entities.GameEntity;
import org.hplr.game.infrastructure.dbadapter.entities.GameTurnScoreEntity;
import org.hplr.game.infrastructure.dbadapter.mappers.GameSideDatabaseMapper;
import org.hplr.game.infrastructure.dbadapter.repositories.GameArmyTypeRepository;
import org.hplr.game.infrastructure.dbadapter.repositories.GameRepository;

import org.hplr.elo.core.model.vo.Score;

import org.hplr.user.infrastructure.dbadapter.entities.PlayerEntity;
import org.hplr.user.infrastructure.dbadapter.repositories.PlayerQueryRepository;

import org.hplr.library.exception.HPLRIllegalStateException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hplr.game.infrastructure.dbadapter.adapters.GameCommandAdapter.mapGamePlayerDataEntityList;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameSideCommandAdapter implements SaveGameSecondSideCommandInterface, SaveScoreCommandInterface {

    final GameRepository gameRepository;
    final PlayerQueryRepository playerQueryRepository;
    final GameArmyTypeRepository gameArmyTypeRepository;

    @Override
    public void saveGameSecondSide(GameSnapshot gameSnapshot) {

        List<PlayerEntity> allPlayerEntityList = playerQueryRepository.findAll();
        if (allPlayerEntityList.isEmpty()) {
            throw new HPLRIllegalStateException("Not enough players!");
        }
        List<GameArmyTypeEntity> armyTypeEntityList = gameArmyTypeRepository.findAll();
        if (armyTypeEntityList.isEmpty()) {
            throw new HPLRIllegalStateException("No army types!");
        }
        GameEntity gameEntity = gameRepository.findByGameId(gameSnapshot.gameId().gameId()).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        gameEntity.setSecondGameSide(GameSideDatabaseMapper.fromSnapshot(
                gameSnapshot.secondGameSide()));
        gameEntity.getSecondGameSide().setGamePlayerDataEntityList( mapGamePlayerDataEntityList(gameSnapshot.secondGameSide(), allPlayerEntityList, armyTypeEntityList));
        gameRepository.save(gameEntity);
    }

    @Override
    public void saveScore(GameSnapshot gameSnapshot, Score score, Boolean firstSide) {
        GameEntity gameEntity = gameRepository.findByGameId(gameSnapshot.gameId().gameId()).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        if(Boolean.TRUE.equals(firstSide)){
            GameTurnScoreEntity gameTurnScoreEntity =
                    gameEntity.getFirstGameSide().getTurnScoreEntityList()
                            .stream()
                            .filter(scoreEntity-> score.turnNumber().equals(scoreEntity.getTurnNumber()))
                            .findFirst()
                            .orElseThrow(() -> new NoSuchElementException("Turn not found"));
            gameTurnScoreEntity.setTurnScore(score.turnScore());
            gameTurnScoreEntity.setTabled(score.tabled());
   }
        else{
            GameTurnScoreEntity gameTurnScoreEntity =
                  gameEntity.getSecondGameSide().getTurnScoreEntityList()
                            .stream()
                            .filter(scoreEntity-> score.turnNumber().equals(scoreEntity.getTurnNumber()))
                            .findFirst()
                            .orElseThrow(() -> new NoSuchElementException("Turn not found"));
            gameTurnScoreEntity.setTurnScore(score.turnScore());
            gameTurnScoreEntity.setTabled(score.tabled());
        }
        gameRepository.save(gameEntity);
    }
}
