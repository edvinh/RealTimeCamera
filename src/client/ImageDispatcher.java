package client;

import javax.swing.SwingUtilities;

import util.ImageFrame;

public class ImageDispatcher extends Thread {
	
	private ClientMonitor monitor;
	private ImagePanel panel;
	public ImageDispatcher(ClientMonitor monitor, ImagePanel panel) {
		this.monitor = monitor;
		this.panel = panel;
	}
	
	public void run() {
		while (true) {
			//System.out.println("imageDispatcher running...");
			final ImageFrame image = monitor.getImage();
			//System.out.println("image length:" + image.getImage().length);
			if (image != null && image.getImage() != null) {
				SwingUtilities.invokeLater(new Runnable () {
					public void run () {
						//System.out.println("refreshing image: " + image.getImage().length);
						panel.refresh(image);
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
