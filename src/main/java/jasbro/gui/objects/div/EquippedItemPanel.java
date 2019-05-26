package jasbro.gui.objects.div;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.TransferHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.items.Equipment;
import jasbro.game.items.EquipmentSlot;
import jasbro.gui.GuiUtil;
import jasbro.gui.dnd.CanReceiveEquipmentDrop;
import jasbro.gui.dnd.MyEquipmentTransferHandler;
import jasbro.gui.pages.CharacterScreen;
import jasbro.texts.TextUtil;

public class EquippedItemPanel extends TranslucentPanel implements CanReceiveEquipmentDrop {
	@SuppressWarnings("unused")
	private final static Logger log = LogManager.getLogger(EquippedItemPanel.class);
	
	private Charakter character;
	private EquipmentSlot equipmentSlot;
	private MyImage equipmentIcon;
	private CharacterScreen characterScreen;
	
	public EquippedItemPanel(Charakter characterTmp, EquipmentSlot equipmentSlotTmp, CharacterScreen characterScreenTmp) {
		this.character = characterTmp;
		this.equipmentSlot = equipmentSlotTmp;
		this.characterScreen = characterScreenTmp;
		setLayout(new GridLayout(1, 1));
		
		equipmentIcon = new MyImage();
		add(equipmentIcon);
		
		JLabel label = new JLabel(equipmentSlot.getText());
		label.setFont(GuiUtil.DEFAULTTINYFONT);
		equipmentIcon.add(label);
		equipmentIcon.addMouseMotionListener(GuiUtil.DELEGATEMOUSELISTENER);
		
		//Enable drag and drop
		setTransferHandler(new MyEquipmentTransferHandler());
		
		MouseAdapter tml = new MouseAdapter() {
			
			@Override
			public void mouseDragged(MouseEvent e) {
				Equipment item = character.getCharacterInventory().getItem(equipmentSlot);
				if (item != null) {
					TransferHandler handle = EquippedItemPanel.this.getTransferHandler();
					handle.exportAsDrag(EquippedItemPanel.this, e, TransferHandler.LINK);
					character.getCharacterInventory().unequip(equipmentSlot);
					Jasbro.getInstance().getData().getInventory().addItem(item);
				}
				super.mouseDragged(e);
			}
		};
		addMouseListener(tml);
		addMouseMotionListener(tml);
		
		updateEquipmentIcon();
	}
	
	public void updateEquipmentIcon() {
		Equipment equipment = character.getCharacterInventory().getItem(equipmentSlot);
		if (equipment != null) {
			equipmentIcon.setImage(equipment.getIcon());
			setToolTipText(TextUtil.htmlItem(equipment));
		}
		else {
			equipmentIcon.setImage(null);
			setToolTipText("");
		}
		repaint();
	}
	
	@Override
	public void receiveEquipmentDrop(Equipment equipment) {
		Jasbro.getInstance().getData().getInventory().removeItem(equipment);
		List<Equipment> returnItems = character.getCharacterInventory().equip(equipmentSlot, equipment);
		for (Equipment curEquipment : returnItems) {
			Jasbro.getInstance().getData().getInventory().addItem(curEquipment);
		}
		characterScreen.update();
	}
	
	public Equipment getItem() {
		return character.getCharacterInventory().getItem(equipmentSlot);
	}
	
	@Override
	public Point getToolTipLocation(MouseEvent event) {
		Container parent = EquippedItemPanel.this.getParent();
		return new Point(parent.getX() - 600, 
				0);
	}
	
	@Override
	public EquipmentSlot getEquipmentSlot() {
		return equipmentSlot;
	}
	
	
}