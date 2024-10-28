package org.hplr.user.core.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hplr.user.core.model.Player;

@Getter
@Setter
@AllArgsConstructor
public class PlayerPair {

    private Player firstPlayer;
    private Player secondPlayer;
}
