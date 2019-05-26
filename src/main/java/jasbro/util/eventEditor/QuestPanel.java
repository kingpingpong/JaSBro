package jasbro.util.eventEditor;

import jasbro.game.world.customContent.CustomQuestTemplate;

import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class QuestPanel extends JPanel {
	private JPanel questEditorPanel;
	
	public QuestPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("120dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		
		QuestListPanel questListPanel = new QuestListPanel(this);
		add(questListPanel, "1, 1, fill, fill");
		
		questEditorPanel = new JPanel();
		add(questEditorPanel, "3, 1, fill, fill");
	}
	
	public void setQuest(CustomQuestTemplate quest) {
		if (questEditorPanel != null) {
			remove(questEditorPanel);
		}
		questEditorPanel = new QuestEditorPanel(quest);
		add(questEditorPanel, "3, 1, fill, fill");
		validate();
		repaint();
	}
	
}