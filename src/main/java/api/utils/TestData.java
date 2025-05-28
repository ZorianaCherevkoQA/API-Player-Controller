package api.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static api.enums.Gender.MALE;
import static api.enums.UserRole.USER;

public class TestData {

    //keys
    public static final String EDITOR = "editor";
    public static final String ID = "id";
    public static final String AGE = "age";
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String SCREEN_NAME = "screenName";
    public static final String ROLE = "role";
    public static final String GENDER = "gender";
    public static final String PLAYERS = "players";

    //json file names
    public static final String JSON_FOLDER = "jsonSchemas/";

    public static final String PLAYER_JSON = JSON_FOLDER + "player.json";
    public static final String GET_PLAYERS_JSON = JSON_FOLDER + "getPlayers.json";


    public static Map<String, Object> generateValidPlayer() {
        return new HashMap<>(Map.of(
                AGE, String.valueOf(generateValidAge()),
                GENDER, MALE.getGender(),
                LOGIN, generateUniqueLogin(),
                PASSWORD, generateValidPassword(),
                ROLE, USER.getRole(),
                SCREEN_NAME, generateUniqueScreenName()
        ));
    }

    public static String generateUniqueLogin() {
        return LOGIN + UUID.randomUUID().toString().replace("-", "");
    }

    public static String generateUniqueScreenName() {
        return SCREEN_NAME + UUID.randomUUID().toString().replace("-", "");
    }

    public static String generateValidPassword() {
        String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder sb = new StringBuilder(PASSWORD);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < 12; i++) {
            sb.append(symbols.charAt(random.nextInt(symbols.length())));
        }
        return sb.toString();
    }

    public static int generateValidAge() {
        return ThreadLocalRandom.current().nextInt(17, 60);
    }
}
