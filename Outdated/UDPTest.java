public class UDPTest {
    Client client;
 

    public void setup(){
        new Server().start();
        client = new Client();
    }
 
    public void whenCanSendAndReceivePacket_thenCorrect() {
        String echo = client.sendMessage("hello server");
        assertEquals("hello server", echo);
        echo = client.sendMessage("server is working");
        assertFalse(echo.equals("hello server"));
    }
 

    public void tearDown() {
        client.sendMessage("end");
        client.close();
    }
}