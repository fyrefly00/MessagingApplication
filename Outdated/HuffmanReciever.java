import java.io.ObjectInputStream;
import java.net.DatagramSocket;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import javax.xml.crypto.Data;
import java.util.*;
import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;

public class HuffmanReciever {
    public static void main(String[] args) throws IOException {
        DatagramSocket ds = new DatagramSocket(1234);

        while (true) {
            DatagramPacket recievedMessage;

            recievedMessage = null;
            byte[] receive = new byte[65535];
            HuffmanPacket packet = null;
            recievedMessage = new DatagramPacket(receive, receive.length);
            ds.receive(recievedMessage);
            System.out.println("Received packet");
            byte[] messageData = recievedMessage.getData();
            ByteArrayInputStream in = new ByteArrayInputStream(messageData);
            ObjectInputStream is = new ObjectInputStream(in);
            try {
                packet = (HuffmanPacket) is.readObject();
                System.out.println(packet.encoded);
                System.out.println(packet.encodedMessage);
                if (packet.encoded == true) {
                    System.out.println("Decoded Message: ");
                    System.out.println(packet.encodedMessage);
                    decode(packet.encodedMessage, packet.queue);
                } else {
                    System.out.println("No De-encoding");
                    System.out.println(packet.encodedMessage);
                }
            } catch (Exception e) {

            }
        }

        // DatagramSocket ds = new DatagramSocket(1234);

        // while (true) {
        //     byte[] dataBuf = new byte[65536];
        //     DatagramPacket packet = new DatagramPacket(dataBuf, dataBuf.length);

        //     ds.receive(packet);
        //     System.out.println("Received packet");
            
        //     byte[] messageData = packet.getData();
        //     ByteArrayInputStream bais = new ByteArrayInputStream(messageData);
        //     ObjectInputStream ois = new ObjectInputStream(bais);
        //     try {
        //         HuffmanPacket p = (HuffmanPacket) ois.readObject();
        //         System.out.println(p.encodedMessage);
        //     } catch (Exception e) {
        //         System.out.println("Error: " + e.getMessage());
        //     }
        // }

    }

    public static void decode(String inString, PriorityQueue<Node> queue) {
        Node temp = queue.peek();
        String finalString = "";
        for (int i = 0; i < inString.length(); i++) {
            if (temp.leftChild == null && temp.rightChild == null) { // If leaf
                finalString = finalString + temp.value;
                temp = queue.peek();
            }
            if (inString.charAt(i) == '0') {
                temp = temp.leftChild;
            }
            if (inString.charAt(i) == '1') {
                temp = temp.rightChild;
            }

        }

        System.out.println(finalString);
    }
}