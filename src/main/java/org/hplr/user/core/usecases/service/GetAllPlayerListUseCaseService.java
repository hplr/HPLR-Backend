package org.hplr.user.core.usecases.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hplr.user.core.model.Player;
import org.hplr.user.core.model.PlayerSnapshot;
import org.hplr.user.core.usecases.port.dto.PlayerSelectDto;
import org.hplr.user.core.usecases.port.in.GetAllPlayerListUseCaseInterface;
import org.hplr.user.core.usecases.port.out.query.SelectAllPlayerListQueryInterface;
import org.hplr.library.exception.HPLRValidationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetAllPlayerListUseCaseService implements GetAllPlayerListUseCaseInterface {

    final SelectAllPlayerListQueryInterface selectAllPlayerListQueryInterface;


    @Override
    public List<PlayerSnapshot> getAllPlayerList() {
        List<PlayerSelectDto> playerSelectDtoList =
                selectAllPlayerListQueryInterface.selectAllPlayerList();

        List<Player> playerList = playerSelectDtoList.stream().map(player -> {
                    try{
                        return Player.fromDto(player);
                    } catch (HPLRValidationException | NoSuchElementException e){
                        log.error("Could not map player: {}",player.playerId());
                        return null;
                    }
        }).toList();

        return playerList.stream().map(Player::toSnapshot).toList();
    }
}
