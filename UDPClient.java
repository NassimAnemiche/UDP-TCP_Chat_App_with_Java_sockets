import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

/**
 * UDPClient reads lines from standard input and sends each line as a UTF-8
 * encoded UDP datagram to the server specified on the command line.
 *
 * Usage: java UDPClient <host> <port>
 * Example: java UDPClient localhost 9876
 */
public class UDPClient {
    private final String host;
    private final int port;
    public static final int MAX_BYTES = 1024; // match server buffer/truncation

    public UDPClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        try (DatagramSocket socket = new DatagramSocket();
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {

            InetAddress address = InetAddress.getByName(host);
            System.out.println("UDPClient: sending to " + host + ":" + port + ". Enter lines to send. EOF to exit.");

            String line;
            while ((line = br.readLine()) != null) {
                byte[] data = line.getBytes(StandardCharsets.UTF_8);
                int len = Math.min(data.length, MAX_BYTES);

                DatagramPacket packet = new DatagramPacket(data, len, address, port);
                socket.send(packet);

                if (data.length > MAX_BYTES) {
                    System.err.println("Warning: message truncated to " + MAX_BYTES + " bytes when sent");
                }
            }

        } catch (IOException e) {
            System.err.println("UDPClient error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "UDPClient[target=" + host + ":" + port + "]";
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java UDPClient <host> <port>");
            System.exit(1);
        }

        String host = args[0];
        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Invalid port number: " + args[1]);
            System.exit(1);
            return;
        }

        UDPClient client = new UDPClient(host, port);
        System.out.println(client.toString());
        client.run();
    }
}
