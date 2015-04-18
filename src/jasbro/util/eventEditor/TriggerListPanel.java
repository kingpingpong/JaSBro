package jasbro.util.eventEditor;

import jasbro.game.world.customContent.CustomQuestStage;
import jasbro.game.world.customContent.Trigger;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.texts.TextUtil;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class TriggerListPanel extends JPanel {
    private CustomQuestStage customQuestStage;
    private WorldEvent worldEvent;
    
    private List<Trigger> triggers;
    private JPanel triggerListPanel;
    
    //Only for Window editor
    @SuppressWarnings("unused")
    private TriggerListPanel() {
        init(null);
    }
    
    public TriggerListPanel(CustomQuestStage customQuestStage) {
        this.customQuestStage = customQuestStage;
        init(customQuestStage.getTriggers());
    }
    
    public TriggerListPanel(WorldEvent worldEvent) {
        this.worldEvent = worldEvent;
        init(worldEvent.getTriggers());
    }
    
    private void init(List<Trigger> triggersTmp) {
        setLayout(new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("default:grow"),
                FormFactory.UNRELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                RowSpec.decode("pref:grow"),
                FormFactory.RELATED_GAP_ROWSPEC,}));
        
        JButton addTriggerButton = new JButton(TextUtil.t("eventEditor.addTrigger"));
        add(addTriggerButton, "1, 2, right, default");
        addTriggerButton.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                Trigger trigger = new Trigger();
                triggers.add(trigger);
                triggerListPanel.add(new TriggerPanel(trigger, customQuestStage));
                
                Component parent = getParent();
                while (parent.getParent() != null && !(parent instanceof QuestEditorPanel)) {
                    parent = parent.getParent();
                }
                parent.validate();
                parent.repaint();
            }
        });
        
        triggerListPanel = new JPanel();
        add(triggerListPanel, "1, 3, 3, 1, fill, fill");
        triggerListPanel.setLayout(new BoxLayout(triggerListPanel, BoxLayout.Y_AXIS));
        
        if (triggersTmp != null) {
            this.triggers = triggersTmp;
            for (Trigger trigger : triggers) {
                if (customQuestStage != null) {
                    triggerListPanel.add(new TriggerPanel(trigger, customQuestStage));
                }
                else {
                    triggerListPanel.add(new TriggerPanel(trigger, worldEvent));
                }
            }
        }
    }

    public List<Trigger> getTriggers() {
        return triggers;
    }

    
}
