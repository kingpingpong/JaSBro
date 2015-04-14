package jasbro.util.eventEditor;

import jasbro.game.world.customContent.WorldEvent;

import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EventPanel extends JPanel {
    private JPanel eventEditorPanel;
    
    public EventPanel() {
        setLayout(new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("120dlu"),
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                RowSpec.decode("default:grow"),}));
        
        EventListPanel eventListPanel = new EventListPanel(this);
        add(eventListPanel, "1, 1, fill, fill");
        
        eventEditorPanel = new JPanel();
        add(eventEditorPanel, "3, 1, fill, fill");
    }

    public void setEvent(WorldEvent event) {
        if (eventEditorPanel != null) {
            remove(eventEditorPanel);
        }
        eventEditorPanel = new EventEditorPanel(event);
        add(eventEditorPanel, "3, 1, fill, fill");
        validate();
        repaint();
    }
    
}
