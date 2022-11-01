package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	public static void main(String args[]) {
        ServerSocket serverSocket = null; 	// Server Socket
        Socket socket = null;				// Client Socket

        try {
            serverSocket = new ServerSocket(Setup.PORT); // Hosts on port 4999
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
			Setup.setup(); // Setup
		} catch (IOException e) {
			e.printStackTrace();
		} 
        
        while (true) { // Always accepts new connections
            try {
                socket = serverSocket.accept(); // Accepts new connection
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            new JDataBase(socket).start(); // Connects client to server and starts thread
        }
        
        try {
			serverSocket.close(); // Closes Server
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
