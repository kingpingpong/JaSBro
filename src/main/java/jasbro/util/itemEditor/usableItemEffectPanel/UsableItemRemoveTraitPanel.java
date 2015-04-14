package jasbro.util.itemEditor.usableItemEffectPanel;

import jasbro.game.character.traits.Trait;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.game.items.usableItemEffects.UsableItemRemoveTrait;
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

public class UsableItemRemoveTraitPanel extends JPanel {
	private UsableItemRemoveTrait itemEffect;

	public UsableItemRemoveTraitPanel(UsableItemEffect usableItemEffect) {
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
		this.itemEffect = (UsableItemRemoveTrait)usableItemEffect;
		add(new JLabel(TextUtil.t("trait")), "1, 2, left, center");
		final JComboBox<Trait> traitCombobox = new JComboBox<Trait>();
		add(traitCombobox, "2, 2, fill, top");
		for (Trait trait : Trait.values()) {
			traitCombobox.addItem(trait);
		}
		traitCombobox.setSelectedItem(itemEffect.getTrait());
		traitCombobox.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				itemEffect.setTrait((Trait)traitCombobox.getSelectedItem());
			}
		});
	}
}
