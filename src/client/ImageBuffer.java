package client;

import java.util.ArrayList;

import util.ImageFrame;

public class ImageBuffer {
	public final static int MAX_BUFFER_SIZE = 10;
	private ArrayList<ImageFrame> queue;

	public ImageBuffer() {
		queue = new ArrayList<ImageFrame>();
	}

	public void put(ImageFrame image) {
		queue.add(image);
	}
	
	public boolean isFull() {
		return queue.size() >= 10;
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

	public int size() {
		return queue.size();
	}
}
