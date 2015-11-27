package client;

import java.io.IOException;

import se.lth.cs.eda040.fakecamera.AxisM3006V;
import server.PollingThread;
import util.Logger;

public class Main {

	public static void main(String[] args) {
		Logger log = Logger.getInstance();
		ClientMonitor monitor = new ClientMonitor();
		int port = 3001;
		String address = "localhost";
		try {
			ClientSocketConnection socket = new ClientSocketConnection(monitor, address, port);
		} catch (IOException e) {
			log.error("Could not connect to server socket!");
			e.printStackTrace();
		}
		
		new ClientGUI(monitor);
	}

}