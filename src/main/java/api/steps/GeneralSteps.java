package api.steps;

import api.objects.Player;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static api.utils.Base.supervisor;
import static api.utils.JsonUtil.getIntResponseValue;
import static api.utils.JsonUtil.getResponseValue;
import static api.utils.TestData.*;

@Slf4j
public class GeneralSteps {

    private final PlayerControllerSteps playerControllerSteps = new PlayerControllerSteps();
    private final ThreadLocal<List<Integer>> createdUsers = ThreadLocal.withInitial(ArrayList::new);

    @Step("Create player")
    public void createPlayer(Player context) {
        Map<String, Object> data = generateValidPlayer();
        Response response = playerControllerSteps.createPlayer(supervisor, data);

        Integer id = getIntResponseValue(response, ID);
        String login = getResponseValue(response, LOGIN);
        log.info("Player created with ID: {}, Login: {}", id, login);

        context.setRequestData(data);
        context.setResponse(response);
        context.setId(id);
        context.setLogin(login);

        trackCreatedPlayer(id);
    }

    public void trackCreatedPlayerInCaseSuccess(Response response) {
        if (response.getStatusCode() == 200) {
            log.info("Tracking unexpectedly successfully created player with ID: {}",
                    Integer.valueOf(response.jsonPath().getString(ID)));
            trackCreatedPlayer(Integer.valueOf(response.jsonPath().getString(ID)));
        }
    }

    public void trackCreatedPlayer(Integer userId) {
        log.debug("Adding user ID {} to tracked created users", userId);
        createdUsers.get().add(userId);
    }

    @Step("Clean up created users")
    public void cleanUpCreatedPlayers() {
        List<Integer> usersToDelete = createdUsers.get();
        log.info("Cleaning up {} created players: {}", usersToDelete.size(), usersToDelete);

        createdUsers.get().forEach(playerControllerSteps::deleteRemainingPlayer);
        createdUsers.remove();
        log.info("Cleanup complete. Cleared tracked user list.");
    }
}
