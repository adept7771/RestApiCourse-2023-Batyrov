package oldTests;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

public class Lesson3Update {

    String cookie, header;
    int userIdOnAuth;
    Response authResponse;

    @BeforeEach
    public void loginUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        authResponse = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        this.cookie = authResponse.getCookie("auth_sid");
        this.header = authResponse.getHeader("x-csrf-token");
        this.userIdOnAuth = authResponse.jsonPath().getInt("user_id");
    }

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
        JsonPath authCheckResponse = authResponse.jsonPath();

        int userId1 = authCheckResponse.getInt("user_id");

        Assertions.assertTrue(userId1 > 0);
        Assertions.assertEquals(userIdOnAuth, userId1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void userAuthTestNegativeAuth(String condition){

        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");

        if (condition.equals("cookie")){
            spec.cookie("auth_sid", cookie);
        }
        else if(condition.equals("headers")){
            spec.header("x-csrf-token", header);
        }
        else {
            throw new IllegalArgumentException(condition);
        }

        JsonPath responseCheckJsonPath = spec.get().jsonPath();

        Assertions.assertEquals(0, responseCheckJsonPath.getInt("user_id"));
    }
}
