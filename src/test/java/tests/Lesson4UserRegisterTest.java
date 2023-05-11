package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.BaseTestCase;
import lib.DataGenerator;
import lib.MyAssertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Lesson4UserRegisterTest extends BaseTestCase {

    @Test
    public void testCreateUserWithExistedEmail(){

        String email = "vinkotov@example.com";

        Map<String, String > userData = new HashMap<>();

        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", "learnqa");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/");

        MyAssertions.assertResponseCodeEquals(responseCreateAuth, 400);
        MyAssertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email
                + "' already exists");
    }

    @Test
    public void testCreateUserSuccessfully(){

        String email = DataGenerator.getRandomEmail();

        Map<String, String > userData = new HashMap<>();

        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", "learnqa");
        userData.put("firstName", "learnqa");
        userData.put("lastName", "learnqa");

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/");

        MyAssertions.assertResponseCodeEquals(responseCreateAuth, 200);
        System.out.println(responseCreateAuth.asString());
        MyAssertions.assertJsonHasField(responseCreateAuth, "id");
    }
}
