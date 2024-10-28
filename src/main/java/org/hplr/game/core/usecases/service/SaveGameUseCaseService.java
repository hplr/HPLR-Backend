package org.hplr.game.core.usecases.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hplr.game.core.model.Game;
import org.hplr.game.core.usecases.port.dto.InitialGameSaveDataDto;
import org.hplr.game.core.usecases.port.dto.InitialGameSidePlayerDataDto;
import org.hplr.game.core.usecases.port.in.SaveGameUseCaseInterface;
import org.hplr.game.core.usecases.port.out.command.SaveGameCommandInterface;

import org.hplr.user.core.model.Player;
import org.hplr.user.core.usecases.port.dto.PlayerSelectDto;
import org.hplr.user.core.usecases.port.out.query.SelectAllPlayerByIdListQueryInterface;

import org.hplr.library.exception.HPLRValidationException;
import org.hplr.library.exception.LocationCalculationException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.hplr.game.core.mappers.PlayerMapper.getPlayerListForSide;

@Slf4j
@Service
@AllArgsConstructor
public class SaveGameUseCaseService implements SaveGameUseCaseInterface {

    SelectAllPlayerByIdListQueryInterface selectAllPlayerByIdListQueryInterface;
    SaveGameCommandInterface saveGameCommandInterface;

    @Override
    public UUID saveGame(InitialGameSaveDataDto initialGameSaveDataDto) throws LocationCalculationException, HPLRValidationException {
        Game game;
        try {
            List<Player> firstSidePlayerList = getPlayerListForSide(
                    retrievePlayerSelectDtoListFromDb(initialGameSaveDataDto.firstSide().playerDataList()));
            List<Player> secondSidePlayerList = null;
            if(Objects.nonNull(initialGameSaveDataDto.secondSide())){
                secondSidePlayerList = getPlayerListForSide(
                        retrievePlayerSelectDtoListFromDb(initialGameSaveDataDto.secondSide().playerDataList()));
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

    private List<PlayerSelectDto> retrievePlayerSelectDtoListFromDb(List<InitialGameSidePlayerDataDto> initialGameSidePlayerDataDtoList){
        List<UUID> selectedSidePlayerUserIdList =  initialGameSidePlayerDataDtoList
                .stream()
                .map(InitialGameSidePlayerDataDto::playerId)
                .toList();
        return selectAllPlayerByIdListQueryInterface
                .selectAllPlayerByIdList(selectedSidePlayerUserIdList);
    }

}
