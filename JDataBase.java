import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class JDataBase extends Thread {

	Socket s = null;

	public JDataBase(Socket clientSocket) {
		this.s = clientSocket;
	}

	private static ArrayList<ArrayList<String>> database = new ArrayList<ArrayList<String>>(); // Database

	public void run() {
		try {
			PrintWriter pr = new PrintWriter(s.getOutputStream()); // Printer

			InputStreamReader in = new InputStreamReader(s.getInputStream()); // Reading
			BufferedReader bf = new BufferedReader(in); // Reading

			while (true) {
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
						for (int i = 2; i < input.length; i++)
							temp += input[i];
						database.get(Integer.parseInt(input[1])).add(temp);
					} else if (input[0].equals("GET")) {
						pr.println(database.get(Integer.parseInt(input[1])).get(Integer.parseInt(input[2])));
						pr.flush();
					} else if (input[0].equals("GETALL")) {
						pr.println(database.get(Integer.parseInt(input[1])).toString());
						pr.flush();
					}
				}
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
}
