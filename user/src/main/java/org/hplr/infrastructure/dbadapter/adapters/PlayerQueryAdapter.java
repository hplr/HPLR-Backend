package org.hplr.infrastructure.dbadapter.adapters;

import org.hplr.core.usecases.port.dto.PlayerSelectDto;
import org.hplr.core.usecases.port.out.query.SelectAllPlayerByIdListQueryInterface;
import org.hplr.core.usecases.port.out.query.SelectAllPlayerListQueryInterface;
import org.hplr.core.usecases.port.out.query.SelectPlayerByUserIdQueryInterface;
import org.hplr.infrastructure.dbadapter.entities.PlayerEntity;
import org.hplr.infrastructure.dbadapter.mappers.PlayerMapper;
import org.hplr.infrastructure.dbadapter.repositories.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerQueryAdapter implements SelectPlayerByUserIdQueryInterface, SelectAllPlayerListQueryInterface, SelectAllPlayerByIdListQueryInterface {
    final PlayerRepository playerRepository;

    public PlayerQueryAdapter(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Optional<PlayerSelectDto> selectPlayerByUserId(UUID userId) throws NoSuchElementException {
        PlayerEntity playerEntity = playerRepository.findByUserId(userId).orElseThrow(NoSuchElementException::new);
        return Optional.of(PlayerMapper.toDto(playerEntity));
    }

    @Override
    public List<PlayerSelectDto> selectAllPlayerList() {
        List<PlayerEntity> playerEntityList = playerRepository.findAll();
        return playerEntityList.stream().map(PlayerMapper::toDto).toList();
    }

    @Override
    public List<PlayerSelectDto> selectAllPlayerByIdList(List<UUID> idList) {
        List<PlayerEntity> playerEntityList = playerRepository.findAllByUserIdIn(idList);
        return playerEntityList.stream().map(PlayerMapper::toDto).toList();
    }
}
