package server;

import java.io.IOException;

import se.lth.cs.eda040.fakecamera.AxisM3006V;
import util.Logger;

public class Server {
	public static void main(String[] args) {
		Logger log = Logger.getInstance();
		int port = 6667;
		String address = "argus-1.student.lth.se";
		AxisM3006V camera = new AxisM3006V();
		camera.init();
		camera.setProxy(address, port);
		ServerSocketConnection socket = null;
		ServerMonitor monitor = new ServerMonitor();
		try {
			// Start server socket
			socket = new ServerSocketConnection(3001, "localhost");
		} catch (IOException e) {
			log.error("Server Socket - cannot connect to camera");
			e.printStackTrace();
		}
		
		// Send data when available, busy wait... TODO change
		while (true) {
			if (monitor.hasNewImage()) {
				try {
					socket.write(monitor.getImageData());
				} catch (IOException e) {
					log.error("Server Socket IO Exception when writing to output stream");
					e.printStackTrace();
				}
			}
		}
		// TODO Start sending camera stuff with ServerSocketConnection and PollingThread
	}
}
