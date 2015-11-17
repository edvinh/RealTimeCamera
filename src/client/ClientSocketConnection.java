package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSocketConnection extends Thread {
	InputStream is;
	OutputStream os;

	// Creates a socket connection and sends an arbitrary byte array
	public ClientSocketConnection() throws UnknownHostException, IOException {
		Socket s = new Socket("argus-7.student.lth.se", 6667);
		s.setTcpNoDelay(true);
		is = s.getInputStream();
		os = s.getOutputStream();
		byte[] data = new byte[100];
		os.write(data, 0, 100);
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
