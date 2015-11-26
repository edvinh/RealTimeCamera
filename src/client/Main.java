package client;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		ClientMonitor monitor = new ClientMonitor();
		try {
			new ClientSocketConnection(monitor, "argus-7.student.lth.se", 6667);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		new ClientGUI(monitor);
	}

}