package server;

import util.Command.CMD;

public class Image {
	
	public static final int MODE_SIZE = 1;
	private byte[] timestamp, image;
	private CMD mode;
	private byte[] total;
	public Image(byte[] timestamp, byte[] image, CMD cmd) {
		this.timestamp = timestamp;
		this.image = image;
		this.mode = cmd;
	}
	
	public byte[] toBytes() {
		
		// If total isn't set, generate it
		if (total == null) {
			// Set the length
			total = new byte[timestamp.length + image.length + MODE_SIZE];
			
			// Copy the timestamp to the beginning of the array
			for (int i = 0; i < timestamp.length; i++) {
				total[i] = timestamp[i];
			}
			
			// Copy the mode after the timestamp
			total[timestamp.length] = mode.toByte();
			
			// Calculate the image offset
			int offset = timestamp.length + 1;
			
			// Copy image to total
			for (int i = 0; i < image.length; i++) {
				total[offset + i] = image[i];
			}
		}
		
		return total;
	}
}

/*                IMAGE PROTOCOL
 *     8 bytes     1 byte       variable bytes
 *  ________________________________________________
 * |__timestamp__|__mode__|__________IMAGE__________| 
 *
 */