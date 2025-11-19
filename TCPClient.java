import java.io.*;
import java.net.*;

public class TCPClient {

    private String host;
    private int port;

    public TCPClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        try {
            // Establish TCP connection
            Socket socket = new Socket(host, port);

            // Streams for reading/writing
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            PrintWriter serverOutput = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

            String line;
            // Repeat until CTRL+D closes stdin
            while ((line = userInput.readLine()) != null) {
                // Send UTF-8 text to server
                serverOutput.println(line);

                // Read echo from server
                String response = serverInput.readLine();
                if (response == null) break;

                System.out.println(response);
            }

            // Close resources
            socket.close();
            userInput.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java TCPClient <host> <port>");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        new TCPClient(host, port).run();
    }
}
