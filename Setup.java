import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Setup {
	
	public static final String IP = "100.20.30.4";
	public static final int PORT = 1234;
	
	public static void setup() throws UnknownHostException, IOException {
		try (var s = new Socket(IP, PORT)) {
			var pr = new PrintWriter(s.getOutputStream());

			// Setup here
			
		}
	}
	
}
