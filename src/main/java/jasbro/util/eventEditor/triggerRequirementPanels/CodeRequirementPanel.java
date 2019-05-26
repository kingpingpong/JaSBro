package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.world.customContent.requirements.CodeRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class CodeRequirementPanel extends JPanel {
	private CodeRequirement triggerRequirement;
	
	public CodeRequirementPanel(TriggerRequirement triggerRequirementTmp) {
		this.triggerRequirement = (CodeRequirement)triggerRequirementTmp;
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("max(350dlu;default):grow"),},
				new RowSpec[] {
				RowSpec.decode("30dlu"),}));
		
		final JTextArea textArea = new JTextArea();
		final JScrollPane scrollPane = new JScrollPane(textArea);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setText(triggerRequirement.getCode());
		textArea.setEditable(true);
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			
			public void insertUpdate(DocumentEvent e) {
				triggerRequirement.setCode(textArea.getText());
			}
			
			public void removeUpdate(DocumentEvent e) {
				triggerRequirement.setCode(textArea.getText());
			}
			
			public void changedUpdate(DocumentEvent e) {
				triggerRequirement.setCode(textArea.getText());
			}
		});
		add(scrollPane, "1, 1, fill, fill");
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				scrollPane.getVerticalScrollBar().setValue(0);
			}
		});
		
	}
}