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

public class TownMenuNew extends MyImage {
	private JButton btnAuctionHouse;

	
	/**
	 * Create the panel.
	 */
	public TownMenuNew() {
	    setBackgroundImage(getTownImage());
		setBackground(Color.WHITE);
		setLayout(null);
		setVisible(true);
		
		int width = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
		int height = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
		int widthRat = (int) (width/1280);
		int heightRat = (int) (height/720);
		int iconSize = 65*width/1280;
		int backHomeBtnWidth = (int) (150*width/1280);
		int backHomeBtnHeight = (int) (50*width/1280);
	    
		if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {

			ImageIcon slaveMarketIcon1 = new ImageIcon("images/buttons/slavemarket.png");
			Image slaveMarketImage1 = slaveMarketIcon1.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH );
			slaveMarketIcon1 = new ImageIcon(slaveMarketImage1);
			
			ImageIcon slaveMarketIcon2 = new ImageIcon("images/buttons/slavemarket hover.png");
			Image slaveMarketImage2 = slaveMarketIcon2.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH );
			slaveMarketIcon2 = new ImageIcon(slaveMarketImage2);
			
			JButton slaveMarketButton = new JButton(slaveMarketIcon1);
			slaveMarketButton.setRolloverIcon(slaveMarketIcon2);
			slaveMarketButton.setPressedIcon(slaveMarketIcon1);
			slaveMarketButton.setBounds(790*width/1280, 115*height/720, iconSize, iconSize);
			slaveMarketButton.setBorderPainted(false); 
			slaveMarketButton.setContentAreaFilled(false); 
			slaveMarketButton.setFocusPainted(false); 
			slaveMarketButton.setOpaque(false);
			slaveMarketButton.setToolTipText("Slave Market");

