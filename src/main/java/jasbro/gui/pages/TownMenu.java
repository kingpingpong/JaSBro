package jasbro.gui.pages;

import jasbro.Jasbro;
import jasbro.game.world.Time;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pages.subView.CheatScreen;
import jasbro.gui.pages.subView.InteriorDecorationPanel;
import jasbro.gui.pages.subView.LaboratoryPanel;
import jasbro.gui.pages.subView.RealEstatePanel;
import jasbro.gui.pages.subView.SchoolPanel;
import jasbro.gui.pages.subView.ShopOverviewPanel;
import jasbro.gui.pages.subView.TrainerBarPanel;
import jasbro.gui.pages.subView.TrainerGuildPanel;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class TownMenu extends MyImage {
	private JPanel menuPanel;
	private JPanel contenPanel;
	private JButton btnAuctionHouse;
	
	/**
	 * Create the panel.
	 */
	public TownMenu() {
	    setBackgroundImage(getTownImage());
		setBackground(Color.WHITE);
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("1dlu:grow(2)"),
				ColumnSpec.decode("1dlu:grow(13)"),},
				new RowSpec[] {
				RowSpec.decode("fill:1dlu:grow"),}));
		
		menuPanel = new JPanel();
		menuPanel.setOpaque(false);
		menuPanel.setBorder(GuiUtil.DEFAULTEMPTYBORDER);
		add(menuPanel, "1, 1, fill, fill");
		
		contenPanel = new JPanel();
		contenPanel.setOpaque(false);
		add(contenPanel, "2, 1, fill, fill");
		contenPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("1dlu:grow"),},
				new RowSpec[] {
				RowSpec.decode("fill:1dlu:grow"),}));
		
		
		menuPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("pref:grow"),},
				new RowSpec[] {
				RowSpec.decode("fill:pref:grow(100)"),}));
		
		if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
			
			JButton slaveMarketButton = new JButton(TextUtil.t("ui.slavemarket"));
			addToMenuPanel(slaveMarketButton);
			slaveMarketButton.addActionListener(new  ActionListener() {
					
				@Override
				public void actionPerformed(ActionEvent e) {
					Jasbro.getInstance().getGui().showSlaveMarketScreen();
				}
			});
			
			JButton btnBar = new JButton("Trainer bar");
			addToMenuPanel(btnBar);
			btnBar.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					setContentPanel(new TrainerBarPanel(), new ImageData("images/backgrounds/trainerbar.jpg"));
				}
			});
			
			JButton trainerGuildButton = new JButton(TextUtil.t("ui.town.guild"));
			addToMenuPanel(trainerGuildButton);
			trainerGuildButton.addActionListener(new  ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setContentPanel(new TrainerGuildPanel(), null);
				}
			});
			
			JButton schoolButton = new JButton(TextUtil.t("ui.town.school"));
			addToMenuPanel(schoolButton);
			schoolButton.addActionListener(new  ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setContentPanel(new SchoolPanel(), new ImageData("images/backgrounds/school.jpg"));
				}
			});
			
			JButton buyHouseButton = new JButton(TextUtil.t("ui.town.realestate"));
			addToMenuPanel(buyHouseButton);
			buyHouseButton.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					setContentPanel(new RealEstatePanel(), null);
				}
			});
			
			JButton interiorDecorationButton = new JButton(TextUtil.t("ui.town.interiordeco"));
			addToMenuPanel(interiorDecorationButton);
			interiorDecorationButton.addActionListener(new  ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setContentPanel(new InteriorDecorationPanel(), null);
				}
			});
			
			JButton visitLaboratoryButton = new JButton(TextUtil.t("ui.town.laboratory"));
			addToMenuPanel(visitLaboratoryButton);
			visitLaboratoryButton.addActionListener(new  ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					setContentPanel(new LaboratoryPanel(), new ImageData("images/backgrounds/labratory.jpg"));
				}
			});
			
			JButton visitShopButton = new JButton(TextUtil.t("ui.town.shop"));
			addToMenuPanel(visitShopButton);
			visitShopButton.addActionListener(new  ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setContentPanel(new ShopOverviewPanel(TownMenu.this), new ImageData("images/backgrounds/shop.jpg"));
				}
			});
		}
		
		if (ConfigHandler.isCheat()) {
			JButton cheatButton = new JButton(TextUtil.t("ui.cheat"));
			addToMenuPanel(cheatButton);
			cheatButton.addActionListener(new  ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setContentPanel(new CheatScreen(), null);
				}
			});
		}
		
		JButton backButton = new JButton(TextUtil.t("ui.back"));
		addToMenuPanel(backButton);
		backButton.addActionListener(new  ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Jasbro.getInstance().getGui().showHouseManagementScreen();
			}
		});
		
		validate();
		repaint();
	}
	
	private void addToMenuPanel(JComponent component) {
		FormLayout layout = (FormLayout) menuPanel.getLayout();
		layout.appendRow(RowSpec.decode("1dlu:grow"));
		layout.appendRow(RowSpec.decode("default:grow"));
		int row = layout.getRowCount();
		menuPanel.add(component, "1,"+(row)+", fill, fill");
	}
	
	public void setContentPanel(JPanel screen, ImageData background) {
		if (background == null) {
			background = getTownImage();
		}
		contenPanel.removeAll();
		contenPanel.add(screen, "1, 1, fill, fill");
		setBackgroundImage(background);
		revalidate();
		repaint();
	}
	
	public ImageData getTownImage() {
		switch(Jasbro.getInstance().getData().getTime()) {
		case AFTERNOON:
			return new ImageData("images/backgrounds/town_afternoon.jpg");
		case NIGHT:
			return new ImageData("images/backgrounds/town_night.jpg");
		default:
			return new ImageData("images/backgrounds/town_morning.jpg");
		}
	}
	
}