package jasbro.util.enemyEditor;

import jasbro.game.world.customContent.npc.ComplexEnemyTemplate;
import jasbro.game.world.customContent.npc.EnemySpawnData;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EnemySpawnDataListPanel extends JPanel {
	private JPanel spawnListPanel;
	private ComplexEnemyTemplate complexEnemyTemplate;
	
	public EnemySpawnDataListPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JButton btnAddSpawndata = new JButton("Add SpawnData");
		add(btnAddSpawndata, "1, 1");
		btnAddSpawndata.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EnemySpawnData enemySpawnData = new EnemySpawnData();
				complexEnemyTemplate.getSpawnDataList().add(enemySpawnData);
				spawnListPanel.add(new EnemySpawnDataPanel(complexEnemyTemplate, enemySpawnData));
				validate();
				repaint();
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "1, 2, fill, fill");
		
		spawnListPanel = new JPanel();
		scrollPane.setViewportView(spawnListPanel);
		spawnListPanel.setLayout(new BoxLayout(spawnListPanel, BoxLayout.Y_AXIS));
	}
	
	public void setComplexEnemyTemplate(ComplexEnemyTemplate complexEnemyTemplate) {
		this.complexEnemyTemplate = complexEnemyTemplate;
		spawnListPanel.removeAll();
		
		for (EnemySpawnData enemySpawnData : complexEnemyTemplate.getSpawnDataList()) {
			spawnListPanel.add(new EnemySpawnDataPanel(complexEnemyTemplate, enemySpawnData));
		}
		
		validate();
	}
	
	
	
}