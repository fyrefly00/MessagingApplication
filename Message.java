import javax.xml.crypto.Data;
import java.util.*;
import java.net.*;
import java.io.*;

//Serializable implementation to allow transmission over network
public  class  Message implements Serializable{
    String username;
    InetAddress originatingIP;
    String message;
    InetAddress destination;
    ArrayList<User> addressBook;
    public Message(String username, InetAddress originatingIP, InetAddress destination, String message /*ArrayList<User> addressBook*/) {
        this.username = username;
        this.originatingIP = originatingIP;
        this.message = message;
        // this.addressBook = addressBook;
        this.destination = destination;
    }
}