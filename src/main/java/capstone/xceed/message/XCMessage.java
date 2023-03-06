package capstone.xceed.message;


import capstone.xceed.api.API;
import org.apache.kafka.common.protocol.types.Field;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.Type;

public class XCMessage {

    int sender_id;
    int task_id;
    String interface_type;
    String api_call;

    public XCMessage(String json_string) throws Exception {
        Type mapType = new TypeToken<Map<String, Map>>(){}.getType();
        Map<String, Object> json_obj = new Gson().fromJson(json_string, HashMap.class);




        try {

            this.sender_id = Integer.parseInt((String)json_obj.get("sender_id"));
            this.task_id = Integer.parseInt((String) json_obj.get("task_id"));
            this.interface_type = (String) json_obj.get("interface_type");
            this.api_call = (String) json_obj.get("api_call");

//            this.sender_id = Integer.parseInt(((json_obj.get("sender_id"))[0]);
//            this.task_id = Integer.parseInt((json_obj.get("task_id"))[0]);
//            this.interface_type = (json_obj.get("interface_type"))[0];
//            this.api_call = (json_obj.get("api_call"))[0];
        }
        catch (Exception e) {
            throw new Exception("Unparsable JSON string. Cannot create XCMessage");
        }
    }

    public XCMessage(int sender_id, int task_id, String interface_type, String api_call) {
        this.sender_id = sender_id;
        this.task_id = task_id;
        this.interface_type = interface_type;
        this.api_call = api_call;
    }

    public JSONObject getJSON() {
        Map<String, Object> json_map = new HashMap<>();
        json_map.put("sender_id", Integer.toString(sender_id));
        json_map.put("task_id", Integer.toString(task_id));
        json_map.put("interface_type", interface_type);
        json_map.put("api_call", api_call);

        return new JSONObject(json_map);
    }

    public String getAPI() {
        //return the api_call
        return api_call;
    }
}
