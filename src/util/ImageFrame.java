package util;

import se.lth.cs.eda040.fakecamera.AxisM3006V;
import util.Command.CMD;

public class ImageFrame {
	
	public static final int MODE_SIZE = Constants.MODE_SIZE;
	public static final int TIMESTAMP_SIZE = Constants.TIMESTAMP_SIZE;
	public static final int IMAGE_PACKAGE_SIZE = Constants.IMAGE_PACKAGE_SIZE;
	private byte[] timestamp, image;
	private long lTimestamp = -1;
	private CMD mode;
	private byte[] total;
	
	
	/**
	 * Creates a new Image object with a timestamp, image data, and mode. 
	 * @param timestamp timestamp as a byte array
	 * @param image image as a byte array
	 * @param cmd as a CMD (byte). 
	 */
	public ImageFrame(byte[] timestamp, byte[] image, CMD cmd) {
		this.timestamp = timestamp;
		this.image = image;
		this.mode = cmd;
	}
	
	
	/**
	 * Creates a new Image object with a timestamp, image data, and mode. 
	 * @param timestamp timestamp as a long
	 * @param image image as a byte array
	 * @param cmd as a CMD (byte). 
	 */
	public ImageFrame(long timestamp, byte[] image, CMD cmd) {
		this.lTimestamp = timestamp;
		this.image = image;
		this.mode = cmd;
	}
	
	public byte[] toBytes() {
		
		// If total isn't set, generate it
		if (total == null) {
			// Set the length
			total = new byte[IMAGE_PACKAGE_SIZE + timestamp.length + image.length + MODE_SIZE];
			byte[] imgSize = Helper.intToByteArray(image.length);
			
			for (int i = 0; i < IMAGE_PACKAGE_SIZE; i++) {
				total[i] = imgSize[i];
			}
			
			int offset = IMAGE_PACKAGE_SIZE;
			
			// Copy the timestamp to the beginning of the array
			for (int i = 0; i < timestamp.length; i++) {
				total[i + offset] = timestamp[i];
			}
			
			offset += timestamp.length;
			
			// Copy the mode after the timestamp
			total[offset] = mode.toByte();
			
			// Calculate the image offset
			offset += 1;
			
			// Copy image to total
			for (int i = 0; i < image.length; i++) {
				total[offset + i] = image[i];
			}
		}
		
		return total;
	}
	
	public byte[] getImage() {
		return image;
	}
	
	public CMD getMode() {
		return mode;
	}
	
	public long getTimestamp() {
		if (lTimestamp == -1) {
			for (int i = 0; i < TIMESTAMP_SIZE; i++) {
				// Convert timestamp to long 
				lTimestamp = (lTimestamp << 8) + (timestamp[i] & 0xff);
			}
		}
		
		return lTimestamp;
	}
}



/*                         IMAGE PROTOCOL
 *     4 bytes      8 bytes     1 byte       variable bytes
 *  _____________________________________________________________
 * |_image size_|__timestamp__|__mode__|__________IMAGE__________|
 */