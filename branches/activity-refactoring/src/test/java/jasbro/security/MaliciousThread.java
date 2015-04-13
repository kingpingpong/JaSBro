package jasbro.security;

import java.io.File;
import java.security.AccessControlException;

/**
 * Simulates a malicious thread that tries to access the filesystem.
 * @author Aiko
 */
public class MaliciousThread extends Thread {
	private String[] list;
	
	public void run() {
		try {
			File f=new File(".");
			list=f.list();
		} catch (AccessControlException e) {
		}
	}
	
	public String[] getList() throws InterruptedException {
		this.join();
		return list;
	}
}
