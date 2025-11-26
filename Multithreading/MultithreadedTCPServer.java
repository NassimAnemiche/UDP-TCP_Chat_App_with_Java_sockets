import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class MultithreadedTCPServer {

    private int port;
    private static AtomicInteger clientCounter = new AtomicInteger(0);

    public MultithreadedTCPServer(int port) {
        this.port = port;
    }

    public void launch() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Multithreaded Server started on port " + port);

            while (true) {

                Socket clientSocket = serverSocket.accept();

                int clientId = clientCounter.incrementAndGet();

                ConnectionThread clientThread =
                        new ConnectionThread(clientSocket, clientId);

                clientThread.start();

                System.out.println("Active threads: " + (Thread.activeCount() - 1));
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 8080;
        new MultithreadedTCPServer(port).launch();
    }
}
