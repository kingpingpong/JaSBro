package jasbro.util.enemyEditor;

import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.world.customContent.npc.ComplexEnemyTemplate;

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

public class CombatEditorPanel extends JPanel {
    private ComplexEnemyTemplate complexEnemyTemplate;
    private JSpinner healthSpinner;
    private JSpinner damageSpinner;
    private JSpinner armorSpinner;
    private JSpinner dodgeSpinner;
    private JSpinner hitSpinner;
    private JSpinner critChanceSpinner;
    private JSpinner critDamageSpinner;
    private JSpinner blockChanceSpinner;
    private JSpinner blockAmountSpinner;
    private JSpinner speedSpinner;

    
    public CombatEditorPanel() {
        setLayout(new FormLayout(new ColumnSpec[] {
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
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,}));
        int row = 0;
        
        JLabel lblNewLabel2 = new JLabel("Hitpoints");
        add(lblNewLabel2, "1, " + (++row));
        healthSpinner = new JSpinner();
        healthSpinner.setModel(new SpinnerNumberModel(1, 1, 9999999, 1));
        add(healthSpinner, "2, " + row);
        healthSpinner.addChangeListener(new ChangeListener() {                
            @Override
            public void stateChanged(ChangeEvent e) {
                complexEnemyTemplate.setHitpoints((int)healthSpinner.getValue());
            }
        });
        
        
        lblNewLabel2 = new JLabel(CalculatedAttribute.DAMAGE.getText());
        add(lblNewLabel2, "1, " + (++row));
        damageSpinner = new JSpinner();
        damageSpinner.setModel(new SpinnerNumberModel(1.0, 0.0, 9999999.0, 0.0));
        add(damageSpinner, "2," + row);
        damageSpinner.addChangeListener(new ChangeListener() {                
            @Override
            public void stateChanged(ChangeEvent e) {
                complexEnemyTemplate.setAttribute(CalculatedAttribute.DAMAGE, ((double)damageSpinner.getValue()));
            }
        });
        
        lblNewLabel2 = new JLabel(CalculatedAttribute.ARMORPERCENT.getText());
        add(lblNewLabel2, "1, " + (++row));
        armorSpinner = new JSpinner();
        armorSpinner.setModel(new SpinnerNumberModel(0, 0, 99, 1));
        add(armorSpinner, "2," + row);
        armorSpinner.addChangeListener(new ChangeListener() {                
            @Override
            public void stateChanged(ChangeEvent e) {
                complexEnemyTemplate.setAttribute(CalculatedAttribute.ARMORPERCENT, ((double)damageSpinner.getValue()));
            }
        });
        
        lblNewLabel2 = new JLabel(CalculatedAttribute.DODGE.getText());
        add(lblNewLabel2, "1, " + (++row));
        dodgeSpinner = new JSpinner();
        dodgeSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
        add(dodgeSpinner, "2," + row);
        dodgeSpinner.addChangeListener(new ChangeListener() {                
            @Override
            public void stateChanged(ChangeEvent e) {
                complexEnemyTemplate.setAttribute(CalculatedAttribute.DODGE, (double)((int)dodgeSpinner.getValue()));
            }
        });
        
        lblNewLabel2 = new JLabel(CalculatedAttribute.HIT.getText());
        add(lblNewLabel2, "1, " + (++row));
        hitSpinner = new JSpinner();
        hitSpinner.setModel(new SpinnerNumberModel(0, -100, 1000, 1));
        add(hitSpinner, "2," + row);
        hitSpinner.addChangeListener(new ChangeListener() {                
            @Override
            public void stateChanged(ChangeEvent e) {
                complexEnemyTemplate.setAttribute(CalculatedAttribute.HIT, (double)((int)hitSpinner.getValue()));
            }
        });
        
        lblNewLabel2 = new JLabel(CalculatedAttribute.CRITCHANCE.getText());
        add(lblNewLabel2, "1, " + (++row));
        critChanceSpinner = new JSpinner();
        critChanceSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
        add(critChanceSpinner, "2," + row);
        critChanceSpinner.addChangeListener(new ChangeListener() {                
            @Override
            public void stateChanged(ChangeEvent e) {
                complexEnemyTemplate.setAttribute(CalculatedAttribute.CRITCHANCE, (double)((int)critChanceSpinner.getValue()));
            }
        });
        
        lblNewLabel2 = new JLabel(CalculatedAttribute.CRITDAMAGEAMOUNT.getText());
        add(lblNewLabel2, "1, " + (++row));
        critDamageSpinner = new JSpinner();
        critDamageSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
        add(critDamageSpinner, "2," + row);
        critDamageSpinner.addChangeListener(new ChangeListener() {                
            @Override
            public void stateChanged(ChangeEvent e) {
                complexEnemyTemplate.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, (double)((int)critDamageSpinner.getValue()));
            }
        });
        
