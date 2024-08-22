package org.hplr.core.usecases.port.out.query;

import org.hplr.core.model.vo.GameDeployment;

import java.util.List;

public interface SelectAllGameDeploymentsQueryInterface {
    List<GameDeployment> getAllGameDeployments();
}
