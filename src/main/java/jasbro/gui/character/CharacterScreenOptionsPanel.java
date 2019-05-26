package jasbro.gui.character;

import jasbro.game.character.Charakter;
import jasbro.gui.objects.div.TranslucentPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class CharacterScreenOptionsPanel extends TranslucentPanel {
	private Charakter character;
	private JCheckBox contraceptivesCheckbox;
	
	public CharacterScreenOptionsPanel(Charakter characterTmp) {
		setPreferredSize(null);
		character = characterTmp;
		
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow")},
				new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		
		contraceptivesCheckbox = new JCheckBox("Use contraceptives");
		add(contraceptivesCheckbox, "1, 1");
		contraceptivesCheckbox.setSelected(character.isUsesContraceptives());
		contraceptivesCheckbox.setOpaque(false);
		contraceptivesCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				character.setUsesContraceptives(contraceptivesCheckbox.isSelected());
			}
		});
	}
}