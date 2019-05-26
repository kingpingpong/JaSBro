package jasbro.gui.pages.subView;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import jasbro.Jasbro;
import jasbro.game.housing.House;
import jasbro.game.housing.HouseType;
import jasbro.game.housing.HouseUtil;
import jasbro.gui.GuiUtil;
import jasbro.gui.pages.MessageScreen;
import jasbro.texts.TextUtil;

public class RealEstatePanel extends JPanel {
	private List<JButton> buyButtons = new ArrayList<JButton>();
	private List<House> houses;
	
	public RealEstatePanel() {
		init();
	}
	
	public void init() {
		removeAll();
		houses = Jasbro.getInstance().getData().getHouses();
		setOpaque(false);
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("pref:grow"),
				ColumnSpec.decode("pref:grow(10)"),
				ColumnSpec.decode("pref:grow"),
				ColumnSpec.decode("pref:grow(10)"),
				ColumnSpec.decode("pref:grow"),
				ColumnSpec.decode("pref:grow(8)"),
				ColumnSpec.decode("pref:grow"),},
				new RowSpec[] {
				RowSpec.decode("20dlu"),
				RowSpec.decode("1dlu:grow"),
				RowSpec.decode("20dlu"),}));
		
		addHierarchyBoundsListener(new HierarchyBoundsAdapter() {
			@Override
			public void ancestorResized(HierarchyEvent e) {
				revalidate();
			}
		});;
		
		//Panel for buying houses
		{
			final JPanel buyHousePanel = new JPanel();
			buyHousePanel.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
			buyHousePanel.setBorder(new LineBorder(new Color(139, 69, 19), 1, false));
			buyHousePanel.setOpaque(true);
			add(buyHousePanel, "2, 2, fill, fill");
			buyHousePanel.setLayout(new FormLayout(new ColumnSpec[] {
					ColumnSpec.decode("1dlu:grow"),},
					new RowSpec[] {
					RowSpec.decode("pref:none"),
					RowSpec.decode("fill:default:grow"),}));
			
			JLabel lblBuyHouse = new JLabel("Buy Houses");
			lblBuyHouse.setFont(new Font("Tahoma", Font.BOLD, 15));
			buyHousePanel.add(lblBuyHouse, "1, 1, center, default");
			
			ActionListener myBuyListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton button = (JButton) e.getSource();
					HouseType type = HouseType.valueOf(button.getActionCommand());
					if (type != null) {
						House house = HouseUtil.newHouse(type);
						if (!playerOwnsHouseType(type) && Jasbro.getInstance().getData().canAfford(house.getValue())) {
							houses.add(house);
							Jasbro.getInstance().getData().spendMoney(house.getValue(), house.getName());
							button.setEnabled(false);
							new MessageScreen(house.getName() + " bought!", house.getImage(), null);
							init();
						}
					}
				}
			};
			
			FormLayout formLayout = (FormLayout)buyHousePanel.getLayout();
			
			List<HouseType> houseTypes = Jasbro.getInstance().getData().getUnlocks().getAvailableHouseTypes();
			for (int i = 0; i < houseTypes.size(); i++) {
				HouseType type = houseTypes.get(i);
				JPanel curHousePanel = new JPanel();
				curHousePanel.setOpaque(false);
				curHousePanel.setLayout(new GridLayout(1, 1));
				curHousePanel.setBorder(new EmptyBorder(10,20,10,20));
				formLayout.insertRow(i+2, RowSpec.decode("pref:none"));
				buyHousePanel.add(curHousePanel, "1,"+(i+2)+", fill, top");
				
				House house = HouseUtil.newHouse(type);
				Object arguments[] = {type.getText(), house.getValue()};
				JButton buyButton = new JButton();
				buyButton.setText(TextUtil.t("ui.realestate.buyhouse", arguments));
				
				buyButton.addActionListener(myBuyListener);
				buyButton.setActionCommand(type.toString());
				buyButton.setEnabled(!playerOwnsHouseType(type));
				if (!Jasbro.getInstance().getData().canAfford(house.getValue())) {
					buyButton.setEnabled(false);
				}
				buyButtons.add(buyButton);
				curHousePanel.add(buyButton);
			}
		}
		
		
		//Panel for selling houses
		{
			final JPanel sellHousePanel = new JPanel();
			sellHousePanel.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
			sellHousePanel.setBorder(new LineBorder(new Color(139, 69, 19), 1, false));
			sellHousePanel.setOpaque(true);
			add(sellHousePanel, "4, 2, fill, fill");
			sellHousePanel.setLayout(new FormLayout(new ColumnSpec[] {
					ColumnSpec.decode("1dlu:grow"),},
					new RowSpec[] {
					RowSpec.decode("pref:none"),
					RowSpec.decode("fill:default:grow"),}));
			
			JLabel lblSellHouse = new JLabel("Sell Houses");
			lblSellHouse.setFont(new Font("Tahoma", Font.BOLD, 15));
			sellHousePanel.add(lblSellHouse, "1, 1, center, default");
			
			FormLayout formLayout = (FormLayout)sellHousePanel.getLayout();
			for (int i = 0; i < houses.size(); i++) {
				House house = houses.get(i);
				JPanel curHousePanel = new JPanel();
				curHousePanel.setOpaque(false);
				curHousePanel.setLayout(new GridLayout(1, 1));
				curHousePanel.setBorder(new EmptyBorder(10,20,10,20));
				formLayout.insertRow(i+2, RowSpec.decode("pref:none"));
				sellHousePanel.add(curHousePanel, "1,"+(i+2)+", fill, top");
				
				Object arguments[] = {house.getName(), house.getSellPrice()};
				JButton sellButton = new JButton(TextUtil.t("ui.realestate.sellhouse", arguments));
				sellButton.addActionListener(new MySellListener(house));
				sellButton.setEnabled(houses.size() > 1);
				curHousePanel.add(sellButton);
			}
		}
		
		
		//Panel for building Houses
		{
			final JPanel buildHousePanel = new JPanel();
			buildHousePanel.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
			buildHousePanel.setBorder(new LineBorder(new Color(139, 69, 19), 1, false));
			buildHousePanel.setOpaque(true);
			add(buildHousePanel, "6, 2, fill, fill");
			buildHousePanel.setLayout(new FormLayout(new ColumnSpec[] {
					ColumnSpec.decode("1dlu:grow"),},
					new RowSpec[] {
					RowSpec.decode("pref:none"),
					RowSpec.decode("fill:default:grow"),}));
			
			JLabel lblBuyHouse = new JLabel("Build Houses");
			lblBuyHouse.setFont(new Font("Tahoma", Font.BOLD, 15));
			buildHousePanel.add(lblBuyHouse, "1, 1, center, default");
			
			ActionListener myBuildListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton button = (JButton) e.getSource();
					HouseType type = HouseType.valueOf(button.getActionCommand());
					if (type != null) {
						House house = HouseUtil.newHouse(type);
						if (Jasbro.getInstance().getData().canAfford(house.getValue() * 2)) {
							houses.add(house);
							Jasbro.getInstance().getData().spendMoney(house.getValue() * 2, house.getName());
							new MessageScreen(house.getName() + " built!", house.getImage(), null);
							init();
						}
					}
				}
			};
			
			FormLayout formLayout = (FormLayout)buildHousePanel.getLayout();
			List<HouseType> houseTypes = Jasbro.getInstance().getData().getUnlocks().getAvailableHouseTypes();
			for (int i = 0; i < houseTypes.size(); i++) {
				HouseType type = houseTypes.get(i);
				JPanel curHousePanel = new JPanel();
				curHousePanel.setOpaque(false);
				curHousePanel.setLayout(new GridLayout(1, 1));
				curHousePanel.setBorder(new EmptyBorder(10,20,10,20));
				formLayout.insertRow(i+2, RowSpec.decode("pref:none"));
				buildHousePanel.add(curHousePanel, "1,"+(i+2)+", fill, top");
				
				House house = HouseUtil.newHouse(type);
				Object arguments[] = {house.getHouseType().getText(), house.getValue() * 2};
				JButton builtButton = new JButton(TextUtil.t("ui.realestate.buildhouse", arguments));
				builtButton.addActionListener(myBuildListener);
				builtButton.setActionCommand(type.toString());
				if (!Jasbro.getInstance().getData().canAfford(house.getValue() * 2)) {
					builtButton.setEnabled(false);
				}
				curHousePanel.add(builtButton);
			}
		}
		validate();
	}
	
	public boolean playerOwnsHouseType(HouseType houseType) {
		for (House house : Jasbro.getInstance().getData().getHouses()) {
			if (houseType == house.getHouseType()) {
				return true;
			}
		}
		return false;
	}
	
	public class MySellListener implements ActionListener {
		private House house;
		
		public MySellListener(House house) {
			this.house = house;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			synchronized (RealEstatePanel.this) {
				if (houses.size() > 0) {
					house.empty();
					houses.remove(house);
					Jasbro.getInstance().getData().earnMoney(house.getSellPrice(), house.getName());
					new MessageScreen(house.getName() + " sold!", house.getImage(), null);
					init();
				}
			}
		}
		
	}
}