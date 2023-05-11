package lib;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;

public class MyAssertions {

    public static void assertJsonByName(Response response, String name, int expectedValue){
        response.then().body("$", hasKey(name));

        int value = response.jsonPath().getInt(name);
        Assertions.assertEquals(expectedValue, value);
    }

    public static void assertResponseTextEquals(Response response, String expectedAnswer){
        Assertions.assertEquals(expectedAnswer, response.asString());
    }

    public static void assertResponseCodeEquals(Response response, int expectedCode){
        Assertions.assertEquals(expectedCode, response.getStatusCode());
    }

    public static void assertJsonHasFields(Response response, String[] expectedFieldNames){
        for(String expectedFieldName : expectedFieldNames){
            assertJsonHasField(response, expectedFieldName);
        }
    }

    public static void assertJsonHasField(Response response, String expectedFieldName){
        response.then().body("$", hasKey(expectedFieldName));
    }

    public static void assertJsonHasNotField(Response response, String expectedFieldName){
        response.then().body("$", not(hasKey(expectedFieldName)));
    }
}
