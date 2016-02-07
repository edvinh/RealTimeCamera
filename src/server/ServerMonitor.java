package server;

import se.lth.cs.eda040.proxycamera.AxisM3006V;
import util.Command;
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
	private boolean motionAlert;

	public ServerMonitor() {
		newImage = false;
		imageData = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
		mode = CMD.IDLE;
		syncMode = CMD.AUTO;
		motionAlert = false;
	}

	public synchronized void setImageData(byte[] data) {
		imageData = data;
		newImage = true;
		notifyAll();
	}

	public synchronized byte[] getImageData() {
		while (!newImage) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		newImage = false;
		return imageData;
	}

	public synchronized void changeMode(CMD cmd) {
		if (mode != cmd) {
			mode = cmd;
		}
	}

	public synchronized CMD getMode() {
		return mode;
	}


	// public synchronized void setMode(CMD cmd) {
	// mode = cmd;
	// notifyAll();
	// }

	// public synchronized void setSyncMode(CMD cmd) {
	// //System.out.println("syncmode set: " + cmd.toString());
	// syncMode = cmd;
	// notifyAll();
	// }

	// public synchronized CMD getSyncMode() {
	// return syncMode;
	// }

	// public synchronized CMD getMode() {
	// return mode;
	// }

	// public synchronized boolean motionDetected() {
	// return motionDetected;
	// }
	//
	// public synchronized void setMotionDetected(boolean motion) {
	// motionDetected = motion;
	//
	// // If sync mode is auto, set the camera to movie or idle
	// // mode depending on if motion was detected or not
	//// if (motion == true && syncMode == CMD.AUTO) {
	//// mode = CMD.MOVIE;
	//// //System.out.println("mode set to movie in motiondetected, sync: " +
	// syncMode.toString());
	//// }
	// // Returns to idle mode when in auto and no motion is detected anymore.
	// /*else if (motion == false && syncMode == CMD.AUTO) {
	// mode = CMD.IDLE;
	// //System.out.println("mode set to idle in motiondetected, sync: " +
	// syncMode.toString());
	// }*/
	//
	// notifyAll();
	// }
}
