package client;

import util.Command.CMD;
import util.ImageFrame;

public class ClientMonitor {
	private ImageBuffer buff;
	private ImageFrame lastImage;
	private CMD mode;
	private CMD syncMode;
	private CMD autoMode;
	private boolean motion = false;
	private boolean autoChanged = false;
	private boolean modeChanged = false;
	private boolean syncModeChanged = true;

	public ClientMonitor() {
		buff = new ImageBuffer();
		mode = CMD.IDLE;
		syncMode = CMD.SYNC;
		autoMode = CMD.AUTO;
	}

	public synchronized ImageFrame getImage() {
		ImageFrame temp = null;
		while (!buff.hasImage()) {
			
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		temp = buff.pop();
		notifyAll();
		return temp;
	}

	public synchronized CMD getMode() {
		return mode;
	}

	public synchronized CMD getSyncMode() {
		return syncMode;
	}

	public synchronized CMD getAutoMode() {
		return autoMode;
	}

	public synchronized void setImage(byte[] image) {
		lastImage = ImageBuilder.build(image);
		motion = (lastImage.getMode() == CMD.MOTION);
		while (buff.isFull()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		buff.put(lastImage);
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

	public synchronized void setAutoMode(CMD cmd) {
		if (autoMode != cmd) {
			autoMode = cmd;
			autoChanged = true;
			notifyAll();
		}
	}

	public synchronized boolean motionDetected() {
		return motion;
	}

	public synchronized boolean syncModeChanged() {
		System.out.println("sync mode changed");
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
		System.out.println("mode changed");
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