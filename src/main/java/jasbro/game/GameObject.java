package jasbro.game;

import jasbro.Jasbro;
import jasbro.WeakList;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.MyEventListener;

import java.awt.Component;
import java.io.Serializable;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

/**
 *
 * @author Azrael
 */
public abstract class GameObject implements MyEventListener, Serializable {
    private final static Logger log = Logger.getLogger(GameObject.class);
    private static final long serialVersionUID = -7717504249836567448L;
    private transient WeakList<MyEventListener> guiListeners = new WeakList<MyEventListener>();
    private WeakList<MyEventListener> listeners = new WeakList<MyEventListener>();
    
    public void handleEvent(MyEvent e) {
    }
    
    public void fireEvent(final MyEvent e) {        
        try {
            if (getListeners().size() > 0) {
                for (final MyEventListener listener : getListeners().strongCopy()) {
                    try {
                        listener.handleEvent(e);
                    } catch (NullPointerException ex) {
                    } catch (Exception ex) {
                        log.error("Error", ex);
                    }
                }
            }
        }
        catch (Exception ex) {
            log.error("Error while notifying listeners", ex);
        }
            
        
        try {
            if (getGuiListeners().size() > 0) {
                for (final MyEventListener listener : getGuiListeners().strongCopy()) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            try {
                                listener.handleEvent(e);
                            } catch (NullPointerException ex) {
                            } catch (Exception ex) {
                                log.error("Error", ex);
                            }
                        };
                    });
                }
            }
        } catch (Exception ex) {
            log.error("Error while notifying gui listeners", ex);
        }
        Jasbro.getInstance().getData().getEventManager().handleEvent(e);
    }

    private WeakList<MyEventListener> getListeners() {
        if (listeners == null) {
            listeners = new WeakList<MyEventListener>();
        }
        return listeners;
    }
    
    private WeakList<MyEventListener> getGuiListeners() {
        if (guiListeners == null) {
        	guiListeners = new WeakList<MyEventListener>();
        }
        return guiListeners;
    }
    
    public void addListener(MyEventListener listener) {
    	if (listener instanceof Component || !(listener instanceof Serializable)) {
    		if (!getGuiListeners().contains(listener)) {
        		getGuiListeners().add(listener);
    		}
    	}
    	else {
            if (!getListeners().contains(listener)) {
                getListeners().add(listener);
            }    		
    	}
    }

    public void clearGuiListeners() {
        try {
            guiListeners.clear();
        }
        catch (Exception e) {
            log.error("Error during clear", e);
        }
    }
}
