import javax.xml.crypto.Data;
import java.util.*;
import java.net.*;
import java.io.*;
public class ObjectClient {
    public static ArrayList<User>  addressBook;
    public static void main(String[]args) throws IOException{
       
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter username");
        String username = scan.nextLine();  
        
        // System.out.println("Enter the IP address of the chat server you'd like to connect to.");
        InetAddress address = InetAddress.getByName("192.168.1.29");

        //Get the IP address of the client computer by pinging the router
        Socket s = new Socket("192.168.1.1", 80);
        // System.out.println(s.getLocalAddress().getHostAddress());
        InetAddress localAddress = InetAddress.getByName(s.getLocalAddress().getHostAddress());
        s.close();

        DatagramSocket ds = new DatagramSocket(); 
        DatagramSocket listeningSocket = new DatagramSocket(1235);


        Runnable messenger = () -> {
            try {
                while(true) {
                    String messageText = scan.nextLine();
                    Message message = new Message(username, localAddress, null, messageText);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(message);
                    oos.flush();
                    byte[] buf = bos.toByteArray();
                    DatagramPacket messagePacket = new DatagramPacket(buf, buf.length, address, 1234);
                    ds.send(messagePacket);
                }
            }
           
            catch(Exception e) { 
                    System.out.println("Yeet sending that message broke fam")
            }
        };
    

        Runnable listener = () -> {
            try {
                while(true) {
                    byte[] receive = new byte[65535]; 
                    DatagramPacket recievedMessage = new DatagramPacket(receive, receive.length);
                    listeningSocket.receive(recievedMessage);

                    byte[] messageData = recievedMessage.getData();
                    ByteArrayInputStream in = new ByteArrayInputStream(messageData);
                    ObjectInputStream is = new ObjectInputStream(in);
                    try {
                        Message message = (Message) is.readObject();
                        System.out.println(message.username+ ": " + message.message + " From: " + message.originatingIP);
                        // if(message.addressBook != null) {
                        // System.out.println(message.addressBook.get(0));
                        // addressBook = message.addressBook;
                        // System.out.println(addressBook.get(0).username);
                        // }
                    }
                    catch (Exception e) {
                        System.out.println("dang");
                    }
                    
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

    // public static class AddressBook {
    //     ArrayList <User> addresses;
    //     public AddressBook(ArrayList<User> addresses) {
    //         this.addresses = addresses;
    //     }
    // }

}