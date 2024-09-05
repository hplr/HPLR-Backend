package org.hplr.infrastructure.dbadapter.adapters;

import lombok.RequiredArgsConstructor;
import org.hplr.core.model.PlayerFullDataSnapshot;
import org.hplr.core.usecases.port.out.command.SaveLastLoginDateCommandInterface;
import org.hplr.core.usecases.port.out.command.SavePlayerDataCommandInterface;
import org.hplr.infrastructure.dbadapter.mappers.PlayerMapper;
import org.hplr.infrastructure.dbadapter.repositories.PlayerCommandRepository;
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
