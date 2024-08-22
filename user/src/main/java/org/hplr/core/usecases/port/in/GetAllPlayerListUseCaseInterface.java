package org.hplr.core.usecases.port.in;

import org.hplr.core.model.PlayerSnapshot;

import java.util.List;

public interface GetAllPlayerListUseCaseInterface {
    List<PlayerSnapshot> getAllPlayerList();
}
