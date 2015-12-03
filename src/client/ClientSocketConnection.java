package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import se.lth.cs.eda040.fakecamera.AxisM3006V;
import util.Command.CMD;

public class ClientSocketConnection extends Thread {
	private InputStream is;
	private OutputStream os;
	private ClientMonitor monitor;
	private ImageBuffer imageBuffer;
	private LinkedList<byte[]> imgQueue;
	private Socket s;
	private int cameraId;
	
	public ClientSocketConnection(ClientMonitor monitor, String proxy, int port, int cameraId)
			throws UnknownHostException, IOException {
		// Socket s = new Socket("argus-7.student.lth.se", 6667);
		this.monitor = monitor;
		this.cameraId = cameraId;
		s = new Socket(proxy, port);
		s.setTcpNoDelay(true);
		is = s.getInputStream();
		os = s.getOutputStream();
		imageBuffer = monitor.getImageBuffer();
		imgQueue = new LinkedList<byte[]>();
		imageBuffer.addQueue(imgQueue);
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
		int bufferSize = AxisM3006V.IMAGE_BUFFER_SIZE + AxisM3006V.TIME_ARRAY_SIZE + 1;
		byte[] data = new byte[bufferSize];
		int read = 0;
		while (read < bufferSize) {
			int n = is.read(data, read, bufferSize - read); // Blocking
			if (n == -1)
				throw new IOException();
			read += n;
		}
		if (read != 0) {
			System.out.println(data.length);
			monitor.setImageData(data, cameraId);
		}
		System.out.println("total data read:" + read);
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
