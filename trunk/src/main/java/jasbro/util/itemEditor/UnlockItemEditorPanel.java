package jasbro.util.itemEditor;

import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.housing.HouseType;
import jasbro.game.housing.RoomType;
import jasbro.game.interfaces.UnlockObject;
import jasbro.game.items.UnlockItem;
import jasbro.game.world.locations.LocationType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class UnlockItemEditorPanel extends JPanel {
	private UnlockItem item;
	
	public UnlockItemEditorPanel(UnlockItem curItem) {
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
        
        JLabel lblUnlocks = new JLabel("Unlocks:");
        panel.add(lblUnlocks, "1, 2, right, default");
        
        final JComboBox<UnlockObject> unlockComboBox = new JComboBox<UnlockObject>();
        panel.add(unlockComboBox, "2, 2, fill, default");
        for (HouseType houseType : HouseType.values()) {
            unlockComboBox.addItem(houseType);
        }
        for (RoomType roomType : RoomType.values()) {
            unlockComboBox.addItem(roomType);
        }
        for (LocationType locationType : LocationType.values()) {
            unlockComboBox.addItem(locationType);
        }
        for (SpecializationType specializationType : SpecializationType.values()) {
            if (specializationType != SpecializationType.TRAINER && 
                    specializationType != SpecializationType.SLAVE &&
                    specializationType != SpecializationType.UNDERAGE &&
                    specializationType != SpecializationType.SEX &&
                    specializationType.getAssociatedSkillTree() != null) {
                unlockComboBox.addItem(specializationType);
            }
        }
        unlockComboBox.setSelectedItem(item.getUnlockObject());
        
        unlockComboBox.addActionListener(new ActionListener() {          
            @Override
            public void actionPerformed(ActionEvent e) {
                item.setUnlockObject((UnlockObject)unlockComboBox.getSelectedItem());
            }
        });
        
        JLabel lblUnlockDescription = new JLabel("Unlockdescription:");
        panel.add(lblUnlockDescription, "1, 4, right, default");
        
        final JTextArea textArea = new JTextArea();
        panel.add(textArea, "2, 4, fill, fill");
        
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText(item.getUnlockMessage());
        textArea.setEditable(true);
        textArea.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                item.setUnlockMessage(textArea.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                item.setUnlockMessage(textArea.getText());
            }

            public void changedUpdate(DocumentEvent e) {
                item.setUnlockMessage(textArea.getText());
            }
        });

		
        SpawnDataPanel spawnDataPanel = new SpawnDataPanel(curItem);
        add(spawnDataPanel, "3, 1, fill, fill");

        validate();
        repaint();
    }
}
