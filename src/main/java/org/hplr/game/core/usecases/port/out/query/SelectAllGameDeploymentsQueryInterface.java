package org.hplr.game.core.usecases.port.out.query;

import org.hplr.game.core.model.vo.GameDeployment;

import java.util.List;

public interface SelectAllGameDeploymentsQueryInterface {
    List<GameDeployment> getAllGameDeployments();
}
