package jasbro.util.enemyEditor;

import jasbro.Jasbro;
import jasbro.game.character.CharacterBase;
import jasbro.game.character.Gender;
import jasbro.game.world.customContent.npc.ComplexEnemyTemplate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class EnemyDataEditorPanel extends JTabbedPane {
    private JTextField nameField;
    private JComboBox<Gender> genderSelection;
    private ComplexEnemyTemplate complexEnemyTemplate;
    private CombatEditorPanel combatEditorPanel;
    private EnemySpawnDataListPanel spawnDataListPanel;
    private JLabel lblCanBeCaptured;
    private JComboBox<String> characterBaseComboBox;
    
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
        
        lblCanBeCaptured = new JLabel("Can be captured as:");
        mainEnemyDataPanel.add(lblCanBeCaptured, "1, 3, right, default");
        characterBaseComboBox = new JComboBox<String>();
        mainEnemyDataPanel.add(characterBaseComboBox, "2, 3, fill, default");
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

        
        combatEditorPanel = new CombatEditorPanel();
        addTab("Combat stats", null, combatEditorPanel, null);
        
        spawnDataListPanel = new EnemySpawnDataListPanel();
        addTab("Spawn", null, spawnDataListPanel, null);
    }
    
    public void setEnemyTemplate(ComplexEnemyTemplate enemy) {
        this.complexEnemyTemplate = enemy;
        nameField.setText(enemy.getName());
        genderSelection.setSelectedItem(enemy.getGender());
        combatEditorPanel.setComplexEnemyTemplate(complexEnemyTemplate);
        spawnDataListPanel.setComplexEnemyTemplate(enemy);
        characterBaseComboBox.setSelectedItem(complexEnemyTemplate.getCharacterBaseId());
        repaint();
    }
}
