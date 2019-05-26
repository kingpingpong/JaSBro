package jasbro.util.eventEditor.effectPanels;

import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventComment;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EventEffectCommentPanel extends JPanel {
	private WorldEventComment worldEventEffect;
	
	public EventEffectCommentPanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("80dlu"),}));
		this.worldEventEffect = (WorldEventComment)worldEventEffectTmp;
		
		final JTextArea textArea = new JTextArea();
		final JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setText(worldEventEffect.getComment());
		textArea.setEditable(true);
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			
			public void insertUpdate(DocumentEvent e) {
				worldEventEffect.setComment(textArea.getText());
			}
			
			public void removeUpdate(DocumentEvent e) {
				worldEventEffect.setComment(textArea.getText());
			}
			
			public void changedUpdate(DocumentEvent e) {
				worldEventEffect.setComment(textArea.getText());
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