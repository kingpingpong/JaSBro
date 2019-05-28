/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jasbro.gui;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.housing.House;
import jasbro.game.items.ItemLocation;
import jasbro.game.realestate.BuyPlotMapMenu;
import jasbro.game.world.market.Auction;
import jasbro.game.world.market.AuctionHouse;
import jasbro.gui.objects.div.MessageInterface;
import jasbro.gui.objects.menus.MainMenuBar;
import jasbro.gui.pages.AuctionScreen;
import jasbro.gui.pages.CharacterScreen;
import jasbro.gui.pages.GameOverScreen;
import jasbro.gui.pages.HouseScreen;
import jasbro.gui.pages.MainMenu;
import jasbro.gui.pages.ManagementScreen;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pages.NewGameScreen;
import jasbro.gui.pages.QuestScreen;
import jasbro.gui.pages.TownMenu;
import jasbro.gui.pages.UnlockScreen;
import jasbro.gui.pages.subView.CheatScreen;
import jasbro.gui.town.AdventurerBarMenu;
import jasbro.gui.town.AdventurersGuildMenu;
import jasbro.gui.town.AlchemistEntranceMenu;
import jasbro.gui.town.AuctionHouseMenu;
import jasbro.gui.town.BuildersGuildMenu;
import jasbro.gui.town.GeneralMarketMenu;
import jasbro.gui.town.GeneralMarketMenuTwo;
import jasbro.gui.town.InteriorDecorationMenu;
import jasbro.gui.town.LaboratoryMenu;
import jasbro.gui.town.QuestMenu;
import jasbro.gui.town.RealEstateMenu;
import jasbro.gui.town.SchoolMenu;
import jasbro.gui.town.ShopMenu;
import jasbro.gui.town.SlaveMarketMenu;
import jasbro.gui.town.SlavePensMenu;
import jasbro.gui.town.SlaverGuildMenu;
import jasbro.gui.town.TownMenuNew;
import jasbro.gui.town.TrainerBarMenu;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.filechooser.FileFilter;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 *
 * @author Azrael
 */
public class RPGView extends JFrame {
	private JPanel layerPane;
	private List<JComponent> layers = new ArrayList<JComponent>();
	private MainMenuBar menuBar;
	private boolean repaintScheduled = false;
	private List<MessageScreen> previousMessages = new ArrayList<MessageScreen>();
	private int resolutionHeight = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
	private int resolutionWidth = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
	
	private enum Screen { TOWN, BUILDER, SLAVE, SCHOOL, MARKET, MARKET2 , GUILD, ARCHITECT, CARPENTRY, REALESTATE,
						BOOKSTORE, MAGICSHOP, ADULTSTORE, GENERALSTORE,  TAILOR, ADVENTURERSTORE, SLAVEPENS, MAINMENU,
						AUCTIONHOUSE, LABORATORY, ALCHEMIST, TRAVELING, BLACKMARKET, TRAINERBAR, ADVENTURERSGUILD, NONE;
	};
	private Screen actualScreen = Screen.NONE;
	
	//Filtered character list
	private CharacterFilterListModel filteredModel = new CharacterFilterListModel();
	
	public RPGView() {
		super(TextUtil.t("version"));
				
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);		
		
		ToolTipManager.sharedInstance().setDismissDelay(20000);
		ToolTipManager.sharedInstance().setInitialDelay(0);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menuBar = new MainMenuBar();
		setJMenuBar (menuBar);
		setResizable(false);
		
