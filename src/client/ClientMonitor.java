package client;

import java.util.LinkedList;

import se.lth.cs.eda040.fakecamera.AxisM3006V;
import util.Command.CMD;

public class ClientMonitor {
	private byte[] imageData;
	private long timestamp;
	private CMD cmd;
	private boolean newImage;
	private ImageBuffer imageBuffer;
	private ImageBuilder imgBuilder;
	
	public ClientMonitor() {
		imageData = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
		imageBuffer = new ImageBuffer();
		newImage = false;
	}
	
	public synchronized byte[] getImageData(int cameraId) {
		while(!imageBuffer.hasImage(cameraId)){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		newImage = false;
		return imageData;
	}
	
//	public synchronized long getTimestamp() {
//		return timestamp;
//	}
	
	public synchronized CMD getCommand() {
		return cmd;
	}
	
	public synchronized ImageBuffer getImageBuffer() {
		return imageBuffer;
	}
	
	public synchronized void setImageData(byte[] data, int cameraId) {
		imgBuilder = new ImageBuilder(data);
		imageData = imgBuilder.getImage();
		timestamp = imgBuilder.getTimestamp();
		cmd = imgBuilder.getCommand();
		newImage = true;
		LinkedList<byte[]> buffer;
		
		if (!imageBuffer.exists(cameraId)) {
			buffer = new LinkedList<byte[]>();
			imageBuffer.addQueue(buffer);
		} else {
			imageBuffer.put(imageData, cameraId);
		}
		
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