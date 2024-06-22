package org.hplr.core.model;

import lombok.Getter;
import lombok.Setter;
import org.hplr.core.enums.Status;
import org.hplr.core.model.vo.*;
import org.hplr.core.usecases.port.dto.InitialGameSaveDataDto;
import org.hplr.core.usecases.port.dto.InitialGameSidePlayerDataDto;
import org.hplr.exception.LocationCalculationException;

import java.time.Duration;
import java.util.*;

@Getter
public class Game {
    private GameId gameId;
    private GameLocation gameLocation;
    private GameData gameData;
    @Setter
    private Long gameELOChangeValue;
    private Status gameStatus;
    private GameSide firstGameSide;
    private GameSide secondGameSide;

    private Game(GameId gameId,
                 GameLocation gameLocation,
                 GameData gameData,
                 Status gameStatus,
                 GameSide firstGameSide,
                 GameSide secondGameSide
    ) {
        this.gameId = gameId;
        this.gameLocation = gameLocation;
        this.gameData = gameData;
        this.gameStatus = gameStatus;
        this.firstGameSide = firstGameSide;
        this.secondGameSide = secondGameSide;
    }


    public static Game fromDto(InitialGameSaveDataDto initialGameSaveDataDto, List<Player> firstSidePlayerList, List<Player> secondSidePlayerList) throws LocationCalculationException {
        Location location = Location.fromDto(initialGameSaveDataDto.locationSaveDto());
        Duration gameDuration = Duration.ofHours(initialGameSaveDataDto.gameTime());
        List<GameSidePlayerData> firstGameSidePlayerDataList = new ArrayList<>();
        List<GameSidePlayerData> secondGameSidePlayerDataList = new ArrayList<>();


        initialGameSaveDataDto.firstSide().playerDataList().forEach(
                initialGameSidePlayerDataDto -> populateSide(
                        firstSidePlayerList,
                        initialGameSidePlayerDataDto,
                        firstGameSidePlayerDataList
                )
        );

        initialGameSaveDataDto.secondSide().playerDataList().forEach(
                initialGameSidePlayerDataDto -> populateSide(
                        secondSidePlayerList,
                        initialGameSidePlayerDataDto,
                        secondGameSidePlayerDataList
                )
        );

        Game game = new Game(
                new GameId(UUID.randomUUID()),
                new GameLocation(location),
                new GameData(
                        new GameMission(initialGameSaveDataDto.gameMission()),
                        new GameDeployment(initialGameSaveDataDto.gameDeployment()),
                        initialGameSaveDataDto.gamePointSize(),
                        initialGameSaveDataDto.gameTurnLength(),
                        gameDuration,
                        initialGameSaveDataDto.gameStartTime(),
                        initialGameSaveDataDto.gameStartTime().plus(gameDuration),
                        initialGameSaveDataDto.ranking()
                ),
                Status.AWAITING,
                new GameSide(
                        initialGameSaveDataDto.firstSide().allegiance(),
                        firstGameSidePlayerDataList,
                        null,
                        initialGameSaveDataDto.gameTurnLength()
                ),
                new GameSide(
                        initialGameSaveDataDto.secondSide().allegiance(),
                        secondGameSidePlayerDataList,
                        null,
                        initialGameSaveDataDto.gameTurnLength())
                );

        GameValidator.validateCreatedGame(game);
        return game;
    }

    public GameSnapshot toSnapshot() {
        return new GameSnapshot(this);
    }

    private static void populateSide(List<Player> playerList, InitialGameSidePlayerDataDto initialGameSidePlayerDataDto, List<GameSidePlayerData> gameSidePlayerDataList) {
        Optional<Player> playerOptional = playerList.stream().filter(playerLambda -> playerLambda.getUserId().id().equals(initialGameSidePlayerDataDto.playerId())).findFirst();
        if (playerOptional.isEmpty()) {
            throw new IllegalArgumentException("Could not retrieve player!");
        }
        Player player = playerOptional.get();
        List<GameArmy> allyArmyList;
        if(Objects.nonNull(initialGameSidePlayerDataDto.allyArmyList())){
            allyArmyList = new ArrayList<>();
            initialGameSidePlayerDataDto.allyArmyList().forEach(
                    allyArmy -> allyArmyList.add(
                            new GameArmy(
                                    new GameArmyType(allyArmy.armyType()),
                                    allyArmy.armyName(),
                                    allyArmy.pointValue()
                            )
                    )
            );
        } else {
            allyArmyList = null;
        }

        gameSidePlayerDataList.add(
                new GameSidePlayerData(
                        player,
                        new ELO(player.getRanking().score()),
                        new GameArmy(
                                new GameArmyType(initialGameSidePlayerDataDto.primaryArmy().armyType()),
                                initialGameSidePlayerDataDto.primaryArmy().armyName(),
                                initialGameSidePlayerDataDto.primaryArmy().pointValue()
                        ),
                        allyArmyList

        ));
    }
}
