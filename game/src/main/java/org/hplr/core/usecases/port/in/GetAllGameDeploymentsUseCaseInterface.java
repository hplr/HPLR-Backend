package org.hplr.core.usecases.port.in;

import org.hplr.core.model.vo.GameDeployment;

import java.util.List;

public interface GetAllGameDeploymentsUseCaseInterface {
    List<GameDeployment> getAllGameDeployments();
}
