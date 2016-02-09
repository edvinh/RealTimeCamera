package client;

import java.util.LinkedList;

import util.ImageFrame;

public class ImageBuffer {
	public final static int MAX_BUFFER_SIZE = 10;
	private LinkedList<ImageFrame> q;

	public ImageBuffer() {
		q = new LinkedList<ImageFrame>();
	}

	public void put(ImageFrame image) {
		if (q.size() >= MAX_BUFFER_SIZE) {
			q.remove();
			q.add(image);
		} else {
			q.add(image);
		}
	}

	public ImageFrame pop() {
		return q.pop();
	}

	public boolean isEmpty() {
		return q.size() == 0;
	}
}
