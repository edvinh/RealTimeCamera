package util;

public class Command {
	public final static int LENGTH = 1;
	public enum CMD {
		
		MOVIE('m'), IDLE('i'), SYNC('s'), ASYNC('a'), AUTO('q');
		private byte value;
		private CMD(char value) {
			this.value = (byte) value;
		}
		
		public byte toByte() {
			return value;
		}
	}
}
