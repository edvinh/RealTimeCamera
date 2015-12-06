package client;

import java.io.IOException;

import javax.swing.SwingUtilities;

public class Main {

	public static void main(String[] args) {

		final ClientMonitor monitor = new ClientMonitor();
		int port = 3001;
		String address = "localhost";
		try {
			ClientSocketConnection socket = new ClientSocketConnection(monitor,
					address, port);
			socket.start();
		} catch (IOException e) {
			System.err.println("Unable connect to port");
			e.printStackTrace();
		}

		// ClientGUI gui = new ClientGUI(monitor);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ClientGUI gui = new ClientGUI(monitor);
				ImageDispatcher imageDispatcher = new ImageDispatcher(monitor,
						gui.getImagePanel());
				imageDispatcher.start();
			}
		});
	}

}