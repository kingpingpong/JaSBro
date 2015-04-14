package jasbro.security;

import java.io.File;
import java.security.PrivilegedAction;

public class FileAccessPrivilegedAction implements PrivilegedAction<String[]> {

	@Override
	public String[] run() {
		File f=new File(".");
		return f.list();
	}

}
