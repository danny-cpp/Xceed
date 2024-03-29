package capstone.xceed;

import capstone.xceed.api.API;
import capstone.xceed.message.XCMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 64998); // Create and connect the socket
        OutputStream outputStream = socket.getOutputStream();

        String to_be_encrypted = "To be or not to be";
        XCMessage m1 = new XCMessage(1, 2, "T1", API.T1.RECEIVED_ID.name(), 1, 0, to_be_encrypted.length(), to_be_encrypted);
        String content = m1.getJSON().toString();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(content);
        String prettyJsonString = gson.toJson(je);

        System.out.println(prettyJsonString);
        System.out.println(content);

        // Send first message
           // dOut.writeByte(0);
        outputStream.write(content.getBytes());
        outputStream.write("\n".getBytes());
        outputStream.flush(); // Send off the data

//        Scanner input = new Scanner(System.in);
//        input.next();


    //send a another message, this time is receiver_id
        XCMessage m2 = new XCMessage(1, 2, "T1", API.T1.RECEIVED_ID.name(), 1, 0, to_be_encrypted.length(), to_be_encrypted);
        String content2 = m2.getJSON().toString();

        Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp2 = new JsonParser();
        JsonElement je2 = jp2.parse(content2);
        String prettyJsonString2 = gson2.toJson(je2);

        System.out.println(prettyJsonString2);
        System.out.println(content2);

        // Send first message
        // dOut.writeByte(0);
        outputStream.write(content2.getBytes());
        outputStream.write("\n".getBytes());
        outputStream.flush(); // Send off the data

        // Close the socket
//        socket.close();
    }

}
