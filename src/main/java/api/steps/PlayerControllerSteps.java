package api.steps;

import api.builders.PlayerId;
import api.objects.SuperUser;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static api.endpoints.Endpoints.*;
import static api.utils.ApiServices.*;
import static api.utils.Base.supervisor;
import static api.utils.BuilderUtil.buildPlayerId;
import static api.utils.JsonUtil.findById;
import static api.utils.JsonUtil.mapOf;
import static api.utils.TestData.*;
import static org.testng.Assert.assertEquals;

@Slf4j
public class PlayerControllerSteps {

    @Step("Create player")
    public Response createPlayer(String superUserLogin,  Map<String, Object> queryParams) {
        log.info("Creating player with editor: {} and data: {}", superUserLogin, queryParams);
        HashMap<String, String> pathParams = new HashMap<>();
        pathParams.put(EDITOR, superUserLogin);

        return getRequest(CREATE_PLAYER, null, null, queryParams, pathParams);
    }

    @Step("Create player")
    public Response createPlayer(SuperUser superUser, Map<String, Object> queryParams) {
        return  createPlayer(superUser.getLogin(), queryParams);
    }

    @Step("Delete player")
    public Response deletePlayer(String superUserLogin, Integer playerId) {
        log.info("Deleting player with ID {} by editor {}", playerId, superUserLogin);
        HashMap<String, String> pathParams = new HashMap<>();
        pathParams.put(EDITOR, superUserLogin);

        PlayerId playerIdBody = buildPlayerId(playerId);
        return deleteRequest(DELETE_PLAYER, null, playerIdBody, null, pathParams);
    }

    @Step("Delete player")
    public Response deletePlayer(SuperUser superUser, Integer playerId) {
        return  deletePlayer(superUser.getLogin(), playerId);
    }

    @Step("Get player")
    public Response getPlayer(Integer playerId) {
        log.info("Getting player with ID: {}", playerId);
        return postRequest(GET_PLAYER, null, buildPlayerId(playerId), null, null);
    }

    @Step("Get all players")
    public Response getAllPlayers() {
        log.info("Fetching list of all players");
        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("size", "4");
        return getRequest(GET_PLAYERS, null, null, queryParams, null);
    }

    @Step("Update player")
    public Response updatePlayer(String superUserLogin, Integer userId, Object... keyValueParams) {
        log.info("Updating player with ID {} by editor {} with params: {}", userId, superUserLogin, Arrays.toString(keyValueParams));
        HashMap<String, String> pathParams = new HashMap<>();
        pathParams.put(EDITOR, superUserLogin);
        pathParams.put(ID, String.valueOf(userId));

        return patchRequest(UPDATE_PLAYER, null, mapOf(keyValueParams), null, pathParams);
    }

    @Step("Update player")
    public Response updatePlayer(SuperUser superUser, Integer userId, Object... keyValueParams) {
        return updatePlayer(superUser.getLogin(), userId, keyValueParams);
    }

    @Step("Delete remaining players")
    public void deleteRemainingPlayer(Integer playerId) {
        List<Map<String, Object>> players = getAllPlayers().jsonPath().getList(PLAYERS);
        log.info("Searching for player with ID {} to delete", playerId);

        findById(players, ID, playerId)
                .ifPresent(player -> {
                    Response deleteResp = deletePlayer(supervisor, playerId);
                    assertEquals(deleteResp.statusCode(), 204, "Failed to delete user with id: " + playerId);
                    log.info("Player with ID {} successfully deleted", playerId);
                });
    }

}
