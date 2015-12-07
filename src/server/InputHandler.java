package server;

import util.Command;
import util.Command.CMD;
import util.Helper;

public class InputHandler extends Thread {
	
	private ServerSocketConnection socket;
	private ServerMonitor monitor;
	public InputHandler(ServerSocketConnection socket, ServerMonitor monitor) {
		this.socket = socket;
		this.monitor = monitor;
	}
	
	public void run () {
		byte[] data = new byte[Command.LENGTH];
		while (true) {
			try {
				//System.out.println("Reading...");
				socket.read(data);
				// Get the command
				byte bCmd = data[0];
				CMD cmd = Helper.byteToCmd(bCmd);
				
				if (cmd == CMD.MOVIE || cmd == cmd.IDLE) {
					monitor.setMode(cmd);	
				} else {
					monitor.setSyncMode(cmd);
				}
			} catch (Exception e) {
				//e.printStackTrace();
				//System.out.println("Exception in input handler");
			}
			
		}
	}
}
