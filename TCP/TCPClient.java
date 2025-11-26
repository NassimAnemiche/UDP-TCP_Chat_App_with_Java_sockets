import java.io.*;
import java.net.*;

public class TCPClient {

    private String host;
    private int port;

    public TCPClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try {
            long start = System.nanoTime();
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 2000);

            long end = System.nanoTime();
            double ms = (end - start) / 1_000_000.0;
            System.out.println("TCP connection established in " + ms + " ms");

            BufferedReader userInput = new BufferedReader(
                    new InputStreamReader(System.in)
            );
            BufferedReader serverInput = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            PrintWriter serverOutput = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream()), true
            );

            // ✅ READ WELCOME MESSAGE IMMEDIATELY
            String welcome = serverInput.readLine();
            if (welcome != null) {
                System.out.println(welcome);
            }

            String line;

            while ((line = userInput.readLine()) != null) {

                if (line.equalsIgnoreCase("quit")) {
                    serverOutput.println("quit");
                    serverOutput.flush();
                    System.out.println("Closing connection...");
                    break;
                }

                serverOutput.println(line);
                serverOutput.flush();

                String response = serverInput.readLine();

                if (response == null) {
                    System.out.println("(No response yet)");
                    continue;
                }

                System.out.println(response);
            }

            socket.close();
            userInput.close();

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java TCPClient <host> <port>");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        new TCPClient(host, port).start();
    }
}
