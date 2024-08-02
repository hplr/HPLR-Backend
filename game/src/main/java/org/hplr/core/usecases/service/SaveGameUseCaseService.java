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
import org.hplr.core.usecases.port.out.query.SelectAllPlayerByIdListQueryInterface;
import org.hplr.exception.HPLRValidationException;
import org.hplr.exception.LocationCalculationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
            List<Player> firstSidePlayerList = getPlayerListForSide(initialGameSaveDataDto.firstSide().playerDataList());
            List<Player> secondSidePlayerList = null;
            if(Objects.nonNull(initialGameSaveDataDto.secondSide())){
                secondSidePlayerList = getPlayerListForSide(initialGameSaveDataDto.secondSide().playerDataList());
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

    private List<Player> getPlayerListForSide(List<InitialGameSidePlayerDataDto> initialGameSidePlayerDataDtoList){
        List<Player> playerList = new ArrayList<>();
        List<UUID> selectedSidePlayerUserIdList =  initialGameSidePlayerDataDtoList
                .stream()
                .map(InitialGameSidePlayerDataDto::playerId)
                .toList();
        List<PlayerSelectDto> selectedSidePlayerSelectDtoList = selectAllPlayerByIdListQueryInterface
                .selectAllPlayerByIdList(selectedSidePlayerUserIdList);
        selectedSidePlayerSelectDtoList.forEach(player -> {
            try {
                Player playerCreated = Player.fromDto(player);
                playerList.add(playerCreated);
            }
            catch(HPLRValidationException e) {
                log.error("Validation Error for user: {}", player.playerId());
            }
        });
        return playerList;
    }
}
