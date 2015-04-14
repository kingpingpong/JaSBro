package jasbro.security;

import jasbro.Jasbro;
import jasbro.game.world.customContent.WorldEvent;

import java.io.File;
import java.security.AccessControlException;

import org.junit.Assert;
import org.junit.Test;

import bsh.EvalError;
import bsh.Interpreter;

public class SecureInterpreterTest {

	private static final String DIR_LIST_CODE="import java.io.*; File f=new File(\".\"); f.list();";
	
	private static final String CREATE_THREAD_CODE="Thread t=new jasbro.security.MaliciousThread(); t.start(); t.getList();";
	
	private static final String USE_THREADPOOL_CODE="import jasbro.Jasbro; r = new jasbro.security.SecureInterpreterTest.MyRunnable(); Jasbro.getInstance().getThreadpool().execute(r); Thread.sleep(100); r.getList();"; //don't want to work with futures, let's use sleep
	
	private static final String EXECUTE_WITH_THREADPOOL_CODE="Thread t=new jasbro.security.MaliciousThread(); jasbro.Jasbro.getThreadpool().execute(t); Thread.sleep(100); t.getList();";
	
	private static final String SWING_UTILITIES_INVOKE_LATER_CODE="Thread t=new jasbro.security.MaliciousThread(); javax.swing.SwingUtilities.invokeLater(t); Thread.sleep(100); t.getList(); ";
	
	private static final String EXECUTE_WITH_PRIVILEGED_ACTION_CODE="java.security.AccessController.doPrivileged(new jasbro.security.MaliciousPrivilegedAction());";
	
	private static final String ACCESS_WORLD_EVENTS_CODE="Jasbro.getInstance().getWorldEvents().get(\"contestQuestMaidContestGeneration\");";
	
	private Interpreter interpreter=new Interpreter();

	@Test
	public void testEval() throws EvalError {
		Jasbro.setupSecurity();

		try {
			interpreter.eval(DIR_LIST_CODE);
			Assert.fail("Security check should have caused exception.");
		} catch (EvalError e) {
			// ok
		}
		
		Assert.assertNull("Secure interpreter should be unable to get directory list from malicious thread", interpreter.eval(CREATE_THREAD_CODE));

		try {
			interpreter.eval(USE_THREADPOOL_CODE);
			Assert.fail("Security check should have caused exception.");
		} catch (EvalError e) {
			// good
		}

		try {
			interpreter.eval(EXECUTE_WITH_THREADPOOL_CODE);
			Assert.fail("Security check should have caused exception.");
		} catch (EvalError e) {
			// good
		}

		Assert.assertNull("Secure interpreter should be unable to get directory list via SwingUtilities.invokeLater(..)", interpreter.eval(SWING_UTILITIES_INVOKE_LATER_CODE));
		
		Assert.assertTrue(interpreter.eval(ACCESS_WORLD_EVENTS_CODE) instanceof WorldEvent);
		
		try {
			interpreter.eval(EXECUTE_WITH_PRIVILEGED_ACTION_CODE);
			Assert.fail("Security check should have caused exception.");
		} catch (EvalError e) {
			// ok
		}
		
		Assert.assertNotNull("Privileged action executed from code base outside interpreter should work fine.", interpreter.eval("jasbro.security.LegitimateActionExecutor.listDir();"));
	}
	
	public static class MyRunnable implements Runnable {
	    private String[] list;
	    
	    public void run() {
	        try {
	            File f=new File(".");
	            list=f.list();
	        } catch (AccessControlException e) {
	        }
	    }
	    
	    public String[] getList() throws InterruptedException {
	        return list;
	    }
	}
}
