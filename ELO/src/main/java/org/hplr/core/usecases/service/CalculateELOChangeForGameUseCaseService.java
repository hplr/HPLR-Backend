package org.hplr.core.usecases.service;

import org.hplr.core.usecases.port.in.CalculateELOChangeForGameUseCaseInterface;

public class CalculateELOChangeForGameUseCaseService implements CalculateELOChangeForGameUseCaseInterface {
 //todo: refactor
    @Override
    public Integer generate_test(Integer i1) {
        return i1*15;
    }
}
