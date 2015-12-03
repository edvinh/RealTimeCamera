package client;

import javax.swing.SwingUtilities;

public class ImageDispatcher extends Thread {
	
	private ClientMonitor monitor;
	private ImagePanel panel;
	public ImageDispatcher(ClientMonitor monitor, ImagePanel panel) {
		this.monitor = monitor;
		this.panel = panel;
	}
	
	public void run() {
		while (true) {
			// TODO do image class instead
			byte[] imageData = monitor.getImageData(0);
			if (imageData != null) {
				SwingUtilities.invokeLater(new Runnable () {
					public void run () {
						panel.refresh(imageData);
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