		initComponents();
		
	}
	
	
	public void showStartScreen() {
		synchronized (getTreeLock()) {
			removeAllLayers();
			addLayer(new NewGameScreen());
		}
	}
	
	public void showMainMenu() {
		synchronized (getTreeLock()) {
			removeAllLayers();
			actualScreen = Screen.MAINMENU;
			addLayer(new MainMenu());
		}
	}
	
	public void showHouseManagementScreen() {
		synchronized (getTreeLock()) {
			removeAllLayers();
			addLayer(new ManagementScreen());
		}
	}
	
	public void addLayer(JComponent layer) {
		synchronized (getTreeLock()) {
			if (layer instanceof MessageInterface) {
				((MessageInterface) layer).init();
			}
			if (layerPane.getComponents().length != 0) {
				layerPane.getComponents()[0].setVisible(false);
				layers.add((JComponent)layerPane.getComponents()[0]);
			}
			layerPane.removeAll();
			layerPane.add(layer, "1, 1, fill, fill");
			layer.setVisible(true);
			layerPane.validate();
			this.repaint();
		}
	}
	
	public void addLayerBottom(JPanel layer) {
		synchronized (getTreeLock()) {
			if (layerPane.getComponents().length != 0) {
				layers.add(0, layer);
				layer.setVisible(false);
			}
			else {
				if (layer != null) {
					addLayer(layer);
				}
			}
		}
	}
	
	public void removeLayer(JComponent component) {
		synchronized (getTreeLock()) {
			if (layers.contains(component) || (layerPane.getComponents().length > 0 &&
					layerPane.getComponent(0) == component)) {
				if (component instanceof MessageScreen && !previousMessages.contains(component)) {
					previousMessages.add((MessageScreen) component);
				}
			}
			layers.remove(component);
			layerPane.remove(component);
			if (layerPane.getComponents().length == 0 && layers.size() > 0) {
				JComponent component2 = layers.remove(layers.size()-1);
				addLayer(component2);
			}
		}
	}
	
	public void removeAllLayers() {
		synchronized (getTreeLock()) {
			if (layerPane.getComponents().length > 0) {
				layerPane.getComponent(0).setVisible(false);
			}
			
			layerPane.removeAll();
			layers.clear();
			previousMessages.clear();
		}
	}
	
	public void addMessage(final JPanel message) {
		Thread.yield();
		synchronized (getTreeLock()) {
			if (layerPane.getComponents().length == 0  || ! (layerPane.getComponent(0) instanceof MessageInterface)) {
				addLayer(message);
			}
			else {
				boolean alreadyAdded = false;
				for (int i = 0; i < layers.size(); i++) {
					if (layers.get(i) instanceof MessageInterface) {
						layers.add(i, message);
						alreadyAdded = true;
						break;
					}
				}
				if (!alreadyAdded) {
					layers.add(message);
				}
				message.setVisible(false);
			} 
		}
	}
	
	public void closeAllMessages() {
		synchronized (getTreeLock()) {
			if (layerPane.getComponents()[0] instanceof MessageScreen) {
				previousMessages.add((MessageScreen) layerPane.getComponents()[0]);
			}
			for (int i = layers.size() - 1; i >= 0; i--) {
				if (layers.get(i) != null && layers.get(i) instanceof MessageInterface && 
						!((MessageInterface)layers.get(i)).isPriorityMessage() ) {
					if (layers.get(i) instanceof MessageScreen) {
						previousMessages.add((MessageScreen) layers.get(i));
					}
					layers.remove(i);
				}
				else {
					break;
				}
			}
			
			if (layerPane.getComponents()[0] instanceof MessageInterface) {
				removeLayer((JComponent)layerPane.getComponents()[0]);
			}
			repaint();
		}
	}
	
	public void closeAllMessagesByLocation(Object groupObject) {
		synchronized (getTreeLock()) {
			if (layerPane.getComponents()[0] instanceof MessageScreen) {
				previousMessages.add((MessageScreen) layerPane.getComponents()[0]);
			}
			for (int i = layers.size() - 1; i >= 0; i--) {
				if (layers.get(i) != null && layers.get(i) instanceof MessageInterface && 
						!((MessageInterface)layers.get(i)).isPriorityMessage() &&
						((MessageInterface)layers.get(i)).getMessageGroupObject() == groupObject) {
					if (layers.get(i) instanceof MessageScreen) {
						previousMessages.add((MessageScreen) layers.get(i));
					}
					layers.remove(i);
				}
				else {
					break;
				}
			}
			
			if (layerPane.getComponents()[0] instanceof MessageInterface) {
				removeLayer((JComponent)layerPane.getComponents()[0]);
			}
			repaint();
		}
	}
	
	public void restoreLastMessage() {
		if (previousMessages.size() > 0) {
			MessageScreen messageScreen = previousMessages.remove(previousMessages.size()-1);
			messageScreen.setPriorityMessage(false);
			addLayer(messageScreen);
		}
	}
	
	public void save() {
		JFileChooser fileChooser = getFileChooser();
		fileChooser.showSaveDialog(this);
		Jasbro.getInstance().save(fileChooser.getSelectedFile());
	}
	
	private JFileChooser getFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		
		fileChooser.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if (f.isDirectory() || f.getName().endsWith(".xml")) {
					return true;
				}
				else {
					return false;
				}
			}
			@Override
			public String getDescription() {
				return "";
			}
		});
		fileChooser.setMultiSelectionEnabled(false);
		return fileChooser;
	}
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {
		
		layerPane = new JPanel();
		
		setPreferredSize(new java.awt.Dimension(resolutionWidth, resolutionHeight));
		getContentPane().setLayout(new java.awt.GridLayout(1, 1));
		
		layerPane.setDoubleBuffered(true);
		layerPane.addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
			public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
			}
			public void ancestorResized(java.awt.event.HierarchyEvent evt) {
				layerPaneAncestorResized(evt);
			}
		});
		getContentPane().add(layerPane);
		layerPane.setBackground(Color.WHITE);
		layerPane.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("1dlu:grow"),},
				new RowSpec[] {
				RowSpec.decode("fill:1dlu:grow"),}));
		
		pack();
	}// </editor-fold>//GEN-END:initComponents
	
	private void layerPaneAncestorResized(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_layerPaneAncestorResized
		for (Component layer : layerPane.getComponents()) {
			layer.setSize(getContentPane().getWidth(), getContentPane().getHeight());
		}
		getContentPane().validate();
		getContentPane().repaint();
	}//GEN-LAST:event_layerPaneAncestorResized
	
	public void showTownScreen() {
		synchronized (getTreeLock()) {
			removeAllLayers();
			if(ConfigHandler.getSetting(Settings.TOWNSCREENNEW, true) == true){
				addLayer(new TownMenuNew());
			}			
			else{
				addLayer(new TownMenu());
			}
		}
	}
	
	public void showSlaveMarketScreen() {
		synchronized (getTreeLock()) {
			removeAllLayers();
			actualScreen = Screen.SLAVE;
			addLayer(new SlaveMarketMenu());
		}
	}
	
	public void showSlaverGuildScreen() {
		synchronized (getTreeLock()) {
			removeAllLayers();
			actualScreen = Screen.GUILD;
			addLayer(new SlaverGuildMenu());
		}
	}
	
	public void showAdventurersGuildScreen() {
		synchronized (getTreeLock()) {
			removeAllLayers();
			actualScreen = Screen.ADVENTURERSGUILD;
			addLayer(new AdventurersGuildMenu());
		}
	}
	
	public void showGeneralMarketScreen(int marketwindow) {
		synchronized (getTreeLock()) {
			removeAllLayers();
			if(marketwindow == 1){
				actualScreen = Screen.MARKET;
				addLayer(new GeneralMarketMenu());
			}
			if(marketwindow == 2){
				actualScreen = Screen.MARKET2;
				addLayer(new GeneralMarketMenuTwo());
			}
		}
	}
	
	public void showSlavePens() {
		synchronized (getTreeLock()) {
			removeAllLayers();
			actualScreen = Screen.SLAVEPENS;
			addLayer(new SlavePensMenu());
		}
	}
	
	public void showBuildersGuildScreen() {
		synchronized (getTreeLock()) {
			removeAllLayers();
			actualScreen = Screen.BUILDER;
			addLayer(new BuildersGuildMenu());
		}
	}
	
	public void showCheatScreen() {
		synchronized (getTreeLock()) {
			removeAllLayers();
			addLayer(new CheatScreen());
		}
	}
	
	public void showBuyPlotMapScreen(String map) {
		synchronized (getTreeLock()) {
			removeAllLayers();
			addLayer(new BuyPlotMapMenu(map));
		}
	}
	
	public void showSchoolScreen() {
		synchronized (getTreeLock()) {
			removeAllLayers();
			actualScreen = Screen.SCHOOL;
			addLayer(new SchoolMenu());
		}
	}
	
	public void showShopScreen(String type) {
		synchronized (getTreeLock()) {
			removeAllLayers();
			switch(type){
				case "general": addLayer(new ShopMenu(ItemLocation.SHOP)); actualScreen = Screen.GENERALSTORE; break;
				case "clothing": addLayer(new ShopMenu(ItemLocation.CLOTHINGSTORE)); actualScreen = Screen.TAILOR; break;
				case "adventurer": addLayer(new ShopMenu(ItemLocation.ADVENTURERSSHOP)); actualScreen = Screen.ADVENTURERSTORE; break;
				case "alchemist": addLayer(new ShopMenu(ItemLocation.APOTHECARY)); actualScreen = Screen.MAGICSHOP; break;
				case "adult": addLayer(new ShopMenu(ItemLocation.ADULTSTORE)); actualScreen = Screen.ADULTSTORE; break;
				case "traveling": addLayer(new ShopMenu(ItemLocation.TRAVELLINGMERCHANT)); actualScreen = Screen.TRAVELING; break;
				case "blackmarket": addLayer(new ShopMenu(ItemLocation.BLACKMARKET)); actualScreen = Screen.BLACKMARKET; break;
				case "architect": addLayer(new ShopMenu(ItemLocation.ARCHITECT)); actualScreen = Screen.ARCHITECT; break;
				case "book": addLayer(new ShopMenu(ItemLocation.BOOKSTORE)); actualScreen = Screen.BOOKSTORE; break;
			}	
		}
	}
	
	public void showAlchemist() {
		synchronized (getTreeLock()) {
			removeAllLayers();
			actualScreen = Screen.ALCHEMIST;
			addLayer(new AlchemistEntranceMenu());
		}
	}
	
	public void showTrainerBar() {
		synchronized (getTreeLock()) {
			removeAllLayers();
			actualScreen = Screen.TRAINERBAR;
			addLayer(new TrainerBarMenu());
		}
	}
	
	public void showQuestScreen() {
		synchronized (getTreeLock()) {
			removeAllLayers();
			actualScreen = Screen.TRAINERBAR;
			addLayer(new QuestMenu());
		}
	}
	
	public void showAdventurerScreen() {
		synchronized (getTreeLock()) {
			removeAllLayers();
			actualScreen = Screen.TRAINERBAR;
			addLayer(new AdventurerBarMenu());
		}
	}
	
	public void showUnlockScreen() {
		synchronized (getTreeLock()) {
			removeAllLayers();
			addLayer(new UnlockScreen());
		}
	}
	
	public void showRealEstate() {
		synchronized (getTreeLock()) {
			removeAllLayers();
			actualScreen = Screen.REALESTATE;
			addLayer(new RealEstateMenu());
		}
	}
	
	// TODO
	public void showConversationWindow() {
		synchronized (getTreeLock()) {
			removeAllLayers();
			actualScreen = Screen.REALESTATE;
			addLayer(new RealEstateMenu());
		}
	}
	
	public void showLaboratory() {
		synchronized (getTreeLock()) {
			removeAllLayers();
			actualScreen = Screen.LABORATORY;
			addLayer(new LaboratoryMenu());
		}
	}
	
	public void showInteriorDecoration() {
		synchronized (getTreeLock()) {
			removeAllLayers();
			actualScreen = Screen.CARPENTRY;
			addLayer(new InteriorDecorationMenu());
		}
	}
	
	public void showAuctionHouse() {
		synchronized (getTreeLock()) {
			removeAllLayers();
			actualScreen = Screen.AUCTIONHOUSE;
			addLayer(new AuctionHouseMenu());
		}
	}
	
	public JPanel getLayerPane() {
		return layerPane;
	}
	
	public MainMenuBar getMainMenuBar() {
		return menuBar;
	}
	
	public void showCharacterView(Charakter character) {
		addMessage(new CharacterScreen(character));
	}
	
	public void showHouseScreen(House house) {
		removeAllLayers();
		addLayer(new HouseScreen(house));
	}
	
	public void showQuestsScreen() {
		removeAllLayers();
		addLayer(new QuestScreen());
	}
	
	public void showGameOverScreen() {
		addMessage(new GameOverScreen());
	}
	
	public void startAuction(AuctionHouse auctionHouse, Charakter selectedSlave, boolean sell) {
		synchronized (getTreeLock()) {
			Auction auction = new Auction();
			auction.setSlave(selectedSlave);
			AuctionScreen auctionScreen = new AuctionScreen(auction);
			auction.setGui(auctionScreen);
			addLayer(auctionScreen);
			auction.startAuction();
		}
	}
	
	public void updateStatus() {
		if (!repaintScheduled) {
			AccessController.doPrivileged(new PrivilegedAction<Void>() {
				
				@Override
				public Void run() {
					Jasbro.getThreadpool().execute(new Runnable() {
						@Override
						public void run() {
							repaintScheduled = true;
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
							}
							getMainMenuBar().updateStatusInfo();
							repaintScheduled = false;
						}
					}); 
					return null;
				}
			});
		}
	}
	
	public boolean hasPreviousMessages() {
		return previousMessages.size() > 0;
	}
	
	
	public CharacterFilterListModel getFilteredModel() {
		return filteredModel;
	}
	
	public void setFilteredModel(CharacterFilterListModel filteredModel) {
		this.filteredModel = filteredModel;
	}
	
	public void changeResolution(int width, int height) {
		Dimension size = new Dimension(width, height);
		setSize(size);
		repaint();
		
		switch(actualScreen) {
		case ADULTSTORE: showShopScreen("adult"); break;
		case ADVENTURERSTORE: showShopScreen("adventurer"); break;
		case ALCHEMIST: showAlchemist(); break;
		case ARCHITECT: showShopScreen("architect"); break;
		case AUCTIONHOUSE: showAuctionHouse(); break;
		case BLACKMARKET: showShopScreen("blackmarket"); break;
		case BOOKSTORE: showShopScreen("book"); break;
		case BUILDER: showBuildersGuildScreen(); break;
		case CARPENTRY: showInteriorDecoration(); break;
		case GENERALSTORE: showShopScreen("general"); break;
		case GUILD: showSlaverGuildScreen(); break;
		case LABORATORY: showLaboratory(); break;
		case MAGICSHOP: showShopScreen("alchemist");; break;
		case MARKET: showGeneralMarketScreen(1); break;
		case MARKET2: showGeneralMarketScreen(2); break;
		case MAINMENU: showMainMenu(); break;
		case REALESTATE: showRealEstate(); break;
		case SCHOOL: showSchoolScreen(); break;
		case SLAVE: showSlaveMarketScreen(); break;
		case SLAVEPENS: showSlavePens(); break;
		case TAILOR: showShopScreen("clothing"); break;
		case TOWN: showTownScreen(); break;
		case TRAINERBAR: showTrainerBar(); break;
		case TRAVELING: showShopScreen("traveling"); break;
		case ADVENTURERSGUILD: showAdventurersGuildScreen(); break;
		default: break;
		
		}

		// TODO Figure out why this is here C-L
		// actualScreen = Screen.NONE;
	}
}