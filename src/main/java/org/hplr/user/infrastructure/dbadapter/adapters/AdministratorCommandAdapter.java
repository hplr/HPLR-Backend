package org.hplr.user.infrastructure.dbadapter.adapters;

import lombok.RequiredArgsConstructor;
import org.hplr.user.core.model.AdministratorSnapshot;
import org.hplr.user.core.usecases.port.out.command.SaveAdministratorDataCommandInterface;
import org.hplr.user.core.usecases.port.out.command.SaveLastAdministratorLoginDateCommandInterface;
import org.hplr.user.infrastructure.dbadapter.entities.AdministratorEntity;
import org.hplr.user.infrastructure.dbadapter.mappers.AdministratorMapper;
import org.hplr.user.infrastructure.dbadapter.repositories.AdministratorCommandRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdministratorCommandAdapter implements SaveAdministratorDataCommandInterface, SaveLastAdministratorLoginDateCommandInterface {

    private final AdministratorCommandRepository administratorCommandRepository;

    @Override
    public void saveAdministrator(AdministratorSnapshot administratorSnapshot) {
        AdministratorEntity administratorEntity = AdministratorMapper.toEntity(administratorSnapshot);
        administratorCommandRepository.save(administratorEntity);
    }

    @Override
    public void saveLastLoginDate(LocalDateTime lastLoginDate, UUID userId) {
        administratorCommandRepository.updateLastLoginDate(lastLoginDate, userId);
    }
}
