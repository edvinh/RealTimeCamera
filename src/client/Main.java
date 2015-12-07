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
			ClientOutput clientOutput = new ClientOutput(socket, monitor);
			clientOutput.start();
		} catch (IOException e) {
			
		}

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