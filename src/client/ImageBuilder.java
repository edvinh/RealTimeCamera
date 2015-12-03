package client;

import se.lth.cs.eda040.fakecamera.AxisM3006V;
import util.Command.CMD;

public class ImageBuilder {
	
	public static final int IMAGE_BUFFER_SIZE = AxisM3006V.IMAGE_BUFFER_SIZE;
	public static final int TIMESTAMP_SIZE = AxisM3006V.TIME_ARRAY_SIZE;
	public static final int CMD_SIZE = 1;
	
	private byte[] imageData; 
	private byte[] timestamp;
	private long lTimestamp;
	private CMD cmd;
	
	public ImageBuilder(byte[] data) {
		timestamp = new byte[TIMESTAMP_SIZE];
		
		// Copy the first 8 bytes to the timestamp array
		for (int i = 0; i < TIMESTAMP_SIZE; i++) {
			timestamp[i] = data[i];
			// Convert timestamp to int 
			lTimestamp = (lTimestamp << 8) + (data[i] & 0xff);
		}
		
		// Copy the 9th byte to the cmd 
		byte recvCmd = data[TIMESTAMP_SIZE];
		
		if (recvCmd == CMD.IDLE.toByte()) {
			cmd = CMD.IDLE;
		} else if (recvCmd == CMD.MOVIE.toByte()) {
			cmd = CMD.MOVIE;
		} else if (recvCmd == CMD.SYNC.toByte()) {
			cmd = CMD.SYNC;
		} else if (recvCmd == CMD.ASYNC.toByte()) {
			cmd = CMD.ASYNC;
		} else {
			cmd = CMD.AUTO;
		}
		

		int offset = TIMESTAMP_SIZE + 1;
		imageData = new byte[IMAGE_BUFFER_SIZE];
		// Copy the image to the image array
		for (int i = 0; i < IMAGE_BUFFER_SIZE; i++) {
			imageData[i] = data[offset + i]; 
		}
	}
	
	public byte[] getImage() {
		return imageData;
	}
	
	public CMD getCommand() {
		return cmd;
	}
	
	public long getTimestamp() {
		return lTimestamp;
	}
	
	public byte[] getTimestampAsByteArray() {
		return timestamp;
	}
}
