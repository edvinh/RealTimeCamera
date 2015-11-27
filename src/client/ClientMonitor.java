package client;

import se.lth.cs.eda040.fakecamera.AxisM3006V;

public class ClientMonitor {
	private byte[] imageData;
	private boolean newImage;
	
	public ClientMonitor() {
		imageData = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
		newImage = false;
	}
	
	public synchronized byte[] getImageData() {
		return imageData;
	}
	
	public synchronized void setImageData(byte[] data) {
		imageData = data;
		newImage = true;
		notifyAll();
	}
	
	/**
	 * Returns true once if there is a new image. Will only work with one socket connection. TODO FIX 
	 * @return
	 */
	public synchronized boolean hasNewImage() {
		if (newImage) {
			newImage = false;
			return true;
		}
		return false;
	}
}