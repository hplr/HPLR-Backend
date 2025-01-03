package org.hplr.elo.infrastructure.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.hplr.elo.core.usecases.port.dto.PlayerRankingDto;
import org.hplr.elo.core.usecases.port.in.GetAllPlayersWithRankingUseCaseInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/score")
@Tag(name = "Ranking")
public class RESTScoreController {

    private final GetAllPlayersWithRankingUseCaseInterface getAllPlayersWithRankingUseCaseInterface;

    @GetMapping(path = "/getPlayerList")
    public ResponseEntity<List<PlayerRankingDto>> getPlayersWithScore() {
        List<PlayerRankingDto> playerWithScoreSortedList= getAllPlayersWithRankingUseCaseInterface.getAllPlayersWithRanking();
        return new ResponseEntity<>(playerWithScoreSortedList, HttpStatus.OK);
    }
}
