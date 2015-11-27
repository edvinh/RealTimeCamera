package server;

import java.io.IOException;

import se.lth.cs.eda040.fakecamera.AxisM3006V;

public class Server {
	public static void main(String[] args) {
		int port = 3001;
		String address = "argus-1.student.lth.se";
		AxisM3006V camera = new AxisM3006V();
		camera.init();
		camera.setProxy(address, port);
		// TODO Start sending camera stuff with ServerSocketConnection and PollingThread
	}
}
