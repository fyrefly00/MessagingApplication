import javax.swing.*;
import java.util.*;
public class Coordinator {
    public static void main(String args[]) {
        Queue<Message> messageQueue = new LinkedList<>();
        ClientApplication client = new ClientApplication();
        client.initialize();

        RealFrame frame = new RealFrame("Application Main Window");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}