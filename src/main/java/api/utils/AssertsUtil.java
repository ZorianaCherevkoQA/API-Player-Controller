package api.utils;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.asserts.SoftAssert;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.Assert.*;

@Slf4j
public class AssertsUtil {

    @Step("Assert code and json")
    public static void assertCodeAndJson(Response response, String jsonFileName, int statusCode) {
        log.info("Asserting response code is {} and matches schema {}", statusCode, jsonFileName);
        assertEquals(response.getStatusCode(), statusCode);
        assertThat(response.getBody().asString(), matchesJsonSchemaInClasspath(jsonFileName));
    }

    @Step("Assert code and json")
    public static void assertCode(Response response,  int statusCode) {
        log.info("Asserting response code is {}", statusCode);
        assertEquals(response.getStatusCode(), statusCode);
    }

    @Step("Assert response matches required fields")
    public static void assertResponseMatchesRequestFields(Map<String, Object> expected, Response response) {
        SoftAssert softAssert = new SoftAssert();

        log.info("Starting comparison of expected fields against response...");
        for (String key : expected.keySet()) {
            Object expectedValue = expected.get(key);
            Object actualValue = response.jsonPath().getString(key);

            log.debug("Comparing field '{}': expected={}, actual={}", key, expectedValue, actualValue);

            softAssert.assertEquals(
                    actualValue,
                    String.valueOf(expectedValue),
                    String.format("Field '%s' does not match. Expected: %s, Actual: %s", key, expectedValue, actualValue)
            );
        }
        softAssert.assertAll();
        log.info("Finished asserting response fields");
    }
}
