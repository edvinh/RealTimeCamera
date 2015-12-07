package client;

import java.io.IOException;

public class ClientOutput extends Thread {

	private ClientSocketConnection socket;
	private ClientMonitor monitor;

	public ClientOutput(ClientSocketConnection socket, ClientMonitor monitor) {
		this.socket = socket;
		this.monitor = monitor;
	}

	/**
	 * Writes the mode to the server if the mode changes.
	 */
	public void run() {
		while (!interrupted()) {
			if (monitor.modeChanged()) {
				try {
					socket.writeCmd(monitor.getMode());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
