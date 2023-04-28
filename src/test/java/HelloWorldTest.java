import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HelloWorldTest {

    @Test
    public void testRestAssured1() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "John");

        JsonPath response = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();

        response.prettyPrint();

        String answer = response.get("answer");
        System.out.println(answer);
    }

    @Test
    public void testRestAssured2_paramsAsStrings() {
        Response response = RestAssured
                .given()
                .queryParam( "param1" , "value1")
                .get( "https://playground.learnqa.ru/api/check_type")
                .andReturn();

        response.print();
    }

    @Test
    public void testRestAssured3_bodyMap() {
        Map<String, Object> body = new HashMap<>();
        body.put("param1" , "value1");
        body.put("param2" , "value1");

        Response response = RestAssured
                .given()
                .body(body)
                .post( "https://playground.learnqa.ru/api/check_type")
                .andReturn();

        response.print();
    }

    @Test
    public void testRestAssured4_redirects() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get( "https://playground.learnqa.ru/api/get_303")
                .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);

        String locationHeader = response.getHeader("Location");
        System.out.println(locationHeader);
    }

    @Test
    public void testRestAssured5_headers() {

        Map<String, String> headers = new HashMap<>();
        headers.put("MuHeader1", "MyValue1");
        headers.put("MuHeader2", "MyValue2");

        Response response = RestAssured
                .given()
                .headers(headers)
                .when()
                .get( "https://playground.learnqa.ru/api/show_all_headers")
                .andReturn();

        response.prettyPrint();

        Headers headersResp = response.getHeaders();

        System.out.println(headersResp);
    }

    @Test
    public void testRestAssured6_cookies() {

        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass");

        Response response = RestAssured
                .given()
                .body(data)
                .when()
                .post( "https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        System.out.println("\n Response:");
        response.prettyPrint();

        System.out.println("\nHeaders:");
        System.out.println(response.getHeaders());

        System.out.println("\nCookies:");
        System.out.println(response.getCookies());
    }

    @Test
    public void testRestAssured7_wrongCookies() {

        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login_wrong");
        data.put("password", "secret_pass_wrong");

        Response response = RestAssured
                .given()
                .body(data)
                .when()
                .post( "https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        System.out.println("\n Response:");
        response.prettyPrint();

        System.out.println("\nHeaders:");
        System.out.println(response.getHeaders());

        System.out.println("\nCookies:");
        System.out.println(response.getCookies());
    }

    @Test
    public void testRestAssured8_saveCookies() {

        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass");

        Response response = RestAssured
                .given()
                .body(data)
                .when()
                .post( "https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        String authCookie = response.getCookies().get("auth_cookie");

        Map<String, String> cookies = new HashMap<>();
        cookies.put("auth_cookie", authCookie);

        response = RestAssured
                .given()
                .body(data)
                .cookies(cookies)
                .when()
                .post( "https://playground.learnqa.ru/api/check_auth_cookie")
                .andReturn();

        response.print();
    }

    @Test
    public void testHomeWork1() {
        JsonPath jsonPath = RestAssured
                .given()
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        jsonPath.prettyPrint();

        Object messages = jsonPath.get("messages");

        ArrayList<Object> messagesList = (ArrayList<Object>) messages;

        Object secondMessage = messagesList.get(1);

        System.out.println(secondMessage.toString());
    }

    @Test
    public void testHomeWork2() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(true)
                .get( "https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String locationHeader = response.getHeader("X-Host");
        System.out.println(locationHeader);
    }

    @Test
    public void testHomeWork3() {

        String urlToCall = "https://playground.learnqa.ru/api/long_redirect";
        int statusCode = 0;

        while (statusCode != 200){

            System.out.println("Url to call: " + urlToCall);

            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .get(urlToCall)
                    .andReturn();

            urlToCall = response.getHeader("Location");

            if (urlToCall == null){
                urlToCall = response.getHeader("X-Host");
            }

            statusCode = response.getStatusCode();
        }
    }

    @Test
    public void testHomeWork4() {

    }
}
