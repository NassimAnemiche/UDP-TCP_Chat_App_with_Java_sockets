import java.io.*;
import java.net.*;

public class TCPServer {

    private int port;

    public TCPServer(int port) {
        this.port = port;
    }

    public void launch() {
        try {
            // 1. Create an instance of ServerSocket
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            // 2. Waiting for a connection via accept()
            Socket clientSocket = serverSocket.accept();

            // 3. Accepting this connection → already done by accept()

            // 4. Obtaining the InputStream associated with the Socket
            InputStream input = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // OutputStream to respond to client
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            // 5. Reading the data and displaying it
            String line = reader.readLine();
            System.out.println(line);

            // 6. Respond (echo) to the client with its IP
            String clientIP = clientSocket.getInetAddress().getHostAddress();
            writer.println("[" + clientIP + "] " + line);

            // Close everything (not required but clean)
            clientSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 9876;
        new TCPServer(port).launch();
    }
}
