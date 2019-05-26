package jasbro.gui.character;

import jasbro.game.character.Charakter;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class CharacterScreenNamePanel extends TranslucentPanel {
	
	public CharacterScreenNamePanel(final Charakter character) {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("1dlu:grow"),
				ColumnSpec.decode("8dlu:none"),},
				new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		setPreferredSize(null);
		final JTextField nameTextField = new JTextField(character.getName());
		add(nameTextField, "1, 1");
		
		nameTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				character.setName((nameTextField.getText().trim()));
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				character.setName((nameTextField.getText().trim()));
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				character.setName((nameTextField.getText().trim()));
			}
		});
		
		MyImage infoImage = new MyImage(new ImageData("images/icons/info-pic.png"));
		infoImage.setToolTipText(TextUtil.htmlPreformatted(character.getBase().getFullDescription()));
		add(infoImage, "2, 1, fill, fill");
	}
}