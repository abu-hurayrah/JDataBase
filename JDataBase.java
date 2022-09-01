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
	
	@SuppressWarnings("unchecked")
	private static <K, V> HashMap<K, V> stringToHashMap(String str) {
		var map = new HashMap<K, V>();
		
		str = str.replace("{", "").replace("}", "");
		
		String[] elements = str.split(", ");
		for (String element : elements) {
			String[] kV = element.split("=");
			map.put((K) kV[0], (V) kV[1]);
		}
		
		return map;
	}

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
				String[] input = null;
				if (raw != null) {
					input = raw.split(", ");
					System.out.println(raw); // TODO: Remove; test
					
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
					}
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
}
