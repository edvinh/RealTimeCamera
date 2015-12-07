package server;

import se.lth.cs.eda040.fakecamera.AxisM3006V;
import util.ImageFrame;
import util.Command.CMD;

public class CameraCaptureThread extends Thread {

	private byte[] image;
	private AxisM3006V camera;
	private ServerMonitor monitor;
	public final static int IDLE = 5;
	
	

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
		monitor.cameraConnected();
		int bytesRead = 0;
		CMD mode = CMD.NO_MOTION;
		
		while (!interrupted()) {
			
			// Read image data from camera
			bytesRead = camera.getJPEG(image, 0);
			
			byte[] trimmedImage = trim(image, bytesRead);
			
			//Get the timestamp
			byte[] timestamp = new byte[AxisM3006V.TIME_ARRAY_SIZE];
			camera.getTime(timestamp, 0);
			//System.out.println(camera.motionDetected());
			
			// Set to movie if motion detected
			mode = camera.motionDetected() ? CMD.MOTION : CMD.NO_MOTION;
			
			if (camera.motionDetected()) {
				mode = CMD.MOTION;
				monitor.setMotionDetected(true);
			}  else {
				mode = CMD.NO_MOTION;
				monitor.setMotionDetected(false);
			} 
			
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
