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
		newImage = false;
		return imageData;
	}
	
	public synchronized void setImageData(byte[] data) {
		for (int i = 0; i < 10; i++) {
			System.out.print(data[i] + " ");
		}
		System.out.println();
		imageData = data;
		newImage = true;
		notifyAll();
	}
	
	/**
	 * Returns true once if there is a new image. Will only work with one socket connection. TODO FIX 
	 * @return
	 */
	public synchronized boolean hasNewImage() {
		return newImage;
	}
}