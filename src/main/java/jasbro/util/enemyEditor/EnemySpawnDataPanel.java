package jasbro.util.enemyEditor;

import jasbro.game.world.customContent.npc.ComplexEnemyTemplate;
import jasbro.game.world.customContent.npc.EnemySpawnData;
import jasbro.game.world.customContent.npc.EnemySpawnLocation;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EnemySpawnDataPanel extends JPanel {
    private ComplexEnemyTemplate complexEnemyTemplate;
    private EnemySpawnData enemySpawnData;
    
    public EnemySpawnDataPanel(ComplexEnemyTemplate complexEnemyTemplateTmp, EnemySpawnData enemySpawnDataTmp) {
        this.complexEnemyTemplate = complexEnemyTemplateTmp;
        this.enemySpawnData = enemySpawnDataTmp;
        
        setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.DEFAULT_COLSPEC,
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                FormFactory.UNRELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.UNRELATED_GAP_ROWSPEC,}));
        

        
        JLabel lblLocation = new JLabel("Spawn-Location");
        add(lblLocation, "1, 2, right, default");
        
        final JComboBox<EnemySpawnLocation> comboBox = new JComboBox<EnemySpawnLocation>();
        add(comboBox, "2, 2, fill, default");
        for (EnemySpawnLocation enemySpawnLocation : EnemySpawnLocation.values()) {
            comboBox.addItem(enemySpawnLocation);
        }
        comboBox.setSelectedItem(enemySpawnData.getEnemySpawnLocation());
        comboBox.addActionListener(new ActionListener() {           
            @Override
            public void actionPerformed(ActionEvent e) {
                enemySpawnData.setEnemySpawnLocation((EnemySpawnLocation)(comboBox.getSelectedItem()));
            }
        });
        
        JLabel lblChance = new JLabel("Encounter Chance Modifier");
        add(lblChance, "1, 3");
        
        final JSpinner spinner = new JSpinner();
        spinner.setModel(new SpinnerNumberModel(1, 1, 999999, 1));
        spinner.setValue(enemySpawnData.getEncounterChanceModifier());
        spinner.addChangeListener(new ChangeListener() {            
            @Override
            public void stateChanged(ChangeEvent e) {
                enemySpawnData.setEncounterChanceModifier((Integer)spinner.getValue());
            }
        });
        add(spinner, "2, 3");
        
        JButton btnDelete = new JButton("Delete");
        btnDelete.setForeground(Color.RED);
        add(btnDelete, "2, 6");
        btnDelete.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                complexEnemyTemplate.getSpawnDataList().remove(enemySpawnData);
                Container parent = getParent();
                parent.remove(EnemySpawnDataPanel.this);
                parent.validate();
                parent.repaint();
            }
        });
    }
}
