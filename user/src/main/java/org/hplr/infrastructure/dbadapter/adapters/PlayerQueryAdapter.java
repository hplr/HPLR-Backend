package org.hplr.infrastructure.dbadapter.adapters;

import org.hplr.core.usecases.port.dto.PlayerSelectDto;
import org.hplr.core.usecases.port.out.query.SelectPlayerByUserIdQueryInterface;
import org.hplr.infrastructure.dbadapter.entities.PlayerEntity;
import org.hplr.infrastructure.dbadapter.mappers.PlayerMapper;
import org.hplr.infrastructure.dbadapter.repositories.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerQueryAdapter implements SelectPlayerByUserIdQueryInterface {
    final PlayerRepository playerRepository;

    public PlayerQueryAdapter(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Optional<PlayerSelectDto> selectPlayerByUserId(UUID userId) throws NoSuchElementException {
        PlayerEntity playerEntity = playerRepository.findByUserId(userId).orElseThrow(NoSuchElementException::new);
        return Optional.of(PlayerMapper.fromEntity(playerEntity));
    }
}
