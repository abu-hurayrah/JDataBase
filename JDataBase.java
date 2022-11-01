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
	
	public JDataBase(Socket clientSocket) {
        this.s = clientSocket;
    }
	
	// Helper functions

	private static String read(String filePath) {
	    String out = "";
	    try {
	        var path = Path.of(filePath);
	        out = Files.readString(path);
	    } catch (IOException e) { e.printStackTrace(); }
	    return out;
	}
	
	private static void create(String filePath, boolean err) {
	    try {
	        var file = new File(filePath);
	        if (file.createNewFile()) {} else {
	            if (err) { System.out.println(filePath + " already exists."); }
	        }
	    } catch (IOException e) {
	        System.out.println(filePath + " could not be created.");
	        e.printStackTrace();
	    }
	} 
	
	private static void write(String filePath, String data) {
	    try {
	        var path = Path.of(filePath);
	        Files.writeString(path, data);
	    } catch (IOException e) { e.printStackTrace(); }
	}
	
	public void run() {
		try {
			PrintWriter pr = new PrintWriter(s.getOutputStream()); 				// Writing

			InputStreamReader in = new InputStreamReader(s.getInputStream()); 	// Reading
			BufferedReader bf = new BufferedReader(in);							// Reading
			
			while(true) {
				String raw = bf.readLine(); // Read text
				String[] input = null;
				if (raw != null) {
					input = raw.split(", ");
					System.out.println(raw);
					
					// Parse commands
					
					if (input[0].equals("NEW")) {
						for (int i = 0; true; i++) {
							if (!new File(i + ".txt").exists()) {
								create(i + ".txt", true);
								break;
							}
						}
					} else if (input[0].equals("ADD")) {
						String temp = "";
						for (int i = 2; i < input.length; i++) temp += input[i];
						for (int i = 0; true; i++) {
							if (!new File(input[1] + "-" + i + ".txt").exists()) {
								create(input[1] + "-" + i + ".txt", true);
								write(input[1] + "-" + i + ".txt", temp);
								break;
							}
						}
					} else if (input[0].equals("GET")) {
						pr.println(read(input[1] + "-" + input[2] + ".txt"));
						pr.flush();
					} else if (input[0].equals("GETALL")) {
						String temp = "[";
						for (int i = 0; true; i++) {
							if (new File(input[1] + "-" + i + ".txt").exists()) {
								temp += read(input[1] + "-" + i + ".txt") + ", ";
							} else {
								temp += "]";
								break;
							}
						}
						pr.println(temp);
						pr.flush();
					}
				}
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
}
