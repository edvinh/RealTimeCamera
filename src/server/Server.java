package server;

import java.io.IOException;

public class Server {
	public static void main(String[] args) {
		
		
		ServerSocketConnection socket = null;
		ServerMonitor monitor = new ServerMonitor();
		new CameraCaptureThread("argus-1.student.lth.se", 6667, monitor).start();
		try {
			// Start server socket
			socket = new ServerSocketConnection(3001, monitor);
			InputHandler inputHandler = new InputHandler(socket);
			socket.start();
			inputHandler.start();
		} catch (IOException e) {
			System.err.println("Server connection failure");
			e.printStackTrace();
		}
		
		System.out.println("Server listening...");
		
	}
}
