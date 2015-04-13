package jasbro.util.eventEditor.effectPanels;

import jasbro.Jasbro;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventCallAnotherEvent;
import jasbro.texts.TextUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EventEffectCallAnotherEventPanel extends JPanel {
    private WorldEventCallAnotherEvent worldEventEffect;
    
    public EventEffectCallAnotherEventPanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
        setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.DEFAULT_COLSPEC,
                ColumnSpec.decode("default:grow")
                },
            new RowSpec[] {
                RowSpec.decode("default:grow"),}));
        this.worldEventEffect = (WorldEventCallAnotherEvent)worldEventEffectTmp;        
        
        JLabel lblNewLabel = new JLabel(TextUtil.t("eventEditor.target"));
        add(lblNewLabel, "1, 1, right, fill");
        
        final JComboBox<String> eventComboBox = new JComboBox<String>();
        add(eventComboBox, "2, 1, fill, default");
        eventComboBox.setEditable(true);
        
        List<String> eventIds = new ArrayList<String>(Jasbro.getInstance().getWorldEvents().keySet());
        Collections.sort(eventIds);
        for (String worldEvent : eventIds) {
            eventComboBox.addItem(worldEvent);
        }
        
        eventComboBox.setSelectedItem(worldEventEffect.getEventId());
        eventComboBox.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                worldEventEffect.setEventId((String)eventComboBox.getSelectedItem());
            }
        });
        
        
    }
}
