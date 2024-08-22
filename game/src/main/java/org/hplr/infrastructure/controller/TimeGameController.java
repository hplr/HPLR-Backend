package org.hplr.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hplr.core.usecases.port.in.StartAllDueGamesAutomaticallyUseCaseInterface;
import org.hplr.core.util.ConstValues;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TimeGameController {

    final StartAllDueGamesAutomaticallyUseCaseInterface startAllDueGamesAutomaticallyUseCaseInterface;

    @Scheduled(fixedRate = ConstValues.DEFAULT_CHECK_INTERVAL)
    public void startGameAtGivenTime(){
        startAllDueGamesAutomaticallyUseCaseInterface.startGameAutomatically();
    }
}
