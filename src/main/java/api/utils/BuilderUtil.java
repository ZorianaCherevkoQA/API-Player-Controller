package api.utils;

import api.builders.PlayerId;

public class BuilderUtil {


    public static PlayerId buildPlayerId(Integer playerId) {
        return PlayerId.builder().playerId(playerId).build();
    }

}
