package server;

import java.io.IOException;

public class Server {
	public static void main(String[] args) {
		
		ServerSocketConnection socket = null;
		ServerMonitor monitor = new ServerMonitor();
		int port = 3001;
		new CameraCaptureThread("argus-1.student.lth.se", port, monitor).start();
		try {
			// Start server socket
			socket = new ServerSocketConnection(3001, monitor);
			InputHandler inputHandler = new InputHandler(socket, monitor);
			socket.start();
			inputHandler.start();
			//JPEGHTTPServer HTTPServer = new JPEGHTTPServer(6667);
			//HTTPServer.main(null);
		} catch (IOException e) {
			System.err.println("Server connection failure");
			e.printStackTrace();
		}
		
		System.out.println("Server listening...");
		
	}

}