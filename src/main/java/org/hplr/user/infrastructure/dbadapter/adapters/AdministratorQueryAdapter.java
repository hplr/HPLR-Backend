package org.hplr.user.infrastructure.dbadapter.adapters;

import org.hplr.user.core.usecases.port.dto.AdministratorSelectDto;
import org.hplr.user.core.usecases.port.out.query.*;
import org.hplr.user.infrastructure.dbadapter.entities.AdministratorEntity;
import org.hplr.user.infrastructure.dbadapter.mappers.AdministratorMapper;
import org.hplr.user.infrastructure.dbadapter.repositories.AdministratorQueryRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service

public class AdministratorQueryAdapter implements SelectAdministratorByEmailQueryInterface {
    final AdministratorQueryRepository administratorQueryRepository;

    public AdministratorQueryAdapter(AdministratorQueryRepository administratorQueryRepository) {
        this.administratorQueryRepository = administratorQueryRepository;
    }


    @Override
    public Optional<AdministratorSelectDto> selectAdministratorByEmail(String email) {
        Optional<AdministratorEntity> administratorEntity = administratorQueryRepository.findByEmail(email);
        if(administratorEntity.isEmpty()){
            throw new NoSuchElementException("Administrator not found!");
        }
        return Optional.of(AdministratorMapper.toDto(administratorEntity.get()));
    }
}
