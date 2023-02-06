package capstone.xceed;

import capstone.xceed.api.API;
import capstone.xceed.message.XCMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Test {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 65000); // Create and connect the socket
        DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());

        XCMessage m1 = new XCMessage(1, 2, "T1", API.T1.REQUEST_HANDSHAKE.name());
        String content = m1.getJSON().toString();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(content);
        String prettyJsonString = gson.toJson(je);

        System.out.println(prettyJsonString);

        // Send first message
//        dOut.writeByte(2);
        dOut.writeUTF(content);
        dOut.flush(); // Send off the data
    }

}
