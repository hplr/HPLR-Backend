package org.hplr.user.core.usecases.port.in;

import org.hplr.user.core.usecases.port.dto.GetTokenResponseDto;
import org.hplr.user.core.usecases.port.dto.PlayerLoginDto;

public interface LoginPlayerUseCaseInterface {
    GetTokenResponseDto loginPlayer(PlayerLoginDto playerLoginDto);
}
