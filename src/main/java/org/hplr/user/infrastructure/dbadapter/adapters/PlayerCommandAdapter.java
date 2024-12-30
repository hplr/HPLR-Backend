package org.hplr.user.infrastructure.dbadapter.adapters;

import lombok.RequiredArgsConstructor;
import org.hplr.user.core.model.PlayerFullDataSnapshot;
import org.hplr.user.core.usecases.port.out.command.SaveLastLoginDateCommandInterface;
import org.hplr.user.core.usecases.port.out.command.SavePlayerDataCommandInterface;
import org.hplr.user.infrastructure.dbadapter.mappers.PlayerMapper;
import org.hplr.user.infrastructure.dbadapter.repositories.PlayerCommandRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerCommandAdapter implements SavePlayerDataCommandInterface, SaveLastLoginDateCommandInterface {

    final PlayerCommandRepository playerCommandRepository;
    @Override
    public void savePlayer(PlayerFullDataSnapshot playerFullDataSnapshot) {
        playerCommandRepository.save(PlayerMapper.toEntity(playerFullDataSnapshot));
    }

    @Override
    public void saveLastLoginDate(LocalDateTime lastLoginDate, UUID userId) {
        playerCommandRepository.updateLastLoginDate(lastLoginDate, userId);
    }
}
