package jasbro.gui.objects.div;


public interface MessageInterface {
	public void init();
	public boolean isPriorityMessage();
	public void setMessageGroupObject(Object charcterGroupObject);
	public Object getMessageGroupObject();
}