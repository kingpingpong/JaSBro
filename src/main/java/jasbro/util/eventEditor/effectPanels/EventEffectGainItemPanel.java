package jasbro.util.eventEditor.effectPanels;

import jasbro.Jasbro;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventGainItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EventEffectGainItemPanel extends JPanel {
	
	private WorldEventGainItem worldEventEffect;
	
	public EventEffectGainItemPanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		this.worldEventEffect = (WorldEventGainItem)worldEventEffectTmp;
		
		final JComboBox<String> gainItemComboBox = new JComboBox<String>();
		add(gainItemComboBox, "1, 1, fill, default");
		gainItemComboBox.setEditable(true);
		
		gainItemComboBox.setSelectedItem(worldEventEffect.getItemId());
		gainItemComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				worldEventEffect.setItemId((String)gainItemComboBox.getSelectedItem());
			}
		});
		
		Jasbro.getThreadpool().execute(new Runnable() {
			
			@Override
			public void run() {
				List<String> itemIds = new ArrayList<String>(Jasbro.getInstance().getItems().keySet());
				Collections.sort(itemIds);
				for (String itemId : itemIds) {
					gainItemComboBox.addItem(itemId);
				}
				validate();
				repaint();
			}
		});
	}
	
}