package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.world.customContent.requirements.AttributeRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement.Comparison;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AttributeRequirementPanel extends JPanel {
	private AttributeRequirement triggerRequirement;

	public AttributeRequirementPanel(TriggerRequirement triggerRequirementTmp) {
		this.triggerRequirement = (AttributeRequirement)triggerRequirementTmp;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		        
        final JComboBox<AttributeType> attributeTypeCombobox = new JComboBox<AttributeType>();
        add(attributeTypeCombobox, "1, 1");
        for (AttributeType attributeType : EssentialAttributes.values()) {
            attributeTypeCombobox.addItem(attributeType);
        }
        for (AttributeType attributeType : BaseAttributeTypes.values()) {
            attributeTypeCombobox.addItem(attributeType);
        }
        for (AttributeType attributeType : Sextype.values()) {
            attributeTypeCombobox.addItem(attributeType);
        }
        for (AttributeType attributeType : SpecializationAttribute.values()) {
            attributeTypeCombobox.addItem(attributeType);
        }
        for (AttributeType attributeType : CalculatedAttribute.values()) {
            attributeTypeCombobox.addItem(attributeType);
        }
        attributeTypeCombobox.setSelectedItem(triggerRequirement.getAttributeType());
        attributeTypeCombobox.addActionListener(new ActionListener() {          
            @Override
            public void actionPerformed(ActionEvent e) {
                triggerRequirement.setAttributeType((AttributeType)attributeTypeCombobox.getSelectedItem());
            }
        });
		
        final JComboBox<Comparison> comboBox = new JComboBox<Comparison>();
        add(comboBox);
        for (Comparison dayComparison : Comparison.values()) {
            comboBox.addItem(dayComparison);
        }
        comboBox.setSelectedItem(triggerRequirement.getComparison());
        comboBox.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                triggerRequirement.setComparison((Comparison)comboBox.getSelectedItem());
            }
        });
		
        final JSpinner spinner = new JSpinner();
        spinner.setValue(triggerRequirement.getAmount());
        add(spinner);
        spinner.addChangeListener(new ChangeListener() {                
            @Override
            public void stateChanged(ChangeEvent e) {
                triggerRequirement.setAmount((int)spinner.getValue());
            }
        });
	}
}