        lblNewLabel2 = new JLabel(CalculatedAttribute.BLOCKCHANCE.getText());
        add(lblNewLabel2, "1, " + (++row));
        blockChanceSpinner = new JSpinner();
        blockChanceSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
        add(blockChanceSpinner, "2," + row);
        blockChanceSpinner.addChangeListener(new ChangeListener() {                
            @Override
            public void stateChanged(ChangeEvent e) {
                complexEnemyTemplate.setAttribute(CalculatedAttribute.BLOCKCHANCE, (double)((int)blockChanceSpinner.getValue()));
            }
        });
        
        lblNewLabel2 = new JLabel(CalculatedAttribute.BLOCKAMOUNT.getText());
        add(lblNewLabel2, "1, " + (++row));
        blockAmountSpinner = new JSpinner();
        blockAmountSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
        add(blockAmountSpinner, "2," + row);
        blockAmountSpinner.addChangeListener(new ChangeListener() {                
            @Override
            public void stateChanged(ChangeEvent e) {
                complexEnemyTemplate.setAttribute(CalculatedAttribute.BLOCKAMOUNT, (double)((int)blockAmountSpinner.getValue()));
            }
        });
        
        lblNewLabel2 = new JLabel(CalculatedAttribute.SPEED.getText());
        add(lblNewLabel2, "1, " + (++row));
        speedSpinner = new JSpinner();
        speedSpinner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
        add(speedSpinner, "2," + row);
        speedSpinner.addChangeListener(new ChangeListener() {                
            @Override
            public void stateChanged(ChangeEvent e) {
                complexEnemyTemplate.setAttribute(CalculatedAttribute.SPEED, (double)((int)speedSpinner.getValue()));
            }
        });
    }

    public void setComplexEnemyTemplate(ComplexEnemyTemplate enemy) {
        this.complexEnemyTemplate = enemy;
        healthSpinner.setValue(enemy.getHitpoints());
        damageSpinner.setValue(enemy.getAttribute(CalculatedAttribute.DAMAGE));
        armorSpinner.setValue((int)enemy.getAttribute(CalculatedAttribute.ARMORPERCENT));
        dodgeSpinner.setValue((int)enemy.getAttribute(CalculatedAttribute.DODGE));
        hitSpinner.setValue((int)enemy.getAttribute(CalculatedAttribute.HIT));
        critChanceSpinner.setValue((int)enemy.getAttribute(CalculatedAttribute.CRITCHANCE));
        critDamageSpinner.setValue((int)enemy.getAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT));
        blockChanceSpinner.setValue((int)enemy.getAttribute(CalculatedAttribute.BLOCKCHANCE));
        blockAmountSpinner.setValue((int)enemy.getAttribute(CalculatedAttribute.BLOCKAMOUNT));
        speedSpinner.setValue((int)enemy.getAttribute(CalculatedAttribute.SPEED));
    }
}
