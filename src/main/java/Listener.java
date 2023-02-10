// A Java program for a Server
import capstone.xceed.message.XCMessage;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Listener {

    // constructor with port
    public Listener(int port, String components_name, List<XCMessage> task_queue) {
        // starts server and waits for a connection
        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Listening on port " + port);

            System.out.println("Waiting for " + components_name);

            //initialize socket and input stream
            Socket socket = server.accept();
            System.out.println(components_name + "successfully connected");

            // takes input from the client socket
            DataInputStream in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));

            String line;
            StringBuilder json_builder = new StringBuilder();

            // reads message from client until "Over" is sent
            while (true) {
                try {
                    line = in.readUTF();
                    json_builder.append(line);
                }
                catch(EOFException e) {
                    try {
                        XCMessage message = new XCMessage(json_builder.toString());
                    }
                    catch (Exception unparsable) {
                        System.out.println("A message received was unparsable: " + json_builder.toString());
                    }
                }
            }

            // close connection
            // socket.close();
            // in.close();
        }
        catch(IOException i) {
            System.out.println("Connection with " + components_name + "is terminated because an exception occurs");
            System.out.println(i);
        }
    }

    public static void main(String[] args) {

        List<XCMessage> tq = new ArrayList<>();
        Listener listener = new Listener(65000, "abc", tq);
    }
}