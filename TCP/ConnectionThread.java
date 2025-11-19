import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public class ConnectionThread extends Thread {

    private BufferedReader reader;  // field stocké pour l'utiliser dans run()

    // Constructeur : on passe l'InputStream reçu du socket
    public ConnectionThread(InputStream in) {
        this.reader = new BufferedReader(new InputStreamReader(in));
    }

    @Override
    public void run() {
        try {
            String line;
            // même boucle infinie que dans TCPServer
            while ((line = reader.readLine()) != null) {
                System.out.println(getName() + " : " + line);
            }
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }
}
