package jasbro.util.itemEditor.usableItemEffectPanel;

import jasbro.game.character.conditions.ConditionType;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.game.items.usableItemEffects.UsableItemRemoveCondition;
import jasbro.texts.TextUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class UsableItemRemoveConditionPanel extends JPanel {
	private UsableItemRemoveCondition itemEffect;

	public UsableItemRemoveConditionPanel(UsableItemEffect usableItemEffect) {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("left:default"),
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		add(new JLabel(usableItemEffect.getName()), "1, 1, left, center");
		this.itemEffect = (UsableItemRemoveCondition)usableItemEffect;
		add(new JLabel(TextUtil.t("condition")), "1, 2, left, center");
		final JComboBox<ConditionType> conditionTypeCombobox = new JComboBox<ConditionType>();
		add(conditionTypeCombobox, "2, 2, fill, top");
		for (ConditionType conditionType : ConditionType.values()) {
			conditionTypeCombobox.addItem(conditionType);
		}
		conditionTypeCombobox.setSelectedItem(itemEffect.getConditionType());
		conditionTypeCombobox.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				itemEffect.setConditionType((ConditionType)conditionTypeCombobox.getSelectedItem());
			}
		});
	}
}
