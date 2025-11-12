import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UDPServer {
    public static final int DEFAULT_PORT = 9876;
    private int port;
    
    // adding sequence numbers to messages
    private Map<String, Integer> lastSeqPerClient = new HashMap<>();
    
    // acknowledgment system
    private int acksSent = 0;
    
    // measuring packet loss
    private int totalMsgs = 0;
    private int duplicates = 0;

    public UDPServer(int port) { this.port = port; }
    public UDPServer() { this(DEFAULT_PORT); }

    public void launch() {
        byte[] buf = new byte[1024];
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("Listening on " + port);
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                
                int len = packet.getLength();
                if (len > 1024) len = 1024;
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
                
                // measuring packet loss
                int lastSeq = lastSeqPerClient.getOrDefault(client, -1);
                totalMsgs++;
                
                if (seq > lastSeq) {
                    lastSeqPerClient.put(client, seq);
                    System.out.println("[SEQ:" + seq + "] " + client + " - " + content);
                    
                    // acknowledgment system
                    String ack = "ACK:" + seq;
                    byte[] ackData = ack.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket ackPacket = new DatagramPacket(ackData, ackData.length, packet.getAddress(), packet.getPort());
                    socket.send(ackPacket);
                    acksSent++;
                } else {
                    // measuring packet loss
                    duplicates++;
                    System.out.println("[DUP] " + client + " SEQ:" + seq);
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try { port = Integer.parseInt(args[0]); } catch (Exception e) { }
        }
        new UDPServer(port).launch();
    }
}
