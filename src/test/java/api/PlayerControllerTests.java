package api;

import api.objects.Player;
import api.steps.GeneralSteps;
import api.steps.PlayerControllerSteps;
import io.qameta.allure.Issue;
import io.restassured.response.Response;
import jdk.jfr.Description;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static api.utils.AssertsUtil.*;
import static api.utils.Base.supervisor;
import static api.utils.JsonUtil.*;
import static api.utils.TestData.*;
import static org.testng.Assert.assertEquals;

@Slf4j
public class PlayerControllerTests {

    private final GeneralSteps generalSteps = new GeneralSteps();
    private final Player firstPlayer = new Player();
    private final Player secondPlayer = new Player();
    private final PlayerControllerSteps playerControllerSteps = new PlayerControllerSteps();

    @BeforeMethod(alwaysRun = true, onlyForGroups = "onePlayer")
    public void setupOne() {
        log.info("Setting up test with one player");
        generalSteps.createPlayer(firstPlayer);
    }

    @BeforeMethod(alwaysRun = true, onlyForGroups = "twoPlayers")
    public void setupTwo() {
        log.info("Setting up test with two players");
        generalSteps.createPlayer(firstPlayer);
        generalSteps.createPlayer(secondPlayer);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        log.info("Cleaning up created players and resetting test context");
        generalSteps.cleanUpCreatedPlayers();
        firstPlayer.clear();
        secondPlayer.clear();
    }

    // ----------------- Tests for Player Creation --------------------
    @Issue("All fields except id and login are null")
    @Test(groups = "onePlayer")
    @Description("Verify player creation as supervisor with valid queries")
    public void checkCreatePlayerAsSupervisor() {
        assertCodeAndJson(firstPlayer.getResponse(), PLAYER_JSON, 200);
        assertResponseMatchesRequestFields(firstPlayer.getRequestData(), firstPlayer.getResponse());
    }

    @Test(groups = "onePlayer")
    @Description("Verify player creation as supervisor with valid queries (null values bypass)")
    public void checkCreatePlayerAsSupervisorNullBypass() {
        assertCode(firstPlayer.getResponse(), 200);
        assertResponseMatchesRequestFields(firstPlayer.getRequestData(), playerControllerSteps.getPlayer(firstPlayer.getId()));
    }

    @Test(groups = "onePlayer")
    @Description("Verify player creation as user is forbidden with valid queries")
    public void checkCreatePlayerAsUserIsForbidden() {
        Response response = playerControllerSteps.createPlayer(firstPlayer.getLogin(), generateValidPlayer());
        assertCode(response, 403);
        generalSteps.trackCreatedPlayerInCaseSuccess(response);
    }

    // ----------------- Tests for Player Deletion --------------------
    @Test(groups = "onePlayer")
    @Description("Verify supervisor can delete player")
    public void checkDeletePlayerAsSupervisor() {
        Response response = playerControllerSteps.deletePlayer(supervisor, firstPlayer.getId());
        assertCode(response, 204);
    }

    @Test
    @Description("Verify deleting invalid player is forbidden")
    public void checkDeleteInvalidPlayerIsForbidden() {
        Response deleteResp = playerControllerSteps.deletePlayer(supervisor, Integer.MAX_VALUE);
        assertCode(deleteResp, 403);
    }

    // ----------------- Tests for Player Retrieval --------------------
    @Test(groups = "onePlayer")
    @Description("Verify retrieving existing player returns correct data")
    public void checkGetPlayer() {
        Response response = playerControllerSteps.getPlayer(firstPlayer.getId());
        assertCode(response, 200);
        assertResponseMatchesRequestFields(firstPlayer.getRequestData(), response);
    }

    @Issue("Retrieving invalid player returns 200, but empty response")
    @Test
    @Description("Verify retrieving invalid player returns error")
    public void checkGetInvalidPlayerFails() {
        assertCode(playerControllerSteps.getPlayer(Integer.MAX_VALUE), 400);
    }

