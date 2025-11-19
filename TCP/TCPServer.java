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

    public String toString() {
        return "TCPServer running on port " + port;
    }

    public void launch() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            Socket clientSocket = serverSocket.accept();

            // --- Connection Logging ---
            clientCounter++;
            int clientID = clientCounter;
            String clientIP = clientSocket.getInetAddress().getHostAddress();
            LocalDateTime timestamp = LocalDateTime.now();
            System.out.println("[" + timestamp + "] Client #" + clientID + " connected from " + clientIP);

            // Streams
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())
            );
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            // Read a message
            String line = reader.readLine();
            System.out.println("Received: " + line);

            // ---- Store message in history (max 10) ----
            messageHistory.addLast(line);
            if (messageHistory.size() > 10) {
                messageHistory.removeFirst();
            }

            // Echo response
            writer.println("[Client#" + clientID + " - " + clientIP + "] " + line);

            // Close
            clientSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 8080;
        new TCPServer(port).launch();
    }
}
