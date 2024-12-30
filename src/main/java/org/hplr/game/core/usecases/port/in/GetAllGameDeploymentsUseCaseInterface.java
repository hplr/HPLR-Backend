package org.hplr.game.core.usecases.port.in;

import org.hplr.game.core.model.vo.GameDeployment;

import java.util.List;

public interface GetAllGameDeploymentsUseCaseInterface {
    List<GameDeployment> getAllGameDeployments();
}
