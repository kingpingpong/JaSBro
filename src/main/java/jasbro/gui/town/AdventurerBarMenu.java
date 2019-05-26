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

public class AdventurerBarMenu extends MyImage {
	private JPanel menuPanel;
	private JPanel contenPanel;
	private JButton btnAuctionHouse;
	
	/**
	 * Create the panel.
	 */
	public AdventurerBarMenu() {
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
	    
		if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
			
			ImageIcon generalStoreIcon1 = new ImageIcon("images/buttons/generalstore.png");
			Image generalStoreImage1 = generalStoreIcon1.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			generalStoreIcon1 = new ImageIcon(generalStoreImage1);
			
			ImageIcon generalStoreIcon2 = new ImageIcon("images/buttons/generalstore hover.png");
			Image generalStoreImage2 = generalStoreIcon2.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			generalStoreIcon2 = new ImageIcon(generalStoreImage2);
			
			JButton generalStore = new JButton(generalStoreIcon1);
			generalStore.setRolloverIcon(generalStoreIcon2);
			generalStore.setPressedIcon(generalStoreIcon1);
			generalStore.setBounds((int)(1025*width/1280), (int)(463*height/720), iconSize, iconSize);
			generalStore.setBorderPainted(false); 
			generalStore.setContentAreaFilled(false); 
			generalStore.setFocusPainted(false); 
			generalStore.setOpaque(false);
			generalStore.setToolTipText("General Store");
			add(generalStore);
			generalStore.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showShopScreen("general");
				}
			});
			
			ImageIcon adultStoreIcon1 = new ImageIcon("images/buttons/adultstore.png");
			Image adultStoreImage1 = adultStoreIcon1.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			adultStoreIcon1 = new ImageIcon(adultStoreImage1);
			
			ImageIcon adultStoreIcon2 = new ImageIcon("images/buttons/adultstore hover.png");
			Image adultStoreImage2 = adultStoreIcon2.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			adultStoreIcon2 = new ImageIcon(adultStoreImage2);
			
			JButton adultStore = new JButton(adultStoreIcon1);
			adultStore.setRolloverIcon(adultStoreIcon2);
			adultStore.setPressedIcon(adultStoreIcon1);
			adultStore.setBounds((int)(810*width/1280), (int)(440*height/720), iconSize, iconSize);
			adultStore.setBorderPainted(false); 
			adultStore.setContentAreaFilled(false); 
			adultStore.setFocusPainted(false); 
			adultStore.setOpaque(false);
			adultStore.setToolTipText("Adult Store");
			add(adultStore);
			adultStore.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showShopScreen("adult");
				}
			});
			
			ImageIcon magicStoreIcon1 = new ImageIcon("images/buttons/magicstore.png");
			Image magicStoreImage1 = magicStoreIcon1.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			magicStoreIcon1 = new ImageIcon(magicStoreImage1);
			
			ImageIcon magicStoreIcon2 = new ImageIcon("images/buttons/magicstore hover.png");
			Image magicStoreImage2 = magicStoreIcon2.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			magicStoreIcon2 = new ImageIcon(magicStoreImage2);
			
			JButton magicStore = new JButton(magicStoreIcon1);
			magicStore.setRolloverIcon(magicStoreIcon2);
			magicStore.setPressedIcon(magicStoreIcon1);
			magicStore.setBounds((int)(510*width/1280), (int)(450*height/720), iconSize, iconSize);
			magicStore.setBorderPainted(false); 
			magicStore.setContentAreaFilled(false); 
			magicStore.setFocusPainted(false); 
			magicStore.setOpaque(false);
			magicStore.setToolTipText("Magic Shop");
			add(magicStore);
			magicStore.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showAlchemist();
				}
			});
			
			ImageIcon bookStoreIcon1 = new ImageIcon("images/buttons/bookstore.png");
			Image bookStoreImage1 = bookStoreIcon1.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			bookStoreIcon1 = new ImageIcon(bookStoreImage1);
			
			ImageIcon bookStoreIcon2 = new ImageIcon("images/buttons/bookstore hover.png");
			Image bookStoreImage2 = bookStoreIcon2.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			bookStoreIcon2 = new ImageIcon(bookStoreImage2);
			
			JButton bookStore = new JButton(bookStoreIcon1);
			bookStore.setRolloverIcon(bookStoreIcon2);
			bookStore.setPressedIcon(bookStoreIcon1);
			bookStore.setBounds((int)(206*width/1280), (int)(470*height/720), iconSize, iconSize);
			bookStore.setBorderPainted(false); 
			bookStore.setContentAreaFilled(false); 
			bookStore.setFocusPainted(false); 
			bookStore.setOpaque(false);
			bookStore.setToolTipText("Book Store");
			add(bookStore);
			bookStore.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showShopScreen("book");
				}
			});
			
			ImageIcon turnIcon1 = new ImageIcon("images/buttons/turn.png");
			Image turnImage1 = turnIcon1.getImage().getScaledInstance( backHomeBtnWidth, backHomeBtnHeight,  java.awt.Image.SCALE_SMOOTH ) ;  
			turnIcon1 = new ImageIcon(turnImage1);
			
			ImageIcon turnIcon2 = new ImageIcon("images/buttons/turn hover.png");
			Image turnImage2 = turnIcon2.getImage().getScaledInstance( backHomeBtnWidth, backHomeBtnHeight,  java.awt.Image.SCALE_SMOOTH ) ;  
			turnIcon2 = new ImageIcon(turnImage2);
			
			JButton turnButton = new JButton(turnIcon1);
			turnButton.setRolloverIcon(turnIcon2);
			turnButton.setPressedIcon(turnIcon1);
			turnButton.setBounds((int) (500*width/1280),(int) (620*height/720), backHomeBtnWidth, backHomeBtnHeight);
			turnButton.setBorderPainted(false); 
			turnButton.setContentAreaFilled(false); 
			turnButton.setFocusPainted(false); 
			turnButton.setOpaque(false);
			add(turnButton);
			turnButton.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showGeneralMarketScreen(2);
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
					Jasbro.getInstance().getGui().showAdventurersGuildScreen();
				}
			});
			
			addMouseListener (new MouseAdapter(){
		        public void mouseClicked(MouseEvent e) {
		            if (SwingUtilities.isRightMouseButton(e)) {
		            	Jasbro.getInstance().getGui().showAdventurersGuildScreen();
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
			return new ImageData("images/backgrounds/marketdistrict afternoon.jpg");
		case NIGHT:
			return new ImageData("images/backgrounds/marketdistrict night.jpg");
		default:
			return new ImageData("images/backgrounds/marketdistrict morning.jpg");
		}
	}
}