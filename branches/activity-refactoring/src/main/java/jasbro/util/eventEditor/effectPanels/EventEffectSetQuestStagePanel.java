package jasbro.util.eventEditor.effectPanels;

import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventSetQuestStage;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EventEffectSetQuestStagePanel extends JPanel {
    
    private WorldEventSetQuestStage worldEventEffect;

    public EventEffectSetQuestStagePanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
        setLayout(new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                RowSpec.decode("default:grow"),}));
        this.worldEventEffect = (WorldEventSetQuestStage)worldEventEffectTmp;        
        
        final JSpinner spinner = new JSpinner();
        spinner.setModel(new SpinnerNumberModel(0, null, null, 1));
        spinner.setValue(worldEventEffect.getQuestStage());
        add(spinner, "1, 1, fill, top");
        spinner.addChangeListener(new ChangeListener() {                
            @Override
            public void stateChanged(ChangeEvent e) {
                worldEventEffect.setQuestStage((int)spinner.getValue());
            }
        });
    }

}
