package jasbro.util.itemEditor;

import jasbro.game.items.Item;
import jasbro.game.items.ItemSpawnData;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SpawnDataPanel extends JPanel {
	public SpawnDataPanel(final Item item) {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JLabel lblSpawnChances = new JLabel("Spawn chances");
		add(lblSpawnChances, "1, 2");
		
		JPanel spawnPanel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(spawnPanel);
		spawnPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("default:grow"),
				FormFactory.MIN_ROWSPEC,}));
		add(scrollPane, "1, 3, fill, fill");
		
		final JPanel spawnListPanel = new JPanel();
		spawnPanel.add(spawnListPanel, "1, 1");
		spawnListPanel.setLayout(new GridLayout(0, 1));
		
		for (ItemSpawnData itemSpawnData : item.getSpawnData()) {
			JPanel spawnSubPanel = new SpawnChancePanel(itemSpawnData, item);
			spawnListPanel.add(spawnSubPanel);
		}
		
		JButton addSpawnLocationButton = new JButton("Add");
		spawnPanel.add(addSpawnLocationButton, "1, 2");
		addSpawnLocationButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ItemSpawnData itemSpawnData = new ItemSpawnData();
				item.getSpawnData().add(itemSpawnData);
				JPanel spawnSubPanel = new SpawnChancePanel(itemSpawnData, item);
				spawnListPanel.add(spawnSubPanel);
				validate();
			}
		});
	}
}