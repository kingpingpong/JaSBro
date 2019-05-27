package jasbro.game.realestate;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import jasbro.Jasbro;
import jasbro.game.world.Time;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;

public class BuyPlotMapMenu extends MyImage {
	int width = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
	int height = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
	int widthRat = (int) (width/1280);
	int heightRat = (int) (height/720);
	int iconSize = 65*width/1280;
	int backHomeBtnWidth = (int) (150*width/1280);
	int backHomeBtnHeight = (int) (50*width/1280);
	int arrowBtnWidth = (int) (150*width/1280);
	int arrowBtnHeight = (int) (50*width/1280);
	private ImageIcon plotIcon1 = new ImageIcon("images/buttons/landmarker.png");
	private ImageIcon plotIcon2 = new ImageIcon("images/buttons/landmarker hover.png");
	private ImageIcon leftIcon1 = new ImageIcon("images/buttons/arrowleft.png");
	private ImageIcon leftIcon2 = new ImageIcon("images/buttons/arrowleft hover.png");
	private ImageIcon rightIcon1 = new ImageIcon("images/buttons/arrowright.png");
	private ImageIcon rightIcon2 = new ImageIcon("images/buttons/arrowright hover.png");
	
	/**
	 * Create the panel.
	 */
	public BuyPlotMapMenu(String map) {
		removeAll();
		
		addMouseListener (new MouseAdapter(){
		    public void mouseClicked(MouseEvent e) {
		        if (SwingUtilities.isRightMouseButton(e)) {
		        	Jasbro.getInstance().getGui().showRealEstate();
		        }
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
				Jasbro.getInstance().getGui().showRealEstate();
			}
		});
		
		plotIcon1 = new ImageIcon("images/buttons/landmarker.png");
		Image plotImage1 = plotIcon1.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH );
		plotIcon1 = new ImageIcon(plotImage1);
		
		plotIcon2 = new ImageIcon("images/buttons/landmarker hover.png");
		Image plotImage2 = plotIcon2.getImage().getScaledInstance( iconSize, iconSize,  java.awt.Image.SCALE_SMOOTH );
		plotIcon2 = new ImageIcon(plotImage2);
		
		Image leftImage1 = leftIcon1.getImage().getScaledInstance( backHomeBtnHeight, backHomeBtnWidth,  java.awt.Image.SCALE_SMOOTH ) ;  
		leftIcon1 = new ImageIcon(leftImage1);
		
		Image leftImage2 = leftIcon2.getImage().getScaledInstance( backHomeBtnHeight, backHomeBtnWidth,  java.awt.Image.SCALE_SMOOTH ) ;  
		leftIcon2 = new ImageIcon(leftImage2);
		
		Image rightImage1 = rightIcon1.getImage().getScaledInstance( backHomeBtnHeight, backHomeBtnWidth,  java.awt.Image.SCALE_SMOOTH ) ;  
		rightIcon1 = new ImageIcon(rightImage1);
				
		Image rightImage2 = rightIcon2.getImage().getScaledInstance( backHomeBtnHeight, backHomeBtnWidth,  java.awt.Image.SCALE_SMOOTH ) ;  
		rightIcon2 = new ImageIcon(rightImage2);
		
