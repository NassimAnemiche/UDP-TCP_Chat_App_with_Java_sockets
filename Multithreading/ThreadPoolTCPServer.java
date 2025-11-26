import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolTCPServer {

    private int port;
    private ExecutorService threadPool;
    private static AtomicInteger clientCounter = new AtomicInteger(0);

    public ThreadPoolTCPServer(int port) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(10); // Max 10 concurrent clients
    }

    public void launch() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Thread Pool Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                int clientId = clientCounter.incrementAndGet();

                threadPool.execute(() -> {
                    ConnectionThread handler =
                            new ConnectionThread(clientSocket, clientId);
                    handler.run(); // executed inside pool thread
                });
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    public void shutdown() {
        threadPool.shutdown();
        System.out.println("Server shutdown initiated");
    }

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 8080;
        new ThreadPoolTCPServer(port).launch();
    }
}
