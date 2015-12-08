package util;

import java.nio.ByteBuffer;

import util.Command.CMD;

public class Helper {

	public static byte[] intToByteArray(int value) {
		return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value };

	}

	public static int byteArrayToInt(byte[] array) {
		return ByteBuffer.wrap(array).getInt();
	}

	public static CMD byteToCmd(byte recvCmd) {
		
		if (recvCmd == CMD.IDLE.toByte()) {
			return CMD.IDLE;
		} else if (recvCmd == CMD.MOVIE.toByte()) {
			return CMD.MOVIE;
		} else if (recvCmd == CMD.SYNC.toByte()) {
			return CMD.SYNC;
		} else if (recvCmd == CMD.ASYNC.toByte()) {
			return CMD.ASYNC;
		} else if (recvCmd == CMD.MOTION.toByte()){
			return CMD.MOTION;
		} else if (recvCmd == CMD.NO_MOTION.toByte()) {
			return CMD.NO_MOTION;
		} else if (recvCmd == CMD.MANUAL.toByte()) {
			return CMD.MANUAL;
		} else if (recvCmd == CMD.AUTO.toByte()) {
			return CMD.AUTO;
		}
		return null;
	}
}
