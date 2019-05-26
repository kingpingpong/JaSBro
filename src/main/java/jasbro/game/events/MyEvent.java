/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jasbro.game.events;

/**
 *
 * @author Azrael
 */
public class MyEvent {
	private EventType type;
	private Object source;
	private boolean cancelled = false;
	
	public MyEvent(EventType type, Object source) {
		this.type = type;
		this.source = source;
	}
	
	public Object getSource() {
		return source;
	}
	
	public EventType getType() {
		return type;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}