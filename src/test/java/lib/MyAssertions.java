package lib;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import static org.hamcrest.Matchers.hasKey;

public class MyAssertions {

    public static void assertJsonByName(Response response, String name, int expectedValue){
        response.then().body("$", hasKey(name));

        int value = response.jsonPath().getInt(name);
        Assertions.assertEquals(expectedValue, value);
    }
}
