package api.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import lombok.SneakyThrows;

import static org.apache.http.HttpHeaders.DATE;

public class AllureUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static String getAllureResponse(String endpoint, Response resp, Object body) {

        String request_date = resp.getHeader(DATE);
        String responseStatus = String.valueOf(resp.getStatusCode());
        String requestBody = body != null ? objectMapper.writeValueAsString(body) : "";

        String threadInfo = "Thread: " + Thread.currentThread().getName();

        return "\n\nRequest date: " + request_date +
                "\n\nEndpoint: " + endpoint +
                "\n\nRequest body: " + requestBody +
                "\n\nResponse status code: " + responseStatus +
                "\n\nResponse: " + resp.getBody().asString() +
                "\n\n" + threadInfo;
    }
}
