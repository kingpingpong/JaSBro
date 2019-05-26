package jasbro.util.itemEditor;

import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import jasbro.game.items.LootItem;

public class LootItemEditorPanel extends JPanel {
	private LootItem item;
	
	public LootItemEditorPanel(LootItem curItem) {
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
		
		SpawnDataPanel spawnDataPanel = new SpawnDataPanel(curItem);
		add(spawnDataPanel, "3, 1, fill, fill");
		
		validate();
		repaint();
	}
}