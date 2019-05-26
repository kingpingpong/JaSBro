package jasbro.gui.pages;

import jasbro.Jasbro;
import jasbro.game.GameData;
import jasbro.game.character.Charakter;
import jasbro.game.character.warnings.Severity;
import jasbro.game.events.MyEvent;
import jasbro.game.housing.House;
import jasbro.game.interfaces.AreaInterface;
import jasbro.game.interfaces.MyEventListener;
import jasbro.game.world.Time;
import jasbro.game.world.Unlocks;
import jasbro.game.world.locations.DivLocations;
import jasbro.game.world.locations.DungeonLocations;
import jasbro.game.world.locations.LocationType;
import jasbro.gui.CharacterFilterListModel;
import jasbro.gui.CharacterFilterListModel.Filter;
import jasbro.gui.dnd.MyCharacterTransferHandler;
import jasbro.gui.dnd.ToleranceMouseListener;
import jasbro.gui.objects.div.HouseInfoPanel;
import jasbro.gui.objects.div.MyButton;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.menus.FilterMenu;
import jasbro.gui.pages.subView.AreaPanel;
import jasbro.gui.pages.subView.CharacterShortView;
import jasbro.gui.pictures.ImageData;
import jasbro.stats.StatCollector;
import jasbro.texts.TextUtil;
import jasbro.util.UserHelper;
import jasbro.util.UserHelper.HelpOption;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class ManagementScreen extends JPanel implements MyEventListener {
	private AreaPanel areaPanel;
	private JList<Charakter> characters;
	private JPanel westernPanel;
	private CharacterShortView selectedCharacter;
	private JPanel buttonPanel;
	private JButton cityButton;
	private JButton nextShiftButton;
	private Box characterListPanel;
	private JPanel easternPanel;
	private JComboBox<AreaInterface> areaSelection;
	
	private static AreaInterface lastSelectedLocation;
	private JPanel topPanel;
	private JComboBox<String> helpOperations;
	private HouseInfoPanel infoPanel;
	private CharacterFilterListModel filteredModel;
	private JButton lastMessageButton;
	private JPanel filterPanel;
	private MyButton filterButton;
	private MyButton resetFilterButton;
	private MyButton moveUpButton;
	private MyButton moveDownButton;
	
	/**
	 * Create the panel.
	 */
	public ManagementScreen() {
		Jasbro.getInstance().getGui().updateStatus(); //A bit of a hack to ensure status is updated at the start
		
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("1dlu:grow(2)"),
				ColumnSpec.decode("8dlu:grow(13)"),},
				new RowSpec[] {
				RowSpec.decode("1dlu:grow"),}));
		
		westernPanel = new JPanel();
		westernPanel.setBackground(Color.WHITE);
		add(westernPanel, "1, 1, fill, fill");
		westernPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("1dlu:grow"),},
				new RowSpec[] {
				RowSpec.decode("1dlu:grow(6)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("1dlu:grow(10)"),
				FormFactory.DEFAULT_ROWSPEC,}));
		
		
		// Search bar
		filterPanel = new JPanel();
		westernPanel.add(filterPanel, "1, 3, fill, fill");
		
		// Filtered List Model
		filteredModel = Jasbro.getInstance().getGui().getFilteredModel();
		final String searchTerm = TextUtil.t("ui.search");
		final JTextField searchField = new JTextField (searchTerm);
		if (filteredModel.getFilter().getSearchString() != null && !filteredModel.getFilter().getSearchString().equals("")) {
			searchField.setText(filteredModel.getFilter().getSearchString());
		}
		filterPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("5dlu"),
				ColumnSpec.decode("5dlu"),
				ColumnSpec.decode("10dlu"),
				ColumnSpec.decode("10dlu"),},
				new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		filterPanel.add(searchField, "1, 1");
		
		searchField.addFocusListener (new FocusListener () {
			public void focusGained (FocusEvent e) {
				if (searchField.getText().equals (searchTerm)) searchField.setText ("");
			}
			
			public void focusLost(FocusEvent e) {
				if (searchField.getText().isEmpty()) searchField.setText (searchTerm);
			}
		});
		
		searchField.getDocument().addDocumentListener (new DocumentListener () {
			public void changedUpdate(DocumentEvent e) {
				if (!searchField.getText().equals (searchTerm)) {
					filteredModel.getFilter().setSearchString(searchField.getText());
					filteredModel.filter();
				}
			}
			
			public void insertUpdate(DocumentEvent e) {
				changedUpdate (e);
			}
			
			public void removeUpdate(DocumentEvent e) {
				changedUpdate (e);
			}
		});
		
		moveUpButton = new MyButton("", new ImageData("images/icons/arrow_up.png"), 
				new ImageData("images/icons/arrow_up.png"));
		filterPanel.add(moveUpButton, "2, 1, fill, fill");
		moveUpButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Charakter selectedChar = characters.getSelectedValue();
				if (selectedChar != null) {
					GameData data = Jasbro.getInstance().getData();
					int index = data.getCharacters().indexOf(selectedChar);
					if (index > 0) {
						data.getCharacters().remove(selectedChar);
						data.getCharacters().add(index-1, selectedChar);
						filteredModel.filter();
						characters.setSelectedValue(selectedChar, true);
					}
				}
			}
		});
		
		moveDownButton = new MyButton("", new ImageData("images/icons/arrow_down.png"), 
				new ImageData("images/icons/arrow_down.png"));
		filterPanel.add(moveDownButton, "3, 1, fill, fill");
		moveDownButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Charakter selectedChar = characters.getSelectedValue();
				if (selectedChar != null) {
					GameData data = Jasbro.getInstance().getData();
					int index = data.getCharacters().indexOf(selectedChar);
					if (index > -1 && index < data.getCharacters().size()-1) {
						data.getCharacters().remove(selectedChar);
						data.getCharacters().add(index+1, selectedChar);
						filteredModel.filter();
						characters.setSelectedValue(selectedChar, true);
					}
				}
			}
		});
		
		filterButton = new MyButton("",  new ImageData("images/icons/filter.png"), new ImageData("images/icons/filter.png"));
		filterPanel.add(filterButton, "4, 1, fill, fill");
		filterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FilterMenu filterMenu = new FilterMenu();
				if (JOptionPane.showConfirmDialog(Jasbro.getInstance().getGui(), filterMenu, "Filter", JOptionPane.OK_CANCEL_OPTION)
						== JOptionPane.OK_OPTION) {
					filteredModel.setFilter(filterMenu.getFilter());
					searchField.setText(filteredModel.getFilter().getSearchString());
					searchField.repaint();
				}
			}
		});
		
		resetFilterButton = new MyButton("", new ImageData("images/icons/x.png"), new ImageData("images/icons/x.png"));
		filterPanel.add(resetFilterButton, "5, 1, fill, fill");
		resetFilterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				filteredModel.setFilter(new Filter());
				searchField.setText("");
			}
		});
		
		
		characterListPanel = new Box (BoxLayout.PAGE_AXIS);
		westernPanel.add(characterListPanel, "1, 4, fill, fill");
		
		// Scroll pane
		JScrollPane scrollPane = new JScrollPane(); 
		characterListPanel.add(scrollPane);
		
		// Character list
		characters = new JList<Charakter>(filteredModel);
		characters.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		characters.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (selectedCharacter == null || characters.getSelectedValue() != selectedCharacter.getCharacter()) {
					if (selectedCharacter != null) {
						westernPanel.remove(selectedCharacter);
					}
					selectedCharacter = new CharacterShortView(characters.getSelectedValue());
					westernPanel.add(selectedCharacter, "1 , 1, center, top");
					westernPanel.validate();
					selectedCharacter.setTransferHandler(new MyCharacterTransferHandler());
					ToleranceMouseListener tml = new ToleranceMouseListener() {
						public void mouseDragged(MouseEvent e) {
							super.mouseDragged(e);
							if (!e.isConsumed()) {
								TransferHandler handle = selectedCharacter.getTransferHandler();
								handle.exportAsDrag(selectedCharacter, e, TransferHandler.LINK);
							}
						}
					};
					selectedCharacter.addMouseMotionListener(tml);
					selectedCharacter.addMouseListener(tml);
				}
			}
		});
		characters.setModel(filteredModel);
		
		for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
			character.addListener(this);
		}
		scrollPane.setViewportView(characters);
		characters.setCellRenderer(new MyListCellRenderer());        
		characters.setTransferHandler(new MyCharacterTransferHandler());
		
		ToleranceMouseListener tml = new ToleranceMouseListener() {
			private CharacterShortView initiallySelectedCharacter;
			
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				initiallySelectedCharacter = selectedCharacter;
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				super.mouseDragged(e);
				if (initiallySelectedCharacter != null && !e.isConsumed()) {
					characters.setAutoscrolls(false);
					TransferHandler handle = characters.getTransferHandler();
					handle.exportAsDrag(initiallySelectedCharacter, e, TransferHandler.LINK);
					characters.setAutoscrolls(true);
				}
			}
		};
		characters.addMouseListener(tml);
		characters.addMouseMotionListener(tml);
		
		//Buttons
		buttonPanel = new JPanel();
		buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		westernPanel.add(buttonPanel, "1, 5, fill, fill");
		buttonPanel.setLayout(new GridLayout(0, 1, 3, 5));
		
		cityButton = new JButton(TextUtil.t("ui.town"));
		cityButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Jasbro.getInstance().getGui().showTownScreen();
			}
		});
		if (Jasbro.getInstance().getData().getTime() == Time.NIGHT) {
			cityButton.setEnabled(false);
		}
		buttonPanel.add(cityButton);
		
		
		JButton unlockButton = new JButton(TextUtil.t("ui.unlocks"));
		unlockButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Jasbro.getInstance().getGui().showUnlockScreen();
			}
		});
		buttonPanel.add(unlockButton);
		
		
		JButton showStatsButton = new JButton(TextUtil.t("ui.showStats"));
		showStatsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Jasbro.getThreadpool().execute(new Runnable() {
					@Override
					public void run() {
						StatCollector statCollector = Jasbro.getInstance().getData().getStatCollector();
						statCollector.showStatScreen();
					}
				});
			}
		});
		buttonPanel.add(showStatsButton);
		if (!Jasbro.getInstance().getData().getStatCollector().getDailyData().isInitialized()) {
			showStatsButton.setEnabled(false);
		}
		
		
		lastMessageButton = new JButton(TextUtil.t("ui.showLastMessage"));
		buttonPanel.add(lastMessageButton);
		lastMessageButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Jasbro.getInstance().getGui().restoreLastMessage();
			}
		});
		
		JButton nextDayButton = new JButton(TextUtil.t("ui.nextDay"));
		nextDayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Jasbro.getThreadpool().execute(new Runnable() {
					@Override
					public void run() {
						Jasbro.getInstance().advanceDay();
					}
				});
			}
		});
		buttonPanel.add(nextDayButton);
		
		if (Jasbro.getInstance().getData().getDay() == 1) {
			nextDayButton.setEnabled(false);
		}
		
		nextShiftButton = new JButton(TextUtil.t("ui.nextShift"));
		nextShiftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Jasbro.getThreadpool().execute(new Runnable() {
					@Override
					public void run() {
						Jasbro.getInstance().advanceShift();
					}
				});
			}
		});
		buttonPanel.add(nextShiftButton);
		
		easternPanel = new JPanel();
		easternPanel.setBackground(Color.WHITE);
		add(easternPanel, "2, 1, fill, fill");
		easternPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("1dlu:grow"),},
				new RowSpec[] {
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("1dlu:grow"),}));
		
		areaPanel = new AreaPanel();
		easternPanel.add(areaPanel, "1, 2, fill, fill");
		
		topPanel = new JPanel();
		topPanel.setBackground(Color.WHITE);
		easternPanel.add(topPanel, "1, 1, fill, fill");
		topPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("1dlu:grow(4)"),
				ColumnSpec.decode("1dlu:grow(2)"),
				ColumnSpec.decode("1dlu:grow(1)"),
				ColumnSpec.decode("1dlu:grow(1)"),
				ColumnSpec.decode("1dlu:grow(1)"),
				ColumnSpec.decode("1dlu:grow(4)"),},
				new RowSpec[] {
				RowSpec.decode("15dlu:grow"),}));
		
		
		helpOperations = new JComboBox<String>();
		topPanel.add(helpOperations, "1, 1, center, fill");
		helpOperations.addItem("--- Assistance functions ---");
		final List<HelpOption> optionList = new ArrayList<HelpOption>();
		
		Time time = Jasbro.getInstance().getData().getTime();
		if (time != Time.MORNING) {
			helpOperations.addItem(HelpOption.COPYMORNINGSHIFT.getText());
			optionList.add(HelpOption.COPYMORNINGSHIFT);
		}
		if (time != Time.AFTERNOON) {
			helpOperations.addItem(HelpOption.COPYAFTERNOONSHIFT.getText());
			optionList.add(HelpOption.COPYAFTERNOONSHIFT);
		}
		if (time != Time.NIGHT) {
			helpOperations.addItem(HelpOption.COPYNIGHTSHIFT.getText());
			optionList.add(HelpOption.COPYNIGHTSHIFT);
		}
		helpOperations.addItem(HelpOption.REMOVEALL.getText());
		optionList.add(HelpOption.REMOVEALL);
		if (time != Time.MORNING) {
			helpOperations.addItem(HelpOption.COPYMORNINGSHIFTEVERYWHERE.getText());
			optionList.add(HelpOption.COPYMORNINGSHIFTEVERYWHERE);
		}
		if (time != Time.AFTERNOON) {
			helpOperations.addItem(HelpOption.COPYAFTERNOONSHIFTEVERYWHERE.getText());
			optionList.add(HelpOption.COPYAFTERNOONSHIFTEVERYWHERE);
		}
		if (time != Time.NIGHT) {
			helpOperations.addItem(HelpOption.COPYNIGHTSHIFTEVERYWHERE.getText());
			optionList.add(HelpOption.COPYNIGHTSHIFTEVERYWHERE);
		}
		helpOperations.addItem(HelpOption.REMOVEALLEVERYWHERE.getText());
		optionList.add(HelpOption.REMOVEALLEVERYWHERE);
		
		helpOperations.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (helpOperations.getSelectedIndex() > 0) {
					HelpOption selectedOption = optionList.get(helpOperations.getSelectedIndex()-1);
					new UserHelper().perform(areaPanel.getArea(), selectedOption);
					helpOperations.setSelectedIndex(0);
					try {
						Thread.sleep(50);
					} catch (InterruptedException e1) {
					}
					AreaInterface area = (AreaInterface)areaSelection.getSelectedItem();
					areaPanel.setArea(area);
					validate();
					repaint();
				}
			}
		});
		
		areaSelection = new JComboBox<AreaInterface>();
		topPanel.add(areaSelection, "2, 1, fill, fill");
		areaSelection.setBorder(new EmptyBorder(1, 0, 1, 0));
		areaSelection.setRenderer(new DefaultListCellRenderer() {
			@SuppressWarnings("rawtypes")
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean hasFocus) {
				JLabel label = (JLabel) super.getListCellRendererComponent(
						list, value, index, isSelected, hasFocus);
				label.setText(((AreaInterface) value).getName());
				return label;
			}
		});
		
		
		//add elements
		final JButton manageHouseButton = new JButton(TextUtil.t("ui.manage"));
		manageHouseButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (areaPanel.getArea() instanceof House) {
					House house = (House) areaPanel.getArea();
					Jasbro.getInstance().getGui().showHouseScreen(house);
				}
			}
		});
		topPanel.add(manageHouseButton, "4, 1, fill, fill");
		
		final JButton questButton = new JButton(TextUtil.t("ui.quests"));
		questButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (areaPanel.getArea() instanceof House) {
					House house = (House) areaPanel.getArea();
					Jasbro.getInstance().getGui().showQuestsScreen();
				}
			}
		});
		topPanel.add(questButton, "5, 1, fill, fill");
		
		infoPanel = new HouseInfoPanel();
		topPanel.add(infoPanel, "6, 1");
		
		if (Jasbro.getInstance().getData() != null) {
			for (House house : Jasbro.getInstance().getData().getHouses()) {
				areaSelection.addItem(house);
			}
			areaSelection.addItem(new DivLocations());
			
			Unlocks unlocks = Jasbro.getInstance().getData().getUnlocks();
			if (unlocks.isUnlocked(LocationType.DUNGEON1) || unlocks.isUnlocked(LocationType.DUNGEON2) ||
					unlocks.isUnlocked(LocationType.DUNGEON3) || unlocks.isUnlocked(LocationType.DUNGEON4)) {
				areaSelection.addItem(new DungeonLocations());
			}
			
			if (lastSelectedLocation == null || !Jasbro.getInstance().getData().getHouses().contains(lastSelectedLocation)) {
				List<House> houses = Jasbro.getInstance().getData().getHouses();
				areaPanel.setArea(houses.get(0));
			} else {
				areaPanel.setArea(lastSelectedLocation);
				areaSelection.setSelectedItem(lastSelectedLocation);
			}
		}
		
		areaSelection.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					AreaInterface area = (AreaInterface)areaSelection.getSelectedItem();
					areaPanel.setArea(area);
					lastSelectedLocation = area;
					if (area instanceof House) {
						manageHouseButton.setVisible(true);
					}
					else {
						manageHouseButton.setVisible(false);
					}
					infoPanel.refresh(areaPanel.getArea());
					validate();
					repaint();
				}
			} 
		});
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				updateListModel();
				repaint();
			}
		});
		
		infoPanel.refresh(areaPanel.getArea());
		filteredModel.filter();
		characters.setSelectedIndex(0);
		validate();
		repaint();
	}
	
	public AreaPanel getHousePanel() {
		return areaPanel;
	}
	
	private class MyListCellRenderer extends DefaultListCellRenderer {
		
		private Map<Charakter, JPanel> panelMap = new HashMap<Charakter, JPanel>();
		
		@SuppressWarnings("rawtypes")
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean hasFocus) {
			Charakter character = (Charakter) value;
			
			JPanel panel;
			if (panelMap.containsKey(character)) {
				panel = panelMap.get(character);
				
				JLabel nameLabel = (JLabel)panel.getComponent(1);
				if (!character.getName().equals(nameLabel.getText())) {
					if(character.getUnspentPerkPoints()!=0){
						nameLabel.setText(character.getName()+" ("+character.getUnspentPerkPoints()+")");
					}
					else{
						nameLabel.setText(character.getName());
					}
				}
			}
			else {
				panel = new JPanel();
				panel.setLayout(new FormLayout(new ColumnSpec[] {
						ColumnSpec.decode("10dlu:none"),
						ColumnSpec.decode("pref:grow(4)"),
				},
				new RowSpec[] { RowSpec.decode("pref:grow") }));
				
				MyImage idleImage = new MyImage();
				idleImage.setOpaque(false);
				panel.add(idleImage, "1, 1, fill, fill");
				
				panel.setToolTipText(TextUtil.htmlPreformatted(character.getWarnString()));
				
				JLabel label = new JLabel();
				if(character.getUnspentPerkPoints()!=0){
					label.setText(character.getName()+" ("+character.getUnspentPerkPoints()+")");
				}
				else{
					label.setText(character.getName());
				}
				panel.add(label, "2, 1, fill, fill");
				
				panelMap.put(character, panel);
				
			}
			
			if (isSelected) {
				panel.setBackground(list.getSelectionBackground());
				panel.setForeground(list.getSelectionForeground());
			}
			else {
				panel.setBackground(list.getBackground());
				panel.setForeground(list.getForeground());
			}
			Border border = null;
			if (hasFocus) {
				if (isSelected) {
					border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
				}
				if (border == null) {
					border = UIManager.getBorder("List.focusCellHighlightBorder");
				}
			} else {
				border = new EmptyBorder(1, 1, 1, 1);
			}
			panel.setBorder(border);
			
			if (character.getWarnLevel() == Severity.DANGER || character.getWarnLevel() == Severity.WARN) {
				panel.getComponent(0).setVisible(true);
				((MyImage)panel.getComponent(0)).setImage(character.getWarnImage());
			}
			else {
				panel.getComponent(0).setVisible(false);
			}
			return panel;
		}
		
	}
	
	@Override
	public void handleEvent(MyEvent e) {
		characterListPanel.repaint();
	}
	
	public void updateListModel() {
		filteredModel.filter();
	}
	
	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		if (aFlag == true) {
			lastMessageButton.setEnabled(Jasbro.getInstance().getGui().hasPreviousMessages());
		}
	}
}