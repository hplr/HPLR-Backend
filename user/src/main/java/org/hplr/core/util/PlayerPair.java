package org.hplr.core.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hplr.core.model.Player;

@Getter
@Setter
@AllArgsConstructor
public class PlayerPair {

    private Player firstPlayer;
    private Player secondPlayer;
}
