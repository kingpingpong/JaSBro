package jasbro.util.eventEditor.effectPanels;

import jasbro.Jasbro;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventCreateQuest;
import jasbro.texts.TextUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EventEffectCreateQuestPanel extends JPanel {
	
	private WorldEventCreateQuest worldEventEffect;
	
	public EventEffectCreateQuestPanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		this.worldEventEffect = (WorldEventCreateQuest)worldEventEffectTmp;
		
		JLabel lblNewLabel = new JLabel(TextUtil.t("eventEditor.quest"));
		add(lblNewLabel, "1, 1, right, fill");
		
		final JComboBox<String> questIdComboBox = new JComboBox<String>();
		add(questIdComboBox, "2, 1, fill, default");
		questIdComboBox.setEditable(true);
		
		questIdComboBox.setSelectedItem(worldEventEffect.getQuestId());
		questIdComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				worldEventEffect.setQuestId((String)questIdComboBox.getSelectedItem());
			}
		});
		
		List<String> questIdList = new ArrayList<String>(Jasbro.getInstance().getCustomQuestTemplates().keySet());
		Collections.sort(questIdList);
		for (String questId : questIdList) {
			questIdComboBox.addItem(questId);
		}
		
	}
	
}