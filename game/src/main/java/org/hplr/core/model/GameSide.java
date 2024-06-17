package org.hplr.core.model;

import lombok.Getter;
import org.hplr.core.enums.Allegiance;
import org.hplr.core.model.vo.GameSideId;
import org.hplr.core.model.vo.GameSidePlayerData;
import org.hplr.core.model.vo.Score;

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

    public GameSide(Allegiance allegiance, List<GameSidePlayerData> gameSidePlayerDataList,  Boolean isFirst, Integer gameTurnLength) {
        this.sideId = new GameSideId(UUID.randomUUID());
        this.allegiance = allegiance;
        this.gameSidePlayerDataList = gameSidePlayerDataList;
        this.isFirst = isFirst;
        this.scorePerTurnList = new ArrayList<>(gameTurnLength);

    }
}
