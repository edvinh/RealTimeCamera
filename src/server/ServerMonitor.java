package server;

import se.lth.cs.eda040.fakecamera.AxisM3006V;

public class ServerMonitor {
	
	private byte[] imageData;
	private boolean newImage = false;
	public ServerMonitor() {
		imageData = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
	}
	
	public synchronized void setImageData(byte[] data) {
		imageData = data;
		newImage = true;
		notifyAll();
	}
	
	public synchronized byte[] getImageData() {
		return imageData;
	}
	
	public boolean hasNewImage() {
		if (newImage) {
			newImage = false;
			return true;
		}
		return false;
	}
}
