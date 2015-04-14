package jasbro.util.eventEditor.effectPanels;

import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventSetQuestStatus;
import jasbro.game.world.customContent.effects.WorldEventSetQuestStatus.QuestStatus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EventEffectSetQuestStatusPanel extends JPanel {
    
    private WorldEventSetQuestStatus worldEventEffect;

    public EventEffectSetQuestStatusPanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
        setLayout(new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                RowSpec.decode("default:grow"),}));
        this.worldEventEffect = (WorldEventSetQuestStatus)worldEventEffectTmp;        
        
        final JComboBox<QuestStatus> questStatusComboBox = new JComboBox<QuestStatus>();
        add(questStatusComboBox, "1, 1, fill, top");
        for (QuestStatus questStatus : QuestStatus.values()) {
            questStatusComboBox.addItem(questStatus);
        }
        questStatusComboBox.setSelectedItem(worldEventEffect.getQuestStatus());
        questStatusComboBox.addActionListener(new ActionListener() {          
            @Override
            public void actionPerformed(ActionEvent e) {
                worldEventEffect.setQuestStatus((QuestStatus)questStatusComboBox.getSelectedItem());
            }
        });
    }
}
