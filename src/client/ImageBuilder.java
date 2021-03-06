package client;

import se.lth.cs.eda040.fakecamera.AxisM3006V;
import util.Command.CMD;
import util.Helper;
import util.ImageFrame;

public class ImageBuilder {
	
	public static final int IMAGE_BUFFER_SIZE = AxisM3006V.IMAGE_BUFFER_SIZE;
	public static final int TIMESTAMP_SIZE = AxisM3006V.TIME_ARRAY_SIZE;
	public static final int CMD_SIZE = 1;
	
	private static byte[] imageData; 
	private static byte[] timestamp;
	private static long lTimestamp;
	private static CMD cmd;
	
	protected ImageBuilder() {
	
	}
	
	public static synchronized ImageFrame build(byte[] data) {
		
		imageData = new byte[data.length - TIMESTAMP_SIZE - CMD_SIZE];
		timestamp = new byte[TIMESTAMP_SIZE];
		
		// Copy the first 8 bytes to the timestamp array
		for (int i = 0; i < TIMESTAMP_SIZE; i++) {
			timestamp[i] = data[i];
			// Convert timestamp to int 
			lTimestamp = (lTimestamp << 8) + (data[i] & 0xff);
		}
		
		// Copy the 9th byte to the cmd 
		byte recvCmd = data[TIMESTAMP_SIZE];
		
		cmd = Helper.byteToCmd(recvCmd);

		int offset = TIMESTAMP_SIZE + 1;
		// Copy the image to the image array
		for (int i = 0; i < data.length - offset; i++) {
			imageData[i] = data[offset + i]; 
		}
		
		return new ImageFrame(lTimestamp, imageData, cmd);
	}
}