    @Issue("Json schema validation fails as there are many incorrect values")
    @Test(groups = "twoPlayers")
    @Description("Verify retrieving all players returns success")
    public void checkGetAllPlayers() {
        assertCodeAndJson(playerControllerSteps.getAllPlayers(), GET_PLAYERS_JSON, 200);
    }

    // ----------------- Tests for Player Update --------------------
    @Test(groups = "onePlayer")
    @Description("Verify updating player age with valid value")
    public void checkUpdatePlayerWithValidAge() {
        Integer newAge = generateValidAge();
        Response response = playerControllerSteps.updatePlayer(supervisor, firstPlayer.getId(),
                AGE, newAge);
        assertCodeAndJson(response, PLAYER_JSON, 200);
        assertEquals(getIntResponseValue(response, AGE), newAge);
    }

    @Test(groups = "twoPlayers")
    @Description("Verify updating player age with with duplicate login fails")
    public void checkUpdatePlayerWithSameLoginFails() {
        Response response = playerControllerSteps.updatePlayer(supervisor, firstPlayer.getId(),
                LOGIN, secondPlayer.getLogin());
        assertCode(response, 409);
    }




    // ----------------- Bugs covered --------------------
    @DataProvider(name = "missingRequiredField")
    public Object[][] missingRequiredField() {
        return new Object[][]{
                {AGE}, {GENDER}, {LOGIN}, {PASSWORD}, {ROLE}, {SCREEN_NAME}
        };
    }

    @Issue("Creating player without password is successful")
    @Test(dataProvider = "missingRequiredField")
    @Description("Verify creating player fails when required field is missing")
    public void checkCreatePlayerWithMissingRequiredFieldFails(String missingField) {
        Map<String, Object> player = generateValidPlayer();
        player.remove(missingField);

        Response response = playerControllerSteps.createPlayer(supervisor, player);
        assertEquals(response.getStatusCode(), 400,  "Should fail due to missing " + missingField);
        generalSteps.trackCreatedPlayerInCaseSuccess(response);
    }

    @Issue("Creating player with duplicate login is successful")
    @Test(groups = "onePlayer")
    @Description("Verify creating player with duplicate login is forbidden")
    public void checkCreatePlayerWithSameLoginIsForbidden() {
        Map<String, Object> secondPlayer = generateValidPlayer();
        secondPlayer.put(LOGIN, firstPlayer.getLogin());

        Response response = playerControllerSteps.createPlayer(supervisor, secondPlayer);
        assertCode(response, 403);
        generalSteps.trackCreatedPlayerInCaseSuccess(response);
    }

    @Issue("Creating player with duplicate screenName is successful")
    @Test(groups = "onePlayer")
    @Description("Verify creating player with duplicate screenName is forbidden")
    public void checkCreatePlayerWithSameScreenIsForbidden() {
        Map<String, Object> secondPlayer = generateValidPlayer();
        secondPlayer.put(SCREEN_NAME, getResponseValue(playerControllerSteps.getPlayer(firstPlayer.getId()), SCREEN_NAME));
        Response response = playerControllerSteps.createPlayer(supervisor, secondPlayer);
        assertCode(response, 403);
        generalSteps.trackCreatedPlayerInCaseSuccess(response);
    }

    @Issue("Updating player with duplicate screenName is successful")
    @Test(groups = "twoPlayers")
    @Description("Verify updating player with duplicate screenName fails")
    public void checkUpdatePlayerWithSameScreenFails() {
        String secondPlayerScreen = getResponseValue(playerControllerSteps.getPlayer(secondPlayer.getId()), SCREEN_NAME);
        Response response = playerControllerSteps.updatePlayer(supervisor, firstPlayer.getId(), SCREEN_NAME, secondPlayerScreen);
        assertCode(response, 409);
    }

    @Issue("Player can delete themselves")
    @Test(groups = "onePlayer")
    @Description("Verify player cannot delete themselves")
    public void checkDeletePlayerHimselfIsForbidden() {
        Response deleteResp = playerControllerSteps.deletePlayer(firstPlayer.getLogin(), firstPlayer.getId());
        assertCode(deleteResp, 403);
    }
}
