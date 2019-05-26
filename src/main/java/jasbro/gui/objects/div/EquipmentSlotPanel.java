package jasbro.gui.objects.div;

import jasbro.game.character.Charakter;
import jasbro.game.items.EquipmentSlot;
import jasbro.gui.pages.CharacterScreen;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;

import java.awt.Color;

import javax.swing.border.LineBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EquipmentSlotPanel extends MyImage {
	private EquippedItemPanel headSlotPanel;
	private EquippedItemPanel outfitSlotPanel;
	private EquippedItemPanel underwearSlotPanel;
	private EquippedItemPanel shoesSlotPanel;
	private EquippedItemPanel accessory1SlotPanel;
	private EquippedItemPanel accessory2SlotPanel;
	private EquippedItemPanel accessory3SlotPanel;
	private EquippedItemPanel accessory4SlotPanel;
	
	public EquipmentSlotPanel(Charakter character, CharacterScreen characterScreen) {
		setBackground(Color.WHITE);
		setBorder(new LineBorder(Color.BLACK));
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("23dlu"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("23dlu"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("23dlu"),
				FormFactory.UNRELATED_GAP_COLSPEC,},
				new RowSpec[] {
				RowSpec.decode("4dlu:grow"),
				RowSpec.decode("23dlu"),
				FormFactory.UNRELATED_GAP_ROWSPEC,
				RowSpec.decode("23dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("23dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("23dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLEANED, character));
		
		headSlotPanel = new EquippedItemPanel(character, EquipmentSlot.HEAD, characterScreen);
		add(headSlotPanel, "4, 2, fill, fill");
		
		outfitSlotPanel = new EquippedItemPanel(character, EquipmentSlot.DRESS, characterScreen);
		add(outfitSlotPanel, "4, 4, fill, fill");
		
		underwearSlotPanel = new EquippedItemPanel(character, EquipmentSlot.UNDERWEAR, characterScreen);
		add(underwearSlotPanel, "4, 6, fill, fill");
		
		shoesSlotPanel = new EquippedItemPanel(character, EquipmentSlot.SHOES, characterScreen);
		add(shoesSlotPanel, "4, 8, fill, fill");
		
		accessory1SlotPanel = new EquippedItemPanel(character, EquipmentSlot.ACCESSORY1, characterScreen);
		add(accessory1SlotPanel, "2, 6, fill, fill");
		
		accessory2SlotPanel = new EquippedItemPanel(character, EquipmentSlot.ACCESSORY2, characterScreen);
		add(accessory2SlotPanel, "2, 8, fill, fill");
		
		accessory3SlotPanel = new EquippedItemPanel(character, EquipmentSlot.ACCESSORY3, characterScreen);
		add(accessory3SlotPanel, "6, 6, fill, fill");
		
		accessory4SlotPanel = new EquippedItemPanel(character, EquipmentSlot.ACCESSORY4, characterScreen);
		add(accessory4SlotPanel, "6, 8, fill, fill");
	}
	
	@Override
	public void update() {
		headSlotPanel.updateEquipmentIcon();
		outfitSlotPanel.updateEquipmentIcon();
		underwearSlotPanel.updateEquipmentIcon();
		shoesSlotPanel.updateEquipmentIcon();
		accessory1SlotPanel.updateEquipmentIcon();
		accessory2SlotPanel.updateEquipmentIcon();
		accessory3SlotPanel.updateEquipmentIcon();
		accessory4SlotPanel.updateEquipmentIcon();
	}
}