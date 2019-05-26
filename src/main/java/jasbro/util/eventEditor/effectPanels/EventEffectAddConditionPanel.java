package jasbro.util.eventEditor.effectPanels;

import jasbro.game.character.conditions.ConditionType;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventAddCondition;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EventEffectAddConditionPanel extends JPanel {
	
	private WorldEventAddCondition worldEventEffect;
	
	public EventEffectAddConditionPanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		this.worldEventEffect = (WorldEventAddCondition)worldEventEffectTmp;
		
		final JComboBox<ConditionType> conditionComboBox = new JComboBox<ConditionType>();
		add(conditionComboBox, "1, 1, fill, top");
		for (ConditionType questStatus : ConditionType.values()) {
			conditionComboBox.addItem(questStatus);
		}
		conditionComboBox.setSelectedItem(worldEventEffect.getConditionType());
		conditionComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				worldEventEffect.setConditionType((ConditionType)conditionComboBox.getSelectedItem());
			}
		});
	}
	
}