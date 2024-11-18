package org.hplr.library.core.util;

public class ConstDatabaseNames {

    public static final String PLAYER_TABLE = "player";
    public static final String ARMY_TABLE = "army";
    public static final String ARMY_TYPE_TABLE = "army_type";
    public static final String DEPLOYMENT_TABLE = "deployment";
    public static final String GAME_TABLE = "game";
    public static final String MISSION_TABLE = "mission";
    public static final String GAME_PLAYER_TABLE = "game_player";
    public static final String GAME_SIDE_TABLE = "game_side";
    public static final String GAME_TURN_SCORE_TABLE = "game_turn_score";
    public static final String LOCATION_TABLE = "location";
    public static final String LOCATION_GEO_DATA_TABLE = "location_geo_data";
    public static final String ADMINISTRATOR_TABLE = "administrator";
    public static final String ROLE_TABLE = "role";
    public static final String SCORE_TABLE = "score";
    public static final String TOURNAMENT_TABLE = "tournament";
    public static final String TOURNAMENT_ROUND_TABLE = "tournament_round";

    public static final String PLAYER_RANKING_VIEW = "PlayerRanking_v";

    private ConstDatabaseNames() {
        throw new IllegalStateException("Utility class");
    }
}
