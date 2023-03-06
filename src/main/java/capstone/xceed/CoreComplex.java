package capstone.xceed;

import capstone.xceed.api.API;
import capstone.xceed.communication.Listener;
import capstone.xceed.message.XCMessage;

import java.util.ArrayDeque;
import java.util.Deque;

import static java.lang.Thread.sleep;

public class CoreComplex {


    //multiple threads will be accessing this queue
    //the queue will be synchronized
    private static Deque<XCMessage> task_queue = new ArrayDeque<>(10);
    //make the task queue thread safe
    //using synchronized keyword


    public static void main(String[] args) throws InterruptedException {
        //create a thread safe queue for the messages
        //make the task queue thread safe
        //using synchronized keyword

        Thread connectionCLI = new Thread(() -> {
            Listener listener = new Listener(64999, "CLI", task_queue);
        });

        Thread connectionNodeE = new Thread(() -> {
            Listener listener = new Listener(64998, "NodeE", task_queue);
        });

        connectionCLI.start();
        connectionNodeE.start();


        while (true) {
            synchronized (task_queue) {
                if (!task_queue.isEmpty()) {
                    XCMessage message = task_queue.pop();
                    System.out.println(message.getJSON().toString());
                    //if the message is a handshake message
                    //then we sleep for 3 second to mimic the behaviour of the task queue


                    if (message.getAPI().equals(API.T1.REQUEST_HANDSHAKE.name())) {
                        //print message the handshake message is received
                        System.out.println("Handshake message received, need 3 seconds to compelete");
                        try {
                            sleep(3000);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //else if the message is recieved_id, then slerp for 5 seconds
                    else if (message.getAPI().equals(API.T1.RECEIVED_ID.name())) {
                        //print message the received_id is received
                        System.out.println("Received ID message received need 5 seconds to compelete");
                        try {
                            sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    sleep(1000);
                }
            }

        }
//        connectionCLI.join();
//        connectionNodeE.join();

    }

}
