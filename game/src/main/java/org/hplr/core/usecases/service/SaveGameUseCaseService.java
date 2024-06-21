package org.hplr.core.usecases.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hplr.core.model.Game;
import org.hplr.core.model.Player;
import org.hplr.core.usecases.port.dto.InitialGameSaveDataDto;
import org.hplr.core.usecases.port.dto.InitialGameSidePlayerDataDto;
import org.hplr.core.usecases.port.dto.PlayerSelectDto;
import org.hplr.core.usecases.port.in.SaveGameUseCaseInterface;
import org.hplr.core.usecases.port.out.command.SaveGameCommandInterface;
import org.hplr.core.usecases.port.out.query.SelectAllPlayerListQueryInterface;
import org.hplr.core.usecases.port.out.query.SelectPlayerByUserIdQueryInterface;
import org.hplr.exception.HPLRValidationException;
import org.hplr.exception.LocationCalculationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class SaveGameUseCaseService implements SaveGameUseCaseInterface {

    SelectPlayerByUserIdQueryInterface selectPlayerByUserIdQueryInterface;
    SelectAllPlayerListQueryInterface selectAllPlayerListQueryInterface;
    SaveGameCommandInterface saveGameCommandInterface;

    @Override
    public UUID saveGame(InitialGameSaveDataDto initialGameSaveDataDto) throws LocationCalculationException {
        Game game;
        try {
            List<Player> firstSidePlayerList = new ArrayList<>();
            List<Player> secondSidePlayerList = new ArrayList<>();
            List<UUID> firstSidePlayerUserIdList = initialGameSaveDataDto.firstSide().playerDataList()
                    .stream()
                    .map(InitialGameSidePlayerDataDto::playerId)
                    .toList();
            List<UUID> secondSidePlayerUserIdList = initialGameSaveDataDto.secondSide().playerDataList()
                    .stream()
                    .map(InitialGameSidePlayerDataDto::playerId)
                    .toList();
            List<PlayerSelectDto> playerSelectDtoList = selectAllPlayerListQueryInterface.selectAllPlayerList();
            try{
                playerSelectDtoList.forEach(playerSelectDto -> {

                    if (firstSidePlayerUserIdList.contains(playerSelectDto.playerId())) {
                        try {
                            firstSidePlayerList.add(Player.fromDto(playerSelectDto));
                        } catch (HPLRValidationException e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                    if (secondSidePlayerUserIdList.contains(playerSelectDto.playerId())) {
                        try {
                            secondSidePlayerList.add(Player.fromDto(playerSelectDto));
                        } catch (HPLRValidationException e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }


                });
            } catch (LocationCalculationException e)

            game = Game.fromDto(initialGameSaveDataDto, null, null);
            saveGameCommandInterface.saveGame(game.toSnapshot());
        } catch (LocationCalculationException e) {
            throw new LocationCalculationException(e.getMessage());
        }
        return game.getGameId().gameId();
    }
}
