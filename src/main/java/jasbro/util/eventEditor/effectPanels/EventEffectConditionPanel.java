package jasbro.util.eventEditor.effectPanels;

import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventCondition;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirementType;
import jasbro.texts.TextUtil;
import jasbro.util.eventEditor.triggerRequirementPanels.TriggerRequirementPanel;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EventEffectConditionPanel extends JPanel {
    private final static Logger log = Logger.getLogger(EventEffectConditionPanel.class);
    private WorldEventCondition worldEventEffect;
    private JPanel triggerRequirementPanel;
    private TriggerRequirementPanel selectedRequirementPanel;

    public EventEffectConditionPanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
        setLayout(new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("left:default:none"),
                ColumnSpec.decode("default:grow"),
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.DEFAULT_COLSPEC,},
            new RowSpec[] {
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,}));
        this.worldEventEffect = (WorldEventCondition)worldEventEffectTmp; 
        
        JLabel lblNewLabel_1 = new JLabel(TextUtil.t("eventEditor.requirements"));
        add(lblNewLabel_1, "1, 1, left, default");
        
        final JComboBox<TriggerRequirementType> requirementComboBox = new JComboBox<TriggerRequirementType>();
        add(requirementComboBox, "2, 1, fill, default");
        for (TriggerRequirementType triggerRequirementType : TriggerRequirementType.values()) {
            requirementComboBox.addItem(triggerRequirementType);
        }
        
        JButton addRequirementButton = new JButton(TextUtil.t("eventEditor.addRequirement"));
        add(addRequirementButton, "3, 1");
        addRequirementButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    TriggerRequirement triggerRequirement = ((TriggerRequirementType)requirementComboBox.getSelectedItem()).
                            getRequirementClass().newInstance();
                    TriggerRequirementPanel triggerPanel = new TriggerRequirementPanel(triggerRequirement, new MyMouseListener());
                    
                    if (triggerRequirementPanel.getComponents().length == 0) {
                        triggerRequirementPanel.add(triggerPanel);
                        worldEventEffect.setRequirement(triggerRequirement);
                        setSelected(triggerPanel);
                    }
                    else {
                        if (selectedRequirementPanel != null && 
                                selectedRequirementPanel.getTriggerRequirement().canAddRequirement(triggerRequirement)) {
                            selectedRequirementPanel.getTriggerRequirement().getSubRequirements().add(triggerRequirement);
                            selectedRequirementPanel.addPanel(triggerPanel);
                            setSelected(triggerPanel);
                        }
                    }
                    
                    validate();
                    repaint();
                }
                catch (Exception ex) {
                    log.error("Error when creating trigger requirement panel", ex);
                }
            }
        });
        
        JButton deleteRequirementButton = new JButton(TextUtil.t("eventEditor.deleteRequirement"));
        add(deleteRequirementButton, "4, 1");
        deleteRequirementButton.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedRequirementPanel != null) {
                    Container parent = selectedRequirementPanel.getParent();
                    if (parent == triggerRequirementPanel) {
                        worldEventEffect.setRequirement(null);
                        triggerRequirementPanel.remove(selectedRequirementPanel);
                    }
                    else {
                        parent.remove(selectedRequirementPanel);
                        while (!(parent instanceof TriggerRequirementPanel)) {
                            parent = parent.getParent();
                        }
                        ((TriggerRequirementPanel)parent).getTriggerRequirement().getSubRequirements().remove(selectedRequirementPanel.getTriggerRequirement());
                    }
                    selectedRequirementPanel = null;                    
                    validate();
                    repaint();
                }
            }
        });
        
        triggerRequirementPanel = new JPanel();
        add(triggerRequirementPanel, "1, 2, 4, 1, fill, fill");
        triggerRequirementPanel.setLayout(new GridLayout(0, 1, 0, 0));
        
        if (worldEventEffect.getRequirement() != null) {
            addTriggerRequirementPanel(worldEventEffect.getRequirement());
        }
    }
    
    public void addTriggerRequirementPanel(TriggerRequirement triggerRequirement) {
        TriggerRequirementPanel triggerRequirementPanel = new TriggerRequirementPanel(triggerRequirement, new MyMouseListener());
        this.triggerRequirementPanel.add(triggerRequirementPanel);
    }
    
    public void setSelected(TriggerRequirementPanel triggerRequirementPanel) {
        if (selectedRequirementPanel != null) {                              
            selectedRequirementPanel.setSelected(false);
        }
        selectedRequirementPanel = triggerRequirementPanel;
        triggerRequirementPanel.setSelected(true);

        validate();
        repaint();
    }
    
    private class MyMouseListener extends MouseAdapter {
        public void mouseClicked(java.awt.event.MouseEvent e) {
            if (e.getSource() instanceof TriggerRequirementPanel) {
                TriggerRequirementPanel newPanel = (TriggerRequirementPanel) e.getSource();
                setSelected(newPanel);
            }
        };
    }
}
