package jasbro.security;

import java.security.Permission;
import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.PropertyPermission;

/**
 * Permissions for interpreted bsh scripts. Essentially none.
 * @author Aiko
 *
 */
public class BshPermissionCollection extends PermissionCollection {

    ArrayList<Permission> perms = new ArrayList<Permission>();

    public BshPermissionCollection() {
    	add(new PropertyPermission("*", "read"));
    }
    
    public void add(Permission p) {
        perms.add(p);
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
}
