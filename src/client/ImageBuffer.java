package client;

import java.util.ArrayList;

import util.ImageFrame;

public class ImageBuffer {
	public final static int MAX_BUFFER_SIZE = 10;
	private ArrayList<ImageFrame> queue;
	
	public ImageBuffer () {
		queue = new ArrayList<ImageFrame>();
	}
	
	
	public void put(ImageFrame image) {
		if (queue.size() < MAX_BUFFER_SIZE) {
			queue.add(image);
		}
	}
	
	
	public ImageFrame pop() {
		if (queue.size() > 0) {
			return queue.remove(0);
		} else {
			return null;
		}
	}
	
	public boolean hasImage() {
		return queue.size() > 0;
	}
}