			add(slaveMarketButton);
			slaveMarketButton.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showSlaveMarketScreen();
				}
			});
			
			ImageIcon marketDistrictIcon1 = new ImageIcon("images/buttons/marketdistrict.png");
			Image marketDistrictImage1 = marketDistrictIcon1.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			marketDistrictIcon1 = new ImageIcon(marketDistrictImage1);
			
			ImageIcon marketDistrictIcon2 = new ImageIcon("images/buttons/marketdistrict hover.png");
			Image marketDistrictImage2 = marketDistrictIcon2.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			marketDistrictIcon2 = new ImageIcon(marketDistrictImage2);
			
			JButton generalMarketButton = new JButton(marketDistrictIcon1);
			generalMarketButton.setRolloverIcon(marketDistrictIcon2);
			generalMarketButton.setPressedIcon(marketDistrictIcon1);
			generalMarketButton.setBounds(610*width/1280, 430*height/720, iconSize, iconSize);
			generalMarketButton.setBorderPainted(false); 
			generalMarketButton.setContentAreaFilled(false); 
			generalMarketButton.setFocusPainted(false); 
			generalMarketButton.setOpaque(false);
			generalMarketButton.setToolTipText("Market District");
			add(generalMarketButton);
			generalMarketButton.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showGeneralMarketScreen(1);
				}
			});
			
			ImageIcon schoolIcon1 = new ImageIcon("images/buttons/school.png");
			Image schoolImage1 = schoolIcon1.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			schoolIcon1 = new ImageIcon(schoolImage1);
			
			ImageIcon schoolIcon2 = new ImageIcon("images/buttons/school hover.png");
			Image schoolImage2 = schoolIcon2.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			schoolIcon2 = new ImageIcon(schoolImage2);
			
			JButton schoolButton = new JButton(schoolIcon1);
			schoolButton.setRolloverIcon(schoolIcon2);
			schoolButton.setPressedIcon(schoolIcon1);
			schoolButton.setBounds(245*width/1280, 330*height/720, iconSize, iconSize);
			schoolButton.setBorderPainted(false); 
			schoolButton.setContentAreaFilled(false); 
			schoolButton.setFocusPainted(false); 
			schoolButton.setOpaque(false);
			schoolButton.setToolTipText("School");
			add(schoolButton);
			schoolButton.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showSchoolScreen();
				}
			});
			
			ImageIcon buildersGuildIcon1 = new ImageIcon("images/buttons/buildersguild.png");
			Image buildersGuildImage1 = buildersGuildIcon1.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			buildersGuildIcon1 = new ImageIcon(buildersGuildImage1);
			
			ImageIcon buildersGuildIcon2 = new ImageIcon("images/buttons/buildersguild hover.png");
			Image buildersGuildImage2 = buildersGuildIcon2.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			buildersGuildIcon2 = new ImageIcon(buildersGuildImage2);
			
			JButton buildersGuildButton = new JButton(buildersGuildIcon1);
			buildersGuildButton.setRolloverIcon(buildersGuildIcon2);
			buildersGuildButton.setPressedIcon(buildersGuildIcon1);
			buildersGuildButton.setBounds(240*width/1280, 180*height/720, iconSize, iconSize);
			buildersGuildButton.setBorderPainted(false); 
			buildersGuildButton.setContentAreaFilled(false); 
			buildersGuildButton.setFocusPainted(false); 
			buildersGuildButton.setOpaque(false);
			buildersGuildButton.setToolTipText("Builder's District");
			add(buildersGuildButton);
			buildersGuildButton.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showBuildersGuildScreen();
				}
			});
			
			ImageIcon slaverGuildIcon1 = new ImageIcon("images/buttons/slaverguild.png");
			Image slaverGuildImage1 = slaverGuildIcon1.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			slaverGuildIcon1 = new ImageIcon(slaverGuildImage1);
			
			ImageIcon slaverGuildIcon2 = new ImageIcon("images/buttons/slaverguild hover.png");
			Image slaverGuildImage2 = slaverGuildIcon2.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			slaverGuildIcon2 = new ImageIcon(slaverGuildImage2);
			
			JButton slaverGuildButton = new JButton(slaverGuildIcon1);
			slaverGuildButton.setRolloverIcon(slaverGuildIcon2);
			slaverGuildButton.setPressedIcon(slaverGuildIcon1);
			slaverGuildButton.setBounds(940*width/1280, 370*height/720, iconSize, iconSize);
			slaverGuildButton.setBorderPainted(false); 
			slaverGuildButton.setContentAreaFilled(false); 
			slaverGuildButton.setFocusPainted(false); 
			slaverGuildButton.setOpaque(false);
			slaverGuildButton.setToolTipText("Slaver's Guild");
			add(slaverGuildButton);
			slaverGuildButton.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showSlaverGuildScreen();;
				}
			});
			
			ImageIcon adventurerGuildIcon1 = new ImageIcon("images/buttons/adventurersguild.png");
			Image adventurerGuildImage1 = adventurerGuildIcon1.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			adventurerGuildIcon1 = new ImageIcon(adventurerGuildImage1);
			
			ImageIcon adventurerGuildIcon2 = new ImageIcon("images/buttons/adventurersguild hover.png");
			Image adventurerGuildImage2 = adventurerGuildIcon2.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH ) ;  
			adventurerGuildIcon2 = new ImageIcon(adventurerGuildImage2);
			
			JButton adventurerGuildButton = new JButton(adventurerGuildIcon1);
			adventurerGuildButton.setRolloverIcon(adventurerGuildIcon2);
			adventurerGuildButton.setPressedIcon(adventurerGuildIcon1);
			adventurerGuildButton.setBounds(860*width/1280, 270*height/720, iconSize, iconSize);
			adventurerGuildButton.setBorderPainted(false); 
			adventurerGuildButton.setContentAreaFilled(false); 
			adventurerGuildButton.setFocusPainted(false); 
			adventurerGuildButton.setOpaque(false);
			adventurerGuildButton.setToolTipText("Adventurer's Guild");
			add(adventurerGuildButton);
			adventurerGuildButton.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showAdventurersGuildScreen();
				}
			});
			
			if (ConfigHandler.isCheat()) {
				ImageIcon cheatIcon1 = new ImageIcon("images/buttons/cheat.png");
				Image cheatImage1 = cheatIcon1.getImage().getScaledInstance( backHomeBtnWidth, backHomeBtnHeight,  java.awt.Image.SCALE_SMOOTH ) ;  
				cheatIcon1 = new ImageIcon(cheatImage1);
				
				ImageIcon cheatIcon2 = new ImageIcon("images/buttons/cheat hover.png");
				Image cheatImage2 = cheatIcon2.getImage().getScaledInstance( backHomeBtnWidth, backHomeBtnHeight,  java.awt.Image.SCALE_SMOOTH ) ;  
				cheatIcon2 = new ImageIcon(cheatImage2);
				
				JButton cheatButton = new JButton(cheatIcon1);
				cheatButton.setRolloverIcon(cheatIcon2);
				cheatButton.setPressedIcon(cheatIcon1);
				cheatButton.setBounds(15*width/1280, 550*height/720, backHomeBtnWidth, backHomeBtnHeight);
				cheatButton.setBorderPainted(false); 
				cheatButton.setContentAreaFilled(false); 
				cheatButton.setFocusPainted(false); 
				cheatButton.setOpaque(false);
				add(cheatButton);
				cheatButton.addActionListener(new  ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						Jasbro.getInstance().getGui().showCheatScreen();
					}
				});
			}
	
			
			ImageIcon backIcon1 = new ImageIcon("images/buttons/home.png");
			Image backImage1 = backIcon1.getImage().getScaledInstance( backHomeBtnWidth, backHomeBtnHeight,  java.awt.Image.SCALE_SMOOTH ) ;  
			backIcon1 = new ImageIcon(backImage1);
			
			ImageIcon backIcon2 = new ImageIcon("images/buttons/home hover.png");
			Image backImage2 = backIcon2.getImage().getScaledInstance( backHomeBtnWidth, backHomeBtnHeight,  java.awt.Image.SCALE_SMOOTH ) ;  
			backIcon2 = new ImageIcon(backImage2);
			
			JButton backButton = new JButton(backIcon1);
			backButton.setRolloverIcon(backIcon2);
			backButton.setPressedIcon(backIcon1);
			backButton.setBounds(15*width/1280, 620*height/720, backHomeBtnWidth, backHomeBtnHeight);
			backButton.setBorderPainted(false); 
			backButton.setContentAreaFilled(false); 
			backButton.setFocusPainted(false); 
			backButton.setOpaque(false);
			add(backButton);
			backButton.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showHouseManagementScreen();
				}
			});
	
		}
		
		addMouseListener (new MouseAdapter(){
	        public void mouseClicked(MouseEvent e) {
	            if (SwingUtilities.isRightMouseButton(e)) {
	            	Jasbro.getInstance().getGui().showHouseManagementScreen();
	            }
	        }		
		});
		
		validate();
		repaint();
	}
	
	public ImageData getTownImage() {
		switch(Jasbro.getInstance().getData().getTime()) {
		case AFTERNOON:
			return new ImageData("images/backgrounds/town_afternoon.png");
		case NIGHT:
			return new ImageData("images/backgrounds/town_night.png");
		default:
			return new ImageData("images/backgrounds/town_morning.png");
		}
	}
	
}