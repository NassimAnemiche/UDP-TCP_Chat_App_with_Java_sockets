import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTCPServer {

    private int port;
    private ExecutorService threadPool;
    private static AtomicInteger clientCounter = new AtomicInteger(0);
    private volatile boolean running = true;
    private ServerSocket serverSocket;

    private void printThreadStats() {
    Runtime runtime = Runtime.getRuntime();
    System.out.println("=== Thread Statistics ===");
    System.out.println(" Active threads : " + (Thread.activeCount() - 1));
    System.out.println(" Memory usage : " +
            (runtime.totalMemory() - runtime.freeMemory()) / 1024 + " KB");
}

    public ThreadPoolTCPServer(int port) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(10);
    }

    public void launch() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Thread Pool Server started on port " + port);

            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    int clientId = clientCounter.incrementAndGet();

                    threadPool.execute(() -> {
                        ConnectionThread handler =
                                new ConnectionThread(clientSocket, clientId);
                        handler.run();
                    });

                } catch (IOException e) {
                    if (running == false) {
                        System.out.println("Server stopped accepting connections");
                    } else {
                        System.err.println("Accept error: " + e.getMessage());
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    public void shutdown() {
        System.out.println("Shutdown initiated...");

        running = false;

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Failed to close server socket");
        }

        threadPool.shutdown();

        try {
            if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("Forcing thread termination...");
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
        }

        System.out.println("Server shutdown complete");
    }

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 8080;

        ThreadPoolTCPServer server = new ThreadPoolTCPServer(port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.shutdown();
        }));

        server.launch();
    }
}