		switch(map) {
			case "map2":
				map2();
				break;
			case "map3":
				map3();
				break;
			case "map4":
				map4();
				break;
			default:
				map1();
				break;
		}	
	}
	
	public void map1() {		
		setBackgroundImage(getTownImage("map1"));
		setBackground(Color.WHITE);
		setLayout(null);
		setVisible(true);



		if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
			
			JButton plot1Button = new JButton(plotIcon1);
			plot1Button.setRolloverIcon(plotIcon2);
			plot1Button.setPressedIcon(plotIcon1);
			plot1Button.setBounds(580*width/1280, 320*height/720, iconSize, iconSize);
			plot1Button.setBorderPainted(false); 
			plot1Button.setContentAreaFilled(false); 
			plot1Button.setFocusPainted(false); 
			plot1Button.setOpaque(false);
			plot1Button.setToolTipText("Plot 1");

			add(plot1Button);
			plot1Button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (JOptionPane.showConfirmDialog (Jasbro.getInstance().getGui(), TextUtil.t("ui.realestate.buyplot"), TextUtil.t("ui.confirmResetPerks.title"), JOptionPane.OK_CANCEL_OPTION) ==
							JOptionPane.YES_NO_OPTION) {
						Jasbro.getInstance().getData().spendMoney(100000, "");
					}
				}
			});
			
			JButton plot2Button = new JButton(plotIcon1);
			plot2Button.setRolloverIcon(plotIcon2);
			plot2Button.setPressedIcon(plotIcon1);
			plot2Button.setBounds(50*width/1280, 80*height/720, iconSize, iconSize);
			plot2Button.setBorderPainted(false); 
			plot2Button.setContentAreaFilled(false); 
			plot2Button.setFocusPainted(false); 
			plot2Button.setOpaque(false);
			plot2Button.setToolTipText("Plot 2");

			add(plot2Button);
			plot2Button.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
				//	Jasbro.getInstance().getGui().showSlaveMarketScreen();
				}
			});		
			
			JButton plot3Button = new JButton(plotIcon1);
			plot3Button.setRolloverIcon(plotIcon2);
			plot3Button.setPressedIcon(plotIcon1);
			plot3Button.setBounds(425*width/1280, 280*height/720, iconSize, iconSize);
			plot3Button.setBorderPainted(false); 
			plot3Button.setContentAreaFilled(false); 
			plot3Button.setFocusPainted(false); 
			plot3Button.setOpaque(false);
			plot3Button.setToolTipText("Plot 3");

			add(plot3Button);
			plot3Button.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
				//	Jasbro.getInstance().getGui().showSlaveMarketScreen();
				}
			});		
			
			JButton plot4Button = new JButton(plotIcon1);
			plot4Button.setRolloverIcon(plotIcon2);
			plot4Button.setPressedIcon(plotIcon1);
			plot4Button.setBounds(190*width/1280, 320*height/720, iconSize, iconSize);
			plot4Button.setBorderPainted(false); 
			plot4Button.setContentAreaFilled(false); 
			plot4Button.setFocusPainted(false); 
			plot4Button.setOpaque(false);
			plot4Button.setToolTipText("Plot 4");

			add(plot4Button);
			plot4Button.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
				//	Jasbro.getInstance().getGui().showSlaveMarketScreen();
				}
			});		
			
			JButton leftButton = new JButton(leftIcon1);
			leftButton.setRolloverIcon(leftIcon2);
			leftButton.setPressedIcon(leftIcon1);
			leftButton.setBounds((int) (15*width/1280),(int) (250*height/720), backHomeBtnHeight, backHomeBtnWidth);
			leftButton.setBorderPainted(false); 
			leftButton.setContentAreaFilled(false); 
			leftButton.setFocusPainted(false); 
			leftButton.setOpaque(false);
			add(leftButton);
			
			leftButton.addActionListener(new  ActionListener() {			
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showBuyPlotMapScreen("map4");
				}
			});
			
			JButton rightButton = new JButton(rightIcon1);
			rightButton.setRolloverIcon(rightIcon2);
			rightButton.setPressedIcon(rightIcon1);
			rightButton.setBounds((int) (1210*width/1280),(int) (250*height/720), backHomeBtnHeight, backHomeBtnWidth);
			rightButton.setBorderPainted(false); 
			rightButton.setContentAreaFilled(false); 
			rightButton.setFocusPainted(false); 
			rightButton.setOpaque(false);
			add(rightButton);
			
			rightButton.addActionListener(new  ActionListener() {			
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showBuyPlotMapScreen("map2");
				}
			});

		}

		validate();
		repaint();
	}
	
	public void map2() {		
		setBackgroundImage(getTownImage("map2"));
		setBackground(Color.WHITE);
		setLayout(null);
		setVisible(true);

		if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
			
			JButton plot1Button = new JButton(plotIcon1);
			plot1Button.setRolloverIcon(plotIcon2);
			plot1Button.setPressedIcon(plotIcon1);
			plot1Button.setBounds(860*width/1280, 505*height/720, iconSize, iconSize);
			plot1Button.setBorderPainted(false); 
			plot1Button.setContentAreaFilled(false); 
			plot1Button.setFocusPainted(false); 
			plot1Button.setOpaque(false);
			plot1Button.setToolTipText("Plot 1");

			add(plot1Button);
			plot1Button.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
				//	Jasbro.getInstance().getGui().showSlaveMarketScreen();
				}
			});	
			
			JButton plot2Button = new JButton(plotIcon1);
			plot2Button.setRolloverIcon(plotIcon2);
			plot2Button.setPressedIcon(plotIcon1);
			plot2Button.setBounds(475*width/1280, 15*height/720, iconSize, iconSize);
			plot2Button.setBorderPainted(false); 
			plot2Button.setContentAreaFilled(false); 
			plot2Button.setFocusPainted(false); 
			plot2Button.setOpaque(false);
			plot2Button.setToolTipText("Plot 2");

			add(plot2Button);
			plot2Button.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
				//	Jasbro.getInstance().getGui().showSlaveMarketScreen();
				}
			});		
			
			JButton plot3Button = new JButton(plotIcon1);
			plot3Button.setRolloverIcon(plotIcon2);
			plot3Button.setPressedIcon(plotIcon1);
			plot3Button.setBounds(595*width/1280, 220*height/720, iconSize, iconSize);
			plot3Button.setBorderPainted(false); 
			plot3Button.setContentAreaFilled(false); 
			plot3Button.setFocusPainted(false); 
			plot3Button.setOpaque(false);
			plot3Button.setToolTipText("Plot 3");

			add(plot3Button);
			plot3Button.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
				//	Jasbro.getInstance().getGui().showSlaveMarketScreen();
				}
			});		
			
			JButton plot4Button = new JButton(plotIcon1);
			plot4Button.setRolloverIcon(plotIcon2);
			plot4Button.setPressedIcon(plotIcon1);
			plot4Button.setBounds(800*width/1280, 320*height/720, iconSize, iconSize);
			plot4Button.setBorderPainted(false); 
			plot4Button.setContentAreaFilled(false); 
			plot4Button.setFocusPainted(false); 
			plot4Button.setOpaque(false);
			plot4Button.setToolTipText("Plot 4");

			add(plot4Button);
			plot4Button.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
				//	Jasbro.getInstance().getGui().showSlaveMarketScreen();
				}
			});	
			
			JButton plot5Button = new JButton(plotIcon1);
			plot5Button.setRolloverIcon(plotIcon2);
			plot5Button.setPressedIcon(plotIcon1);
			plot5Button.setBounds(1150*width/1280, 460*height/720, iconSize, iconSize);
			plot5Button.setBorderPainted(false); 
			plot5Button.setContentAreaFilled(false); 
			plot5Button.setFocusPainted(false); 
			plot5Button.setOpaque(false);
			plot5Button.setToolTipText("Plot 5");

			add(plot5Button);
			plot5Button.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
				//	Jasbro.getInstance().getGui().showSlaveMarketScreen();
				}
			});	

			JButton leftButton = new JButton(leftIcon1);
			leftButton.setRolloverIcon(leftIcon2);
			leftButton.setPressedIcon(leftIcon1);
			leftButton.setBounds((int) (15*width/1280),(int) (250*height/720), backHomeBtnHeight, backHomeBtnWidth);
			leftButton.setBorderPainted(false); 
			leftButton.setContentAreaFilled(false); 
			leftButton.setFocusPainted(false); 
			leftButton.setOpaque(false);
			add(leftButton);
			
			leftButton.addActionListener(new  ActionListener() {			
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showBuyPlotMapScreen("map1");
				}
			});

			JButton rightButton = new JButton(rightIcon1);
			rightButton.setRolloverIcon(rightIcon2);
			rightButton.setPressedIcon(rightIcon1);
			rightButton.setBounds((int) (1210*width/1280),(int) (250*height/720), backHomeBtnHeight, backHomeBtnWidth);
			rightButton.setBorderPainted(false); 
			rightButton.setContentAreaFilled(false); 
			rightButton.setFocusPainted(false); 
			rightButton.setOpaque(false);
			add(rightButton);
			
			rightButton.addActionListener(new  ActionListener() {			
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showBuyPlotMapScreen("map3");
				}
			});

		}

		validate();
		repaint();
	}
	
	public void map3() {		
		setBackgroundImage(getTownImage("map3"));
		setBackground(Color.WHITE);
		setLayout(null);
		setVisible(true);

		if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
			
			JButton plot1Button = new JButton(plotIcon1);
			plot1Button.setRolloverIcon(plotIcon2);
			plot1Button.setPressedIcon(plotIcon1);
			plot1Button.setBounds(600*width/1280, 520*height/720, iconSize, iconSize);
			plot1Button.setBorderPainted(false); 
			plot1Button.setContentAreaFilled(false); 
			plot1Button.setFocusPainted(false); 
			plot1Button.setOpaque(false);
			plot1Button.setToolTipText("Plot 1");

			add(plot1Button);
			plot1Button.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
				//	Jasbro.getInstance().getGui().showSlaveMarketScreen();
				}
			});	
			
			JButton plot2Button = new JButton(plotIcon1);
			plot2Button.setRolloverIcon(plotIcon2);
			plot2Button.setPressedIcon(plotIcon1);
			plot2Button.setBounds(630*width/1280, 35*height/720, iconSize, iconSize);
			plot2Button.setBorderPainted(false); 
			plot2Button.setContentAreaFilled(false); 
			plot2Button.setFocusPainted(false); 
			plot2Button.setOpaque(false);
			plot2Button.setToolTipText("Plot 2");

			add(plot2Button);
			plot2Button.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
				//	Jasbro.getInstance().getGui().showSlaveMarketScreen();
				}
			});		
			
			JButton plot3Button = new JButton(plotIcon1);
			plot3Button.setRolloverIcon(plotIcon2);
			plot3Button.setPressedIcon(plotIcon1);
			plot3Button.setBounds(970*width/1280, 125*height/720, iconSize, iconSize);
			plot3Button.setBorderPainted(false); 
			plot3Button.setContentAreaFilled(false); 
			plot3Button.setFocusPainted(false); 
			plot3Button.setOpaque(false);
			plot3Button.setToolTipText("Plot 3");

			add(plot3Button);
			plot3Button.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
				//	Jasbro.getInstance().getGui().showSlaveMarketScreen();
				}
			});		
			
			JButton plot4Button = new JButton(plotIcon1);
			plot4Button.setRolloverIcon(plotIcon2);
			plot4Button.setPressedIcon(plotIcon1);
			plot4Button.setBounds(100*width/1280, 320*height/720, iconSize, iconSize);
			plot4Button.setBorderPainted(false); 
			plot4Button.setContentAreaFilled(false); 
			plot4Button.setFocusPainted(false); 
			plot4Button.setOpaque(false);
			plot4Button.setToolTipText("Plot 4");

			add(plot4Button);
			plot4Button.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
				//	Jasbro.getInstance().getGui().showSlaveMarketScreen();
				}
			});		

			JButton leftButton = new JButton(leftIcon1);
			leftButton.setRolloverIcon(leftIcon2);
			leftButton.setPressedIcon(leftIcon1);
			leftButton.setBounds((int) (15*width/1280),(int) (250*height/720), backHomeBtnHeight, backHomeBtnWidth);
			leftButton.setBorderPainted(false); 
			leftButton.setContentAreaFilled(false); 
			leftButton.setFocusPainted(false); 
			leftButton.setOpaque(false);
			add(leftButton);
			
			leftButton.addActionListener(new  ActionListener() {			
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showBuyPlotMapScreen("map2");
				}
			});
	
			JButton rightButton = new JButton(rightIcon1);
			rightButton.setRolloverIcon(rightIcon2);
			rightButton.setPressedIcon(rightIcon1);
			rightButton.setBounds((int) (1210*width/1280),(int) (250*height/720), backHomeBtnHeight, backHomeBtnWidth);
			rightButton.setBorderPainted(false); 
			rightButton.setContentAreaFilled(false); 
			rightButton.setFocusPainted(false); 
			rightButton.setOpaque(false);
			add(rightButton);
			
			rightButton.addActionListener(new  ActionListener() {			
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showBuyPlotMapScreen("map4");
				}
			});

		}

		validate();
		repaint();
	}
	
	public void map4() {		
		setBackgroundImage(getTownImage("map4"));
		setBackground(Color.WHITE);
		setLayout(null);
		setVisible(true);

		if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
			
			JButton plot1Button = new JButton(plotIcon1);
			plot1Button.setRolloverIcon(plotIcon2);
			plot1Button.setPressedIcon(plotIcon1);
			plot1Button.setBounds(550*width/1280, 585*height/720, iconSize, iconSize);
			plot1Button.setBorderPainted(false); 
			plot1Button.setContentAreaFilled(false); 
			plot1Button.setFocusPainted(false); 
			plot1Button.setOpaque(false);
			plot1Button.setToolTipText("Plot 1");

			add(plot1Button);
			plot1Button.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
				//	Jasbro.getInstance().getGui().showSlaveMarketScreen();
				}
			});	
			
			JButton plot2Button = new JButton(plotIcon1);
			plot2Button.setRolloverIcon(plotIcon2);
			plot2Button.setPressedIcon(plotIcon1);
			plot2Button.setBounds(150*width/1280,385*height/720, iconSize, iconSize);
			plot2Button.setBorderPainted(false); 
			plot2Button.setContentAreaFilled(false); 
			plot2Button.setFocusPainted(false); 
			plot2Button.setOpaque(false);
			plot2Button.setToolTipText("Plot 2");

			add(plot2Button);
			plot2Button.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
				//	Jasbro.getInstance().getGui().showSlaveMarketScreen();
				}
			});		
			
			JButton plot3Button = new JButton(plotIcon1);
			plot3Button.setRolloverIcon(plotIcon2);
			plot3Button.setPressedIcon(plotIcon1);
			plot3Button.setBounds(670*width/1280, 75*height/720, iconSize, iconSize);
			plot3Button.setBorderPainted(false); 
			plot3Button.setContentAreaFilled(false); 
			plot3Button.setFocusPainted(false); 
			plot3Button.setOpaque(false);
			plot3Button.setToolTipText("Plot 3");

			add(plot3Button);
			plot3Button.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
				//	Jasbro.getInstance().getGui().showSlaveMarketScreen();
				}
			});		
			
			JButton plot4Button = new JButton(plotIcon1);
			plot4Button.setRolloverIcon(plotIcon2);
			plot4Button.setPressedIcon(plotIcon1);
			plot4Button.setBounds(685*width/1280, 323*height/720, iconSize, iconSize);
			plot4Button.setBorderPainted(false); 
			plot4Button.setContentAreaFilled(false); 
			plot4Button.setFocusPainted(false); 
			plot4Button.setOpaque(false);
			plot4Button.setToolTipText("Plot 4");

			add(plot4Button);
			plot4Button.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
				//	Jasbro.getInstance().getGui().showSlaveMarketScreen();
				}
			});	
			
			JButton plot5Button = new JButton(plotIcon1);
			plot5Button.setRolloverIcon(plotIcon2);
			plot5Button.setPressedIcon(plotIcon1);
			plot5Button.setBounds(190*width/1280, 170*height/720, iconSize, iconSize);
			plot5Button.setBorderPainted(false); 
			plot5Button.setContentAreaFilled(false); 
			plot5Button.setFocusPainted(false); 
			plot5Button.setOpaque(false);
			plot5Button.setToolTipText("Plot 5");

			add(plot5Button);
			plot5Button.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
				//	Jasbro.getInstance().getGui().showSlaveMarketScreen();
				}
			});		
			
			JButton leftButton = new JButton(leftIcon1);
			leftButton.setRolloverIcon(leftIcon2);
			leftButton.setPressedIcon(leftIcon1);
			leftButton.setBounds((int) (15*width/1280),(int) (250*height/720), backHomeBtnHeight, backHomeBtnWidth);
			leftButton.setBorderPainted(false); 
			leftButton.setContentAreaFilled(false); 
			leftButton.setFocusPainted(false); 
			leftButton.setOpaque(false);
			add(leftButton);
			
			leftButton.addActionListener(new  ActionListener() {			
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showBuyPlotMapScreen("map3");
				}
			});
			
			JButton rightButton = new JButton(rightIcon1);
			rightButton.setRolloverIcon(rightIcon2);
			rightButton.setPressedIcon(rightIcon1);
			rightButton.setBounds((int) (1210*width/1280),(int) (250*height/720), backHomeBtnHeight, backHomeBtnWidth);
			rightButton.setBorderPainted(false); 
			rightButton.setContentAreaFilled(false); 
			rightButton.setFocusPainted(false); 
			rightButton.setOpaque(false);
			add(rightButton);
			
			rightButton.addActionListener(new  ActionListener() {			
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showBuyPlotMapScreen("map1");
				}
			});

		}

		validate();
		repaint();
	}

	public ImageData getTownImage(String map) {
		switch(map) {
			case "map2":
				return new ImageData("images/backgrounds/map2.jpg");
			case "map3":
				return new ImageData("images/backgrounds/map3.jpg");
			case "map4":
				return new ImageData("images/backgrounds/map4.jpg");
			default:
				return new ImageData("images/backgrounds/town_morning.png");
		}
	}	
}