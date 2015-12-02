package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import se.lth.cs.eda040.fakecamera.AxisM3006V;
import util.Command;

public class ServerSocketConnection extends Thread {
	InputStream is;
	OutputStream os;
	ServerSocket ss;
	ServerMonitor monitor;
	public ServerSocketConnection(int port, ServerMonitor monitor) throws IOException {
		ss = new ServerSocket(port);
		this.monitor = monitor;
	}

	
	
	public void run() {
		try {
			Socket s = ss.accept();
			s.setTcpNoDelay(true);
			is = s.getInputStream();
			os = s.getOutputStream();
			
			
			while (true) {
				if (monitor.hasNewImage()) {
					System.out.println("ServerSocket: writing data...");
					write(monitor.getImageData());
				}
			}
		} catch (Throwable e) {
			
		}
	}

	// Reads the contents of data, one byte at a time.
	public void read(byte[] data) throws Exception {
		int read = 0;
		while (read < Command.LENGTH) {
			int n = is.read(data, read, Command.LENGTH- read); // Blocking
			if (n == -1)
				throw new IOException();
			read += n;
		}
	}

	// Writes data to the Output Stream
	public void write(byte[] data) throws IOException {
		os.write(data, 0, AxisM3006V.IMAGE_BUFFER_SIZE);
	}
	
	public void close() throws IOException {
		os.flush();
		os.close();
		is.close();
		ss.close();
	}
}
