package capstone.xceed.communication;// A Java program for a Server
import capstone.xceed.api.API;
import capstone.xceed.message.XCMessage;

import java.net.*;
import java.io.*;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CyclicBarrier;


public class Listener <E extends BlockingDeque<XCMessage>> {

    // constructor with port
    public Listener(int port, String components_name,
                     E incoming,
                     E outgoing,
                     CyclicBarrier proceed) {
        // starts server and waits for a connection
        try {
            ServerSocket server = new ServerSocket(port);
            // System.out.println("Listening on port " + port);

            System.out.println("Waiting for " + components_name + " on port " + port);

            //initialize socket and input stream
            Socket socket = server.accept();
            System.out.println(components_name + " successfully connected");

            try {
                proceed.await();
            }
            catch (Exception e) {
                System.out.println("Barrier fail");
                e.printStackTrace();
            }

            // Writer thread
            Thread writer = new Thread(() -> {
                OutputStream output;
                try {
                    output = socket.getOutputStream();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                while (true) {
                    XCMessage to_be_sent;
                    try {
                        to_be_sent = outgoing.take();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        String temp = to_be_sent.getJSON().toString();
                        output.write(temp.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            });
            writer.start();

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
                        incoming.put(message);
                        //clear the string builder
                        json_builder.setLength(0);

                    }
                    catch (Exception unparsable) {
                        System.out.println("A message received was unparsable: " + json_builder.toString());

                    }
                }
            }

            writer.join();

            // close connection
            socket.close();
            reader.close();
        }
        catch(IOException i) {
            System.out.println("Connection with " + components_name + "is terminated because an exception occurs");
            System.out.println(i);
        } catch (InterruptedException e) {
            System.out.println("Interrupt occurs");
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        Deque<XCMessage> tq = new ArrayDeque<>(10);


        // Listener listener = new Listener(64999, "abc", tq, );
    }
}