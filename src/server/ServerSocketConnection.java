package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketConnection extends Thread {
	InputStream is;
	OutputStream os;

	// Creates a socket connection, accepts, reads the arbitrary byte array and
	// responds with 1.
	public ServerSocketConnection() throws IOException {
		ServerSocket ss = new ServerSocket(6667);
		Socket s = ss.accept();
		s.setTcpNoDelay(true);
		is = s.getInputStream();
		os = s.getOutputStream();
		byte[] data = new byte[100];
		int read = 0;
		while (read < 100) {
			int n = is.read(data, read, 100 - read); // Blocking
			if (n == -1)
				throw new IOException();
			read += n;
		}
		os.write(1);
	}

	// Reads the contents of data, one byte at a time.
	public void read(byte[] data) throws IOException {
		int read = 0;
		while (read < 100) {
			int n = is.read(data, read, 100 - read); // Blocking
			if (n == -1)
				throw new IOException();
			read += n;
		}
	}

	// Writes data to the Output Stream
	public void write(byte[] data) throws IOException {
		os.write(data, 0, 100);
	}

}
