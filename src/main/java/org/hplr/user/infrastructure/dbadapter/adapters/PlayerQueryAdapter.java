package org.hplr.user.infrastructure.dbadapter.adapters;

import org.hplr.user.core.usecases.port.dto.PlayerSelectDto;
import org.hplr.user.core.usecases.port.out.query.SelectAllPlayerByIdListQueryInterface;
import org.hplr.user.core.usecases.port.out.query.SelectAllPlayerListQueryInterface;
import org.hplr.user.core.usecases.port.out.query.SelectPlayerByEmailQueryInterface;
import org.hplr.user.core.usecases.port.out.query.SelectPlayerByUserIdQueryInterface;
import org.hplr.user.infrastructure.dbadapter.entities.PlayerEntity;
import org.hplr.user.infrastructure.dbadapter.mappers.PlayerMapper;
import org.hplr.user.infrastructure.dbadapter.repositories.PlayerQueryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service

public class PlayerQueryAdapter implements SelectPlayerByUserIdQueryInterface, SelectAllPlayerListQueryInterface, SelectAllPlayerByIdListQueryInterface, SelectPlayerByEmailQueryInterface {
    final PlayerQueryRepository playerQueryRepository;

    public PlayerQueryAdapter(PlayerQueryRepository playerQueryRepository) {
        this.playerQueryRepository = playerQueryRepository;
    }

    @Override
    public Optional<PlayerSelectDto> selectPlayerByUserId(UUID userId) throws NoSuchElementException {
        PlayerEntity playerEntity = playerQueryRepository.findByUserId(userId).orElseThrow(NoSuchElementException::new);
        return Optional.of(PlayerMapper.toDto(playerEntity));
    }

    @Override
    public List<PlayerSelectDto> selectAllPlayerList() {
        List<PlayerEntity> playerEntityList = playerQueryRepository.findAll();
        return playerEntityList.stream().map(PlayerMapper::toDto).toList();
    }

    @Override
    public List<PlayerSelectDto> selectAllPlayerByIdList(List<UUID> idList) {
        List<PlayerEntity> playerEntityList = playerQueryRepository.findAllByUserIdIn(idList);
        return playerEntityList.stream().map(PlayerMapper::toDto).toList();
    }

    @Override
    public Optional<PlayerSelectDto> selectPlayerByEmail(String email) throws NoSuchElementException {
        Optional<PlayerEntity> playerEntity = playerQueryRepository.findByEmail(email);
        if(playerEntity.isEmpty()){
            throw new NoSuchElementException("Player not found!");
        }
        return Optional.of(PlayerMapper.toDto(playerEntity.get()));
    }
}
