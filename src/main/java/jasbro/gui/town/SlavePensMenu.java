package jasbro.gui.town;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.Ownership;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.world.market.SlaveMarket;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pages.subView.CharacterShortView;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SlavePensMenu extends MyImage {
	private JPanel hirePanel;
	private MyImage slaveImage;
	private Charakter selectedSlave = null;
	private JTextArea hintArea;
	
	public SlavePensMenu() {
		
		setOpaque(false);
		setBackgroundImage(new ImageData("images/backgrounds/slavePens.png"));
		
		double width = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
		double height = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
		int widthRat = (int) (width/1280);
		int heightRat = (int) (height/720);
		int iconSize = (int) (65*width/1280);
		int backHomeBtnWidth = (int) (150*width/1280);
		int backHomeBtnHeight = (int) (50*width/1280);
		
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
				Jasbro.getInstance().getGui().showSlaveMarketScreen();
			}
		});
		
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("140dlu"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow(20)"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow(18)"),
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("default:grow"),
				RowSpec.decode("default:grow(16)"),
				RowSpec.decode("default:grow(3)"),}));
		
		TranslucentPanel translucentPanel = new TranslucentPanel();
		translucentPanel.setPreferredSize(new Dimension(0, 0));
		add(translucentPanel, "3, 2, fill, fill");
		
		translucentPanel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("default:grow"), }, new RowSpec[] { RowSpec.decode("fill:20dlu"), RowSpec.decode("fill:pref:grow"), }));
		
		JLabel lblBuyGirl = new JLabel(TextUtil.t("ui.buyslave.title"));
		lblBuyGirl.setFont(new Font("Tahoma", Font.BOLD, 18));
		translucentPanel.add(lblBuyGirl, "1, 1");
		
		hirePanel = new JPanel();
		hirePanel.setOpaque(false);
		translucentPanel.add(hirePanel, "1, 2, fill, fill");
		
		slaveImage = new MyImage();
		add(slaveImage, "5, 1, 1, 2");
		
		hintArea = GuiUtil.getDefaultTextarea();				
		hintArea.setText(TextUtil.t("ui.buyslave.introduction"));
		hintArea.setFont(GuiUtil.DEFAULTBOLDFONT);
		
		TranslucentPanel controlPanel = new TranslucentPanel();
		add(controlPanel, "5, 3, fill, fill");
		controlPanel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("default:grow"), }, new RowSpec[] { RowSpec.decode("default:grow"), FormFactory.DEFAULT_ROWSPEC, }));
		controlPanel.add(hintArea, "1, 1, fill, fill");
		
		JButton btnHire = new JButton("Buy");
		btnHire.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object arguments[] = {selectedSlave.getName()};
				int price = 500 + (int) selectedSlave.calculateValue();
				if(price<500){price=500;}
			
				if (selectedSlave != null && Jasbro.getInstance().getData().canAfford(price)) {
					selectedSlave.setOwnership(Ownership.OWNED);
					Jasbro.getInstance().getData().getCharacters().add(selectedSlave);
					Jasbro.getInstance().getData().spendMoney(price, TextUtil.t("slavemarket.bought", arguments));
					Jasbro.getInstance().getData().getSlaveMarket().getSlaves().remove(selectedSlave);
					new MessageScreen(TextUtil.t("ui.buyslave.bought", selectedSlave), ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, selectedSlave), selectedSlave.getBackground());
					Jasbro.getInstance().getData().getEventManager().notifyAll(new MyEvent(EventType.CHARACTERGAINED, selectedSlave));
					selectedSlave = null;
					slaveImage.setImage(null);
					initHireList();
				}
				else{
					new MessageScreen(TextUtil.t("ui.buyslave.cantafford", selectedSlave), ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, selectedSlave), selectedSlave.getBackground());
				}
			}
		});
		
		addMouseListener (new MouseAdapter(){
	        public void mouseClicked(MouseEvent e) {
	            if (SwingUtilities.isRightMouseButton(e)) {
	            	Jasbro.getInstance().getGui().showSlaveMarketScreen();
	            }
	        }		
		});
		
		btnHire.setFont(GuiUtil.DEFAULTBOLDFONT);
		controlPanel.add(btnHire, "1, 2, center, bottom");
		
		initHireList();
	}
	
	public void initHireList() {
		MouseListener ml = new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				mouseAction(e);
			}
			
			public void mouseAction(MouseEvent e) {
				if (!e.isConsumed()) {
					e.consume();
					CharacterShortView shortView = (CharacterShortView) e.getSource();
					selectedSlave = (Charakter) shortView.getCharacter();
					List<ImageTag> imageTags = selectedSlave.getBaseTags();
					imageTags.add(0, ImageTag.NAKED);
					imageTags.add(1, ImageTag.CLEANED);
					slaveImage.setImage(ImageUtil.getInstance().getImageDataByTags(imageTags, selectedSlave.getImages()));
					if(selectedSlave != null) {
						String name = selectedSlave.getName();
						int price = 500 + (int) selectedSlave.calculateValue();
						if(price<500){price=500;}
						hintArea.setText(TextUtil.t("ui.buyslave.costText", name, price));
					}
					repaint();
				}
			}
		};
		
		hirePanel.removeAll();
		SlaveMarket slaveMarket = Jasbro.getInstance().getData().getSlaveMarket();
		for (Charakter character : slaveMarket.getSlaves()) {
			CharacterShortView shortView = new CharacterShortView(character, false);
			hirePanel.add(shortView);
			shortView.addMouseListener(ml);
		}
	}
	
}