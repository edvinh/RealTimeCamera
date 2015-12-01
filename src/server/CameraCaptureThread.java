package server;

import client.ClientMonitor;
import se.lth.cs.eda040.fakecamera.AxisM3006V;

public class CameraCaptureThread extends Thread {

	private byte[] image;
	private AxisM3006V camera;
	private ServerMonitor monitor;
	public final static int IDLE = 5;
	
	
	/**
	 * Creates a polling thread which polls the camera at a set interval
	 * 
	 * @param period
	 *            the periodicity of the polling
	 * @param camera
	 * 			  The camera which will be polled 
	 */
	public CameraCaptureThread(String address, int port, ServerMonitor monitor) {
		this.monitor = monitor;
		image = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
		
		//Set up camera
		this.camera = new AxisM3006V();
		camera.init();
		camera.setProxy(address, port);
	}


	public byte[] getImage() {
		return image;
	}

	public void run() {
		
		// Connect to camera on start
		camera.connect();
		int bytesRead = 0;
		while (true) {
			System.out.println("bytes read: " + bytesRead);
			// Read image data from camera
			bytesRead = camera.getJPEG(image, 0);
			// Set image in monitor
			monitor.setImageData(image);
		}
	}
}
