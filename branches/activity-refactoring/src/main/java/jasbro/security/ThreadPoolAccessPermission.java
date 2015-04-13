package jasbro.security;

import java.security.Permission;

public class ThreadPoolAccessPermission extends Permission {

	public ThreadPoolAccessPermission() {
		super("access");
	}

	@Override
	public boolean implies(Permission permission) {
		return equals(permission);
	}

	@Override
	public String getActions() {
		return "<all actions>";
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ThreadPoolAccessPermission;
	}

	@Override
	public int hashCode() {
		return 0;
	}

}
