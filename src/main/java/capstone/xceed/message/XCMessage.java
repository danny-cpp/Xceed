package capstone.xceed.message;


import capstone.xceed.api.API;
import org.json.JSONObject;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class XCMessage {

    int sender_id;
    int task_id;
    String interface_type;
    String api_call;

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
}
