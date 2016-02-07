package server;

import java.io.IOException;

public class Server {

	public static void main(String[] args) {
		new Server("argus-5.student.lth.se", 3001, 3001).start();
		new Server("argus-6.student.lth.se", 3002, 3002).start();
	}

	private ServerSocketConnection socket;
	private ServerMonitor monitor;
	private CameraCaptureThread ccThread;
	private InputHandler inputHandler;

	/**
	 * Creates a new server.
	 * 
	 * @param cameraAddress
	 *            the camera URL
	 * @param cameraPort
	 *            the camera port
	 * @param port
	 *            the port the server will emit/listen on
	 */
	public Server(String cameraAddress, int cameraPort, int port) {
		monitor = new ServerMonitor();
		ccThread = new CameraCaptureThread(cameraAddress, cameraPort, monitor);
		try {
			socket = new ServerSocketConnection(port, monitor);
		} catch (IOException e) {
			System.err.println("Server conection failure");
			e.printStackTrace();
		}

		// inputHandler = new InputHandler(socket, monitor);
	}

	/**
	 * Starts the server.
	 */
	public void start() {
		ccThread.start();
		socket.start();
		// inputHandler.start();
		System.out.println("Server listening...");
	}

}