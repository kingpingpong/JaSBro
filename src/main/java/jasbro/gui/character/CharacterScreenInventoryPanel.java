package jasbro.gui.character;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.items.CharacterInventory;
import jasbro.game.items.Equipment;
import jasbro.game.items.EquipmentSlot;
import jasbro.game.items.Inventory.ItemData;
import jasbro.game.items.Item;
import jasbro.game.items.UsableItem;
import jasbro.gui.dnd.MyEquipmentTransferHandler;
import jasbro.gui.objects.div.InventoryPanel;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.gui.pages.CharacterScreen;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class CharacterScreenInventoryPanel extends TranslucentPanel {
	private InventoryPanel inventoryPanel;
	private JButton useButton;
	private JButton equipButton;
	private JComboBox<EquipmentSlot> equipmentSlotComboBox;
	private CharacterInventory characterInventory;
	
	public CharacterScreenInventoryPanel(final Charakter character, final CharacterScreen characterScreen) {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("1dlu"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("1dlu"),},
			new RowSpec[] {
				RowSpec.decode("1dlu"),
				RowSpec.decode("default:grow"),
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("1dlu"),}));
		
		characterInventory = character.getCharacterInventory();
		inventoryPanel = new InventoryPanel(Jasbro.getInstance().getData().getInventory(), true);
		add(inventoryPanel, "2, 2, 2, 1, fill, fill");
		setMinimumSize(new Dimension(200, -1));
		setPreferredSize(new Dimension(-1, 1600));
		
		useButton = new JButton("Use");
		useButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (inventoryPanel.getSelectedItem() != null) {
					Jasbro.getThreadpool().execute(new Runnable() {
						@Override
						public void run() {
							Item item = inventoryPanel.getSelectedItem();
							if (item instanceof UsableItem) {
								UsableItem usableItem = (UsableItem) item;
								boolean used = usableItem.use(character);
								if (used) {
									Jasbro.getInstance().getData().getInventory().removeItem(usableItem);
									characterScreen.update();
								}
							}
						}
					});
				}
			}
		});
		add(useButton, "2, 3");
		
		equipButton = new JButton("Equip");
		equipButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Item item = inventoryPanel.getSelectedItem();
				if (item != null && item instanceof Equipment 
						&& equipmentSlotComboBox.getSelectedItem() != null) {
					Equipment equipment = (Equipment) item;
					Jasbro.getInstance().getData().getInventory().removeItem(item);
					List<? extends Equipment> oldEquipmentList = character.getCharacterInventory().equip(
							(EquipmentSlot)equipmentSlotComboBox.getSelectedItem(), equipment);
					if (oldEquipmentList != null) {
						for (Equipment oldEquipment : oldEquipmentList) {
							Jasbro.getInstance().getData().getInventory().addItem(oldEquipment);
						}
					}
					characterScreen.update();
				}
			}
		});
		add(equipButton, "2, 4");
		equipButton.setEnabled(false);
		
		equipmentSlotComboBox = new JComboBox<EquipmentSlot>();
		add(equipmentSlotComboBox, "3, 4, fill, default");
		equipmentSlotComboBox.setEnabled(false);
		
		inventoryPanel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				@SuppressWarnings("unchecked")
				ItemData item = ((JList<ItemData>)e.getSource()).getSelectedValue();
				if (item != null) {
					if (item.getItem() instanceof Equipment) {
						useButton.setEnabled(false);
						useButton.setVisible(false);
						
						if (!character.getType().isChildType()) {
							equipmentSlotComboBox.setVisible(true);
							equipmentSlotComboBox.setEnabled(true);
							equipButton.setEnabled(true);
							equipButton.setVisible(true);
						}
						initEquipmentComboBox();
					}
					else {
						useButton.setEnabled(true);
						equipButton.setEnabled(false);
						equipmentSlotComboBox.setEnabled(false);
						equipButton.setVisible(false);
						equipmentSlotComboBox.setVisible(false);
						useButton.setVisible(true);
					}
				}
				repaint();
			}
		});
		equipButton.setVisible(false);
		equipmentSlotComboBox.setVisible(false);
		
		
		
		//Enable drag and drop
		inventoryPanel.getItemList().setTransferHandler(new MyEquipmentTransferHandler());
		
		
		MouseAdapter tml = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (SwingUtilities.isRightMouseButton(e)) {
					JList<ItemData> itemList = inventoryPanel.getItemList();
					int index = itemList.locationToIndex(e.getPoint());
					itemList.setSelectedIndex(index);
					if (inventoryPanel.getSelectedItem() instanceof Equipment) {
						equipButton.doClick();
					}
					else {
						useButton.doClick();
					}
				}
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				super.mouseDragged(e);
				inventoryPanel.getItemList().setAutoscrolls(false);
				TransferHandler handle = inventoryPanel.getItemList().getTransferHandler();
				handle.exportAsDrag(inventoryPanel.getItemList(), e, TransferHandler.LINK);
				inventoryPanel.getItemList().setAutoscrolls(true);
			}
		};
		inventoryPanel.getItemList().addMouseListener(tml);
		inventoryPanel.getItemList().addMouseMotionListener(tml);
	}
	
	public void initEquipmentComboBox() {
		Item item = inventoryPanel.getSelectedItem();
		if (item != null && item instanceof Equipment) {
			Equipment equipment = (Equipment) item;
			equipmentSlotComboBox.removeAllItems();
			for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
				if (equipmentSlot.getEquipmentType() == equipment.getEquipmentType()) {
					equipmentSlotComboBox.addItem(equipmentSlot);
				}
			}
			equipmentSlotComboBox.setSelectedIndex(0);
			for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
				if (equipmentSlot.getEquipmentType() == equipment.getEquipmentType()) {
					if (characterInventory.getItem(equipmentSlot) == null) {
						equipmentSlotComboBox.setSelectedItem(equipmentSlot);
						break;
					}
				}
			}
		}
	}
	
	public void update() {
		inventoryPanel.updateView();
	}
}