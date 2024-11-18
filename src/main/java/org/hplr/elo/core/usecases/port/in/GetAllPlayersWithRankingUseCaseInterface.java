package org.hplr.elo.core.usecases.port.in;

import org.hplr.elo.core.usecases.port.dto.PlayerRankingDto;

import java.util.List;

public interface GetAllPlayersWithRankingUseCaseInterface {
    List<PlayerRankingDto> getAllPlayersWithRanking();
}
