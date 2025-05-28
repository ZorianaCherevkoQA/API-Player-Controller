package api.objects;

import io.restassured.response.Response;

import java.util.Map;

public class Player {

    private final ThreadLocal<Map<String, Object>> requestData = new ThreadLocal<>();
    private final ThreadLocal<Response> response = new ThreadLocal<>();
    private final ThreadLocal<Integer> id = new ThreadLocal<>();
    private final ThreadLocal<String> login = new ThreadLocal<>();

    public void setRequestData(Map<String, Object> data) {
        requestData.set(data);
    }

    public Map<String, Object> getRequestData() {
        return requestData.get();
    }

    public void setResponse(Response resp) {
        response.set(resp);
    }

    public Response getResponse() {
        return response.get();
    }

    public void setId(Integer playerId) {
        id.set(playerId);
    }

    public Integer getId() {
        return id.get();
    }

    public void setLogin(String playerLogin) {
        login.set(playerLogin);
    }

    public String getLogin() {
        return login.get();
    }

    public void clear() {
        requestData.remove();
        response.remove();
        id.remove();
        login.remove();
    }
}
