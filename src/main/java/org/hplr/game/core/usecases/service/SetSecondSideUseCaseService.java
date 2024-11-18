package org.hplr.game.core.usecases.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.hplr.game.core.enums.Status;
import org.hplr.game.core.model.Game;
import org.hplr.game.core.model.GameSide;
import org.hplr.game.core.model.vo.GameArmy;
import org.hplr.game.core.model.vo.GameSidePlayerData;
import org.hplr.game.core.usecases.port.dto.CreatedGameSaveSecondSideDto;
import org.hplr.game.core.usecases.port.in.SetSecondSideUseCaseInterface;
import org.hplr.game.core.usecases.port.out.command.SaveGameSecondSideCommandInterface;
import org.hplr.game.core.usecases.port.out.query.SelectGameByGameIdQueryInterface;

import org.hplr.user.core.model.Player;

import org.hplr.user.core.usecases.port.out.query.SelectPlayerByUserIdQueryInterface;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SetSecondSideUseCaseService implements SetSecondSideUseCaseInterface {

    final SelectPlayerByUserIdQueryInterface selectPlayerByUserIdQueryInterface;
    final SelectGameByGameIdQueryInterface selectGameByGameIdQueryInterface;
    final SaveGameSecondSideCommandInterface saveGameSecondSideCommandInterface;

    @Override
    public UUID setSecondSideForGame(CreatedGameSaveSecondSideDto createdGameSaveSecondSideDto) {

        Game game = selectGameByGameIdQueryInterface
                .selectGameByGameId(createdGameSaveSecondSideDto.gameId())
                .map(Game::fromDto)
                .orElseThrow(() -> new NoSuchElementException("Game not found!"));
        List<GameSidePlayerData> sidePlayerList = createdGameSaveSecondSideDto
                .playerDataList()
                .stream()
                .map(player -> {
                    Optional<Player> dbFetchedPlayer = selectPlayerByUserIdQueryInterface
                            .selectPlayerByUserId(player.playerId())
                            .map(Player::fromDto);
                    return dbFetchedPlayer.map(value -> new GameSidePlayerData(
                            value,
                            GameArmy.fromDto(
                                    player.primaryArmy()
                            ),
                            player.allyArmyList().stream().map(GameArmy::fromDto).toList()
                    )).orElse(null);
                }).toList().stream().filter(Objects::nonNull).toList();
        GameSide secondGameSide = GameSide.fromDto(createdGameSaveSecondSideDto.allegiance(), sidePlayerList, game.getGameData().gameTurnLength());
        game.setSecondGameSide(secondGameSide);
        //todo: validate
        game.setGameStatus(Status.AWAITING);
        saveGameSecondSideCommandInterface.saveGameSecondSide(game.toSnapshot());
        return secondGameSide.getSideId().sideId();
    }


}
