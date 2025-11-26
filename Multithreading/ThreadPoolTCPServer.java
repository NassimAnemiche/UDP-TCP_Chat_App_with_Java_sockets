import java . util . concurrent . ExecutorService ;
import java . util . concurrent . Executors ;
public class ThreadPoolTCPServer {
private int port ;
private ExecutorService threadPool ;
private static AtomicInteger clientCounter = new AtomicInteger (0) ;
public ThreadPoolTCPServer ( int port ) {
this . port = port ;
this . threadPool = Executors . newFixedThreadPool (10) ; // Max 10
concurrent
}
public void launch () {
try ( ServerSocket serverSocket = new ServerSocket ( port ) ) {
System . out . println (" Thread Pool Server started on port " + port ) ;
while ( true ) {
Socket clientSocket = serverSocket . accept () ;
int clientId = clientCounter . incrementAndGet () ;
threadPool . execute (() -> {
ConnectionThread handler =
new ConnectionThread ( clientSocket , clientId ) ;
handler . run () ; // Call run () directly in pool thread
}) ;
}
} catch ( IOException e ) {
System . err . println (" Server error : " + e . getMessage () ) ;
}
}
public void shutdown () {
threadPool . shutdown () ;
System . out . println (" Server shutdown initiated ") ;
}
}