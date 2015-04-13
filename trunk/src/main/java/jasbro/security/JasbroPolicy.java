package jasbro.security;

import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Policy;

/**
 * Jasbro's global security policy.
 * @author Aiko
 *
 */
public class JasbroPolicy extends Policy {
    private PermissionCollection perms = new JasbroPermissionCollection();
    
    private PermissionCollection interpreterPerms = new BshPermissionCollection();

    @Override
    public PermissionCollection getPermissions(CodeSource codesource) {
    	boolean bsh;
    	String path=codesource.getLocation().getPath();
    	String libName=path.substring(path.lastIndexOf('/')+1);
    	bsh=libName.startsWith("bsh-");
    	
        return bsh ? interpreterPerms : perms;
    }
}
