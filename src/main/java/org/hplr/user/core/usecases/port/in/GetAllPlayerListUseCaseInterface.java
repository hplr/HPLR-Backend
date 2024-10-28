package org.hplr.user.core.usecases.port.in;

import org.hplr.user.core.model.PlayerSnapshot;

import java.util.List;

public interface GetAllPlayerListUseCaseInterface {
    List<PlayerSnapshot> getAllPlayerList();
}
