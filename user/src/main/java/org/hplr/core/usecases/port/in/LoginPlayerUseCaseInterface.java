package org.hplr.core.usecases.port.in;

import org.hplr.core.usecases.port.dto.GetTokenResponseDto;
import org.hplr.core.usecases.port.dto.PlayerLoginDto;

public interface LoginPlayerUseCaseInterface {
    GetTokenResponseDto loginPlayer(PlayerLoginDto playerLoginDto);
}
