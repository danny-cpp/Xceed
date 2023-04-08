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
    int payload_total_fragments;
    int payload_frag_number;
    int payload_size;
    String payload_content;

    public XCMessage(String json_string) throws Exception {
        Type mapType = new TypeToken<Map<String, Map>>(){}.getType();
        Map<String, Object> json_obj = new Gson().fromJson(json_string, HashMap.class);


        try {

            this.sender_id = ((Double) json_obj.get("sender_id")).intValue();
            this.task_id = ((Double) json_obj.get("task_id")).intValue();
            this.interface_type = (String) json_obj.get("interface_type");
            this.api_call = (String) json_obj.get("api_call");
            this.payload_total_fragments = ((Double) json_obj.get("payload_total_fragments")).intValue();
            this.payload_frag_number = ((Double) json_obj.get("payload_frag_number")).intValue();
            this.payload_size = ((Double) json_obj.get("payload_size")).intValue();
            this.payload_content = (String) json_obj.get("payload_content");

//            this.sender_id = Integer.parseInt(((json_obj.get("sender_id"))[0]);
//            this.task_id = Integer.parseInt((json_obj.get("task_id"))[0]);
//            this.interface_type = (json_obj.get("interface_type"))[0];
//            this.api_call = (json_obj.get("api_call"))[0];
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Unparsable JSON string. Cannot create XCMessage");
        }
    }

    public XCMessage(int sender_id, int task_id, String interface_type, String api_call, int payload_total_fragments,
                     int payload_frag_number,
                     int payload_size,
                     String payload_content) {
        this.sender_id = sender_id;
        this.task_id = task_id;
        this.interface_type = interface_type;
        this.api_call = api_call;
        this.payload_total_fragments = payload_total_fragments;
        this.payload_frag_number = payload_frag_number;
        this.payload_size = payload_size;
        this.payload_content = payload_content;
    }

    public JSONObject getJSON() {
        Map<String, Object> json_map = new HashMap<>();
        json_map.put("sender_id", sender_id);
        json_map.put("task_id", task_id);
        json_map.put("interface_type", interface_type);
        json_map.put("api_call", api_call);
        json_map.put("payload_total_fragments", payload_total_fragments);
        json_map.put("payload_frag_number", payload_frag_number);
        json_map.put("payload_size", payload_size);
        json_map.put("payload_content", payload_content);

        return new JSONObject(json_map);
    }

    public String getAPI() {
        //return the api_call
        return api_call;
    }

    public static void main(String[] args) throws Exception {
        XCMessage m = new XCMessage("{\"api_call\": \"ENCRYPT\", \"task_id\": 0, \"interface_type\": \"GI\", \"sender_id\": 0, \"payload_total_fragments\": 1, \"payload_fragment_number\": 1, \"payload_size\": 16, \"payload_content\": \"NH0icEQ5R3d3VA==\"}");
    }
}
