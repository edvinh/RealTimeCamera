package client;

import javax.swing.SwingUtilities;

import util.Command.CMD;
import util.Constants;
import util.ImageFrame;

public class ImageDispatcher extends Thread {

	private ClientMonitor[] monitors;
	private ImagePanel[] panels;

	public ImageDispatcher(ClientMonitor[] monitors, ImagePanel[] panels) {
		this.monitors = monitors;
		this.panels = panels;
	}

	public void run() {
		while (true) {
			
			final ImageFrame images[] = { monitors[0].getImage(), monitors[1].getImage() };
			
			/*boolean synced = false;
			int firstImageWait = 0;
			int secondImageWait = 0;
			CMD syncMode = monitors[0].getSyncMode();
			boolean isAuto = (syncMode == CMD.AUTO);
			
			if (isAuto || syncMode == CMD.SYNC) {
				
				long diff = Math.abs(images[0].getTimestamp() - images[1].getTimestamp());
				
				if (diff < Constants.SYNC_THRESHOLD || syncMode == CMD.SYNC) {
					synced = true;

					if (images[0].getTimestamp() > images[1].getTimestamp()) {
						secondImageWait = (int) diff;
					} else {
						firstImageWait = (int) diff;
					}
					System.out.println("syncing, ttw: " + diff + " synced: " + synced);
				} else {
					System.out.println("not sync mode or threshold too large, ttw: " + diff + " synced: " + synced);
				}
			}
			
			// lol :))))))))))))))))))
			final int finalFirstWait = firstImageWait;
			final int finalSecondWait = secondImageWait;
			final boolean finalSynced = synced;
			*/
			if (images[0] != null && images[0].getImage() != null) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						/*if (finalSynced) {
							try {
								Thread.sleep(finalFirstWait);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}*/
						//System.out.println("panel 1 refreshed at: " + System.currentTimeMillis());
						panels[0].refresh(images[0]);
					}
				});
			}

			if (images[1] != null && images[1].getImage() != null) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						/*if (finalSynced) {
							try {
								Thread.sleep(finalSecondWait);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						*/
						//System.out.println("panel 2 refreshed at: " + System.currentTimeMillis() + " finalSynced: " + finalSynced);
						panels[1].refresh(images[1]);
					}
				});
			}
		}
	}
}
