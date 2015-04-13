package jasbro.util.eventEditor;

import jasbro.texts.TextUtil;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

import org.apache.log4j.Logger;

public class EventEditor extends JFrame {
    private final static Logger log = Logger.getLogger(EventEditor.class);
    private static EventEditor instance;
    
    public EventEditor() {
        instance = this;
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                log.error("Uncaught Exception", e);
            }
        });
        ToolTipManager.sharedInstance().setDismissDelay(10000);
        ToolTipManager.sharedInstance().setInitialDelay(100);
        
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);       
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
        
        JTabbedPane selectEditorTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(selectEditorTabbedPane);
        
        QuestPanel questPanel = new QuestPanel();
        selectEditorTabbedPane.addTab(TextUtil.t("eventEditor.quests"), null, questPanel, null);
        
        EventPanel eventPanel = new EventPanel();
        selectEditorTabbedPane.addTab(TextUtil.t("eventEditor.events"), null, eventPanel, null);
        
    }
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    EventEditor frame = new EventEditor();
                    frame.setVisible(true);
                } catch (Exception e) {
                    log.error("Error on creating frame", e);
                }
            }
        });
    }

    public static EventEditor getInstance() {
        return instance;
    }

    public static void setInstance(EventEditor instance) {
        EventEditor.instance = instance;
    }   
    
}
