import javax.xml.crypto.Data;
import java.util.*;
import java.net.*;
import java.io.*;
import javax.swing.*;  
import java.awt.event.*;  
public class GroopChat implements ActionListener{
    static WorkingMessageText wMessage;
    JLabel l1,l2;  
    JTextArea area;  
    JButton b;  
    GroopChat() {  
        JFrame f= new JFrame();  
        l1=new JLabel();  
        l1.setBounds(50,25,100,30);  
        l2=new JLabel();  
        l2.setBounds(160,25,100,30);  
        area=new JTextArea();  
        area.setBounds(20,75,250,200);  
        b=new JButton("Send Message");  
        b.setBounds(100,300,120,30);  
        b.addActionListener(this);  
        f.add(l1);f.add(l2);f.add(area);f.add(b);  
        f.setSize(450,450);  
        f.setLayout(null);  
        f.setVisible(true);  
    }



    public void actionPerformed(ActionEvent e){  
        String text=area.getText();  
        wMessage.messageText = text;
        System.out.println(wMessage.messageText);
        
    }  
    public static void main(String[]args) throws IOException{
        wMessage = new WorkingMessageText("");
        new GroopChat(); 
        

        AddressBook addressBook = new AddressBook(null);
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter username");
        String username = scan.nextLine();  
        
        // System.out.println("Enter the IP address of the chat server you'd like to connect to.");
        InetAddress address = InetAddress.getByName("10.10.2.15");

        //Get the IP address of the client computer by pinging the router
        Socket s = new Socket("10.10.0.1", 80);
        // System.out.println(s.getLocalAddress().getHostAddress());
        InetAddress localAddress = InetAddress.getByName(s.getLocalAddress().getHostAddress());
        s.close();

        DatagramSocket ds = new DatagramSocket(); 
        DatagramSocket listeningSocket = new DatagramSocket(1235);


        Runnable messenger = () -> {
            try {
                while(true) {
                    String messageText = wMessage.messageText;
                    if(!messageText.equals("")) {
                        Message message = new Message(username, localAddress, null, messageText);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(bos);
                        oos.writeObject(message);
                        oos.flush();
                        byte[] buf = bos.toByteArray();
                        DatagramPacket messagePacket = new DatagramPacket(buf, buf.length, address, 1234);
                        ds.send(messagePacket);
                        wMessage.messageText = "";
                    }
                    wMessage.messageText = "";
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
                    listeningSocket.receive(recievedMessage);

                    byte[] messageData = recievedMessage.getData();
                    ByteArrayInputStream in = new ByteArrayInputStream(messageData);
                    ObjectInputStream is = new ObjectInputStream(in);
                    try {
                        Message message = (Message) is.readObject();
                        System.out.println(message.username+ ": " + message.message + " From: " + message.originatingIP);
                        // addressBook.addresses= message.addressBook;
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

    public static class AddressBook {
        ArrayList <User> addresses;
        public AddressBook(ArrayList<User> addresses) {
            this.addresses = addresses;
        }
    }

    public static class WorkingMessageText {
        String messageText;
        public WorkingMessageText(String messageText) {
            this.messageText = messageText;
        }
    }



    
}