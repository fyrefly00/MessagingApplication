import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.xml.crypto.Data;



import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket; 
import java.net.DatagramSocket; 
import java.net.InetAddress; 
import java.net.SocketException; 
import java.util.*;

public class ChatServer2 {
    public static void main(String[]args)throws IOException  {
        ArrayList<InetAddress> addressBook = new ArrayList<>();
        DatagramSocket ds = new DatagramSocket(1234); 
        byte[] receive = new byte[65535]; 
  
        DatagramPacket recievedMessage = null; 
        while(true) {
            recievedMessage = new DatagramPacket(receive, receive.length);
            ds.receive(recievedMessage);
            // ByteArrayInputStream in = new ByteArrayInputStream(data);
            // ObjectInputStream is = new ObjectInputStream(in);
            // Message message = (Message) is.readObject();
            // System.out.println(message.message);
            
            String message = data(receive).toString();
            System.out.println(message);
            String [] split = message.split(" ", 1000);
            InetAddress returnAddress = InetAddress.getByName(split[split.length - 1]);
            if(!addressBook.contains(returnAddress)) {
                System.out.println("added " + returnAddress);
                addressBook.add(returnAddress);
                // byte buf[] = null; 
                // String ack = "Welcome to the server " + split[0] + " " + returnAddress.getHostAddress();
                // buf = ack.getBytes();
                // DatagramPacket messagePacket = new DatagramPacket(buf, buf.length, returnAddress, 1234);
                // ds.send(messagePacket);
            }
            for(int i = 0; i < addressBook.size();i ++) {
                // DatagramSocket tempSocket = new DatagramSocket();

                // InetAddress group = InetAddress.getByName("224.0.0.3");
                // recievedMessage.setAddress(group);
                // recievedMessage.setPort(4446);
            if(!addressBook.get(i).getHostAddress().equals(split[split.length - 1])) {
                System.out.println(InetAddress.getByName(addressBook.get(i).getHostAddress()));
                recievedMessage.setAddress(InetAddress.getByName(addressBook.get(i).getHostAddress()));
                recievedMessage.setPort(1235);
                // System.out.println(recievedMessage.getAddress());
                // recievedMessage.setAddress(InetAddress.getByName(split[split.length-1]));
                ds.send(recievedMessage); //Added
            }

            }
            receive = new byte[65535]; 

        }
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
}