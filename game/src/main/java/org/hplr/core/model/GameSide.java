package org.hplr.core.model;

import lombok.Getter;
import org.hplr.core.enums.Allegiance;
import org.hplr.core.model.vo.ELO;
import org.hplr.core.model.vo.GameSideId;
import org.hplr.core.model.vo.GameSidePlayerData;
import org.hplr.core.model.vo.Score;
import org.hplr.core.usecases.port.dto.GameSidePlayerDataDto;
import org.hplr.core.usecases.port.dto.GameSideSelectDto;
import org.hplr.core.usecases.port.dto.InitialGameSaveSideDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
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
        this.scorePerTurnList = new ArrayList<>(gameTurnLength);

    }

    public static GameSide fromDto(InitialGameSaveSideDto initialGameSaveSideDto, List<GameSidePlayerData> gameSidePlayerDataList, Integer turnLength) {
        return new GameSide(
                initialGameSaveSideDto.allegiance(),
                gameSidePlayerDataList,
                null,
                turnLength
        );
    }

    public static GameSide fromDto(GameSideSelectDto gameSideSelectDto, List<GameSidePlayerDataDto> gameSidePlayerDataList, Integer turnLength){
        List<GameSidePlayerData> gameSidePlayerDataArrayList = new ArrayList<>();
        for (GameSidePlayerDataDto gameSidePlayerDataDto : gameSidePlayerDataList) {
            gameSidePlayerDataArrayList.add(new GameSidePlayerData(
                    Player.fromDto(gameSidePlayerDataDto.playerSelectDto()),
                    new ELO(gameSidePlayerDataDto.currentELO().ELOValue()),
                    gameSidePlayerDataDto.armyPrimary(),
                    gameSidePlayerDataDto.allyArmyList()
            ));
        }
        return new GameSide(
                gameSideSelectDto.allegiance(),
                gameSidePlayerDataArrayList,
                gameSideSelectDto.first(),
                turnLength
        );
    }
}
