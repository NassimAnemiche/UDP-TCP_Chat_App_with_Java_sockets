import java.io.*;
import java.net.Socket;

public class ConnectionThread extends Thread {

    private Socket clientSocket;
    private int clientId;
    private BufferedReader reader;
    private PrintWriter writer;

    public ConnectionThread(Socket clientSocket, int clientId) {
        this.clientSocket = clientSocket;
        this.clientId = clientId;

        this.setName("ClientHandler-" + clientId);
    }

    @Override
    public void run() {

        String clientIP = clientSocket.getInetAddress().getHostAddress();

        System.out.println("Client " + clientId + " connected from " + clientIP);

        try {

            writer = new PrintWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream()), true
            );

            // ✅ Send welcome message IMMEDIATELY
            writer.println("Welcome! You are client #" + clientId);
            writer.flush();

            // ✅ Setup reader AFTER sending welcome
            reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())
            );

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.equalsIgnoreCase("quit")) {
                    System.out.println("Client " + clientId + " requested quit");
                    break;
                }

                System.out.println("[Client " + clientId + "] " + line);
                writer.println("[Client " + clientId + "] " + line);
                writer.flush();
            }

        } catch (IOException e) {
            System.err.println("Client " + clientId + " error: " + e.getMessage());

        } finally {
            cleanup();
        }
    }

    private void cleanup() {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (clientSocket != null) clientSocket.close();
            System.out.println("Client " + clientId + " disconnected");
        } catch (IOException e) {
            System.err.println("Cleanup failed for client " + clientId);
        }
    }
}
