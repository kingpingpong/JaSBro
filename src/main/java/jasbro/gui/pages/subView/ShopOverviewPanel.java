package jasbro.gui.pages.subView;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.traits.Trait;
import jasbro.game.items.ItemLocation;
import jasbro.gui.GuiUtil;
import jasbro.gui.pages.TownMenu;
import jasbro.gui.pictures.ImageData;
import jasbro.util.ConfigHandler;

import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class ShopOverviewPanel extends JPanel {
	private ItemLocation shownShops[] = {ItemLocation.SHOP, ItemLocation.CLOTHINGSTORE, ItemLocation.ADVENTURERSSHOP,
			ItemLocation.APOTHECARY, ItemLocation.ADULTSTORE};
	
	public ShopOverviewPanel(final TownMenu townMenu) {
		setOpaque(false);
		
		FormLayout formLayout = new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("10dlu"),
				ColumnSpec.decode("1dlu:grow"),
				ColumnSpec.decode("10dlu"),},
				new RowSpec[] {
				RowSpec.decode("10dlu"),
				RowSpec.decode("default:grow"),
				RowSpec.decode("10dlu"),});
		formLayout.setHonorsVisibility(false);
		setLayout(formLayout);
		
		UIManager.put("TabbedPane.contentOpaque", false);
		UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(tabbedPane, "2, 2, fill, fill");
		tabbedPane.setOpaque(false);
		tabbedPane.setFont(GuiUtil.DEFAULTLARGEBOLDFONT);
		tabbedPane.setBorder(null);
		
		for (ItemLocation shop : shownShops) {
			JPanel shopPanel = new ShopPanel(shop);
			tabbedPane.addTab(shop.getText(), null, shopPanel, null);
		}
		
		for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
			if (character.getTraits().contains(Trait.BUSINESSRELATIONS)) {
				JPanel shopPanel = new ShopPanel(ItemLocation.TRAVELLINGMERCHANT);
				tabbedPane.addTab(ItemLocation.TRAVELLINGMERCHANT.getText(), null, shopPanel, null);
				break;
			}
		}
		
		if (ConfigHandler.isCheat()) {
			JPanel shopPanel = new ShopPanel(ItemLocation.BLACKMARKET);
			tabbedPane.addTab(ItemLocation.BLACKMARKET.getText(), null, shopPanel, null);
		}
		
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				ItemLocation shop = ((ShopPanel)((JTabbedPane)e.getSource()).getSelectedComponent()).getShop();
				if (shop == ItemLocation.ADVENTURERSSHOP) {
					townMenu.setBackgroundImage(new ImageData("images/backgrounds/adventurersShop.png"));
				}
				else if (shop == ItemLocation.APOTHECARY) {
					townMenu.setBackgroundImage(new ImageData("images/backgrounds/apothecary.jpg"));
				}
				else if (shop == ItemLocation.CLOTHINGSTORE) {
					townMenu.setBackgroundImage(new ImageData("images/backgrounds/clothingStore.png"));
				}
				else if ((shop == ItemLocation.TRAVELLINGMERCHANT)) {
					townMenu.setBackgroundImage(new ImageData("images/backgrounds/travellingMerchant.png"));
				}
				else if ((shop == ItemLocation.ADULTSTORE)) {
					townMenu.setBackgroundImage(new ImageData("images/backgrounds/adultshop.jpg"));
				}
				else {
					townMenu.setBackgroundImage(new ImageData("images/backgrounds/shop.jpg"));
				}
				townMenu.repaint();
			}
			
		});
	}
	
}