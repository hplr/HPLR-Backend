package org.hplr.game.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hplr.elo.core.model.vo.Elo;
import org.hplr.game.core.enums.Allegiance;
import org.hplr.game.core.model.vo.GameSideId;
import org.hplr.game.core.model.vo.GameSidePlayerData;
import org.hplr.elo.core.model.vo.Score;
import org.hplr.game.core.usecases.port.dto.CreatedGameSaveSecondSideDto;
import org.hplr.game.core.usecases.port.dto.GameSidePlayerDataDto;
import org.hplr.game.core.usecases.port.dto.GameSideSelectDto;
import org.hplr.game.core.usecases.port.dto.InitialGameSaveSideDto;
import org.hplr.user.core.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class GameSide {
    private GameSideId sideId;
    private Allegiance allegiance;
    private List<GameSidePlayerData> gameSidePlayerDataList;
    private Boolean isFirst;
    private List<Score> scorePerTurnList;

    private GameSide(Allegiance allegiance, List<GameSidePlayerData> gameSidePlayerDataList, Boolean isFirst, Integer gameTurnLength) {
        this.sideId = new GameSideId(UUID.randomUUID());
        this.allegiance = allegiance;
        this.gameSidePlayerDataList = gameSidePlayerDataList;
        this.isFirst = isFirst;
        this.scorePerTurnList = new ArrayList<>();
        for (int i = 0; i < gameTurnLength; i++) {
            scorePerTurnList.add(new Score(
                    (long) i+1,
                    0L,
                    false
            ));
        }

    }


    public static GameSide fromDto(InitialGameSaveSideDto initialGameSaveSideDto, List<GameSidePlayerData> gameSidePlayerDataList, Integer turnLength) {
        return new GameSide(
                initialGameSaveSideDto.allegiance(),
                gameSidePlayerDataList,
                null,
                turnLength
        );
    }

    public static GameSide fromDto(CreatedGameSaveSecondSideDto createdGameSaveSecondSideDto, List<GameSidePlayerData> gameSidePlayerDataList, Integer turnLength) {
        return new GameSide(
                createdGameSaveSecondSideDto.allegiance(),
                gameSidePlayerDataList,
                null,
                turnLength
        );
    }

    public static GameSide fromDto(GameSideSelectDto gameSideSelectDto, List<GameSidePlayerDataDto> gameSidePlayerDataList){
        List<GameSidePlayerData> gameSidePlayerDataArrayList = new ArrayList<>();
        for (GameSidePlayerDataDto gameSidePlayerDataDto : gameSidePlayerDataList) {
            gameSidePlayerDataArrayList.add(new GameSidePlayerData(
                    Player.fromDto(gameSidePlayerDataDto.playerSelectDto()),
                    new Elo(gameSidePlayerDataDto.currentELO().ELOValue()),
                    gameSidePlayerDataDto.armyPrimary(),
                    gameSidePlayerDataDto.allyArmyList()
            ));
        }
        return new GameSide(
                new GameSideId(gameSideSelectDto.sideId()),
                gameSideSelectDto.allegiance(),
                gameSidePlayerDataArrayList,
                gameSideSelectDto.first(),
                gameSideSelectDto.scorePerTurnList().stream().map(Score::fromDto).collect(Collectors.toCollection(ArrayList::new))
        );
    }
}
