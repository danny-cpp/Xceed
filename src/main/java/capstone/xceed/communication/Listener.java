package capstone.xceed.communication;// A Java program for a Server
import capstone.xceed.message.XCMessage;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;



public class Listener {

    // constructor with port
    public Listener(int port, String components_name, Deque<XCMessage> task_queue) {
        // starts server and waits for a connection
        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Listening on port " + port);

            System.out.println("Waiting for " + components_name);

            //initialize socket and input stream
            Socket socket = server.accept();
            System.out.println(components_name + " successfully connected");

            // takes input from the client socket
            // DataInputStream in = new DataInputStream(
            //         new BufferedInputStream(socket.getInputStream()));
            //
            String line;
            StringBuilder json_builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // reads message from client until "Over" is sent
            while ((line = reader.readLine()) != null) {

                System.out.println(line);
                json_builder.append(line);
                System.out.println(line.length());


                if (line.charAt(line.length() - 1) == '}') {
                    try {
                        XCMessage message = new XCMessage(json_builder.toString());
                        System.out.println(json_builder.toString());
                        //add to the task queue using synchronized
                        synchronized (task_queue) {
                            task_queue.add(message);
                        }
                        //clear the string builder
                        json_builder.setLength(0);

                    }
                    catch (Exception unparsable) {
                        System.out.println("A message received was unparsable: " + json_builder.toString());

                    }
                }
            }


            // close connection
            socket.close();
            reader.close();
        }
        catch(IOException i) {
            System.out.println("Connection with " + components_name + "is terminated because an exception occurs");
            System.out.println(i);
        }
    }

    public static void main(String[] args) {

        Deque<XCMessage> tq = new ArrayDeque<>(10);

        Listener listener = new Listener(64999, "abc", tq);
    }
}