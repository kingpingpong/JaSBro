package jasbro.security;

import java.security.AccessController;

public class LegitimateActionExecutor {
	public static String[] listDir() {
		return AccessController.doPrivileged(new FileAccessPrivilegedAction());
	}
}
