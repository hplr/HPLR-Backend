package org.hplr.core.util;

import java.util.UUID;

public class ConstValues {

    public static final Integer MIN_NAME_LENGTH = 2;
    public static final Integer MAX_NAME_LENGTH = 12;
    public static final UUID DEFAULT_PLAYER_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private ConstValues() {
        throw new IllegalStateException("Utility class");
    }
}
