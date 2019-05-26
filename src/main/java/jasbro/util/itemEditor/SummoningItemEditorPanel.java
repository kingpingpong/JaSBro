package jasbro.util.itemEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import jasbro.Jasbro;
import jasbro.game.items.SummoningItem;
import jasbro.game.world.customContent.npc.ComplexEnemyTemplate;

public class SummoningItemEditorPanel extends JPanel {
	private JComboBox<ComplexEnemyTemplate> unlockComboBox;
	private SummoningItem item;
	private String monsterID;
	private ComplexEnemyTemplate enemyTemplate;
	
	public SummoningItemEditorPanel(SummoningItem curItem) {
		this.item = curItem;
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow(8)"),
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		
		
		JPanel panel = new JPanel();
		add(panel, "1, 1, fill, fill");
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JLabel lblUnlocks = new JLabel("Summons:");
		panel.add(lblUnlocks, "1, 4, right, default");
		
		unlockComboBox = new JComboBox<ComplexEnemyTemplate>();
		panel.add(unlockComboBox, "2, 4, fill, default");
		for (ComplexEnemyTemplate enemyTemplate : Jasbro.getInstance().getEnemyTemplates().values()) {
				unlockComboBox.addItem(enemyTemplate);
				monsterID = enemyTemplate.getId();
		}
		
		unlockComboBox.setSelectedItem(item.getSummonedMonster());
		unlockComboBox.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				item.setSummonedMonster((String) monsterID);
			}
		});
		
		
		SpawnDataPanel spawnDataPanel = new SpawnDataPanel(curItem);
		add(spawnDataPanel, "3, 1, fill, fill");
		
		validate();
		repaint();
	}
}