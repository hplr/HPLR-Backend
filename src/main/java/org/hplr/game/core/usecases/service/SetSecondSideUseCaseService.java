package org.hplr.game.core.usecases.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.hplr.game.core.enums.Status;
import org.hplr.game.core.model.Game;
import org.hplr.game.core.model.GameSide;
import org.hplr.game.core.model.vo.GameSidePlayerData;
import org.hplr.game.core.usecases.port.dto.CreatedGameSaveSecondSideDto;
import org.hplr.game.core.usecases.port.dto.InitialGameSidePlayerDataDto;
import org.hplr.game.core.usecases.port.in.SetSecondSideUseCaseInterface;
import org.hplr.game.core.usecases.port.out.command.SaveGameSecondSideCommandInterface;
import org.hplr.game.core.usecases.port.out.query.SelectGameByGameIdQueryInterface;

import org.hplr.user.core.model.Player;
import org.hplr.user.core.usecases.port.dto.PlayerSelectDto;
import org.hplr.user.core.usecases.port.out.query.SelectAllPlayerByIdListQueryInterface;

import org.springframework.stereotype.Service;

import java.util.*;

import static org.hplr.game.core.mappers.GameSideMapper.createPlayerListForSide;
import static org.hplr.game.core.mappers.PlayerMapper.getPlayerListForSide;

@Slf4j
@Service
@RequiredArgsConstructor
public class SetSecondSideUseCaseService implements SetSecondSideUseCaseInterface {

    final SelectAllPlayerByIdListQueryInterface selectAllPlayerByIdListQueryInterface;
    final SelectGameByGameIdQueryInterface selectGameByGameIdQueryInterface;
    final SaveGameSecondSideCommandInterface saveGameSecondSideCommandInterface;

    @Override
    public UUID setSecondSideForGame(CreatedGameSaveSecondSideDto createdGameSaveSecondSideDto) {

        Game game = selectGameByGameIdQueryInterface
                .selectGameByGameId(createdGameSaveSecondSideDto.gameId())
                .map(Game::fromDto)
                .orElseThrow(() -> new NoSuchElementException("Game not found!"));
        List<Player> sidePlayerList = getPlayerListForSide(
                retrievePlayerSelectDtoListFromDb(createdGameSaveSecondSideDto.playerDataList()));
        List<GameSidePlayerData> playerListForSide = createPlayerListForSide(sidePlayerList, createdGameSaveSecondSideDto.playerDataList());
        GameSide secondGameSide = GameSide.fromDto(createdGameSaveSecondSideDto, playerListForSide, game.getGameData().gameTurnLength());
        game.setSecondGameSide(secondGameSide);
        //todo: validate
        game.setGameStatus(Status.AWAITING);
        saveGameSecondSideCommandInterface.saveGameSecondSide(game.toSnapshot());
        return secondGameSide.getSideId().sideId();
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
