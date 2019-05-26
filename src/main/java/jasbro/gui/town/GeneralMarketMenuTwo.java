package jasbro.gui.town;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import jasbro.Jasbro;
import jasbro.game.world.Time;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;

public class GeneralMarketMenuTwo extends MyImage {
	private JPanel menuPanel;
	private JPanel contenPanel;
	private JButton btnAuctionHouse;
	
	/**
	 * Create the panel.
	 */
	public GeneralMarketMenuTwo() {
	    setBackgroundImage(getMarket());
		setBackground(Color.WHITE);
		setLayout(null);
		setVisible(true);
		
		double width = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
		double height = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
		int widthRat = (int) (width/1280);
		int heightRat = (int) (height/720);
		int iconSize = (int) (65*width/1280);
		int backHomeBtnWidth = (int) (150*width/1280);
		int backHomeBtnHeight = (int) (50*width/1280);
		int downBtnWidth = (int) (50*width/1280);
		int downBtnHeight = (int) (150*width/1280);
	    
		if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
			
			ImageIcon adventurerStoreIcon1 = new ImageIcon("images/buttons/adventurerstore.png");
			Image adventurerStoreImage1 = adventurerStoreIcon1.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			adventurerStoreIcon1 = new ImageIcon(adventurerStoreImage1);
			
			ImageIcon adventurerStoreIcon2 = new ImageIcon("images/buttons/adventurerstore hover.png");
			Image adventurerStoreImage2 = adventurerStoreIcon2.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			adventurerStoreIcon2 = new ImageIcon(adventurerStoreImage2);
			
			JButton adventurerStore = new JButton(adventurerStoreIcon1);
			adventurerStore.setRolloverIcon(adventurerStoreIcon2);
			adventurerStore.setPressedIcon(adventurerStoreIcon1);
			adventurerStore.setBounds((int)(840*width/1280), (int)(500*height/720), iconSize, iconSize);
			adventurerStore.setBorderPainted(false); 
			adventurerStore.setContentAreaFilled(false); 
			adventurerStore.setFocusPainted(false); 
			adventurerStore.setOpaque(false);
			adventurerStore.setToolTipText("Adventurer Store");
			add(adventurerStore);
			adventurerStore.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showShopScreen("adventurer");
				}
			});
			
			ImageIcon tailorIcon1 = new ImageIcon("images/buttons/tailor.png");
			Image tailorImage1 = tailorIcon1.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			tailorIcon1 = new ImageIcon(tailorImage1);
			
			ImageIcon tailorIcon2 = new ImageIcon("images/buttons/tailor hover.png");
			Image tailorImage2 = tailorIcon2.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			tailorIcon2 = new ImageIcon(tailorImage2);
			
			JButton tailor = new JButton(tailorIcon1);
			tailor.setRolloverIcon(tailorIcon2);
			tailor.setPressedIcon(tailorIcon1);
			tailor.setBounds((int)(465*width/1280), (int)(490*height/720), iconSize, iconSize);
			tailor.setBorderPainted(false); 
			tailor.setContentAreaFilled(false); 
			tailor.setFocusPainted(false); 
			tailor.setOpaque(false);
			tailor.setToolTipText("Tailor");
			add(tailor);
			tailor.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showShopScreen("clothing");
				}
			});
			
			if(ConfigHandler.isCheat() == true){
				ImageIcon blackMarketIcon1 = new ImageIcon("images/buttons/blackmarket.png");
				Image blackMarketImage1 = blackMarketIcon1.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
				blackMarketIcon1 = new ImageIcon(blackMarketImage1);
				
				ImageIcon blackMarketIcon2 = new ImageIcon("images/buttons/blackmarket hover.png");
				Image blackMarketImage2 = blackMarketIcon2.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
				blackMarketIcon2 = new ImageIcon(blackMarketImage2);
				
				JButton blackMarket = new JButton(blackMarketIcon1);
				blackMarket.setRolloverIcon(blackMarketIcon2);
				blackMarket.setPressedIcon(blackMarketIcon1);
				blackMarket.setBounds((int)(650*width/1280), (int)(480*height/720), iconSize, iconSize);
				blackMarket.setBorderPainted(false); 
				blackMarket.setContentAreaFilled(false); 
				blackMarket.setFocusPainted(false); 
				blackMarket.setOpaque(false);
				blackMarket.setToolTipText("Black Market");
				add(blackMarket);
				blackMarket.addActionListener(new  ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						Jasbro.getInstance().getGui().showShopScreen("blackmarket");
					}
				});	
			}
			
			ImageIcon turnIcon1 = new ImageIcon("images/buttons/arrowdown.png");
			Image turnImage1 = turnIcon1.getImage().getScaledInstance( downBtnHeight, downBtnWidth,  java.awt.Image.SCALE_SMOOTH ) ;  
			turnIcon1 = new ImageIcon(turnImage1);
			
			ImageIcon turnIcon2 = new ImageIcon("images/buttons/arrowdown hover.png");
			Image turnImage2 = turnIcon2.getImage().getScaledInstance( downBtnHeight, downBtnWidth,  java.awt.Image.SCALE_SMOOTH ) ;  
			turnIcon2 = new ImageIcon(turnImage2);
			
			JButton turnButton = new JButton(turnIcon1);
			turnButton.setRolloverIcon(turnIcon2);
			turnButton.setPressedIcon(turnIcon1);
			turnButton.setBounds((int) (550*width/1280),(int) (620*height/720), downBtnHeight, downBtnWidth);
			turnButton.setBorderPainted(false); 
			turnButton.setContentAreaFilled(false); 
			turnButton.setFocusPainted(false); 
			turnButton.setOpaque(false);
			add(turnButton);
			turnButton.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showGeneralMarketScreen(1);
				}
			});
			
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
			add(homeButton);
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
			add(backButton);
			backButton.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showTownScreen();
				}
			});
			
			addMouseListener (new MouseAdapter(){
		        public void mouseClicked(MouseEvent e) {
		            if (SwingUtilities.isRightMouseButton(e)) {
		            	Jasbro.getInstance().getGui().showTownScreen();
		            }
		        }		
			});
		}
		
		validate();
		repaint();
	}
	
	public ImageData getMarket() {
		switch(Jasbro.getInstance().getData().getTime()) {
		case AFTERNOON:
			return new ImageData("images/backgrounds/marketdistrict2 afternoon.jpg");
		case NIGHT:
			return new ImageData("images/backgrounds/marketdistrict2 night.jpg");
		default:
			return new ImageData("images/backgrounds/marketdistrict2 morning.jpg");
		}
	}
}