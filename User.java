import javax.xml.crypto.Data;
import java.util.*;
import java.net.*;
import java.io.*;
public class User {
    String username;
    InetAddress IPAddress;
    public User(String username, InetAddress IPAddress) {
        this.username = username;
        this.IPAddress = IPAddress;
    }
    @Override
    public boolean equals (Object object) {
        User x = (User) object;
        if(x.username.equals(username)) {
            return true;
        } 
        else {
            return false;
        }
    }
}