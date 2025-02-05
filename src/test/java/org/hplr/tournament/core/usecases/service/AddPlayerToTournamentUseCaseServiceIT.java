package org.hplr.tournament.core.usecases.service;


import lombok.extern.slf4j.Slf4j;
import org.hplr.game.core.enums.Allegiance;
import org.hplr.game.core.usecases.port.dto.InitialGameSidePlayerArmyDto;
import org.hplr.game.infrastructure.dbadapter.entities.GameArmyTypeEntity;
import org.hplr.game.infrastructure.dbadapter.repositories.GameArmyTypeRepository;
import org.hplr.game.infrastructure.dbadapter.repositories.GameRepository;
import org.hplr.library.infrastructure.controller.AccessValidator;
import org.hplr.location.infrastructure.dbadapter.entities.LocationEntity;
import org.hplr.location.infrastructure.dbadapter.entities.LocationGeoDataEntity;
import org.hplr.tournament.core.usecases.service.dto.AddPlayerToTournamentDto;
import org.hplr.tournament.infrastructure.dbadapter.adapters.TournamentCommandAdapter;
import org.hplr.tournament.infrastructure.dbadapter.adapters.TournamentQueryAdapter;
import org.hplr.tournament.infrastructure.dbadapter.entities.TournamentEntity;
import org.hplr.tournament.infrastructure.dbadapter.repositories.TournamentRepository;
import org.hplr.user.infrastructure.dbadapter.adapters.PlayerQueryAdapter;
import org.hplr.user.infrastructure.dbadapter.entities.PlayerEntity;
import org.hplr.user.infrastructure.dbadapter.repositories.AdministratorQueryRepository;
import org.hplr.user.infrastructure.dbadapter.repositories.PlayerCommandRepository;
import org.hplr.user.infrastructure.dbadapter.repositories.PlayerQueryRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = org.hplr.bootstrap.RankingApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
public class AddPlayerToTournamentUseCaseServiceIT {

    @Autowired
    PlayerQueryRepository playerQueryRepository;
    @Autowired
    AdministratorQueryRepository administratorQueryRepository;
    @Autowired
    GameRepository gameRepository;

    @Autowired
    TournamentRepository tournamentRepository;
    @Autowired
    AccessValidator accessValidator;

    @Autowired
    PlayerQueryAdapter playerQueryAdapter;

    @Autowired
    TournamentQueryAdapter tournamentQueryAdapter;

    @Autowired
    TournamentCommandAdapter tournamentCommandAdapter;

    @Autowired
    GameArmyTypeRepository gameArmyTypeRepository;

    static final UUID test_tournamentId = UUID.randomUUID();
    static final String test_name = "test";
    static final LocalDateTime test_tournamentStart = LocalDateTime.of(2001, 12, 25, 12, 0, 0);
    static final Long test_pointSize = 100L;
    static final Integer test_gameLength = 1;
    static final Integer test_gameTurnAmount = 2;
    static final Integer test_maxPlayers = 4;
    static final Boolean test_closed_true = true;
    static final String test_country = "Poland";
    static final String test_city = "Łódź";
    static final String test_street = "Duża";
    static final String test_houseNumber = "23";
    static final Double test_latitude = 23.0;
    static final Double test_longitude = 23.0;
    static final List<UUID> test_playerIdList = List.of(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID()
    );
    static final String test_email = "john@example.com";
    static final String test_pwHash = "c421";
    static final LocalDateTime test_registrationTime = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final LocalDateTime test_lastLogin = LocalDateTime.of(2001, 12, 21, 12, 2, 21);
    static final String test_nickname = "Playa";
    static final String test_motto = "to play is play";
    static final Long test_score = 25L;
    static final Allegiance test_allegiance = Allegiance.LOYALIST;
    static final String test_armyType = "type_test";
    static final String test_armyName = "name_test";
    static final Long test_armyPointSize = 1L;

    private AddPlayerToTournamentUseCaseService addPlayerToTournamentUseCaseService;
    @Autowired
    private PlayerCommandRepository playerCommandRepository;

    @Test
    public void addMultiplePlayersToTournament() throws BrokenBarrierException, InterruptedException {
        gameArmyTypeRepository.save(new GameArmyTypeEntity(1L, test_armyType));
        addPlayerToTournamentUseCaseService = new AddPlayerToTournamentUseCaseService(
              playerQueryAdapter,
              tournamentQueryAdapter,
              tournamentCommandAdapter
        );
        TournamentEntity tournamentEntity = new TournamentEntity(
                test_tournamentId,
                test_name,
                test_tournamentStart,
                test_pointSize,
                test_gameLength,
                test_gameTurnAmount,
                test_maxPlayers,
                new LocationEntity(
                        null,
                        UUID.randomUUID(),
                        test_name,
                        false,
                        new LocationGeoDataEntity(
                                null,
                                test_country,
                                test_city,
                                test_street,
                                test_houseNumber,
                                test_longitude,
                                test_latitude
                        )
                ),
                new ArrayList<>(),
                new ArrayList<>(),
                test_closed_true
        );
        tournamentRepository.save(tournamentEntity);
        for(int i=0;i<test_maxPlayers+1;i++){
            PlayerEntity playerEntity = new PlayerEntity(
                    test_playerIdList.get(i),
                    test_name,
                    test_email,
                    test_pwHash,
                    test_registrationTime,
                    test_lastLogin,
                    test_nickname,
                    test_motto,
                    test_score);
            playerCommandRepository.save(playerEntity);
        }
        try(ExecutorService es = Executors.newCachedThreadPool()){
            for(int i=0; i<test_maxPlayers+1; i++) {
                int finalI = i;
                es.execute(()->
                {
                    AddPlayerToTournamentDto addPlayerToTournamentDto = new AddPlayerToTournamentDto(
                            test_tournamentId,
                            test_playerIdList.get(finalI),
                            test_allegiance,
                            new InitialGameSidePlayerArmyDto(
                                    test_armyType,
                                    test_armyName,
                                    test_armyPointSize
                            ),
                            new ArrayList<>()
                    );
                    addPlayerToTournamentUseCaseService.addPlayerToTournament(addPlayerToTournamentDto);
                });
            }
            es.shutdown();
            Assertions.assertTrue(es.awaitTermination(1, TimeUnit.MINUTES));
        }
        Assertions.assertEquals(5,playerQueryAdapter.selectAllPlayerList().size());
        Assertions.assertTrue(tournamentQueryAdapter
                .selectTournamentByTournamentId(test_tournamentId)
                .isPresent());
        Assertions.assertEquals(1,tournamentQueryAdapter
                .selectTournamentByTournamentId(test_tournamentId)
                .get().playerDtoList().size());
    }

}
