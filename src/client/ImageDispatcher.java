package client;

import javax.swing.SwingUtilities;

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
			
			if (images[0] != null && images[0].getImage() != null) {
				SwingUtilities.invokeLater(new Runnable () {
					public void run () {
						panels[0].refresh(images[0]);
					}
				});
			}
			
			if (images[1] != null && images[1].getImage() != null) {
				SwingUtilities.invokeLater(new Runnable () {
					public void run () {
						panels[1].refresh(images[1]);
					}
				});
			}
		}
	}
//	public void run
//	while true
//		images = monitor.getimages
//		if image1!=null
//		SwingUtils.invokelatet(new image, 1)
//		if image12=null
//		SwingUtils.invokelatet(new image, 2)
//		
}
