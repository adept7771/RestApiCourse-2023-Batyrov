package oldTests;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class Lesson1_2 {

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
    public void testHomeWork4() throws InterruptedException {
        Response response = RestAssured
                .given()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn();

        String token = response.jsonPath().getString("token");

        response = RestAssured
                .given()
                .param("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn();

        String status = response.jsonPath().getString("status");

        Assertions.assertEquals("Job is NOT ready", status);

        Thread.sleep(20000);

        response = RestAssured
                .given()
                .param("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn();

        status = response.jsonPath().getString("status");
        String result = response.jsonPath().getString("result");

        Assertions.assertEquals("Job is ready", status);
        Assertions.assertNotNull(result);
        Assertions.assertNotEquals("0", result);
    }

    @Test
    public void testHomeWork5() throws InterruptedException {

        String allPasswordsFromWikiPage = "1\tpassword\tpassword\t123456\t123456\t123456\t123456\t123456\t123456\t123456\n" +
                "2\t123456\t123456\tpassword\tpassword\tpassword\tpassword\tpassword\tpassword\t123456789\n" +
                "3\t12345678\t12345678\t12345678\t12345\t12345678\t12345\t12345678\t123456789\tqwerty\n" +
                "4\tqwerty\tabc123\tqwerty\t12345678\tqwerty\t12345678\tqwerty\t12345678\tpassword\n" +
                "5\tabc123\tqwerty\tabc123\tqwerty\t12345\tfootball\t12345\t12345\t1234567\n" +
                "6\tmonkey\tmonkey\t123456789\t123456789\t123456789\tqwerty\t123456789\t111111\t12345678\n" +
                "7\t1234567\tletmein\t111111\t1234\tfootball\t1234567890\tletmein\t1234567\t12345\n" +
                "8\tletmein\tdragon\t1234567\tbaseball\t1234\t1234567\t1234567\tsunshine\tiloveyou\n" +
                "9\ttrustno1\t111111\tiloveyou\tdragon\t1234567\tprincess\tfootball\tqwerty\t111111\n" +
                "10\tdragon\tbaseball\tadobe123[a]\tfootball\tbaseball\t1234\tiloveyou\tiloveyou\t123123\n" +
                "11\tbaseball\tiloveyou\t123123\t1234567\twelcome\tlogin\tadmin\tprincess\tabc123\n" +
                "12\t111111\ttrustno1\tadmin\tmonkey\t1234567890\twelcome\twelcome\tadmin\tqwerty123\n" +
                "13\tiloveyou\t1234567\t1234567890\tletmein\tabc123\tsolo\tmonkey\twelcome\t1q2w3e4r\n" +
                "14\tmaster\tsunshine\tletmein\tabc123\t111111\tabc123\tlogin\t666666\tadmin\n" +
                "15\tsunshine\tmaster\tphotoshop[a]\t111111\t1qaz2wsx\tadmin\tabc123\tabc123\tqwertyuiop\n" +
                "16\tashley\t123123\t1234\tmustang\tdragon\t121212\tstarwars\tfootball\t654321\n" +
                "17\tbailey\twelcome\tmonkey\taccess\tmaster\tflower\t123123\t123123\t555555\n" +
                "18\tpassw0rd\tshadow\tshadow\tshadow\tmonkey\tpassw0rd\tdragon\tmonkey\tlovely\n" +
                "19\tshadow\tashley\tsunshine\tmaster\tletmein\tdragon\tpassw0rd\t654321\t7777777\n" +
                "20\t123123\tfootball\t12345\tmichael\tlogin\tsunshine\tmaster\t!@#$%^&*\twelcome\n" +
                "21\t654321\tjesus\tpassword1\tsuperman\tprincess\tmaster\thello\tcharlie\t888888\n" +
                "22\tsuperman\tmichael\tprincess\t696969\tqwertyuiop\thottie\tfreedom\taa123456\tprincess\n" +
                "23\tqazwsx\tninja\tazerty\t123123\tsolo\tloveme\twhatever\tdonald\tdragon\n" +
                "24\tmichael\tmustang\ttrustno1\tbatman\tpassw0rd\tzaq1zaq1\tqazwsx\tpassword1\tpassword1\n" +
                "25\tFootball\tpassword1\t000000\ttrustno1\tstarwars\tpassword1\ttrustno1\tqwerty123\t123qwe";

        String[] split = allPasswordsFromWikiPage.split("\t");
        Set<String> mostCommonPasswords = new HashSet<>();

        for (String pass : split) {
            if(pass.contains("\n")){
                pass = pass.substring(0, pass.indexOf("\n"));
            }
            mostCommonPasswords.add(pass);
        }

        String realPassword = null;

        for(String iteratedPassword : mostCommonPasswords){
            Response response = RestAssured
                    .given()
                    .param("login", "super_admin")
                    .param("password", iteratedPassword)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();

            if(response.getStatusCode() == 500){
                continue;
            }

            Map<String, String> cookies = response.getCookies();

            response = RestAssured
                    .given()
                    .cookies(cookies)
                    .get("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();

            if (response.asString().contains("You are authorized")){
                realPassword = iteratedPassword;
                break;
            }
        }

        Assertions.assertNotNull(realPassword);
        System.out.println("Real password is: " + realPassword);
    }
}
