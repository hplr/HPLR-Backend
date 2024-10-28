package org.hplr.game.core.mappers;

import lombok.extern.slf4j.Slf4j;
import org.hplr.elo.core.model.vo.Elo;
import org.hplr.user.core.model.Player;
import org.hplr.game.core.model.vo.GameArmy;
import org.hplr.game.core.model.vo.GameArmyType;
import org.hplr.game.core.model.vo.GameSidePlayerData;
import org.hplr.game.core.usecases.port.dto.InitialGameSidePlayerDataDto;
import org.hplr.library.exception.HPLRIllegalStateException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class GameSideMapper {

    private GameSideMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static List<GameSidePlayerData> createPlayerListForSide(List<Player> sidePlayerList,
                                                                   List<InitialGameSidePlayerDataDto> playerDataList)
                                                                   {
        List<GameSidePlayerData> gameSidePlayerDataList = new ArrayList<>();
        playerDataList.forEach(
                initialGameSidePlayerDataDto -> gameSidePlayerDataList.add(populateSide(
                        sidePlayerList,
                        initialGameSidePlayerDataDto
                ))
        );
        checkPlayers(sidePlayerList, playerDataList);
        return gameSidePlayerDataList;
    }

//    public static GameSide from

    private static GameSidePlayerData populateSide(List<Player> playerList, InitialGameSidePlayerDataDto initialGameSidePlayerDataDto) {
        Optional<Player> playerOptional = playerList.stream().filter(playerLambda -> playerLambda.getUserId().id().equals(initialGameSidePlayerDataDto.playerId())).findFirst();
        if (playerOptional.isEmpty()) {
            log.error("Could not retrieve player!");
            //handle this
            return null;
        } else {
            Player player = playerOptional.get();
            List<GameArmy> allyArmyList;
            if (Objects.nonNull(initialGameSidePlayerDataDto.allyArmyList())) {
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

            return
                    new GameSidePlayerData(
                            player,
                            new Elo(player.getRanking().score()),
                            new GameArmy(
                                    new GameArmyType(initialGameSidePlayerDataDto.primaryArmy().armyType()),
                                    initialGameSidePlayerDataDto.primaryArmy().armyName(),
                                    initialGameSidePlayerDataDto.primaryArmy().pointValue()
                            ),
                            allyArmyList

                    );
        }


    }


    private static void checkPlayers(List<Player> sidePlayerList, List<InitialGameSidePlayerDataDto> dtoPlayerList) throws HPLRIllegalStateException {
        if (sidePlayerList.size() != dtoPlayerList.size()) {
            throw new HPLRIllegalStateException("Player retrieval failed!");
        }
    }
}
