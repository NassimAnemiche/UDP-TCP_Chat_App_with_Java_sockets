import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class UDPServer {
    public static final int DEFAULT_PORT = 9876;
    private int port;

    public UDPServer(int port) { 
        this.port = port; 
    }

    public UDPServer() { 
        this(DEFAULT_PORT); 
    }

    public void launch() {
        byte[] buf = new byte[10];
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("Listening on " + port);
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                
                int len = packet.getLength();
                String msg = new String(packet.getData(), 0, len, StandardCharsets.UTF_8);
                String client = packet.getAddress().getHostAddress() + ":" + packet.getPort();
                
                // adding sequence numbers to messages
                String[] parts = msg.split("\\|", 2);
                int seq = 0;
                String content = msg;
                try {
                    seq = Integer.parseInt(parts[0]);
                    content = parts.length > 1 ? parts[1] : "";
                } catch (NumberFormatException e) {
                }
                
                System.out.println("[SEQ:" + seq + "] " + client + " - " + content);
                
                // send ACK back
                String ack = "OK";
                byte[] ackData = ack.getBytes(StandardCharsets.UTF_8);
                DatagramPacket ackPacket = new DatagramPacket(ackData, ackData.length, packet.getAddress(), packet.getPort());
                socket.send(ackPacket);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try { 
                port = Integer.parseInt(args[0]); 
            } catch (Exception e) { 
            }
        }
        new UDPServer(port).launch();
    }
}
