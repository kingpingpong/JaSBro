package jasbro.gui.town;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import jasbro.Jasbro;
import jasbro.game.world.Time;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;

public class SlaveMarketMenu extends MyImage {
	private JButton btnAuctionHouse;
	
	/**
	 * Create the panel.
	 */
	public SlaveMarketMenu() {
	    setBackgroundImage(getTownImage());
		setBackground(Color.WHITE);
		setLayout(null);
		setVisible(true);
		
		int width = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
		int height = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
		int widthRat = (int) (width/1280);
		int heightRat = (int) (height/720);
		int iconSize = (int) (65*width/1280);
		int backHomeBtnWidth = (int) (150*width/1280);
		int backHomeBtnHeight = (int) (50*width/1280);
	    
		if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
			
			ImageIcon auctionHouseIcon1 = new ImageIcon("images/buttons/auctionhouse.png");
			Image auctionHouseImage1 = auctionHouseIcon1.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			auctionHouseIcon1 = new ImageIcon(auctionHouseImage1);
			
			ImageIcon auctionHouseIcon2 = new ImageIcon("images/buttons/auctionhouse hover.png");
			Image auctionHouseImage2 = auctionHouseIcon2.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;    
			auctionHouseIcon2 = new ImageIcon(auctionHouseImage2);
			
			JButton auctionHouseButton = new JButton(auctionHouseIcon1);
			auctionHouseButton.setRolloverIcon(auctionHouseIcon2);
			auctionHouseButton.setPressedIcon(auctionHouseIcon1);
			auctionHouseButton.setBounds((int)(987*width/1280), (int)(315*height/720), iconSize, iconSize);
			auctionHouseButton.setBorderPainted(false); 
			auctionHouseButton.setContentAreaFilled(false); 
			auctionHouseButton.setFocusPainted(false); 
			auctionHouseButton.setOpaque(false);
			auctionHouseButton.setToolTipText("Auction House");
			add(auctionHouseButton);
			auctionHouseButton.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showAuctionHouse();
				}
			});
			
			ImageIcon slavePensIcon1 = new ImageIcon("images/buttons/slavepens.png");
			Image slavePensImage1 = slavePensIcon1.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			slavePensIcon1 = new ImageIcon(slavePensImage1);
			
			ImageIcon slavePensIcon2 = new ImageIcon("images/buttons/slavepens hover.png");
			Image slavePensImage2 = slavePensIcon2.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;    
			slavePensIcon2 = new ImageIcon(slavePensImage2);
			
			JButton slavePensButton = new JButton(slavePensIcon1);
			slavePensButton.setRolloverIcon(slavePensIcon2);
			slavePensButton.setPressedIcon(slavePensIcon1);
			slavePensButton.setBounds((int)(107*width/1280), (int)(390*height/720), iconSize, iconSize);
			slavePensButton.setBorderPainted(false); 
			slavePensButton.setContentAreaFilled(false); 
			slavePensButton.setFocusPainted(false); 
			slavePensButton.setOpaque(false);
			slavePensButton.setToolTipText("Slave Pens");
			add(slavePensButton);
			slavePensButton.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showSlavePens();
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
	
	public ImageData getTownImage() {
		switch(Jasbro.getInstance().getData().getTime()) {
		case AFTERNOON:
			return new ImageData("images/backgrounds/slavemarket afternoon.jpg");
		case NIGHT:
			return new ImageData("images/backgrounds/slavemarket night.jpg");
		default:
			return new ImageData("images/backgrounds/slavemarket morning.jpg");
		}
	}
	
}