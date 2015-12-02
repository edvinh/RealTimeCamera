package client;

import se.lth.cs.eda040.fakecamera.AxisM3006V;
import util.Command.CMD;

public class ClientMonitor {
	private byte[] imageData;
	private long timestamp;
	private CMD cmd;
	private boolean newImage;
	
	public ClientMonitor() {
		imageData = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
		newImage = false;
	}
	
	public synchronized byte[] getImageData() {
		newImage = false;
		return imageData;
	}
	
	public synchronized long getTimestamp() {
		return timestamp;
	}
	
	public synchronized CMD getCommand() {
		return cmd;
	}
	
	public synchronized void setImageData(byte[] data) {
		ImageBuilder imgBuilder = new ImageBuilder(data);
		imageData = imgBuilder.getImage();
		timestamp = imgBuilder.getTimestamp();
		cmd = imgBuilder.getCommand();
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