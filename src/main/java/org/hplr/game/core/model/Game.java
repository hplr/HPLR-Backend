package org.hplr.game.core.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hplr.game.core.enums.Status;
import org.hplr.game.core.model.vo.*;
import org.hplr.game.core.usecases.port.dto.GameSelectDto;
import org.hplr.game.core.usecases.port.dto.InitialGameSaveDataDto;
import org.hplr.library.core.util.ConstValues;
import org.hplr.library.exception.HPLRValidationException;
import org.hplr.library.exception.LocationCalculationException;
import org.hplr.location.core.model.Location;
import org.hplr.tournament.core.model.dto.TournamentGameDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Getter
public class Game {
    private final GameId gameId;
    private final GameLocation gameLocation;
    private final GameData gameData;
    @Setter
    private Long gameELOChangeValue;
    @Setter
    private Status gameStatus;
    private final GameSide firstGameSide;
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


    public static Game fromDto(InitialGameSaveDataDto initialGameSaveDataDto, List<GameSidePlayerData> firstSidePlayerList, List<GameSidePlayerData> secondSidePlayerList) throws LocationCalculationException, HPLRValidationException {
        Location location = Location.fromDto(initialGameSaveDataDto.locationSaveDto());
        Duration gameDuration = Duration.ofHours(initialGameSaveDataDto.gameTime());
        GameSide secondSide = null;
        Status status = Status.CREATED;
        if (Objects.nonNull(secondSidePlayerList)) {
            secondSide = GameSide.fromDto(
                    initialGameSaveDataDto.secondSide().allegiance(),
                    secondSidePlayerList,
                    initialGameSaveDataDto.gameTurnLength()
            );
            status = Status.AWAITING;
        }

        Game game = new Game(
                new GameId(UUID.randomUUID()),
                new GameLocation(location),
                new GameData(
                        new GameMission(initialGameSaveDataDto.gameMission()),
                        new GameDeployment(initialGameSaveDataDto.gameDeployment()),
                        initialGameSaveDataDto.gamePointSize(),
                        initialGameSaveDataDto.gameTurnLength(),
                        gameDuration,
                        LocalDateTime.parse(initialGameSaveDataDto.gameStartTime(), DateTimeFormatter.ofPattern(ConstValues.DATE_PATTERN)),
                        LocalDateTime.parse(initialGameSaveDataDto.gameStartTime(), DateTimeFormatter.ofPattern(ConstValues.DATE_PATTERN)).plus(gameDuration),
                        initialGameSaveDataDto.ranking()
                ),
                status,
                GameSide.fromDto(
                        initialGameSaveDataDto.firstSide().allegiance(),
                        firstSidePlayerList,
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

    public static Game fromTournamentDto(TournamentGameDto tournamentGameDto){
        return new Game(
                new GameId(UUID.randomUUID()),
                new GameLocation(tournamentGameDto.location()),
                new GameData(
                        tournamentGameDto.gameMission(),
                        tournamentGameDto.gameDeployment(),
                        tournamentGameDto.gamePointSize(),
                        tournamentGameDto.gameTurnLength(),
                        Duration.ofHours(tournamentGameDto.gameHoursDuration()),
                        LocalDateTime.parse(tournamentGameDto.gameStartTime(), DateTimeFormatter.ofPattern(ConstValues.DATE_PATTERN)),
                        LocalDateTime.parse(tournamentGameDto.gameEndTime(), DateTimeFormatter.ofPattern(ConstValues.DATE_PATTERN)),
                        true
                ),
                Status.AWAITING,
                GameSide.fromTournamentDto(tournamentGameDto.firstSide(), tournamentGameDto.gameTurnLength()),
                GameSide.fromTournamentDto(tournamentGameDto.secondSide(), tournamentGameDto.gameTurnLength())
        );
    }

    public GameSnapshot toSnapshot() {
        return new GameSnapshot(this);
    }

}
