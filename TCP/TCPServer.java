import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class TCPServer {

    private int port;
    private static int clientCounter = 0;               // Unique IDs
    private LinkedList<String> messageHistory = new LinkedList<>();  // Last 10 messages

    // Default constructor
    public TCPServer() {
        this.port = 8080;
    }

    // Constructor with custom port
    public TCPServer(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "TCPServer running on port " + port;
    }

    public void launch() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            // Accept multiple clients sequentially
            while (true) {

                Socket clientSocket = serverSocket.accept();

                // Connection Logging
                clientCounter++;
                int clientID = clientCounter;
                String clientIP = clientSocket.getInetAddress().getHostAddress();
                LocalDateTime timestamp = LocalDateTime.now();

                System.out.println("[" + timestamp + "] Client #" + clientID + " connected from " + clientIP);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream(), "UTF-8")
                );
                PrintWriter writer = new PrintWriter(
                        new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true
                );

                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("Received: " + line);

                        // Save last 10 messages
                        messageHistory.addLast(line);
                        if (messageHistory.size() > 10) {
                            messageHistory.removeFirst();
                        }

                        // Echo response
                        writer.println("[Client#" + clientID + " - " + clientIP + "] " + line);
                    }

                } finally {
                    // Always executed, even if the client disconnects abruptly
                    System.out.println("Client disconnected: " + clientIP);
                    clientSocket.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 8080;
        new TCPServer(port).launch();
    }
}
