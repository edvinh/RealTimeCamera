package client;

import java.util.ArrayList;

import util.Command.CMD;
import util.Image;

public class ClientMonitor {
	private Image lastImage;
	private boolean newImage;
	private ImageBuffer imageBuffer;
	
	public ClientMonitor() {
		imageBuffer = new ImageBuffer();
		newImage = false;
	}
	
	public synchronized Image getImage() {
		while(!imageBuffer.hasImage()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	
		newImage = true;
		notifyAll();
		return lastImage;
	}
	
//	public synchronized long getTimestamp() {
//		return timestamp;
//	}
	
	public synchronized CMD getCommand() {
		return lastImage.getMode();
	}
	
	public synchronized ImageBuffer getImageBuffer() {
		return imageBuffer;
	}
	
	public synchronized void setImage(byte[] image) {
		lastImage = ImageBuilder.build(image);
		newImage = true;
		imageBuffer.put(lastImage);
		notifyAll();
	}


	
	/**
	 * Returns true once if there is a new image. Will only work with one socket connection. TODO FIX 
	 * @return
	 */
//	public synchronized boolean hasNewImage() {
//		return newImage;
//	}
}