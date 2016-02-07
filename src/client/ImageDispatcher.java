package client;

import util.Command.CMD;
import util.Constants;
import util.ImageFrame;

public class ImageDispatcher extends Thread {

	private ClientMonitor monitor1, monitor2;
	private ImagePanel[] panels;

	public ImageDispatcher(ClientMonitor monitor1, ClientMonitor monitor2, ImagePanel[] panels) {
		this.monitor1 = monitor1;
		this.monitor2 = monitor2;
		this.panels = panels;
	}

	public void run() {
		while (true) {
			ImageFrame im1 = monitor1.getImage();
			ImageFrame im2 = monitor2.getImage();
			long im1Stamp = im1.getTimestamp();
			long im2Stamp = im2.getTimestamp();
			
			//Set Idle
			
			
			// Check sync threshold
			long diff = Math.abs(im1Stamp - im2Stamp);
			if ((diff > Constants.SYNC_THRESHOLD && monitor1.getAutoMode() == CMD.AUTO)
					|| (monitor1.getAutoMode() != CMD.AUTO && monitor1.getSyncMode() == CMD.ASYNC)) {
				monitor1.setSyncMode(CMD.ASYNC);
				monitor2.setSyncMode(CMD.ASYNC);
				panels[0].refresh(im1);
				panels[1].refresh(im2);
			} else {
				ImageFrame toWait, toSend;
				int panelToWait, panelToSend;
				long latestStamp = Math.max(im1Stamp, im2Stamp);
				if (im1Stamp == latestStamp) {
					toWait = im1;
					toSend = im2;
					panelToWait = 0;
					panelToSend = 1;
				} else {
					toWait = im2;
					toSend = im1;
					panelToWait = 1;
					panelToSend = 0;
				}
				panels[panelToSend].refresh(toSend);
				try {
					Thread.sleep(diff);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				panels[panelToWait].refresh(toWait);
			}
		}
	}
}
