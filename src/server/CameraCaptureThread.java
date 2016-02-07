package server;

import se.lth.cs.eda040.proxycamera.AxisM3006V;
import util.Command.CMD;
import util.ImageFrame;

public class CameraCaptureThread extends Thread {

	private byte[] image;
	private AxisM3006V camera;
	private ServerMonitor monitor;
	public final static int IDLE = 5;

	public CameraCaptureThread(String address, int port, ServerMonitor monitor) {
		this.monitor = monitor;
		image = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];

		// Set up camera
		this.camera = new AxisM3006V();
		camera.init();
		camera.setProxy(address, port);
	}

	public void run() {

		// Connect to camera on start
		camera.connect();
		int bytesRead = 0;
		CMD mode = CMD.NO_MOTION;

		while (!interrupted()) {

			// Read image data from camera
			bytesRead = camera.getJPEG(image, 0);

			byte[] trimmedImage = trim(image, bytesRead);

			// Get the timestamp
			byte[] timestamp = new byte[AxisM3006V.TIME_ARRAY_SIZE];
			camera.getTime(timestamp, 0);

			// Set to movie if motion detected
			mode = camera.motionDetected() ? CMD.MOTION : CMD.NO_MOTION;

			// Set image in monitor
			ImageFrame img = new ImageFrame(timestamp, trimmedImage, mode);
			byte[] imgBytes = img.toBytes();
			monitor.setImageData(imgBytes);
		}
	}

	private byte[] trim(byte[] array, int size) {
		byte[] newArray = new byte[size];
		for (int i = 0; i < size; i++) {
			newArray[i] = array[i];
		}

		return newArray;
	}

}
