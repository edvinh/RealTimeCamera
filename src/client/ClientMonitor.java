package client;

import util.Command.CMD;
import util.ImageFrame;

public class ClientMonitor {
	private ImageBuffer imageBuffer;
	private ImageFrame lastImage;
	private CMD mode;
	private boolean modeChanged = true; 
	public ClientMonitor() {
		imageBuffer = new ImageBuffer();
		mode = CMD.AUTO;
	}
	
	public synchronized ImageFrame getImage() {
		while (!imageBuffer.hasImage()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	
		notifyAll();
		return imageBuffer.pop();
	}
	
	public synchronized CMD getMode() {
		return mode;
	}
	
	public synchronized ImageBuffer getImageBuffer() {
		return imageBuffer;
	}
	
	public synchronized void setImage(byte[] image) {
		lastImage = ImageBuilder.build(image);
		imageBuffer.put(lastImage);
		notifyAll();
	}
	
	public synchronized void setMode(CMD cmd) {
		if (mode != cmd) {
			mode = cmd;
			modeChanged = true;
			notifyAll();
		}
	}
	
	public synchronized boolean modeChanged() {
		while (!modeChanged) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		modeChanged = false;
		return true;
	}
}