package jasbro.util.eventEditor.effectPanels;

import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventCode;
import jasbro.gui.objects.div.MyButton;
import jasbro.gui.pictures.ImageData;
import jasbro.util.eventEditor.EventEditorPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

public class EventEffectCodePanel extends JPanel {
	private WorldEventCode worldEventEffect;
	private FormLayout layout;
	private JScrollPane scrollPane;
	private JPanel buttonPanel;
	
	public EventEffectCodePanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
		this.worldEventEffect = (WorldEventCode)worldEventEffectTmp;
		layout = new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("5dlu"),},
				new RowSpec[] {
				RowSpec.decode("0dlu"),});
		if (worldEventEffect != null) {
			updateLayout(false);
		}
		setLayout(layout);
		
		final JTextArea textArea = new JTextArea();
		scrollPane = new JScrollPane(textArea);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setText(worldEventEffect.getCode());
		textArea.setEditable(true);
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			
			public void insertUpdate(DocumentEvent e) {
				worldEventEffect.setCode(textArea.getText());
			}
			
			public void removeUpdate(DocumentEvent e) {
				worldEventEffect.setCode(textArea.getText());
			}
			
			public void changedUpdate(DocumentEvent e) {
				worldEventEffect.setCode(textArea.getText());
			}
		});
		add(scrollPane, "1, 1, fill, fill");
		
		buttonPanel = new JPanel();
		add(buttonPanel, "2, 1, fill, fill");
		buttonPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("default:grow"),
				RowSpec.decode("10dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("10dlu"),}));
		
		MyButton smallerButton = new MyButton((String) null, new ImageData("images/icons/arrow_up.png"), 
				new ImageData("images/icons/arrow_up.png"));
		buttonPanel.add(smallerButton, "1, 2, fill, fill");
		smallerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				worldEventEffect.setDisplayHeight(worldEventEffect.getDisplayHeight() - 1);
				updateLayout(true);
			}
		});
		
		MyButton biggerButton = new MyButton((String) null, new ImageData("images/icons/arrow_down.png"), 
				new ImageData("images/icons/arrow_down.png"));
		buttonPanel.add(biggerButton, "1, 4, fill, fill");
		biggerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				worldEventEffect.setDisplayHeight(worldEventEffect.getDisplayHeight() + 1);
				updateLayout(true);
			}
		});
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				scrollPane.getVerticalScrollBar().setValue(0);
			}
		});
	}
	
	public void updateLayout(boolean repaint) {
		layout = new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("5dlu"),},
				new RowSpec[] {
				RowSpec.decode((worldEventEffect.getDisplayHeight() * 80) + "dlu:grow"),});
		setLayout(layout);
		
		
		if (repaint) {
			removeAll();
			setLayout(layout);
			add(scrollPane, "1, 1, fill, fill");
			add(buttonPanel, "2, 1, fill, fill");
			EventEditorPanel eventEditorPanel = 
					(EventEditorPanel)SwingUtilities.getAncestorOfClass(EventEditorPanel.class, this);
			eventEditorPanel.validate();
			eventEditorPanel.repaint();
		}
		else {
			setLayout(layout);
		}
	}
}