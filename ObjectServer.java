import javax.xml.crypto.Data;
import java.util.*;
import java.net.*;
import java.io.*;

public class ObjectServer {
    public static void main(String[]args)throws IOException  {

        ArrayList<User> addressBook = new ArrayList<>();
        DatagramSocket ds = new DatagramSocket(1234); 
        byte[] receive = new byte[65535]; 

        //Constantly listen for incoming packets
        DatagramPacket recievedMessage = null; 
        while(true) {
            recievedMessage = new DatagramPacket(receive, receive.length);
            ds.receive(recievedMessage);
            byte[] messageData = recievedMessage.getData();
            ByteArrayInputStream in = new ByteArrayInputStream(messageData);
            ObjectInputStream is = new ObjectInputStream(in);
            try {
                //Deserialize a recieved message
                Message message = (Message) is.readObject();
                System.out.println(message.username+ ": " + message.message + " From: " + message.originatingIP);
                User tempUser = new User(message.username, message.originatingIP);
                if(!addressBook.contains(tempUser)) {
                    addressBook.add(tempUser);
                    Message ackPack = new Message("Server", message.originatingIP, message.originatingIP, message.username + " has joined the server" /*, addressBook*/);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(ackPack);
                    oos.flush();
                    byte[] buf = bos.toByteArray(); 
                    DatagramPacket ack = new DatagramPacket(buf, buf.length, message.originatingIP, 1235);
                    ds.send(ack);
                }

                //Reserialze and prepare to send to all other clients
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(message);
                oos.flush();
                byte[] buf = bos.toByteArray();
                for(int i = 0; i < addressBook.size(); i ++) {
                    //Forward the message to everyone except the original sender
                   if(!addressBook.get(i).equals(tempUser)) {
                        DatagramPacket messagePacket = new DatagramPacket(buf, buf.length, InetAddress.getByName(addressBook.get(i).IPAddress.getHostAddress()), 1235);
                        ds.send(messagePacket);
                   }
                }
            }
            catch (Exception e) {
                System.out.println("darn");
            }
        }
    }
}
