package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class JDataBase extends Thread {
	
	Socket s = null;
	
	public JDataBase(Socket clientSocket) { this.s = clientSocket; }
	
	// Helper functions

	private static String read(String filePath) { // Returns contents of a file
		try { return Files.readString(Path.of(filePath)); } catch (IOException e) { e.printStackTrace(); }
		return "";
	}
	
	private static void create(String filePath, boolean err) { // Creates a file
	    try { if (new File(filePath).createNewFile()) {} else { if (err) { System.out.println(filePath + " already exists."); }}} catch (IOException e) { e.printStackTrace(); }
	} 
	
	private static void write(String filePath, String data) { // Writes to file
	    try { Files.writeString(Path.of(filePath), data); } catch (IOException e) { e.printStackTrace(); }
	}
	
	public void run() {
		try {
			PrintWriter pr = new PrintWriter(s.getOutputStream()); // Writing

			InputStreamReader in = new InputStreamReader(s.getInputStream()); // Reading
			BufferedReader bf = new BufferedReader(in); // Reading

			while (true) {
				String raw = bf.readLine(); // Read text
				String[] input = null;
				if (raw != null) {
					input = raw.split(", ");
					System.out.println(raw); // Debug

					// Parse commands

					if (input[0].equals("NEW")) {
						for (int i = 0; true; i++) { // Until file does not exist
							if (!new File("Server/" + i + ".txt").exists()) { // File does not exist?
								create("Server/" + i + ".txt", true); // Create file
								break;
							}
						}
					} else if (input[0].equals("ADD")) {
						String temp = "";
						for (int i = 2; i < input.length; i++)
							temp += input[i]; // Adds all elements after command to "temp"
						for (int i = 0; true; i++) { // Until file does not exist
							if (!new File("Server/" + input[1] + "-" + i + ".txt").exists()) { // File does not exist? 
								create("Server/" + input[1] + "-" + i + ".txt", true); // Create file
								write("Server/" + input[1] + "-" + i + ".txt", temp); // Write data from "temp" to file
								break;
							}
						}
					} else if (input[0].equals("GET")) {
						pr.println(read("Server/" + input[1] + "-" + input[2] + ".txt")); // Sends data from file to client
						pr.flush();
					} else if (input[0].equals("GETALL")) {
						String temp = "[";
						for (int i = 0; true; i++) { // Until file found
							if (new File("Server/" + input[1] + "-" + i + ".txt").exists()) { // File exists?
								temp += read("Server/" + input[1] + "-" + i + ".txt") + ", "; // Adds to string representation of ArrayList
							} else {
								temp += "]";
								break;
							}
						}

						pr.println(temp); // Sends string representation of ArrayList containing table data to client
						pr.flush();
					}
				}
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
}
