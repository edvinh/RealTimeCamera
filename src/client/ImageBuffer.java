package client;

import java.util.ArrayList;

import util.Image;

public class ImageBuffer {
	public final static int MAX_BUFFER_SIZE = 10;
	private ArrayList<Image> queue;
	
	public ImageBuffer () {
		queue = new ArrayList<Image>();
	}
	
	
	public void put(Image image) {
		if (queue.size() < MAX_BUFFER_SIZE) {
			queue.add(image);
		}
	}
	
	
	public Image pop() {
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





//package client;
//
//import java.util.ArrayList;
//
//import util.Image;
//
//public class ImageBuffer {
//	public final static int MAX_BUFFER_SIZE = 10;
//	private ArrayList<ArrayList<Image>> queues;
//	
//	public ImageBuffer () {
//		queues = new ArrayList<ArrayList<Image>>();
//	}
//	
//	public void addQueue(ArrayList<Image> list) {
//		queues.add(list);
//	}
//	
//	public void put(Image image, int listIndex) {
//		//System.out.println("buffer size: " + queues.get(listIndex).size());
//		if (!exists(listIndex)) {
//			System.err.println("ImageBuffer in putImage(): index out of bounds in queue");
//			return;
//		}
//		ArrayList<Image> list = queues.get(listIndex);
//		
//		if (list.size() < MAX_BUFFER_SIZE) {
//			list.add(image);
//		}
//	}
//	
//	
//	public Image pop(int listIndex) {
//		if (!exists(listIndex)) {
//			System.err.println("ImageBuffer in pop(): index out of bounds in queue");
//			return null;
//		}
//		return queues.get(listIndex).remove(0);
//	}
//	
//	public boolean exists(int listIndex) {
//		if (listIndex >= queues.size() - 1) {
//			return false;
//		}
//		return true;
//	}
//	
//	public boolean hasImage(int listIndex) {
//		if (!exists(listIndex)) return false;
//		return (queues.get(listIndex).size() > 0);
//	}
//}
