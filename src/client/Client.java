package client;

import java.io.IOException;

import javax.swing.SwingUtilities;

public class Client {

	public static void main(String[] args) {
		new Client("localhost", 3001, "localhost", 3002).start();
	}
	
	private ClientMonitor monitor1, monitor2;
	private ClientSocketConnection socket1, socket2;
	private ClientOutput clientOutput1, clientOutput2;
	private ClientGUI gui;
	
	
	/**
	 * Creates a Client 
	 * @param address The server address
	 * @param port The server port
	 */
	public Client(String address1, int port1, String address2, int port2) {
		monitor1 = new ClientMonitor();
		monitor2 = new ClientMonitor();
		
		try {
			socket1 = new ClientSocketConnection(monitor1, address1, port1);
			socket2 = new ClientSocketConnection(monitor2, address2, port2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientOutput1 = new ClientOutput(socket1, monitor1);
		clientOutput2 = new ClientOutput(socket2, monitor2);
	}
	
	/**
	 * Starts the client and creates the GUI.
	 */
	public void start() {
		socket1.start();
		clientOutput1.start();
		socket2.start();
		clientOutput2.start();
		final ClientMonitor[] monitors = { monitor1, monitor2 };
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				gui = new ClientGUI(monitors, 2);
				ImageDispatcher imageDispatcher = new ImageDispatcher(monitors, gui.getImagePanel());
				imageDispatcher.start();
			}
		});
	}
}