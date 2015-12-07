package server;

import se.lth.cs.eda040.fakecamera.AxisM3006V;
import util.Command.CMD;
import util.Constants;

public class ServerMonitor {
	
	private byte[] imageData;
	private boolean newImage;
	private boolean cameraConnected = false;
	private int imageWaitPeriod = Constants.IDLE_WAIT_PERIOD;
	private long imageUpdatedAt = 0;
	private CMD mode;
	private CMD syncMode;
	private boolean motionDetected;
	
	public ServerMonitor() {
		newImage = false;
		imageData = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
		mode = CMD.IDLE;
		syncMode = CMD.AUTO;
	}
	
	public synchronized void setImageData(byte[] data) {
		imageData = data;
		newImage = true;
		notifyAll();
	}
	
	public synchronized byte[] getImageData() {
		if (mode == CMD.IDLE) {
			imageWaitPeriod = Constants.IDLE_WAIT_PERIOD;
		} else {
			imageWaitPeriod = Constants.MOVIE_WAIT_PERIOD;
		}
		
		
		long waitUntil = imageUpdatedAt + imageWaitPeriod; 
		
		long timeLeft = waitUntil - System.currentTimeMillis();
		
		
		while (timeLeft > 0) {
			//System.out.println("timeLeft: " + timeLeft);
			try {
				
				// If mode is auto and motion was detected, cancel the wait
				if (motionDetected() && syncMode == CMD.AUTO && mode == CMD.IDLE) {
					waitUntil = 0;
				}
				wait();
				timeLeft = waitUntil - System.currentTimeMillis();
			} catch (InterruptedException e) {}
		}
		
		
		newImage = false;
		imageUpdatedAt = System.currentTimeMillis();
		return imageData;
	}
	
	public synchronized void cameraConnected() {
		cameraConnected = true;
		notifyAll();
	}
	
	
	public synchronized boolean hasNewImage() {
		return newImage;
	}
	
	public synchronized void setMode(CMD cmd) {
		mode = cmd;
		notifyAll();
	}
	
	public synchronized void setSyncMode(CMD cmd) {
		syncMode = cmd;
		notifyAll();
	}
	
	public synchronized CMD getSyncMode() {
		return syncMode;
	}
	
	
	public synchronized CMD getMode() {
		return mode;
	}
	
	public synchronized boolean motionDetected() {
		return motionDetected;
	}
	
	public synchronized void setMotionDetected(boolean motion) {
		motionDetected = motion;
		
		// If sync mode is auto, set the camera to movie or idle 
		// mode depending on if motion was detected or not
		if (motion == true && syncMode == CMD.AUTO) {
			mode = CMD.MOVIE;
		} else if (motion == false && syncMode == CMD.AUTO) {
			mode = CMD.IDLE;
		}
		
		notifyAll();
	}
}
