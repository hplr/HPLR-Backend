package org.hplr.core.usecases.service;

import lombok.RequiredArgsConstructor;
import org.hplr.core.model.vo.GameDeployment;
import org.hplr.core.usecases.port.in.GetAllGameDeploymentsUseCaseInterface;
import org.hplr.core.usecases.port.out.query.SelectAllGameDeploymentsQueryInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetAllGameDeploymentsUseCaseService implements GetAllGameDeploymentsUseCaseInterface {

    final SelectAllGameDeploymentsQueryInterface selectAllGameDeploymentsQueryInterface;

    @Override
    public List<GameDeployment> getAllGameDeployments() {
        return selectAllGameDeploymentsQueryInterface.getAllGameDeployments();
    }
}
