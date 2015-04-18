package jasbro.security;

import java.io.FilePermission;
import java.io.SerializablePermission;
import java.lang.reflect.ReflectPermission;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.PropertyPermission;

/**
 * Permissions for required by Jasbro.
 * @author Aiko
 *
 */
public class JasbroPermissionCollection extends PermissionCollection {

    ArrayList<Permission> perms = new ArrayList<Permission>();

    public JasbroPermissionCollection() {
        perms.add(new PropertyPermission("*", "read, write"));
        perms.add(new FilePermission("<<ALL FILES>>", "read, write, delete"));

        perms.add(new ThreadPoolAccessPermission());
        
        // required by truevfs:
        perms.add(new RuntimePermission("shutdownHooks"));
        perms.add(new RuntimePermission("fileSystemProvider"));
        
        // required by xstream libs:
        perms.add(new RuntimePermission("createClassLoader"));
        perms.add(new ReflectPermission("suppressAccessChecks"));
        perms.add(new SerializablePermission("enableSubclassImplementation"));
        perms.add(new RuntimePermission("accessClassInPackage.sun.misc"));
        perms.add(new RuntimePermission("accessDeclaredMembers"));
    }
    
    public boolean implies(Permission p) {
        for (Iterator<Permission> i = perms.iterator(); i.hasNext();) {
            if (((Permission) i.next()).implies(p)) {
                return true;
            }
        }
        return false;
    }

    public Enumeration<Permission> elements() {
        return Collections.enumeration(perms);
    }

	@Override
	public void add(Permission permission) {
		if (!isReadOnly()) {
			perms.add(permission);
		} else {
			throw new SecurityException("Permission collection is read-only.");
		}
	}
}