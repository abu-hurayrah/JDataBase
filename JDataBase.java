import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

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
	
	@SuppressWarnings("unchecked")
	private static <T> ArrayList<T> stringToArrayList(String str) {
		var list = new ArrayList<T>();
		
		str = str.replace("[", "").replace("]", "");
		
		String[] elements = str.split(", ");
		for (String element : elements) {
			list.add((T) element);
		}
		
		return list;
	}
	
	private static ArrayList<ArrayList<String>> database = new ArrayList<ArrayList<String>>(); // Database
	
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
						database.add(new ArrayList<String>());
					} else if (input[0].equals("ADD")) {
						String temp = "";
						for (int i = 2; i < input.length; i++) temp += input[i];
						database.get(Integer.parseInt(input[1])).add(temp);
					} else if (input[0].equals("GET")) {
						pr.println(database.get(Integer.parseInt(input[1])).get(Integer.parseInt(input[2])));
						pr.flush();
					} else if (input[0].equals("GETALL")) {
						pr.println(database.get(Integer.parseInt(input[1])).toString());
						pr.flush();
					} else if (input[0].equals("SAVE")) { 
						for (int i = 0; i < database.size(); i++) {
							create(i + ".txt", false);
							write(i + ".txt", database.get(i).toString());
						}
					} else if (input[0].equals("LOAD")) {
						int i = 0;
						while (true) {
							if (!new File(i + ".txt").exists()) break;
							database.set(i, stringToArrayList(read(i + ".txt")));
							i++;
						}
					}
				}
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
}
