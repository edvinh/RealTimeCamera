package client;

import java.util.ArrayList;
import java.util.LinkedList;

public class ImageBuffer {
	//TODO implement max buffer size
	public final static int MAX_BUFFER_SIZE = 10;
	private ArrayList<LinkedList<byte[]>> queues;
	
	public ImageBuffer () {
		queues = new ArrayList<LinkedList<byte[]>>();
	}
	
	public void addQueue(LinkedList<byte[]> list) {
		queues.add(list);
	}
	
	public void put(byte[] image, int listIndex) {
		System.out.println("buffer size: " + queues.get(listIndex).size());
		if (!exists(listIndex)) {
			System.err.println("ImageBuffer in putImage(): index out of bounds in queue");
			return;
		}
		LinkedList<byte[]> list = queues.get(listIndex);
		
		if (list.size() < MAX_BUFFER_SIZE) {
			list.add(image);
		}
	}
	
	
	public byte[] pop(int listIndex) {
		if (!exists(listIndex)) {
			System.err.println("ImageBuffer in pop(): index out of bounds in queue");
			return new byte[0];
		}
		return queues.get(listIndex).pop();
	}
	
	public boolean exists(int listIndex) {
		if (listIndex >= queues.size() - 1) {
			return false;
		}
		return true;
	}
	
	public boolean hasImage(int listIndex) {
		if (!exists(listIndex)) return false;
		return (queues.get(listIndex).size() > 0);
	}
}
