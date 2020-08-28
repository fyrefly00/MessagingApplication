
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import javax.xml.crypto.Data;
import java.util.*;
import java.net.*;
import java.io.*;
public class HuffmanPacket implements Serializable {
    Boolean encoded;
    String encodedMessage;
    PriorityQueue<Node> queue;
    public HuffmanPacket(String encodedMessage, PriorityQueue<Node> queue, Boolean encoded) {
        this.encodedMessage = encodedMessage;
        this.queue = queue;
        this.encoded = encoded;
    }
}