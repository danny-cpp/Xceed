import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

public class TestAcceptor {

    public static void main(String[] args) {


        Type type = new TypeToken<Map<String, String>>(){}.getType();
//        Map<String, String> myMap = gson.fromJson("{'k1':'apple','k2':'orange'}", type);
    }
}
