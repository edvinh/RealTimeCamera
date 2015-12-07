package client;

import util.Command.CMD;
import util.ImageFrame;

public class ClientMonitor {
	private ImageBuffer imageBuffer;
	private ImageFrame lastImage;
	private CMD mode;
	private CMD syncMode;
	private boolean motion = false;
	
	private boolean modeChanged = true; 
	private boolean syncModeChanged = true; 
	
	public ClientMonitor() {
		imageBuffer = new ImageBuffer();
		mode = CMD.IDLE;
		syncMode = CMD.AUTO;
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
	
	public synchronized CMD getSyncMode() {
		return syncMode;
	}
	
	public synchronized ImageBuffer getImageBuffer() {
		return imageBuffer;
	}
	
	public synchronized void setImage(byte[] image) {
		lastImage = ImageBuilder.build(image);
		// set motion detected
		motion = (lastImage.getMode() == CMD.MOTION);
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
	
	public synchronized void setSyncMode(CMD cmd) {
		if (syncMode != cmd) {
			syncMode = cmd;
			syncModeChanged = true;
			notifyAll();
		}
	}
	
	public synchronized boolean motionDetected() {
		return motion;
	}
	
	public synchronized void setMotionDetected(boolean motion) {
		this.motion = motion;
		notifyAll();
	}
	
	public synchronized boolean syncModeChanged() {
		while (!syncModeChanged) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		syncModeChanged = false;
		return true;
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