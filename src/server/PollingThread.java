package server;

import client.ClientMonitor;
import se.lth.cs.eda040.fakecamera.AxisM3006V;

public class PollingThread extends Thread {

	private int period;
	private long oldTime = 0;
	private long lastTime = 0;
	private byte[] image;
	private AxisM3006V camera;
	private ServerMonitor monitor;
	public final static int IDLE = 5;
	
	
	/**
	 * Creates a polling thread which polls the camera at a set interval
	 * 
	 * @param period
	 *            the periodicity of the polling
	 * @param camera
	 * 			  The camera which will be polled 
	 */
	public PollingThread(int period, AxisM3006V camera, ServerMonitor monitor) {
		this.period = period;
		this.camera = camera;
		this.monitor = monitor;
		image = new byte[AxisM3006V.IMAGE_BUFFER_SIZE];
	}

	public synchronized void setPeriod(int period) {
		this.period = period;
	}

	public byte[] getImage() {
		return image;
	}

	public void run() {
		while (true) {
			long currTime = System.currentTimeMillis();
			long diff = Math.abs(currTime - oldTime);
			
			if (currTime + diff < lastTime + period) {
				System.out.println("Polling thread waits...");
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				// Time to get the jpeg
				System.out.println("Polling thread requests a picture...");
				camera.getJPEG(image, 0);

				
				monitor.setImageData(image);
				notifyAll();
			}
			oldTime = System.currentTimeMillis();
		}
	}
}
