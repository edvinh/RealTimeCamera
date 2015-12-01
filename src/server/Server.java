package server;

import java.io.IOException;

import se.lth.cs.eda040.fakecamera.AxisM3006V;
import util.Logger;

public class Server {
	public static void main(String[] args) {
		Logger log = Logger.getInstance();
		
		// Setup Camera
		AxisM3006V camera = new AxisM3006V();
		camera.init();
		camera.setProxy("argus-1.student.lth.se", 6667);
		
		ServerSocketConnection socket = null;
		ServerMonitor monitor = new ServerMonitor();
		new PollingThread(50, camera, monitor).start();
		try {
			// Start server socket
			socket = new ServerSocketConnection(3001, monitor);
			//InputHandler inputHandler = new InputHandler(socket);
			socket.start();
			//inputHandler.start();
		} catch (IOException e) {
			log.error("Server Socket - cannot connect to camera");
			e.printStackTrace();
		}
		
		// Send data when available, busy wait... TODO change
		System.out.println("Server listening...");
		
		// TODO Start sending camera stuff with ServerSocketConnection and PollingThread
	}
}
