package jasbro.gui.pages.subView;

import jasbro.Jasbro;
import jasbro.game.character.traits.Trait;
import jasbro.game.items.Inventory;
import jasbro.game.items.Inventory.ItemData;
import jasbro.game.items.Item;
import jasbro.game.items.ItemLocation;
import jasbro.gui.objects.div.InventoryPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultFormatter;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class ShopPanel extends JPanel {
	
	private InventoryPanel playerInventoryPanel;
	private InventoryPanel shopInventoryPanel;
	private JButton sellButton;
	private JButton buyButton;
	private JPanel sellPanel;
	private JPanel buyPanel;
	private JSpinner sellSpinner;
	private JSpinner buySpinner;
	private Inventory playerInventory;
	private Inventory shopInventory;
	private ItemLocation shop;
	
	public ShopPanel(ItemLocation shop) {
		this.shop = shop;
		setOpaque(false);
		playerInventory = Jasbro.getInstance().getData().getInventory();
		shopInventory = Jasbro.getInstance().getData().getShop().getInventory(shop);
		FormLayout layout = new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("150dlu"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("150dlu"),
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("50dlu"),
				RowSpec.decode("default:grow(3)"),
				RowSpec.decode("50dlu"),});
		setLayout(layout);
		
		playerInventoryPanel = new InventoryPanel(playerInventory);
		add(playerInventoryPanel, "2, 2, fill, fill");
		playerInventoryPanel.getItemList().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (playerInventoryPanel.getSelectedItem() != null) {
					int value = (Integer) sellSpinner.getValue();
					Integer itemAmount = new Integer(playerInventory.getAmount(playerInventoryPanel.getSelectedItem()));
					sellSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), 
							itemAmount, new Integer(1)));
					sellSpinner.setValue(value);
					sellSpinner.setValue(Math.min(value, itemAmount));
					JComponent comp = sellSpinner.getEditor();
					JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
					DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
					formatter.setCommitsOnValidEdit(true);
					updateSellButton();
				}
			}
		});
		
		shopInventoryPanel = new InventoryPanel(shopInventory);
		add(shopInventoryPanel, "4, 2, fill, fill");
		shopInventoryPanel.getItemList().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (shopInventoryPanel.getSelectedItem() != null) {
					int value = (Integer) buySpinner.getValue();
					Integer itemAmount = shopInventory.getAmount(shopInventoryPanel.getSelectedItem());
					buySpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), 
							itemAmount, new Integer(1)));
					buySpinner.setValue(Math.min(value, itemAmount));
					JComponent comp = buySpinner.getEditor();
					JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
					DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
					formatter.setCommitsOnValidEdit(true);
					updateBuyButton();
				}
			}
		});
		
		sellPanel = new JPanel();
		sellPanel.setOpaque(false);
		add(sellPanel, "2, 3, fill, fill");
		sellPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("right:default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:default:grow"),},
				new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("fill:default"),
				RowSpec.decode("default:grow"),}));
		
		sellSpinner = new JSpinner();
		sellSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		sellSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateSellButton();
			}
		});
		sellPanel.add(sellSpinner, "1, 2");
		
		sellButton = new JButton("Sell");
		sellPanel.add(sellButton, "3, 2, left, top");
		sellButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Item item = playerInventoryPanel.getSelectedItem();
				if (item != null) {
					int amount = (Integer)sellSpinner.getValue();
					if (playerInventory.getAmount(item) >= amount) {
						Jasbro.getInstance().getData().earnMoney(item.getValue() / 2 * amount, item.getName());
						playerInventory.removeItems(item, amount);
						shopInventory.addItems(item, amount);
						updateLists();
					}
				}
			}
		});
		sellButton.setEnabled(false);
		
		buyPanel = new JPanel();
		buyPanel.setOpaque(false);
		add(buyPanel, "4, 3, fill, fill");
		buyPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("right:default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:default:grow"),},
				new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("fill:default"),
				RowSpec.decode("default:grow"),}));
		
		buySpinner = new JSpinner();
		buySpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		buySpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateBuyButton();
			}
		});
		buyPanel.add(buySpinner, "1, 2");
		
		buyButton = new JButton("Buy");
		buyPanel.add(buyButton, "3, 2, left, top");
		buyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Item item = shopInventoryPanel.getSelectedItem();
				int amount = (Integer)buySpinner.getValue();
				int discount=100;
				if(Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.DISCOUNTSHOPS))
					discount=75;
				if (shopInventory.getAmount(item) >= amount && Jasbro.getInstance().getData().getMoney() >= amount * item.getValue()*discount/100) {
					Jasbro.getInstance().getData().spendMoney(item.getValue() * amount* discount / 100, item.getName());
					shopInventory.removeItems(item, amount);
					playerInventory.addItems(item, amount);
					updateLists();
				}
			}
		});
		buyButton.setEnabled(false);
		
		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (SwingUtilities.isRightMouseButton(e)) {
					JList<ItemData> itemList = playerInventoryPanel.getItemList();
					int index = itemList.locationToIndex(e.getPoint());
					itemList.setSelectedIndex(index);
					sellButton.doClick();
				}
			}
		};
		playerInventoryPanel.getItemList().addMouseListener(mouseAdapter);
		
		MouseAdapter mouseAdapter2 = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (SwingUtilities.isRightMouseButton(e)) {
					JList<ItemData> itemList = shopInventoryPanel.getItemList();
					int index = itemList.locationToIndex(e.getPoint());
					itemList.setSelectedIndex(index);
					buyButton.doClick();
				}
			}
		};
		shopInventoryPanel.getItemList().addMouseListener(mouseAdapter2);
	}
	
	public void updateLists() {
		playerInventoryPanel.updateView();
		shopInventoryPanel.updateView();
	}
	
	public void updateBuyButton() {
		Item item = shopInventoryPanel.getSelectedItem();
		int amount = (Integer)buySpinner.getValue();
		int discount=100;
		if(Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.DISCOUNTSHOPS))
			discount=75;
		if (item != null) {
			
			buyButton.setText("Buy (" + shopInventoryPanel.getSelectedItem().getValue() * amount * discount /100 +")");
			if (item.getValue() * amount <= Jasbro.getInstance().getData().getMoney()) {
				buyButton.setEnabled(true);
			}
			else {
				buyButton.setEnabled(false);
			}
		}
		else {
			buyButton.setText("Buy");
			buyButton.setEnabled(false);
		}
		repaint();
	}
	
	public void updateSellButton() {
		Item item = playerInventoryPanel.getSelectedItem();
		if (item != null) {
			if(item.getValue()!=0){
				int amount = (Integer)sellSpinner.getValue();
				sellButton.setText("Sell (" + item.getValue() / 2 * amount +")");
				sellButton.setEnabled(true);
			}
		}
		else {
			sellButton.setText("Sell");
			sellButton.setEnabled(false);
		}
		repaint();
	}
	
	public ItemLocation getShop() {
		return shop;
	}
	
	
}