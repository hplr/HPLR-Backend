package org.hplr.library.core.util;

import java.util.UUID;

public class ConstValues {

    public static final Integer MIN_NAME_LENGTH = 2;
    public static final Integer MAX_NAME_LENGTH = 12;
    public static final UUID DEFAULT_PLAYER_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public static final int DEFAULT_CHECK_INTERVAL = 1000000000;
    public static final Long INITIAL_ELO_VALUE = 1400L;
    public static final Long TOKEN_DURATION = 75L;
    public static final Long INITIAL_WEIGHT = 400L;
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";
    public static final int ELO_CONST = 30;
    public static final int TURN_LENGTH = 7;

    private ConstValues() {
        throw new IllegalStateException("Utility class");
    }
}
