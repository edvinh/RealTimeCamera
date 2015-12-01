package server;

import java.io.IOException;

import se.lth.cs.eda040.fakecamera.AxisM3006V;
import util.Logger;

public class Server {
	public static void main(String[] args) {
		Logger log = Logger.getInstance();
		
		
		ServerSocketConnection socket = null;
		ServerMonitor monitor = new ServerMonitor();
		new CameraCaptureThread("argus-1.student.lth.se", 6667, monitor).start();
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
		
		System.out.println("Server listening...");
		
	}
}
