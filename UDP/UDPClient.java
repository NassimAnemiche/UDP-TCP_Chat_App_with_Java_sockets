import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class UDPClient {
    private String host;
    private int port;
    private int seq = 0;

    public UDPClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws IOException {

        long start = System.nanoTime();   // Timer start
        DatagramSocket socket = new DatagramSocket();
        long end = System.nanoTime();     // Timer end
        double ms = (end - start) / 1_000_000.0;

        System.out.println("UDP socket created in " + ms + " ms");

        socket.setSoTimeout(2000);
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8)
        );
        InetAddress addr = InetAddress.getByName(host);

        System.out.println("Ready to send UDP messages to " + host + ":" + port);

        String line;
        while ((line = br.readLine()) != null) {

            seq++;
            String msg = seq + "|" + line;

            byte[] data = msg.getBytes(StandardCharsets.UTF_8);

            DatagramPacket pkt = new DatagramPacket(data, data.length, addr, port);
            socket.send(pkt);

            byte[] ackBuf = new byte[256];
            DatagramPacket ackPkt = new DatagramPacket(ackBuf, ackBuf.length);
            try {
                socket.receive(ackPkt);
                System.out.println(">> message received");
            } catch (SocketTimeoutException e) {
                System.err.println("No response");
            }
        }

        socket.close();
        br.close();
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java UDPClient <host> <port>");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try {
            new UDPClient(host, port).run();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
