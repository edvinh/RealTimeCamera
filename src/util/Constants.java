package util;

import se.lth.cs.eda040.fakecamera.AxisM3006V;

public class Constants {
	public static final int IDLE_WAIT_PERIOD = 5000; // Time in ms
	public static final int MOVIE_WAIT_PERIOD = 40;  // Time in ms
	public static final int SYNC_THRESHOLD = 40;    // Threshold before switching to async
	public static final int MODE_SIZE = 1;
	public static final int TIMESTAMP_SIZE = AxisM3006V.TIME_ARRAY_SIZE;
	public static final int IMAGE_PACKAGE_SIZE = 4;
}
