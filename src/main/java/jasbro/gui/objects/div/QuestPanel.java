package jasbro.gui.objects.div;

import jasbro.game.quests.Quest;
import jasbro.gui.GuiUtil;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class QuestPanel extends TranslucentPanel {
	
	public QuestPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				RowSpec.decode("default:grow"),
				RowSpec.decode("default:grow"),
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		setBorder(GuiUtil.DEFAULTEMPTYBORDER);
		setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
		setOpaque(false);
		setPreferredSize(null);
		getPreferredSize().width = 0;
	}
	
	public void init(Quest quest) {
		removeAll();
		
		JLabel lblNewLabel = new JLabel(quest.getTitle());
		lblNewLabel.setFont(GuiUtil.DEFAULTBOLDFONT);
		add(lblNewLabel, "1, 1, fill, fill");
		
		JTextArea textArea = GuiUtil.getDefaultTextarea();
		textArea.setText(quest.getDescription());
		add(textArea, "1, 2, fill, fill");
	}
}