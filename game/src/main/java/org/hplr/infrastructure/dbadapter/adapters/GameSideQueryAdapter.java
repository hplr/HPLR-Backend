package org.hplr.infrastructure.dbadapter.adapters;

import lombok.RequiredArgsConstructor;
import org.hplr.core.usecases.port.dto.GameSideSelectDto;
import org.hplr.core.usecases.port.out.query.SelectGameSideBySideIdQueryInterface;
import org.hplr.exception.HPLRIllegalStateException;
import org.hplr.infrastructure.dbadapter.entities.GameSideEntity;
import org.hplr.infrastructure.dbadapter.mappers.GameSideDatabaseMapper;
import org.hplr.infrastructure.dbadapter.repositories.GameSideRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameSideQueryAdapter implements SelectGameSideBySideIdQueryInterface {

    private final GameSideRepository gameSideRepository;

    @Override
    public GameSideSelectDto selectGameSideBySideId(UUID sideId) {
        Optional<GameSideEntity> gameSideEntityOptional = gameSideRepository.findBySideId(sideId);
        if (gameSideEntityOptional.isPresent()) {
            return GameSideDatabaseMapper.fromEntity(gameSideEntityOptional.get());
        }
        throw new HPLRIllegalStateException("Game side not found!");
    }
}
