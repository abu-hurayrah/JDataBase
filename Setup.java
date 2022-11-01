package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Setup {
	
	public static final String IP = "100.127.255.253";
	public static final int PORT = 4999;
	
	public static void setup() throws UnknownHostException, IOException {
		try (var s = new Socket(IP, PORT)) {
			var pr = new PrintWriter(s.getOutputStream());

			pr.println("NEW"); // Names
			pr.flush();
			
			pr.println("NEW"); // Tokens
			pr.flush();
			
			pr.println("NEW"); // Blogs
			pr.flush();
			
			pr.println("NEW"); // Blog names
			pr.flush();
			
			pr.println("LOAD"); // Load
			pr.flush();
		}
	}
	
}
