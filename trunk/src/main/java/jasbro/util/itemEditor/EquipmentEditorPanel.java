package jasbro.util.itemEditor;

import jasbro.game.items.AccessoryType;
import jasbro.game.items.Equipment;
import jasbro.game.items.EquipmentType;
import jasbro.game.items.equipmentEffect.EquipmentEffect;
import jasbro.game.items.equipmentEffect.EquipmentEffectType;
import jasbro.texts.TextUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.JLabel;

public class EquipmentEditorPanel extends JPanel {
    private final static Logger log = Logger.getLogger(EquipmentEditorPanel.class);
    private Equipment item;
    private JComboBox<EquipmentEffectType> effectTypeComboBox;
    private EquipmentEffectPanel selectedEffectPanel;
    private JComboBox<EquipmentType> equipmentTypeComboBox;
    private JComboBox<AccessoryType> accessoryTypeComboBox;

    public EquipmentEditorPanel(Equipment curItem) {
        this.item = curItem;
        setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("default:grow(8)"), FormFactory.UNRELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"), }, new RowSpec[] { FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("default:grow"), }));

        final JPanel effectPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(effectPanel);

        add(scrollPane, "1, 1, 1, 2, fill, fill");
        effectPanel.setLayout(new BoxLayout(effectPanel, BoxLayout.Y_AXIS));

        JPanel panel = new JPanel();
        add(panel, "3, 1, fill, fill");
        panel.setLayout(new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.UNRELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.UNRELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                RowSpec.decode("default:grow"),}));

        { //Equipment combo box
            equipmentTypeComboBox = new JComboBox<EquipmentType>();
            panel.add(equipmentTypeComboBox, "1, 1, fill, default");
            for (EquipmentType equipmentType : EquipmentType.values()) {
                equipmentTypeComboBox.addItem(equipmentType);
            }
            equipmentTypeComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    EquipmentType equipmentType = (EquipmentType) equipmentTypeComboBox.getSelectedItem();
                    item.setEquipmentType(equipmentType);
                    
                    if (equipmentType == EquipmentType.ACCESSORY) {
                        accessoryTypeComboBox.setVisible(true);
                    }
                    else {
                        accessoryTypeComboBox.setVisible(false);
                        item.setAccessoryType(null);
                        accessoryTypeComboBox.setSelectedItem(null);
                    }
                }
            });
        }

        { //Accessory combo box
            accessoryTypeComboBox = new JComboBox<AccessoryType>();
            panel.add(accessoryTypeComboBox, "1, 3, fill, default");
            accessoryTypeComboBox.addItem(null);
            for (AccessoryType accessoryType : AccessoryType.values()) {
                accessoryTypeComboBox.addItem(accessoryType);
            }
            accessoryTypeComboBox.setSelectedItem(item.getAccessoryType());
            accessoryTypeComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    item.setAccessoryType((AccessoryType) accessoryTypeComboBox.getSelectedItem());
                }
            });
        }
        
        //Set equipment type now to trigger actionEvent
        equipmentTypeComboBox.setSelectedItem(item.getEquipmentType());

        effectTypeComboBox = new JComboBox<EquipmentEffectType>();
        panel.add(effectTypeComboBox, "1, 5, fill, default");

        for (EquipmentEffectType itemEffectType : EquipmentEffectType.values()) {
            effectTypeComboBox.addItem(itemEffectType);
        }

        JButton btnAdd = new JButton("Add");
        panel.add(btnAdd, "1, 6");
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (selectedEffectPanel != null) {
                        selectedEffectPanel.setSelected(false);
                    }
                    EquipmentEffectType equipmentEffectType = (EquipmentEffectType) effectTypeComboBox.getSelectedItem();
                    EquipmentEffect equipmentEffect = equipmentEffectType.getItemEffectClass().newInstance();
                    final EquipmentEffectPanel newPanel = new EquipmentEffectPanel();
                    newPanel.setItemEffect(equipmentEffect);
                    item.getEquipmentEffects().add(equipmentEffect);
                    effectPanel.add(newPanel);
                    newPanel.setSelected(true);
                    selectedEffectPanel = newPanel;
                    validate();
                    repaint();
                    newPanel.addMouseListener(new MyMouseListener());
                } catch (Exception ex) {
                    log.error("Error", ex);
                }
            }
        });
        
        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedEffectPanel != null) {
                    if (!selectedEffectPanel.hasChildEffects()) {
                        item.getEquipmentEffects().remove(selectedEffectPanel.getItemEffect());
                        effectPanel.remove(selectedEffectPanel);
                        if (effectPanel.getComponents().length > 0) {
                            EquipmentEffectPanel equipmentEffectPanel = (EquipmentEffectPanel) effectPanel.getComponent(0);
                            equipmentEffectPanel.setSelected(true);
                            selectedEffectPanel = equipmentEffectPanel;
                        } else {
                            selectedEffectPanel = null;
                        }

                        validate();
                        repaint();
                    }
                }
            }
        });
        panel.add(btnDelete, "1, 7");
        
        JLabel lblCalculatedValue = new JLabel("Calculated Value:");
        panel.add(lblCalculatedValue, "1, 8");
        
        final JLabel valueLabel = new JLabel(TextUtil.t("formatted", new Object[] {item.calculateValue()}));
        panel.add(valueLabel, "1, 9");
        
        JButton btnRecalculate = new JButton("Recalculate");
        panel.add(btnRecalculate, "1, 10");
        btnRecalculate.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                valueLabel.setText(TextUtil.t("formatted", new Object[] {item.calculateValue()}));
            }
        });

        if (item.getEquipmentEffects() != null) {
            for (EquipmentEffect equipmentEffect : item.getEquipmentEffects()) {
                try {
                    EquipmentEffectPanel equipmentEffectPanel = new EquipmentEffectPanel();
                    equipmentEffectPanel.setItemEffect(equipmentEffect);
                    effectPanel.add(equipmentEffectPanel);
                    equipmentEffectPanel.addMouseListener(new MyMouseListener());
                } catch (Exception e) {
                    log.error("Error while initializing item effect panel", e);
                }
            }
        }

        add(new SpawnDataPanel(curItem), "3, 2, fill, fill");

        validate();
        repaint();
    }

    private class MyMouseListener extends MouseAdapter {
        public void mouseClicked(java.awt.event.MouseEvent e) {
            if (selectedEffectPanel != null) {
                selectedEffectPanel.setSelected(false);
            }
            EquipmentEffectPanel newPanel = (EquipmentEffectPanel) e.getSource();
            newPanel.setSelected(true);
            selectedEffectPanel = newPanel;

            validate();
            repaint();
        };
    }
}
