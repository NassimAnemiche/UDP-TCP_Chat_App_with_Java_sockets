import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class UDPServer {
    public static final int DEFAULT_PORT = 9876;
    private final int port;

    public UDPServer(int port) {
        this.port = port;
    }

    public UDPServer() {
        this(DEFAULT_PORT);
    }
    public void launch() {
        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];

        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDPServer listening on port " + port);

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                int len = packet.getLength();
                if (len > BUFFER_SIZE) {
                    len = BUFFER_SIZE; // truncate if necessary (defensive)
                }

                String message = new String(packet.getData(), 0, len, StandardCharsets.UTF_8);
                String clientAddr = packet.getAddress().getHostAddress() + ":" + packet.getPort();

                System.out.println(clientAddr + " - " + message);
            }

        } catch (IOException e) {
            System.err.println("UDPServer error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "UDPServer[port=" + port + "]";
    }

    // Allow launching the server from command line: java UDPServer 8080
    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number: " + args[0]);
                System.err.println("Usage: java UDPServer [port]");
                System.exit(1);
            }
        }

        UDPServer server = new UDPServer(port);
        System.out.println(server.toString());
        server.launch();
    }
}
