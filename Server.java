import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
	
	public static void main(String args[]) throws UnknownHostException, IOException {
        ServerSocket serverSocket = null; 	// Server Socket
        Socket socket = null;				// Client Socket

        try {
            serverSocket = new ServerSocket(Setup.PORT); // Hosts on port 4999
        } catch (IOException e) {
            e.printStackTrace();
        }
        Setup.setup(); // Setup
        while (true) { // Always accepts new connections
            try {
                socket = serverSocket.accept(); // Accepts new connection
            } catch (IOException e) {
                e.printStackTrace();
            }
            new JDataBase(socket).start(); // Connects client to server
        }
    }
}
