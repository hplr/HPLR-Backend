package org.hplr.core.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hplr.core.enums.Status;
import org.hplr.core.model.vo.*;
import org.hplr.core.usecases.port.dto.GameSelectDto;
import org.hplr.core.usecases.port.dto.InitialGameSaveDataDto;
import org.hplr.exception.HPLRValidationException;
import org.hplr.exception.LocationCalculationException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.hplr.core.mappers.GameSideMapper.createPlayerListForSide;

@Slf4j
@Getter
public class Game {
    private final GameId gameId;
    private GameLocation gameLocation;
    private GameData gameData;
    @Setter
    private Long gameELOChangeValue;
    @Setter
    private Status gameStatus;
    private GameSide firstGameSide;
    @Setter
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


    public static Game fromDto(InitialGameSaveDataDto initialGameSaveDataDto, List<Player> firstSidePlayerList, List<Player> secondSidePlayerList) throws LocationCalculationException, HPLRValidationException {
        Location location = Location.fromDto(initialGameSaveDataDto.locationSaveDto());
        Duration gameDuration = Duration.ofHours(initialGameSaveDataDto.gameTime());
        GameSide secondSide = null;
        Status status = Status.CREATED;
        if (Objects.nonNull(secondSidePlayerList)) {
            List<GameSidePlayerData> secondGameSidePlayerDataList = createPlayerListForSide(secondSidePlayerList,
                    initialGameSaveDataDto.secondSide().playerDataList());

            secondSide = GameSide.fromDto(
                    initialGameSaveDataDto.secondSide(),
                    secondGameSidePlayerDataList,
                    initialGameSaveDataDto.gameTurnLength());
            status = Status.AWAITING;
        }

        List<GameSidePlayerData> firstGameSidePlayerDataList = createPlayerListForSide(firstSidePlayerList,
                initialGameSaveDataDto.firstSide().playerDataList());

        Game game = new Game(
                new GameId(UUID.randomUUID()),
                new GameLocation(location),
                new GameData(
                        new GameMission(initialGameSaveDataDto.gameMission()),
                        new GameDeployment(initialGameSaveDataDto.gameDeployment()),
                        initialGameSaveDataDto.gamePointSize(),
                        initialGameSaveDataDto.gameTurnLength(),
                        gameDuration,
                        LocalDateTime.parse(initialGameSaveDataDto.gameStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        LocalDateTime.parse(initialGameSaveDataDto.gameStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).plus(gameDuration),
                        initialGameSaveDataDto.ranking()
                ),
                status,
                GameSide.fromDto(
                        initialGameSaveDataDto.firstSide(),
                        firstGameSidePlayerDataList,
                        initialGameSaveDataDto.gameTurnLength()
                ),
                secondSide);

        GameValidator.validateCreatedStandaloneGame(game);
        return game;
    }

    public static Game fromDto(GameSelectDto gameSelectDto) throws HPLRValidationException {
        Location location = Location.fromDto(gameSelectDto.locationSelectDto());
        GameSide firstGameSide = GameSide.fromDto(gameSelectDto.firstGameSideSelectDto(), gameSelectDto.firstGameSideSelectDto().gameSidePlayerDataList());
        GameSide secondGameSide = null;
        if (Objects.nonNull(gameSelectDto.secondGameSideSelectDto())) {
            secondGameSide = GameSide.fromDto(gameSelectDto.secondGameSideSelectDto(), gameSelectDto.secondGameSideSelectDto().gameSidePlayerDataList());
        }
        return new Game(
                new GameId(gameSelectDto.gameId()),
                new GameLocation(location),
                new GameData(
                        gameSelectDto.gameMission(),
                        gameSelectDto.gameDeployment(),
                        gameSelectDto.gamePointSize(),
                        gameSelectDto.gameTurnLength(),
                        Duration.ofHours(gameSelectDto.gameHoursDuration()),
                        gameSelectDto.gameStartTime(),
                        gameSelectDto.gameEndTime(),
                        gameSelectDto.ranking()
                ),
                gameSelectDto.status(),
                firstGameSide,
                secondGameSide
        );
    }

    public GameSnapshot toSnapshot() {
        return new GameSnapshot(this);
    }

}
