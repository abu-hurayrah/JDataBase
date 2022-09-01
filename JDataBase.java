import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class JDataBase {
	
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

	private static ArrayList<HashMap<Integer, String>> database = new ArrayList<HashMap<Integer, String>>(); // Database
	
	public static void main(String[] args) throws IOException {
		try (ServerSocket ss = new ServerSocket(4999)) {
			Socket s = ss.accept(); // Accept Connections
			
			PrintWriter pr = new PrintWriter(s.getOutputStream()); // Printer

			InputStreamReader in = new InputStreamReader(s.getInputStream()); 	// Reading
			BufferedReader bf = new BufferedReader(in);							// Reading
			
			while(true) {
				String raw = bf.readLine(); // Read text
				String[] input = raw.split(", ");
				
				// Parse commands
				
				if (input[0].equals("NEW")) {
					database.add(new HashMap<Integer, String>());
				} else if (input[0].equals("ADD")) {
					database.get(Integer.parseInt(input[1])).put(database.get(Integer.parseInt(input[1])).size(), input[2]);
				} else if (input[0].equals("GET")) {
					pr.println(database.get(Integer.parseInt(input[1])).get(Integer.parseInt(input[2])));
					pr.flush();
				} else if (input[0].equals("GETALL")) {
					pr.println(database.get(Integer.parseInt(input[1])).toString());
					pr.flush();
				} else if (input[0].equals("SAVE")) {
					database.forEach(list -> list.forEach((k, v) -> {create(k + ".txt", true); write(k + ".txt", v);}));
				} else if (input[0].equals("LOAD")) {
					database.forEach(list -> list.forEach((k, v) -> v = read(k + ".txt")));
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
}
