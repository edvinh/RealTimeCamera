package client;

import util.Command.CMD;
import util.Constants;
import util.ImageFrame;

public class ClientMonitor {
	private ImageBuffer buff;
	private ImageFrame lastImage;
	private CMD mode;
	private CMD syncMode;
	private CMD autoMode;
	private boolean idleWait = false;
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
		while (buff.isEmpty()) {
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
		if (autoMode == CMD.AUTO && motion) {
			mode = CMD.MOVIE;
		}
		buff.put(lastImage);
		notifyAll();
	}

	public synchronized void setMode(CMD cmd) {
		if (mode != cmd) {
			mode = cmd;
			notifyAll();
		}
	}

	public synchronized void setSyncMode(CMD cmd) {
		syncMode = cmd;
		// notifyAll();
	}

	public synchronized void setAutoMode(CMD cmd) {
		autoMode = cmd;
		// notifyAll();
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

	public synchronized void setThreadToWait() {
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < Constants.IDLE_WAIT_PERIOD && mode == CMD.IDLE) {
			try {
				wait(Constants.IDLE_WAIT_PERIOD);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		notifyAll();
	}

}