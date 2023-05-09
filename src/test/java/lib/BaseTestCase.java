package lib;

import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.Map;

import static org.hamcrest.Matchers.hasKey;

public class BaseTestCase {

    protected String getHeader(Response response, String name){
        Headers headers = response.getHeaders();
        Assertions.assertTrue(headers.hasHeaderWithName(name));
        return headers.getValue(name);
    }

    protected String getCookie(Response response, String name){
        Map<String, String> cookies = response.getCookies();

        Assertions.assertTrue(cookies.containsKey(name));
        return cookies.get(name);
    }

    protected Map<String, String> getAllCookies(Response response){
        return response.getCookies();
    }

    protected Headers getAllHeaders(Response response){
        return response.getHeaders();
    }

    protected int getIntFromJson(Response response, String name){
        response.then().assertThat().body("$", hasKey(name));
        return response.jsonPath().getInt(name);
    }
}
