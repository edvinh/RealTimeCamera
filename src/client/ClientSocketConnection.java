package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import se.lth.cs.eda040.fakecamera.AxisM3006V;
import util.Command.CMD;
import util.Constants;
import util.Helper;

public class ClientSocketConnection extends Thread {
	private InputStream is;
	private OutputStream os;
	private ClientMonitor monitor;
	private Socket s;
	
	public ClientSocketConnection(ClientMonitor monitor, String proxy, int port)
			throws UnknownHostException, IOException {
		// Socket s = new Socket("argus-7.student.lth.se", 6667);
		this.monitor = monitor;
		s = new Socket(proxy, port);
		s.setTcpNoDelay(true);
		is = s.getInputStream();
		os = s.getOutputStream();
	}
	
	public void run() {
		while (true) {
			try {
				read();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Reads the contents of data, one byte at a time.
	public void read() throws IOException {		
		
		// Read the image size
		int packageLen = Constants.IMAGE_PACKAGE_SIZE;
		byte[] imageBufferSize = new byte[packageLen];
		int read = 0;
		int n = 0;
		while(read < packageLen) {
			n = is.read(imageBufferSize, read, packageLen - read);
			if(n != -1)	{
				read = read + n;
			}
		}
		
		// Get the image size
		int imageSize = Helper.byteArrayToInt(imageBufferSize);
		
		int bufferSize = imageSize + AxisM3006V.TIME_ARRAY_SIZE + 1;
		byte[] data = new byte[bufferSize];
		read = 0;
		while (read < bufferSize) {
			n = is.read(data, read, bufferSize - read); // Blocking
			if (n == -1)
				throw new IOException();
			read += n;
		}
		
		if (read != 0) {
			monitor.setImage(data);
		}
		//System.out.println("total data read:" + read);
	}

	// Writes data to the Output Stream
	public void write(byte[] data) throws IOException {
		os.write(data, 0, AxisM3006V.IMAGE_BUFFER_SIZE);
	}
	
	
	/**
	 * Writes a command to the server.
	 * @param c Command to send
	 * @throws IOException
	 */
	public void writeCmd(CMD c) throws IOException {
		byte[] cmdByteArray = {c.toByte()};
		os.write(cmdByteArray, 0, 1);
	}
	
	public void close() throws IOException {
		os.close();
		is.close();
		s.close();
	}
}
