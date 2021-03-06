package listener.server;

import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;

import com.google.common.io.Closeables;

public class EC2ListenServer {
	
	private ServerSocket serverSocket;
	private static final Integer PORT = 5055;
	
	public EC2ListenServer() {}
	
	public void run() {	
		try {
			serverSocket = null;
			try {
				serverSocket = new ServerSocket(PORT);
				waitAndListen();
			} finally {
				Closeables.close(serverSocket, true);
			}
		} catch(ConnectException ce) {
		      System.err.println("Could not connect: " + ce);
	    } catch(Throwable t) {
		      System.err.println("Error receiving data: " + t);
	    }
		// TODO better method for restarting the server
		run();
	}
		
	private void waitAndListen() {
		while(true) {
			try {
				new EC2ListenServerThread(serverSocket.accept()).start();
			} catch (IOException e) {
				if(serverSocket.isClosed()) {
					break;
				}
			}
		}
	}
}
