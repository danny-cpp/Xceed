package capstone.xceed;

import capstone.xceed.api.API;
import capstone.xceed.communication.Listener;
import capstone.xceed.message.XCMessage;
import org.apache.kafka.common.protocol.types.Field;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

public class CoreComplex {

    static final int BLOCK_SIZE = 1024;
    static BlockingDeque<XCMessage> from_nodeE_queue = new LinkedBlockingDeque<>();
    static BlockingDeque<XCMessage> to_nodeE_queue = new LinkedBlockingDeque<>();
    static BlockingDeque<XCMessage> from_front_end_queue = new LinkedBlockingDeque<>();
    static BlockingDeque<XCMessage> to_front_end_queue = new LinkedBlockingDeque<>();
    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        //create a thread safe queue for the messages
        //make the task queue thread safe
        //using synchronized keyword

        CyclicBarrier pipe_created = new CyclicBarrier(3);

        Thread connectionCLI = new Thread(() -> {
            Listener<BlockingDeque<XCMessage>> listener = new Listener<>(64999, "CLI",
                    from_front_end_queue,
                    to_front_end_queue,
                    pipe_created);
        });

        Thread connectionNodeE = new Thread(() -> {
            Listener<BlockingDeque<XCMessage>> listener = new Listener<>(64998, "NodeE",
                    from_nodeE_queue,
                    to_nodeE_queue,
                    pipe_created);
        });

        connectionCLI.start();
        connectionNodeE.start();

        pipe_created.await();
        System.out.println("-------------------------------");
        System.out.println("Pipe created successfully");

        StringBuilder seed = new StringBuilder();
        seed.append("a".repeat(BLOCK_SIZE));
        // Initiate handshaking with nodeE
        to_nodeE_queue.put(new XCMessage(0, 0, "T1", "REQUEST_HANDSHAKE", 1, 0, BLOCK_SIZE, ""));
        if (!from_nodeE_queue.take().getJSON().get("api_call").equals("SEND_ID")) {
            System.out.println("Handshaking sequence failed");
        }
        to_nodeE_queue.put(new XCMessage(0, 0, "T1", "SET_SEED", 1, 0, BLOCK_SIZE, seed.toString()));
        if (from_nodeE_queue.take().getJSON().get("api_call").equals("HS_COMPLETE")) {
            System.out.println("Handshaking sequence finished successfully");
        }


        // while (true) {
        //     synchronized (to_nodeE_queue) {
        //         if (!to_nodeE_queue.isEmpty()) {
        //             XCMessage message = to_nodeE_queue.pop();
        //             System.out.println(message.getJSON().toString());
        //             //if the message is a handshake message
        //             //then we sleep for 3 second to mimic the behaviour of the task queue
        //
        //
        //             if (message.getAPI().equals(API.T1.REQUEST_HANDSHAKE.name())) {
        //                 //print message the handshake message is received
        //                 System.out.println("Handshake message received, need 3 seconds to compelete");
        //                 try {
        //                     sleep(3000);
        //
        //                 } catch (InterruptedException e) {
        //                     e.printStackTrace();
        //                 }
        //             }
        //             //else if the message is recieved_id, then slerp for 5 seconds
        //             else if (message.getAPI().equals(API.T1.RECEIVED_ID.name())) {
        //                 //print message the received_id is received
        //                 System.out.println("Received ID message received need 5 seconds to compelete");
        //                 try {
        //                     sleep(5000);
        //                 } catch (InterruptedException e) {
        //                     e.printStackTrace();
        //                 }
        //             }
        //         }
        //     }
        //
        // }
       connectionCLI.join();
       connectionNodeE.join();

    }

}
