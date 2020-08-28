import javax.swing.*;
import java.util.*;

public class ClientApplication {
    // static RealFrame frame;
    public static  Queue<Message> messageQueue;
    public static void initialize() {
       messageQueue = new LinkedList<>();
        // SwingUtilities.invokeLater(new Runnable() {
        //     public void run() {
        // frame = new RealFrame("Application Main Window");
        // frame.setSize(800, 600);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setVisible(true);
        //     }
        // });

        // Runnable messenger = () -> {
        //     try {
        //         // while(true) {
        //         //     Message thing = RealFrame.requestMessage();
        //         //     if(thing != null && !thing.message.equals("")) {
        //         //         System.out.println(thing.message);
        //         //     }
        //         // }
        //         while(true) {
                    
        //             // String messageText = messageQueue.poll().message;
        //            if( messageQueue.peek() != null) {
        //             System.out.println(messageQueue.poll().message);
        //            }
        //             // System.out.println("hello?");
        //             // if(messages.peek()!= null) {
        //             //     Message item = messages.poll();
        //             //     System.out.println(item.message + "o weorpoiajsdf");
        //             // }
        //         }
        //     }
           
        //     catch(Exception e) { 
        //         System.out.println("that really wasn't suposed to happen");
        //     }
        // };
        // Thread thread2 = new Thread(messenger);
        // thread2.start();

        // while(true) {
        //     if(messageQueue.peek() != null) {
        //         Message message = messageQueue.poll();
        //         System.out.println(message.message);
        //     }
        // }
     
        // while(true) {
        //     try {
        //         if(!frame.currentMessage.equals("") || frame.currentMessage == null) {
        //             System.out.println(frame.currentMessage);
        //         }
        //     }
        //     catch(Exception e) {
        //         System.out.println("no");
        //     }
        // }

    }
    public static void addMessage(String message) {
        // System.out.println("yeee");
        if(!message.isEmpty()) {
            Message messagePacket = new Message(null, null, null, message);
            messageQueue.add(messagePacket);
            System.out.println(messageQueue.peek().message + "from me");
        }
    }
    public static Queue<Message> getQueueState() {
        return messageQueue;
    }
}