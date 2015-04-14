package jasbro.util.itemEditor.equipmentEffectPanel;

import jasbro.game.items.equipmentEffect.EquipmentChangeAttack;
import jasbro.game.items.equipmentEffect.EquipmentEffect;
import jasbro.texts.TextUtil;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EquipmentChangeAttackPanel extends JPanel {
	private EquipmentChangeAttack itemEffect;

	public EquipmentChangeAttackPanel(EquipmentEffect equipmentEffect) {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("left:default"),
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		add(new JLabel(equipmentEffect.getName()), "1, 1, left, center");
		this.itemEffect = (EquipmentChangeAttack)equipmentEffect;

		add(new JLabel(TextUtil.t("ui.amount")), "1, 3, left, center");
		final JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Float(0), new Float(-10), new Float(10), new Float(0.1f)));
		spinner.setValue(itemEffect.getAmount());
		add(spinner, "2, 3, fill, top");
		spinner.addChangeListener(new ChangeListener() {				
			@Override
			public void stateChanged(ChangeEvent e) {
				EquipmentChangeAttack effect = (EquipmentChangeAttack) itemEffect;
				effect.setAmount((float)spinner.getValue());
			}
		});
	}
}
