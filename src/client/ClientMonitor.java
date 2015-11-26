package client;

public class ClientMonitor {
	private byte[] imageData;
	private boolean newImage;
	
	public ClientMonitor() {
		imageData = new byte[100];
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
	
	public synchronized boolean hasNewImage() {
		return newImage;
	}
}