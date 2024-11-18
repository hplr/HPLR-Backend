package org.hplr.game.core.usecases.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hplr.game.core.model.Game;
import org.hplr.game.core.model.vo.GameArmy;
import org.hplr.game.core.model.vo.GameArmyType;
import org.hplr.game.core.model.vo.GameSidePlayerData;
import org.hplr.game.core.usecases.port.dto.InitialGameSaveDataDto;
import org.hplr.game.core.usecases.port.dto.InitialGameSidePlayerDataDto;
import org.hplr.game.core.usecases.port.in.SaveGameUseCaseInterface;
import org.hplr.game.core.usecases.port.out.command.SaveGameCommandInterface;

import org.hplr.user.core.model.Player;
import org.hplr.library.exception.HPLRValidationException;
import org.hplr.library.exception.LocationCalculationException;

import org.hplr.user.core.usecases.port.out.query.SelectPlayerByUserIdQueryInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
@AllArgsConstructor
public class SaveGameUseCaseService implements SaveGameUseCaseInterface {

    SelectPlayerByUserIdQueryInterface selectPlayerByUserIdQueryInterface;
    SaveGameCommandInterface saveGameCommandInterface;

    @Override
    public UUID saveGame(InitialGameSaveDataDto initialGameSaveDataDto) throws LocationCalculationException, HPLRValidationException {
        Game game;
        try {
            List<GameSidePlayerData> firstSidePlayerList = initialGameSaveDataDto
                    .firstSide()
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
            List<GameSidePlayerData> secondSidePlayerList = null;
            if(Objects.nonNull(initialGameSaveDataDto.secondSide())){
                 secondSidePlayerList = initialGameSaveDataDto
                        .secondSide()
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
            }
            game = Game.fromDto(initialGameSaveDataDto, firstSidePlayerList, secondSidePlayerList);
            saveGameCommandInterface.saveGame(game.toSnapshot());
        } catch (LocationCalculationException e) {
            throw new LocationCalculationException(e.getMessage());
        } catch (HPLRValidationException e) {
            throw new HPLRValidationException(e.getMessage());
        }
        return game.getGameId().gameId();
    }

}
