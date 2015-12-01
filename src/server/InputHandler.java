package server;

import java.io.IOException;
import java.net.ServerSocket;

public class InputHandler extends Thread {
	
	private ServerSocketConnection socket;
	public InputHandler(ServerSocketConnection socket) {
		this.socket = socket;
	}
	
	public void run () {
		byte[] data = new byte[10000000];
		while (true) {
			try {
				System.out.println("Reading...");
				socket.read(data);
				// Do stuff with input
			} catch (Exception e) {
				//e.printStackTrace();
				System.out.println("Exception in input handler");
			}
			
		}
	}
}
