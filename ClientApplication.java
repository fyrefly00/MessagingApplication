/***********************
 * Author: Robert Walker
 ***********************/

import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.*;


public class ClientApplication extends JFrame  {
    public static  String currentMessage = "";
    String username;
    InetAddress address;
    InetAddress localAddress;
    DatagramSocket ds;
    MulticastSocket socket;
    InetAddress group;
    DatagramSocket listeningSocket;
    public static JTextArea conversationHistory;
    public ClientApplication(String title) {

        //Reuse the JFrame constructor
        super(title);
        try {
            username = JOptionPane.showInputDialog("Enter a username");
            
            //Proof of concept: this could be included to allow a user to switch between servers. Harcoded value for now
            // String remoteAddress = JOptionPane.showInputDialog("Enter the IP address of the chat server you'd like to connect to.");
            address = InetAddress.getByName("10.10.2.82"); //Should be changed to the IP address of whichever computer is running the server

            //Get the IP address of the client computer by pinging the router. Needs to be changed based on the network configuration
            Socket s = new Socket("10.10.1.1", 80);
            localAddress = InetAddress.getByName(s.getLocalAddress().getHostAddress());
            s.close();

            //Set up the listener socket to recieve messages from the server
            ds = new DatagramSocket(); 
            socket = new MulticastSocket(4446);
            group = InetAddress.getByName("224.0.0.3");
            socket.joinGroup(group);
            listeningSocket = new DatagramSocket(1235);

        }
        catch (Exception e) {}


        //GUI Layout Stuff
        setLayout(new BorderLayout()); //Basic layout system

        //Important input elements
        JTextArea textBox = new JTextArea();
        conversationHistory = new JTextArea();
        JButton button = new JButton("Send message");

        //Distinguishes various elements
        Border blackline = BorderFactory.createLineBorder(Color.black);
        textBox.setBorder(blackline);
    
        //Add panels to container in the correct locations
        Container c = getContentPane();
        c.add(conversationHistory, BorderLayout.CENTER);
        c.add(textBox, BorderLayout.SOUTH);
        c.add(button, BorderLayout.EAST);
        textBox.setPreferredSize(new Dimension(400, 75));
        conversationHistory.setEditable(false);

        //Functionality for the send button
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                //Read the data out of the text field
                String messageText = textBox.getText();
                //Display it in the conversation window
                displayMessage(messageText, true, conversationHistory);

                //Create a new Message object with all relevant information
                Message message = new Message(username, localAddress, null, messageText);

                //Serialize it
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(message);
                oos.flush();
                byte[] buf = bos.toByteArray();

                //Sent it
                DatagramPacket messagePacket = new DatagramPacket(buf, buf.length, address, 1234);
                ds.send(messagePacket);
                }
                catch(Exception f) {}

                //Clear the input box
                textBox.setText(null);
            }
            
      });

     //Listener thread to run concurrently with program 
    Runnable listener = () -> {
        try {
            while(true) {
                //Always listen on the socket; recieve bytes as they come
                byte[] receive = new byte[65535]; 
                DatagramPacket recievedMessage = new DatagramPacket(receive, receive.length);
                listeningSocket.receive(recievedMessage);
                   
                //Deserialize
                byte[] messageData = recievedMessage.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(messageData);
                ObjectInputStream is = new ObjectInputStream(in);
                try {
                    Message message = (Message) is.readObject();
                    displayMessage(message.username+ ": " + message.message + " From: " + message.originatingIP, false, conversationHistory);
                }
                catch (Exception e) {
                    System.out.println("dang");
                }
                // Thread.sleep(2000);
                
            }
        }
        catch(Exception e) { 
            System.out.println("that wasn't suposed to happen");
        }
     };

     //Start the thread
     Thread thread = new Thread(listener);
     thread.start();
    }

    //Message formatting method to display things properly
    public static void displayMessage(String message, Boolean local, JTextArea conversationHistory) {

        String choppedMessage = "";

        //If this message is from the same computer
        if(local == true) {
            choppedMessage += "                                                                                                                                                                                                                                                                                                                                                ";
        }

        //Length delimeter
        if(message.length() > 50) {
            int currentLength = 0;
          for(int i = 0; i < message.length(); i ++) {
              if(currentLength < 50) {
                  choppedMessage +=  message.substring(i, i + 1);
                  currentLength ++;
              }
              else {
                if(local == true) {
                    choppedMessage += "\n" + "                                                                                                                                                                                                                                                                                                                                                " +  message.substring(i, i + 1);
                }
                else {
                  choppedMessage += "\n" +  message.substring(i, i + 1);
                 
                }
                currentLength = 0;
              }
          }
        }
        else {
            if(local == true) {
                choppedMessage = "                                                                                                                                                                                                                                                                                                                                                " + message + "\n";
            }
            else {
                 choppedMessage = message + "\n";
             }
        }
        //Append the formatted to the conversation window
        conversationHistory.append(choppedMessage);
    }

    public static void main(String args[]) {
        ClientApplication frame = new ClientApplication("Application Main Window");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}