package jasbro.util.itemEditor;

import jasbro.Jasbro;
import jasbro.game.items.Equipment;
import jasbro.game.items.Item;
import jasbro.game.items.ItemFileLoader;
import jasbro.game.items.ItemType;
import jasbro.game.items.UnlockItem;
import jasbro.game.items.UsableItem;
import jasbro.game.world.customContent.ImageSelection;
import jasbro.game.world.customContent.ImageSelection.ImageLocation;
import jasbro.gui.objects.div.MyImage;
import jasbro.texts.TextUtil;
import jasbro.util.eventEditor.ImageSelectionPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;


public class ItemEditorPanel extends JPanel {
	private Item item;
	private JTextField textField;
	private MyImage imagePreview;
	private ImageSelectionPanel imageSelection;

	public ItemEditorPanel(Item curItem, final ItemEditor itemEditor) {
		this.item = curItem;
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.MIN_ROWSPEC,
				RowSpec.decode("default:grow(3)"),}));
		
		JPanel baseDataPanel = new JPanel();
		add(baseDataPanel, "1, 1, fill, fill");
		FormLayout layout = new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				RowSpec.decode("default:grow"),});
		baseDataPanel.setLayout(layout);
		layout.setColumnGroups(new int[][]{new int[]{1, 3, 5}});
		
		JPanel panel = new JPanel();
		baseDataPanel.add(panel, "1, 1, fill, fill");
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				RowSpec.decode("default:grow"),
				RowSpec.decode("default:grow"),
				RowSpec.decode("default:grow"),
				RowSpec.decode("default:grow"),}));
		
		JLabel lblItemName = new JLabel("Item name:");
		panel.add(lblItemName, "1, 1, right, default");
		
		textField = new JTextField();
		textField.setText(item.getName());
		textField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				item.setName(textField.getText());
			}

			public void removeUpdate(DocumentEvent e) {
				item.setName(textField.getText());
			}

			public void changedUpdate(DocumentEvent e) {
				item.setName(textField.getText());
			}
		});
		panel.add(textField, "2, 1, fill, default");
		
		JLabel lblValue = new JLabel("Value");
		panel.add(lblValue, "1, 2");
		
		final JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		spinner.setValue(item.getValue());
		spinner.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent e) {
				item.setValue((Integer)spinner.getValue());
			}
		});
		panel.add(spinner, "2, 2");
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				ItemFileLoader.getInstance().save(item);
			}
		});
		
		JLabel lblType = new JLabel("Type");
		panel.add(lblType, "1, 3, left, default");
		
		final JComboBox<ItemType> comboBox = new JComboBox<ItemType>();
		for (ItemType itemType : ItemType.values()) {
			comboBox.addItem(itemType);
		}
		comboBox.setSelectedItem(item.getType());
		comboBox.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				Item newItem;
				if (comboBox.getSelectedItem() == ItemType.EQUIPMENT) {
					newItem = new Equipment(item);
				}
				else if (comboBox.getSelectedItem() == ItemType.UNLOCK) {
				    newItem = new UnlockItem(item);
				}
				else {
					newItem = new UsableItem(item);
				}
				Jasbro.getInstance().getItems().remove(item.getId());
				Jasbro.getInstance().getItems().put(newItem.getId(), newItem);
				itemEditor.setItem(newItem);
			}
		});
		panel.add(comboBox, "2, 3, fill, default");
		panel.add(btnSave, "1, 4");
		
		JButton btnDelete = new JButton("Delete");
		panel.add(btnDelete, "2, 4, center, default");
		btnDelete.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(ItemEditorPanel.this,
                        "Delete Item?",
                        "Delete",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == 0) {
                    ItemFileLoader.getInstance().delete(item);
                    itemEditor.setItem(null);
                }
			}
		});
		
		JTabbedPane descriptionTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		baseDataPanel.add(descriptionTabbedPane, "3, 1, fill, fill");		
		
		{
    		JPanel panel_1 = new JPanel();
    		descriptionTabbedPane.addTab("Ingame description", null, panel_1, null);
    		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
    				FormFactory.DEFAULT_COLSPEC,
    				ColumnSpec.decode("default:grow"),},
    			new RowSpec[] {
    				RowSpec.decode("default:grow"),}));
    		
    		JLabel lblDescription = new JLabel(TextUtil.htmlPreformatted("Description\n(ingame)"));
    		panel_1.add(lblDescription, "1, 1");
    		
    		final JTextArea textArea = new JTextArea();
    		JScrollPane scrollPane = new JScrollPane(textArea);
    		
    		textArea.setLineWrap(true);
    		textArea.setWrapStyleWord(true);
    		textArea.setText(item.getDescription());
    		textArea.setEditable(true);
    		textArea.getDocument().addDocumentListener(new DocumentListener() {
    
    			public void insertUpdate(DocumentEvent e) {
    				item.setDescription(textArea.getText());
    			}
    
    			public void removeUpdate(DocumentEvent e) {
    				item.setDescription(textArea.getText());
    			}
    
    			public void changedUpdate(DocumentEvent e) {
    				item.setDescription(textArea.getText());
    			}
    		});
    		panel_1.add(scrollPane, "2, 1, fill, fill");
		}
		
		{
    		JPanel panel_1 = new JPanel();
    		descriptionTabbedPane.addTab("Author description", null, panel_1, null);
            panel_1.setLayout(new FormLayout(new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    ColumnSpec.decode("default:grow"),},
                new RowSpec[] {
                    RowSpec.decode("default:grow"),}));
            
            JLabel lblDescription = new JLabel(TextUtil.htmlPreformatted("Author\ndescription"));
            panel_1.add(lblDescription, "1, 1");
            
            final JTextArea textArea = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(textArea);
            
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setText(item.getAuthorDescription());
            textArea.setEditable(true);
            textArea.getDocument().addDocumentListener(new DocumentListener() {
    
                public void insertUpdate(DocumentEvent e) {
                    item.setAuthorDescription(textArea.getText());
                }
    
                public void removeUpdate(DocumentEvent e) {
                    item.setAuthorDescription(textArea.getText());
                }
    
                public void changedUpdate(DocumentEvent e) {
                    item.setAuthorDescription(textArea.getText());
                }
            });
            panel_1.add(scrollPane, "2, 1, fill, fill");
		}
		
		{ // image selection
		    JPanel imageSelectionPanel = new JPanel();
		    baseDataPanel.add(imageSelectionPanel, "5, 1, fill, fill");     
		    imageSelectionPanel.setLayout(new FormLayout(new ColumnSpec[] {
		            ColumnSpec.decode("left:default:grow"),},
		        new RowSpec[] {
		            FormFactory.DEFAULT_ROWSPEC,
		            RowSpec.decode("default:grow"),
		            RowSpec.decode("default:grow"),}));
		    
		    final JCheckBox chckbxDefaultImage = new JCheckBox("Default Image");
		    imageSelectionPanel.add(chckbxDefaultImage, "1, 1");
		    if (item.getImageSelection() == null) {
		        chckbxDefaultImage.setSelected(true);
		    }
		    chckbxDefaultImage.addActionListener(new ActionListener() {                
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (chckbxDefaultImage.isSelected()) {
                        imagePreview.setVisible(true);
                        imageSelection.setVisible(false);
                        item.setImageSelection(null);
                    }
                    else {
                        imagePreview.setVisible(false);
                        item.setImageSelection(new ImageSelection());
                        item.getImageSelection().setImageLocation(ImageLocation.GLOBAL);
                        imageSelection.init(item.getImageSelection());
                        imageSelection.setVisible(true);
                    }
                    validate();
                    repaint();
                }
            });
		    
            imagePreview = new MyImage(item.getIcon());
            imageSelectionPanel.add(imagePreview, "1, 2, fill, fill");
            if (item.getImageSelection() != null) {
                imagePreview.setVisible(false);
            }
            
            imageSelection = new ImageSelectionPanel(item);
            imageSelectionPanel.add(imageSelection, "1, 3, fill, fill");
            if (item.getImageSelection() == null) {
                imageSelection.setVisible(false);
            }
		    
		}
        
        if (item instanceof Equipment) {
            EquipmentEditorPanel usableItemEditorPanel = new EquipmentEditorPanel((Equipment)item);
            add(usableItemEditorPanel, "1, 2, fill, fill");
        }
        else if (item instanceof UnlockItem) {
            UnlockItemEditorPanel usableItemEditorPanel = new UnlockItemEditorPanel((UnlockItem)item);
            add(usableItemEditorPanel, "1, 2, fill, fill");
        }
        else {
            UsableItemEditorPanel usableItemEditorPanel = new UsableItemEditorPanel((UsableItem)item);
            add(usableItemEditorPanel, "1, 2, fill, fill");
        }
	}

}
