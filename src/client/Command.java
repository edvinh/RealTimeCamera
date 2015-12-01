package client;

public class Command {
	
	public enum CMD {
		MOVIE('m'), IDLE('i'), FLAG('f');
		private byte value;
		private CMD(char value) {
			this.value = (byte) value;
		}
		
		public byte toByte() {
			return value;
		}
	}
}
