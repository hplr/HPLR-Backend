package org.hplr.elo.core.usecases.service;

import org.hplr.elo.core.usecases.port.in.CalculateAverageELOForGameSideUseCaseInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CalculateAverageELOForGameSideUseCaseService implements CalculateAverageELOForGameSideUseCaseInterface {
    @Override
    public Long calculateAverageELO(List<Long> eloList) {
        Optional<Long> eloSum = eloList.stream().reduce(Long::sum);
        if(eloSum.isPresent()){
            return eloSum.get()/eloList.size();
        }
        return null;
    }
}
