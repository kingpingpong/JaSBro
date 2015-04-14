package jasbro.util.itemEditor.usableItemEffectPanel;

import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.items.usableItemEffects.UsableItemChangeAttributeMax;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.texts.TextUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class UsableItemChangeAttributeMaxPanel extends JPanel {
	private UsableItemChangeAttributeMax itemEffect;

	public UsableItemChangeAttributeMaxPanel(UsableItemEffect usableItemEffect) {
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
		this.itemEffect = (UsableItemChangeAttributeMax)usableItemEffect;
		add(new JLabel(TextUtil.t("attribute")), "1, 2, left, center");
		final JComboBox<AttributeType> attributeTypeCombobox = new JComboBox<AttributeType>();
		add(attributeTypeCombobox, "2, 2, fill, top");
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
		attributeTypeCombobox.setSelectedItem(itemEffect.getAttribute());
		attributeTypeCombobox.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				itemEffect.setAttribute((AttributeType)attributeTypeCombobox.getSelectedItem());
			}
		});
		add(new JLabel(TextUtil.t("ui.change")), "1, 3, left, center");
		final JSpinner spinner = new JSpinner();
		spinner.setValue(itemEffect.getChange());
		add(spinner, "2, 3, fill, top");
		spinner.addChangeListener(new ChangeListener() {				
			@Override
			public void stateChanged(ChangeEvent e) {
				itemEffect.setChange((int)spinner.getValue());
			}
		});
		
		add(new JLabel(TextUtil.t("ui.maximum")), "1, 4, left, center");
		final JSpinner spinner2 = new JSpinner();
		spinner2.setValue(itemEffect.getMax());
		add(spinner2, "2, 4, fill, top");
		spinner2.addChangeListener(new ChangeListener() {				
			@Override
			public void stateChanged(ChangeEvent e) {
				itemEffect.setMax((int)spinner2.getValue());
			}
		});
	}
}
