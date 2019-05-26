package jasbro.util.enemyEditor;

import jasbro.Jasbro;
import jasbro.game.character.CharacterBase;
import jasbro.game.character.Gender;
import jasbro.game.character.battle.DamageType;
import jasbro.game.character.battle.MonsterDickType;
import jasbro.game.items.Item;
import jasbro.game.items.ItemType;
import jasbro.game.world.customContent.npc.ComplexEnemyTemplate;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

@SuppressWarnings("serial")
public class EnemyDataEditorPanel extends JTabbedPane {
	private JTextField nameField;
	private JComboBox<Gender> genderSelection;
	private JComboBox<MonsterDickType> dickSelection;
	private JComboBox<DamageType> atunementSelection;
	private ComplexEnemyTemplate complexEnemyTemplate;
	private CombatEditorPanel combatEditorPanel;
	private EnemySpawnDataListPanel spawnDataListPanel;
	private EnemySexPanel enemySexPanel;
	private JLabel lblCanBeCaptured;
	private JLabel lblSummoningStone;
	private JComboBox<String> characterBaseComboBox;
	private JComboBox<String> itemComboBox;
	private JCheckBox customerMonsterCheckbox;
	
	public EnemyDataEditorPanel() {
		JPanel mainEnemyDataPanel = new JPanel();
		addTab("Data", null, mainEnemyDataPanel, null);
		mainEnemyDataPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel jLabel2 = new javax.swing.JLabel();
		mainEnemyDataPanel.add(jLabel2, "1,1");
		jLabel2.setText("Name");
		
		nameField = new javax.swing.JTextField();
		mainEnemyDataPanel.add(nameField, "2,1");
		nameField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				complexEnemyTemplate.setName(nameField.getText());
			}
			
			public void removeUpdate(DocumentEvent e) {
				complexEnemyTemplate.setName(nameField.getText());
			}
			
			public void changedUpdate(DocumentEvent e) {
				complexEnemyTemplate.setName(nameField.getText());
			}
		});
		
		JLabel lblNewLabel = new JLabel("Gender");
		mainEnemyDataPanel.add(lblNewLabel, "1,2");
		
		genderSelection = new JComboBox<Gender>();
		mainEnemyDataPanel.add(genderSelection, "2, 2");
		for (Gender gender : Gender.values()) {
			genderSelection.addItem(gender);
		}
		genderSelection.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				complexEnemyTemplate.setGender((Gender)genderSelection.getSelectedItem());
			}
		});
		
		JLabel lblNewLabel2 = new JLabel("Dick Type");
		mainEnemyDataPanel.add(lblNewLabel2, "1,3");
		
		dickSelection = new JComboBox<MonsterDickType>();
		mainEnemyDataPanel.add(dickSelection, "2, 3");
		for (MonsterDickType dick : MonsterDickType.values()) {
			dickSelection.addItem(dick);
		}
		dickSelection.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				complexEnemyTemplate.setDick((MonsterDickType)dickSelection.getSelectedItem());
			}
		});
		
		JLabel atunementLabel = new JLabel("Element");
		mainEnemyDataPanel.add(atunementLabel, "1,4");
		
		atunementSelection = new JComboBox<DamageType>();
		mainEnemyDataPanel.add(atunementSelection, "2, 4");
		for (DamageType type : DamageType.values()) {
			if(type != DamageType.REGULAR){
				atunementSelection.addItem(type);
			}		
		}
		atunementSelection.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				complexEnemyTemplate.setElement((DamageType)atunementSelection.getSelectedItem());
			}
		});
		
		lblCanBeCaptured = new JLabel("Can be captured as:");
		mainEnemyDataPanel.add(lblCanBeCaptured, "1, 5, right, default");
		characterBaseComboBox = new JComboBox<String>();
		mainEnemyDataPanel.add(characterBaseComboBox, "2, 5, fill, default");
		characterBaseComboBox.setEditable(true);
		characterBaseComboBox.addItem(null);
		for (CharacterBase base : Jasbro.getInstance().getCharacterBases()) {
			characterBaseComboBox.addItem(base.getId());
		}
		characterBaseComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				complexEnemyTemplate.setCharacterBaseId((String)characterBaseComboBox.getSelectedItem());
			}
		});
		
		lblSummoningStone = new JLabel("Summoning Stone:");
		mainEnemyDataPanel.add(lblSummoningStone, "1, 6");
		itemComboBox = new JComboBox<String>();
		mainEnemyDataPanel.add(itemComboBox, "2, 6, fill, default");
		itemComboBox.setEditable(true);
		itemComboBox.addItem(null);
		for (Item item : Jasbro.getInstance().getAvailableItemsByType(ItemType.SUMMONING)) {
			itemComboBox.addItem(item.getId());
		}
		itemComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				complexEnemyTemplate.setItemBaseId((String)itemComboBox.getSelectedItem());
			}
		});
		
		JLabel customerMonsterLabel = new JLabel(TextUtil.t("ui.enemyEditor.customerMonster"));
		customerMonsterLabel.setToolTipText(TextUtil.t("ui.enemyEditor.customerMonster.tooltip"));
		mainEnemyDataPanel.add(customerMonsterLabel, "1, 7, left, center");
		
		customerMonsterCheckbox = new JCheckBox("");
		customerMonsterCheckbox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				complexEnemyTemplate.setCustomerMonster(e.getStateChange() == ItemEvent.SELECTED);
				
			}
		});
		
		mainEnemyDataPanel.add(customerMonsterCheckbox, "2, 7, left, top");
		
		
		combatEditorPanel = new CombatEditorPanel();
		addTab("Combat stats", null, combatEditorPanel, null);
		
		spawnDataListPanel = new EnemySpawnDataListPanel();
		addTab("Spawn", null, spawnDataListPanel, null);
		
		enemySexPanel = new EnemySexPanel();
		addTab("Text", null, enemySexPanel, null);
	}
	
	public void setEnemyTemplate(ComplexEnemyTemplate enemy) {
		this.complexEnemyTemplate = enemy;
		nameField.setText(enemy.getName());
		genderSelection.setSelectedItem(enemy.getGender());
		dickSelection.setSelectedItem(enemy.getDick());
		atunementSelection.setSelectedItem(enemy.getElement());
		combatEditorPanel.setComplexEnemyTemplate(complexEnemyTemplate);
		spawnDataListPanel.setComplexEnemyTemplate(enemy);
		enemySexPanel.setComplexEnemyTemplate(enemy);
		characterBaseComboBox.setSelectedItem(complexEnemyTemplate.getCharacterBaseId());
		itemComboBox.setSelectedItem(complexEnemyTemplate.getItemBaseId());
		customerMonsterCheckbox.setSelected(complexEnemyTemplate.isCustomerMonster());
		repaint();
	}
}