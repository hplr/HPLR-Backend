package org.hplr.elo.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.elo.core.usecases.port.dto.PlayerRankingDto;
import org.hplr.elo.core.usecases.port.in.GetAllPlayersWithRankingUseCaseInterface;
import org.hplr.user.core.model.Player;
import org.hplr.user.core.usecases.port.dto.PlayerSelectDto;
import org.hplr.user.core.usecases.port.out.query.SelectAllPlayerListQueryInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetAllPlayersWithRankingUseCaseService implements GetAllPlayersWithRankingUseCaseInterface {

    private final SelectAllPlayerListQueryInterface selectAllPlayerListQueryInterface;

    @Override
    public List<PlayerRankingDto> getAllPlayersWithRanking() {
        List<PlayerSelectDto> playerSelectDtoList = selectAllPlayerListQueryInterface.selectAllPlayerList();
        return playerSelectDtoList
                .stream()
                .map(Player::fromDto)
                .map(Player::toSnapshot)
                .map(player -> new PlayerRankingDto(
                        player.userId().id(),
                        player.userData().nickname(),
                        player.playerRanking().score()
                ))
                .sorted()
                .toList();
    }
}
