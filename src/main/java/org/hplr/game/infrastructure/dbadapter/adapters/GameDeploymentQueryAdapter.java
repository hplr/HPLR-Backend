package org.hplr.game.infrastructure.dbadapter.adapters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hplr.game.core.model.vo.GameDeployment;
import org.hplr.game.core.usecases.port.out.query.SelectAllGameDeploymentsQueryInterface;
import org.hplr.game.infrastructure.dbadapter.repositories.GameDeploymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameDeploymentQueryAdapter implements SelectAllGameDeploymentsQueryInterface {
    final GameDeploymentRepository gameDeploymentRepository;

    @Override
    public List<GameDeployment> getAllGameDeployments() {
        return gameDeploymentRepository
                .findAll()
                .stream()
                .map(gameDeployment -> new GameDeployment(
                        gameDeployment.getName()
                )).toList();
    }
}
