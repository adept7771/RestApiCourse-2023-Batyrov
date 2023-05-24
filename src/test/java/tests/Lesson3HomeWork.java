package tests;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.BaseTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

public class Lesson3HomeWork extends BaseTestCase {

    @Test
    public void ex10StringTest() {
        String testString = "123456789012345";
        Assertions.assertTrue(testString.length() >= 15);
    }

    @Test
    public void ex11CookieTest() {
        Response response = RestAssured.given()
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        Assertions.assertEquals("hw_value", getCookie(response, "HomeWork"));
    }

    @Test
    public void ex12HeadersTest() {
        Response response = RestAssured.given()
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();

        Assertions.assertTrue(getHeader(response, "x-secret-homework-header").contains("Some secret value"));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30|" +
                    "Mobile|No|Android",
            "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1|" +
                    "Mobile|Chrome|iOS",
            "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)|" +
                    "Googlebot|Unknown|Unknown",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0|" +
                    "Web|Chrome|No",
            "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1|" +
                    "Mobile|No|iPhone"},
            delimiter = '|')
    public void ex13UserAgentTest(String userAgent, String platform, String browser, String device) {

        JsonPath jsonPath = RestAssured.given()
                .header("User-Agent", userAgent)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check").jsonPath();

        boolean platformIsCorrect = platform.equals(jsonPath.get("platform"));
        boolean browserIsCorrect = browser.equals(jsonPath.get("browser"));
        boolean deviceIsCorrect = device.equals(jsonPath.get("device"));

        if (platformIsCorrect == false || browserIsCorrect == false || deviceIsCorrect == false){
            System.out.println("Current user agent: " + userAgent);
            if (platformIsCorrect == false){
                System.out.println("Platform value: " + jsonPath.get("platform") + " (is wrong) | Expected value: " + platform);
            }
            if (browserIsCorrect == false){
                System.out.println("browser value: " + jsonPath.get("browser") + " (is wrong) | Expected value: " + browser);
            }
            if (deviceIsCorrect == false){
                System.out.println("device value: " + jsonPath.get("device") + " (is wrong) | Expected value: " + device);
            }
            Assertions.fail("Test failed with wrong values. Check the log.");
        }
        else {
            System.out.println("Current user agent: " + userAgent);
            System.out.println("All values is correct.");
        }
    }
}
