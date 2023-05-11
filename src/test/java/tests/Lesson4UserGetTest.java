package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.BaseTestCase;
import lib.MyAssertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Lesson4UserGetTest extends BaseTestCase {

    @Test
    public void testGetUserDataNotAuth(){
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2");
        System.out.println(responseUserData.asString());

        MyAssertions.assertJsonHasField(responseUserData, "userName");
        MyAssertions.assertJsonHasNotField(responseUserData, "firstName");
        MyAssertions.assertJsonHasNotField(responseUserData, "lastName");
        MyAssertions.assertJsonHasNotField(responseUserData, "email");
    }

    @Test
    public void testGetUserDetailsAuthAsSameUser(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth =
                RestAssured.given().body(authData).post("https://playground.learnqa.ru/api/user/login");

        String header = getHeader(responseGetAuth, "x-csrf-token");
        String cookie = getCookie(responseGetAuth, "auth_sid");

        Response responseUserData =
                RestAssured.given()
                        .header("x-csrf-token", header)
                        .cookie("auth_sid", cookie)
                        .get("https://playground.learnqa.ru/api/user/2");

        String[] expectedFields = {"username", "firstName", "lastName", "email"};

        MyAssertions.assertJsonHasFields(responseUserData, expectedFields);

//        MyAssertions.assertJsonHasField(responseUserData, "username");
//        MyAssertions.assertJsonHasField(responseUserData, "firstName");
//        MyAssertions.assertJsonHasField(responseUserData, "lastName");
//        MyAssertions.assertJsonHasField(responseUserData, "email");
    }

}
