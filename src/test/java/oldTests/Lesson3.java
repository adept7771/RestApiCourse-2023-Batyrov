package oldTests;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

public class Lesson3 {



    @ParameterizedTest
    @ValueSource(strings = {"", "John", "Pete"})
    public void parametrizedExample(String name) {
        Map<String, String> queryParams = new HashMap<>();

        if(name.length() > 0){
            queryParams.put("name", name);
        }

        JsonPath response = RestAssured
                .given()
                .queryParams(queryParams)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();

        String answer = response.getString("answer");
        String expectedName = (name.length() > 0) ? name : "someone";
        Assertions.assertEquals("Hello, " + expectedName, answer, "The answer is not expected");
    }

    @Test
    public void userAuthTest(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response authResponse = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        Map<String, String> cookies = authResponse.getCookies();
        Headers headers = authResponse.getHeaders();
        int userId = authResponse.jsonPath().getInt("user_id");

        Assertions.assertEquals(200, authResponse.statusCode());
        Assertions.assertTrue(cookies.containsKey("auth_sid"));
        Assertions.assertTrue(headers.hasHeaderWithName("x-csrf-token"));
        Assertions.assertTrue(userId > 0);

        Response authCheckResponseRaw = RestAssured
                .given()
                .header("x-csrf-token", authResponse.getHeader("x-csrf-token"))
                .cookie("auth_sid", authResponse.getCookie("auth_sid"))
                .get("https://playground.learnqa.ru/api/user/auth");

        JsonPath authCheckResponse = authCheckResponseRaw.jsonPath();

        int userId1 = authCheckResponse.getInt("user_id");
        Assertions.assertTrue(userId1 > 0);

        Assertions.assertEquals(userId, userId1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void userAuthTestNegativeAuth(String condition){

        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response authResponse = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        Map<String, String> cookies = authResponse.getCookies();
        Headers headers = authResponse.getHeaders();

        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");

        if (condition.equals("cookie")){
            spec.cookie("auth_sid", cookies.get("auth_sid"));
        }
        else if(condition.equals("headers")){
            spec.header("x-csrf-token", headers.get("x-csrf-token"));
        }
        else {
            throw new IllegalArgumentException(condition);
        }

        JsonPath responseCheckJsonPath = spec.get().jsonPath();

        Assertions.assertEquals(0, responseCheckJsonPath.getInt("user_id"));
    }
}
