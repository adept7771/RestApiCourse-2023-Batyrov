package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.BaseTestCase;
import lib.DataGenerator;
import lib.MyAssertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Lesson4UserEditTest extends BaseTestCase {

    @Test
    public void editJustCreatedUserTest(){
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = RestAssured.given().body(userData)
                .post("https://playground.learnqa.ru/api/user/").jsonPath();

        String userId = responseCreateAuth.getString("id");

        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = RestAssured.given()
                .body(authData).post("https://playground.learnqa.ru/api/user/login");

        String newName = "ChangedName";

        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured.given().header("x-csrf-token", getHeader(responseGetAuth, "x-csrf-token"))
                .cookies("auth_sid", getCookie(responseGetAuth, "auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId);

        Response responseUserData = RestAssured.given()
                .header("x-csrf-token", getHeader(responseGetAuth, "x-csrf-token"))
                .cookies("auth_sid", getCookie(responseGetAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId);

        MyAssertions.assertJsonByName(responseUserData, "firstName", newName);

    }
}
