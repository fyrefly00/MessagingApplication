import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
public class MessageServer extends Thread{
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];
    
    public MessageServer() {
        socket = new DataGram(4445);
    }

    @Override
    public void run() {
        while(running) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.recieve(packet);

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            String recieved = new String(packet.getData(), 0, packet.getLength());
            if (received.equals("end")) {
                running = false;
                continue;
            }
            socket.send(packet);
        }
        socket.close();
    
    
    }
    

}



