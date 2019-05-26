package jasbro.util.itemEditor.usableItemEffectPanel;

import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.game.items.usableItemEffects.UsableItemShowMessage;
import jasbro.gui.pictures.ImageTag;
import jasbro.texts.TextUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class UsableItemShowMessagePanel extends JPanel {
	private UsableItemShowMessage itemEffect;

	public UsableItemShowMessagePanel(UsableItemEffect usableItemEffect) {
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
		this.itemEffect = (UsableItemShowMessage)usableItemEffect;
		add(new JLabel(TextUtil.t("imagetag")), "1, 2, left, center");
		final JComboBox<ImageTag> imageTagComboBox = new JComboBox<ImageTag>();
		add(imageTagComboBox, "2, 2, fill, top");
		for (ImageTag imageTag : ImageTag.values()) {
			imageTagComboBox.addItem(imageTag);
		}
		imageTagComboBox.setSelectedItem(itemEffect.getImageTag());
		imageTagComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				itemEffect.setImageTag((ImageTag)imageTagComboBox.getSelectedItem());
			}
		});
		
		add(new JLabel(TextUtil.t("ui.message")), "1, 3, left, center");
		final JTextArea textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setText(itemEffect.getMessage());
		textArea.setEditable(true);
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			
			public void insertUpdate(DocumentEvent e) {
				itemEffect.setMessage(textArea.getText());
			}
			
			public void removeUpdate(DocumentEvent e) {
				itemEffect.setMessage(textArea.getText());
			}
			
			public void changedUpdate(DocumentEvent e) {
				itemEffect.setMessage(textArea.getText());
			}
		});
		add(scrollPane, "2, 3, fill, top");
		
	
	}
}