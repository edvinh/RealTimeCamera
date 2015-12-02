package server;

import se.lth.cs.eda040.fakecamera.AxisM3006V;
import util.Constants;

public class ServerMonitor {
	
	private byte[] imageData;
	private boolean newImage;
	private boolean cameraConnected = false;
	private int imageWaitPeriod = Constants.IDLE_WAIT_PERIOD;
	public ServerMonitor() {
		newImage = false;
		imageData = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
	}
	
	public synchronized void setImageData(byte[] data) {
		imageData = data;
		newImage = true;
		notifyAll();
	}
	
	public synchronized byte[] getImageData() {
		// TODO wait until image should be sent (depends on imageWaitPeriod; if in idle or movie)
		return imageData;
	}
	
	public synchronized void cameraConnected() {
		cameraConnected = true;
		notifyAll();
	}
	
	public boolean hasNewImage() {
		/*if (newImage) {
			newImage = false;
			return true;
		}
		return false;*/
		return newImage;
	}
}
