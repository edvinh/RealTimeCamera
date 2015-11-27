package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import se.lth.cs.eda040.fakecamera.AxisM3006V;

public class ServerSocketConnection extends Thread {
	InputStream is;
	OutputStream os;
	
	
	public ServerSocketConnection(int port, String address) throws IOException {
		ServerSocket ss = new ServerSocket(port);
		Socket s = ss.accept();
		s.setTcpNoDelay(true);
		is = s.getInputStream();
		os = s.getOutputStream();
	}

	// Reads the contents of data, one byte at a time.
	public void read(byte[] data) throws IOException {
		int read = 0;
		while (read < AxisM3006V.IMAGE_BUFFER_SIZE) {
			int n = is.read(data, read, AxisM3006V.IMAGE_BUFFER_SIZE - read); // Blocking
			if (n == -1)
				throw new IOException();
			read += n;
		}
	}

	// Writes data to the Output Stream
	public void write(byte[] data) throws IOException {
		os.write(data, 0, AxisM3006V.IMAGE_BUFFER_SIZE);
	}
}
