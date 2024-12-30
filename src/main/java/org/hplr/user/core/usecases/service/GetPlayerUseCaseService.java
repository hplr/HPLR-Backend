package org.hplr.user.core.usecases.service;

import lombok.AllArgsConstructor;
import org.hplr.user.core.model.Player;
import org.hplr.user.core.model.PlayerSnapshot;
import org.hplr.user.core.usecases.port.dto.PlayerSelectDto;
import org.hplr.user.core.usecases.port.in.GetPlayerUseCaseInterface;
import org.hplr.user.core.usecases.port.out.query.SelectPlayerByUserIdQueryInterface;
import org.hplr.library.exception.HPLRValidationException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetPlayerUseCaseService implements GetPlayerUseCaseInterface {

    SelectPlayerByUserIdQueryInterface selectPlayerByUserIdQueryInterface;

    @Override
    public PlayerSnapshot getPlayerByUserId(UUID userId) throws NoSuchElementException, HPLRValidationException {
        Optional<PlayerSelectDto> playerSelectDtoOptional =
                selectPlayerByUserIdQueryInterface.selectPlayerByUserId(userId);

        Player player = Player.fromDto(playerSelectDtoOptional.orElseThrow(NoSuchElementException::new));

        return player.toSnapshot();
    }
}
