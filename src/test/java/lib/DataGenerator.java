package lib;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DataGenerator {

    public static String getRandomEmail(){
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return "test" + timestamp + "@example.com";
    }

    public static Map<String, String> getRegistrationData(){
        Map<String, String> data = new HashMap<>();
        data.put("email", getRandomEmail());
        data.put("password", "123");
        data.put("username", "learnQA");
        data.put("firstName", "learnQA");
        data.put("lastName", "learnQA");
        return data;
    }

    public static Map<String, String> getRegistrationData(Map<String, String> nonDefaultValues){

        Map<String, String> defaultValues = getRegistrationData();

        Map<String, String> userData = new HashMap<>();

        String[] keys = {"username", "firstName", "lastName", "email", "password"};

        for (String key : keys){
            if(nonDefaultValues.containsKey(key)){
                userData.put(key, nonDefaultValues.get(key));
            } else {
                userData.put(key, defaultValues.get(key));
            }
        }
        return userData;
    }
}
