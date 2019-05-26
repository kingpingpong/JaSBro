package jasbro.gui.town;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
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
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;

public class RealEstateMenu extends JPanel {
	private List<JButton> buyButtons = new ArrayList<JButton>();
	private List<House> houses;
	
	public RealEstateMenu() {
		removeAll();
		
		init();
	}
	
	public void init() {
		removeAll();
		houses = Jasbro.getInstance().getData().getHouses();
		setOpaque(false);		
		
		double width = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
		double height = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
		int widthRat = (int) (width/1280);
		int heightRat = (int) (height/720);
		int iconSize = (int) (65*width/1280);
		int backHomeBtnWidth = (int) (150*width/1280);
		int backHomeBtnHeight = (int) (50*width/1280);
		
		/*ImageIcon buyPlotIcon1 = new ImageIcon("images/buttons/buyplot.png");
		Image buyPlotImage1 = buyPlotIcon1.getImage().getScaledInstance( backHomeBtnWidth, backHomeBtnHeight,  java.awt.Image.SCALE_SMOOTH ) ;  
		buyPlotIcon1 = new ImageIcon(buyPlotImage1);
		
		ImageIcon buyPlotIcon2 = new ImageIcon("images/buttons/buyplot hover.png");
		Image buyPlotImage2 = buyPlotIcon2.getImage().getScaledInstance( backHomeBtnWidth, backHomeBtnHeight,  java.awt.Image.SCALE_SMOOTH ) ;  
		buyPlotIcon2 = new ImageIcon(buyPlotImage2);
		
		JButton buyPlotButton = new JButton(buyPlotIcon1);
		buyPlotButton.setRolloverIcon(buyPlotIcon2);
		buyPlotButton.setPressedIcon(buyPlotIcon1);
		buyPlotButton.setBounds((int) (15*width/1280),(int) (480*height/720), backHomeBtnWidth, backHomeBtnHeight);
		buyPlotButton.setBorderPainted(false); 
		buyPlotButton.setContentAreaFilled(false); 
		buyPlotButton.setFocusPainted(false); 
		buyPlotButton.setOpaque(false);
	    try {
			add(buyPlotButton);
	    } catch (java.lang.NullPointerException e) {
	    	// We need a way to remove this error
	    }
		buyPlotButton.addActionListener(new  ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Jasbro.getInstance().getGui().showBuyPlotMapScreen("map1");
			}
		});*/
		
		ImageIcon homeIcon1 = new ImageIcon("images/buttons/home.png");
		Image homeImage1 = homeIcon1.getImage().getScaledInstance( backHomeBtnWidth, backHomeBtnHeight,  java.awt.Image.SCALE_SMOOTH ) ;  
		homeIcon1 = new ImageIcon(homeImage1);
		
		ImageIcon homeIcon2 = new ImageIcon("images/buttons/home hover.png");
		Image homeImage2 = homeIcon2.getImage().getScaledInstance( backHomeBtnWidth, backHomeBtnHeight,  java.awt.Image.SCALE_SMOOTH ) ;  
		homeIcon2 = new ImageIcon(homeImage2);
		
		JButton homeButton = new JButton(homeIcon1);
		homeButton.setRolloverIcon(homeIcon2);
		homeButton.setPressedIcon(homeIcon1);
		homeButton.setBounds((int) (15*width/1280),(int) (550*height/720), backHomeBtnWidth, backHomeBtnHeight);
		homeButton.setBorderPainted(false); 
		homeButton.setContentAreaFilled(false); 
		homeButton.setFocusPainted(false); 
		homeButton.setOpaque(false);
	    try {
	    	add(homeButton);
	    } catch (java.lang.NullPointerException e) {
	    	// We need a way to remove this error
	    }
		homeButton.addActionListener(new  ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Jasbro.getInstance().getGui().showHouseManagementScreen();
			}
		});
		
		ImageIcon backIcon1 = new ImageIcon("images/buttons/back.png");
		Image backImage1 = backIcon1.getImage().getScaledInstance( backHomeBtnWidth, backHomeBtnHeight,  java.awt.Image.SCALE_SMOOTH ) ;  
		backIcon1 = new ImageIcon(backImage1);
		
		ImageIcon backIcon2 = new ImageIcon("images/buttons/back hover.png");
		Image backImage2 = backIcon2.getImage().getScaledInstance( backHomeBtnWidth, backHomeBtnHeight,  java.awt.Image.SCALE_SMOOTH ) ;  
		backIcon2 = new ImageIcon(backImage2);
		
		JButton backButton = new JButton(backIcon1);
		backButton.setRolloverIcon(backIcon2);
		backButton.setPressedIcon(backIcon1);
		backButton.setBounds((int) (15*width/1280),(int) (620*height/720), backHomeBtnWidth, backHomeBtnHeight);
		backButton.setBorderPainted(false); 
		backButton.setContentAreaFilled(false); 
		backButton.setFocusPainted(false); 
		backButton.setOpaque(false);
	    try {
			add(backButton);
	    } catch (java.lang.NullPointerException e) {
	    	// We need a way to remove this error
	    }
		backButton.addActionListener(new  ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Jasbro.getInstance().getGui().showBuildersGuildScreen();
			}
		});
		
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("135dlu"),
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
		
		//Panel for selling houses
		{
			final JPanel sellHousePanel = new JPanel();
			sellHousePanel.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
			sellHousePanel.setBorder(new LineBorder(new Color(139, 69, 19), 1, false));
			sellHousePanel.setOpaque(true);
			add(sellHousePanel, "5, 2, fill, fill");
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
			add(buildHousePanel, "3, 2, fill, fill");
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
		
		addMouseListener (new MouseAdapter(){
	        public void mouseClicked(MouseEvent e) {
	            if (SwingUtilities.isRightMouseButton(e)) {
	            	Jasbro.getInstance().getGui().showBuildersGuildScreen();
	            }
	        }		
		});
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
			synchronized (RealEstateMenu.this) {
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