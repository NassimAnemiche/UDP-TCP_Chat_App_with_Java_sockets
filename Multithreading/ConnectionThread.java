import java.io.*;
import java.net.Socket;
import java.util.Date;

public class ConnectionThread extends Thread {

    private Socket clientSocket;
    private int clientId;
    private BufferedReader reader;
    private PrintWriter writer;

    // Constructor
    public ConnectionThread(Socket clientSocket, int clientId) {
        this.clientSocket = clientSocket;
        this.clientId = clientId;

        // Set meaningful thread name for debugging
        this.setName("ClientHandler-" + clientId);
    }

    @Override
    public void run() {

        System.out.println("[" + new Date() + "] Client " + clientId + " connected");

        try {
            // Set up input / output streams
            reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())
            );
            writer = new PrintWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream()), true
            );

            // Send welcome message
            writer.println("Welcome! You are Client " + clientId);

            String line;

            // Echo loop from Session 3 + quit handling
            while ((line = reader.readLine()) != null) {

                if (line.equalsIgnoreCase("quit")) {
                    writer.println("Goodbye Client " + clientId);
                    break;
                }

                System.out.println("[Client " + clientId + "] " + line);
                writer.println("[Client " + clientId + "] " + line);
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
            System.out.println("[" + new Date() + "] Client " + clientId + " disconnected");
        } catch (IOException e) {
            System.err.println("Cleanup failed for client " + clientId);
        }
    }
}
