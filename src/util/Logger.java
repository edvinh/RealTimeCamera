package util;

/**
 * Simple singleton logger for that outputs a varying amount 
 * of info to the console depending on log level specified.
 * @author Edvin Havic
 *
 */
public class Logger {
	private enum LOG {
		INFO(0), DEBUG(1), WARN(2), ERROR(3);
		
		private int val;
		
		private LOG(int val) {
			this.val = val;
		}
		
		public int getLevel() {
			return this.val;
		}
		
		@Override
		public String toString() {
			// \033[32m <- ANSI color code
			switch (this) {
			case INFO:
				return "INFO";
			case DEBUG:
				return "DEBUG";
			case WARN: 
				return "WARN";
			case ERROR:
				return "ERROR";
			}
			return "";
		}
	}
	
	private static Logger instance = null;
	private static LOG logLevel = LOG.DEBUG;
	
	protected Logger() {
		
	}
	
	// Singleton
	public static Logger getInstance() {
		if (instance == null) {
			instance = new Logger();
		}
		
		return instance;
	}
	
	public synchronized void setLogLevel(LOG logLevel) {
		this.logLevel = logLevel;
	}
	
	/**
	 * Outputs a string to the console with the specified log level.
	 * @param text The log 
	 * @param level The log level
	 */
	public synchronized void log(String text, LOG level) {
		if (this.logLevel.getLevel() <= level.getLevel()) {
			System.out.println(level.toString() + ": " + text);
		}
	}
	
	/**
	 * Shorthand method for the log method with log level INFO
	 * @param text The log
	 */
	public synchronized void info(String text) {
		log(text, LOG.INFO);
	}
	
	/**
	 * Shorthand method for the log method with log level DEBUG
	 * @param text The log
	 */
	public synchronized void debug(String text) {
		log(text, LOG.DEBUG);
	}
	
	/**
	 * Shorthand method for the log method with log level WARN
	 * @param text The log
	 */
	public synchronized void warn(String text) {
		log(text, LOG.WARN);
	}
	
	/**
	 * Shorthand method for the log method with log level ERROR
	 * @param text The log
	 */
	public synchronized void error(String text) {
		log(text, LOG.ERROR);
	}
}
