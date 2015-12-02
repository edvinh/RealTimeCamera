package server;

import se.lth.cs.eda040.fakecamera.AxisM3006V;
import util.Command.CMD;

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
		CMD mode = CMD.IDLE;
		
		while (!interrupted()) {
			//System.out.println("bytes read: " + bytesRead);
			
			// Read image data from camera
			bytesRead = camera.getJPEG(image, 0);
			
			//Get the timestamp
			byte[] timestamp = new byte[AxisM3006V.TIME_ARRAY_SIZE];
			camera.getTime(timestamp, 0);
			
			// Set to movie if motion detected
			mode = camera.motionDetected() ? CMD.MOVIE : CMD.IDLE;  
			
			// Set image in monitor
			Image img = new Image(timestamp, image, mode);
			monitor.setImageData(img.toBytes());
		}
	}
}
