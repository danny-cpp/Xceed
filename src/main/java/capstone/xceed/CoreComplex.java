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
        seed.append("a".repeat(BLOCK_SIZE * 2));
        // Initiate handshaking with nodeE
        to_nodeE_queue.put(new XCMessage(0, 0, "T1", "REQUEST_HANDSHAKE", 1, 0, 0, " "));
        if (!from_nodeE_queue.take().getJSON().get("api_call").equals("SEND_ID")) {
            System.out.println("Handshaking sequence failed");
        }
        to_nodeE_queue.put(new XCMessage(0, 0, "T1", "SET_SEED", 1, 0, BLOCK_SIZE, seed.toString()));
        if (from_nodeE_queue.take().getJSON().get("api_call").equals("HS_COMPLETE")) {
            System.out.println("Handshaking sequence finished successfully");
        }

        Thread forward_to_nodeE = new Thread(() -> {
            while (true) {
                System.out.println("waiting message from frontend");
                try {
                    XCMessage message = from_front_end_queue.take();
                    System.out.println("Sending message to nodeE");
                    System.out.println(message.getJSON().toString());
                    to_nodeE_queue.put(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        Thread forward_to_frontend = new Thread(() -> {
            while (true) {
                try {
                    XCMessage message = from_nodeE_queue.take();
                    to_front_end_queue.put(message);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        forward_to_nodeE.start();
        forward_to_frontend.start();

//        connectionCLI.join();
//        connectionNodeE.join();
//        forward_to_nodeE.join();
//        forward_to_frontend.join();

    }

}
