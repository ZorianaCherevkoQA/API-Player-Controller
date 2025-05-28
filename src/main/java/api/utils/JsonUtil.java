package api.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import lombok.SneakyThrows;

import java.util.*;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <K, V> Map<K, V> mapOf(Object... keyValueParams) {
        Map<K, V> map = new HashMap<>();
        for (int i = 0; i < keyValueParams.length; i += 2) {
            map.put((K) keyValueParams[i], (V) keyValueParams[i + 1]);
        }
        return map;
    }

    public static Optional<Map<String, Object>> findById(List<Map<String, Object>> items, String key, Object value) {
        return items.stream()
                .filter(map -> Objects.equals(map.get(key), value))
                .findFirst();
    }

    @SneakyThrows
    public static String getResponseValue(Response response, String key) {
        return objectMapper.readTree(response.getBody().asString()).at("/" + key).asText();
    }

    @SneakyThrows
    public static int getIntResponseValue(Response response, String key) {
        return objectMapper.readTree(response.getBody().asString()).at("/" + key).asInt();
    }
}
