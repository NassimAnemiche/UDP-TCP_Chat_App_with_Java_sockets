import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class UDPServer {
    public static final int DEFAULT_PORT = 9876;
    private int port;

    public UDPServer(int port) { this.port = port; }
    public UDPServer() { this(DEFAULT_PORT); }

    public void launch() {
        byte[] buf = new byte[1024];
        try (DatagramSocket s = new DatagramSocket(port)) {
            System.out.println("Listening on " + port);
            while (true) {
                DatagramPacket p = new DatagramPacket(buf, buf.length);
                s.receive(p);
                int n = p.getLength();
                if (n > buf.length) n = buf.length;
                String msg = new String(p.getData(), 0, n, StandardCharsets.UTF_8);
                System.out.println(p.getAddress().getHostAddress() + ":" + p.getPort() + " - " + msg);
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
