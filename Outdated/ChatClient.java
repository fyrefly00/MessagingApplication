import java.io.ByteArrayOutputStream;
import java.io.IOException; 
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.InetAddress; 
import java.util.Scanner; 
import java.net.*;
import java.io.*;

public class ChatClient {
    public static void main(String [] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter username");
        String username = scan.nextLine();  
        
        System.out.println("Enter the IP address of the chat server you'd like to connect to.");
        InetAddress address = InetAddress.getByName("10.10.1.245");

        //Get the IP address of the client computer by pinging the router
        Socket s = new Socket("10.10.1.1", 80);
        // System.out.println(s.getLocalAddress().getHostAddress());
        InetAddress localAddress = InetAddress.getByName(s.getLocalAddress().getHostAddress());
        s.close();

        DatagramSocket ds = new DatagramSocket(); 
        // DatagramSocket listeningSocket = new DatagramSocket(1235);
        MulticastSocket socket = new MulticastSocket(4446);
        InetAddress group = InetAddress.getByName("224.0.0.3");
        socket.joinGroup(group);

        Runnable messenger = () -> {
            try {
                while(true) {
                    byte buf[] = null; 
                    String message = username + ": " +  scan.nextLine() + " From: " + localAddress.getHostAddress();
                    buf = message.getBytes();
                    DatagramPacket messagePacket = new DatagramPacket(buf, buf.length, address, 1234);
                    ds.send(messagePacket);
                }
            }
           
            catch(Exception e) { 
                System.out.println("that really wasn't suposed to happen");
            }
        };
    

        Runnable listener = () -> {
            try {
                while(true) {
                    byte[] receive = new byte[65535]; 
                    DatagramPacket recievedMessage = new DatagramPacket(receive, receive.length);
                    socket.receive(recievedMessage);
                    // listeningSocket.receive(recievedMessage);
                    String message = data(receive).toString();
                    String [] split = message.split(" ", 1000);
                    // System.out.println(split[split.length-1]);
                    // if(!split[split.length - 1].equals(localAddress.getHostAddress())) { //For ignoring irrelevant messages
                        System.out.println(data(receive));
                    // }
                }
            }
            catch(Exception e) { 
                System.out.println("that wasn't suposed to happen");
            }
         };
                
        Thread thread2 = new Thread(messenger);
        thread2.start();
        
        Thread thread = new Thread(listener);
        thread.start();
    }
    public static StringBuilder data(byte[] a) 
    { 
        if (a == null) 
            return null; 
        StringBuilder ret = new StringBuilder(); 
        int i = 0; 
        while (a[i] != 0) 
        { 
            ret.append((char) a[i]); 
            i++; 
        } 
        return ret; 
    } 
    public static class Message {
        String username;
        InetAddress originatingIP;
        String message;
        InetAddress destination;
        public Message(String username, InetAddress originatingIP, String message) {
            this.username = username;
            this.originatingIP = originatingIP;
            this.message = message;
        }
    }
}