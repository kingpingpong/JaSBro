package jasbro.gui.pages.subView;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import jasbro.Jasbro;
import jasbro.game.character.CharacterBase;
import jasbro.game.character.CharacterSpawner;
import jasbro.game.character.Charakter;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.housing.House;
import jasbro.game.housing.HouseType;
import jasbro.game.housing.HouseUtil;
import jasbro.game.housing.Room;
import jasbro.game.housing.RoomInfo;
import jasbro.game.housing.RoomInfoUtil;
import jasbro.game.items.Inventory;
import jasbro.game.items.Item;
import jasbro.game.world.locations.LocationType;
import jasbro.gui.objects.div.InventoryPanel;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;

public class CheatScreen extends MyImage {
	private final JComboBox<House> houseSelectBox;
	private final JPanel roomListPanel;
	private final Map<JComboBox<String>, Room> comboBoxToRoomMap = new HashMap<>();
	private Color backgroundColor = new Color(1.0f,1.0f,0.9f,0.8f);
	
	/**
	 * Create the panel.
	 */
	public CheatScreen() {
		setOpaque(false);
		setBackgroundImage(new ImageData("images/backgrounds/cheat.jpg"));
		
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
				Jasbro.getInstance().getGui().showTownScreen();
			}
		});
		
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("1dlu:grow(2)"),
				ColumnSpec.decode("1dlu:grow(4)"),
				ColumnSpec.decode("20dlu"),
				ColumnSpec.decode("1dlu:grow"),
				ColumnSpec.decode("20dlu"),
				ColumnSpec.decode("1dlu:grow(2)"),
				ColumnSpec.decode("20dlu"),
				ColumnSpec.decode("1dlu:grow(2)"),},
				new RowSpec[] {
				RowSpec.decode("30dlu"),
				RowSpec.decode("1dlu:grow"),}));
		
		addHierarchyBoundsListener(new HierarchyBoundsAdapter() {
			@Override
			public void ancestorResized(HierarchyEvent e) {
				revalidate();
			}
		});
		
		addMouseListener (new MouseAdapter(){
	        public void mouseClicked(MouseEvent e) {
	            if (SwingUtilities.isRightMouseButton(e)) {
	            	Jasbro.getInstance().getGui().showTownScreen();
	            }
	        }		
		});
		
		{
			JPanel panel = new JPanel();
			add(panel, "2, 1, fill, fill");
			panel.setBackground(backgroundColor);
			panel.setBorder(new LineBorder(new Color(139, 69, 19), 1, false));
			JLabel lblCheatMoney = new JLabel("Cheat Money");
			lblCheatMoney.setFont(new Font("Tahoma", Font.BOLD, 15));
			panel.add(lblCheatMoney);
			
			JButton cheatMoneyButton = new JButton("Get Money (1000000)");
			panel.add(cheatMoneyButton);
			cheatMoneyButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Jasbro.getInstance().getData().earnMoney(1000000, "Cheat");
					Jasbro.getInstance().getGui().repaint();
				}
			});
		}
		
		{
			JPanel cheatUnlockPanel = new JPanel();
			add(cheatUnlockPanel, "4, 1, fill, fill");
			cheatUnlockPanel.setBackground(backgroundColor);
			cheatUnlockPanel.setBorder(new LineBorder(new Color(139, 69, 19), 1, false));
			JLabel lblCheatUnlocks = new JLabel("Cheat Unlocks");
			lblCheatUnlocks.setFont(new Font("Tahoma", Font.BOLD, 15));
			cheatUnlockPanel.add(lblCheatUnlocks);
			
			JButton cheatUnlocksButton = new JButton("Unlock everything");
			cheatUnlockPanel.add(cheatUnlocksButton);
			cheatUnlocksButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					for (HouseType houseType : HouseType.values()) {
						Jasbro.getInstance().getData().getUnlocks().addUnlock(houseType);
					}
					for (RoomInfo roomInfo : RoomInfoUtil.getRoomInfos()) {
						Jasbro.getInstance().getData().getUnlocks().addUnlock(RoomInfoUtil.getRoomUnlock(roomInfo.getId()));
					}
					for (SpecializationType specializationType : SpecializationType.values()) {
						if (specializationType.getAssociatedSkillTree() != null) {
							Jasbro.getInstance().getData().getUnlocks().addUnlock(specializationType);
						}
					}
					for (LocationType locationType : LocationType.values()) {
						Jasbro.getInstance().getData().getUnlocks().addUnlock(locationType);
					}
				}
			});
		}
		
		
		JPanel girlPanel = new JPanel();
		girlPanel.setBackground(backgroundColor);
		girlPanel.setBorder(new LineBorder(new Color(139, 69, 19), 1, false));
		girlPanel.setOpaque(true);
		add(girlPanel, "2, 2, fill, fill");
		girlPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("1dlu:grow(2)"),
				ColumnSpec.decode("1dlu:grow(8)"),},
				new RowSpec[] {
				RowSpec.decode("20dlu"),
				RowSpec.decode("1dlu:grow"),
				RowSpec.decode("20dlu"),}));;
				
				JLabel lblCheatGirls = new JLabel("Cheat girls");
				lblCheatGirls.setFont(new Font("Tahoma", Font.BOLD, 15));
				girlPanel.add(lblCheatGirls, "1, 1, 2, 1, fill, fill");
				
				final MyImage girlImage = new MyImage();
				girlImage.setBackground(Color.WHITE);
				girlPanel.add(girlImage, "2, 2, fill, fill");
				
				final JList<CharacterBase> list = new JList<CharacterBase>();
				list.setBorder(new EmptyBorder(5, 5, 5, 5));
				list.addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (e.getValueIsAdjusting()) {
							ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, list.getSelectedValue().getImages());
							girlImage.setImage(image);
							repaint();
						}
					}
				});
				JScrollPane scrollPane = new JScrollPane(list);
				girlPanel.add(scrollPane, "1, 2, left, fill");
				
				list.setModel(new AbstractListModel<CharacterBase>() {
					@Override
					public int getSize() {
						return Jasbro.getInstance().getSlaveBases().size();
					}
					
					@Override
					public CharacterBase getElementAt(int index) {
						return Jasbro.getInstance().getSlaveBases().get(index);
					}
				}); 
				list.setSelectedIndex(0);
				
				JButton buyButton = new JButton("Get girl");
				buyButton.setMaximumSize(new Dimension(999, 999));
				buyButton.setMinimumSize(new Dimension(200, 35));
				buyButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Charakter character = CharacterSpawner.create(list.getSelectedValue());
						Jasbro.getInstance().getData().getCharacters().add(character);
						new MessageScreen("You bought " + character.getName(), ImageUtil.getInstance().getImageDataByTag(
								ImageTag.NAKED, character), character.getBackground());
						
					}
				});
				girlPanel.add(buyButton, "1, 3, 2, 1, center, center");
				
				final JPanel housePanel = new TranslucentPanel();
				housePanel.setBackground(backgroundColor);
				housePanel.setBorder(new LineBorder(new Color(139, 69, 19), 1, false));
				housePanel.setOpaque(true);
				add(housePanel, "4, 2, fill, fill");
				FormLayout housePanelLayout = new FormLayout(new ColumnSpec[] {
						ColumnSpec.decode("1dlu:grow"),},
						new RowSpec[] {
						RowSpec.decode("20dlu"),
						RowSpec.decode("fill:1dlu:grow"),});
				
				housePanel.setLayout(housePanelLayout);
				
				JLabel lblCheatHouse = new JLabel("Cheat House");
				lblCheatHouse.setFont(new Font("Tahoma", Font.BOLD, 15));
				housePanel.add(lblCheatHouse, "1, 1, center, default");
				
				final List<House> houses = Jasbro.getInstance().getData().getHouses();
				
				ActionListener myListener = new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						JCheckBox checkBox = (JCheckBox) e.getSource();
						if (checkBox.isSelected()) {
							HouseType type = HouseType.valueOf(checkBox.getActionCommand());
							if (type != null) {
								houses.add(HouseUtil.newHouse(type));
							}
						}
						else {
							if (houses.size() > 1) {
								HouseType type = HouseType.valueOf(checkBox.getActionCommand());
								for (int i = 0; i < houses.size(); i++) {
									House house = houses.get(i);
									if (type == house.getHouseType()) {
										house.empty();
										houses.remove(house);
										break;
									}
								}
							}
							else {
								checkBox.setSelected(true);
							}
						}
						fillHouseItems(houses);
					}
				};
				
				for (int i = 0; i < HouseType.values().length; i++) {
					HouseType type = HouseType.values()[i];
					JCheckBox chckbx = new JCheckBox(type.getText());
					chckbx.setActionCommand(type.toString());
					chckbx.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
					chckbx.setOpaque(false);
					housePanelLayout.insertRow(i+2, RowSpec.decode("default:none"));
					housePanel.add(chckbx, "1, "+(2+i));
					chckbx.addActionListener(myListener);
					for (House house : houses) {
						if (type == house.getHouseType()) {
							chckbx.setSelected(true);
							break;
						}
					}
				}
				
				
				
				JPanel roomPanel = new JPanel();
				roomPanel.setBorder(new LineBorder(new Color(139, 69, 19), 1, false));
				roomPanel.setBackground(backgroundColor);
				roomPanel.setOpaque(true);
				add(roomPanel, "6, 1, 1, 2, fill, fill");
				roomPanel.setLayout(new FormLayout(new ColumnSpec[] {
						ColumnSpec.decode("1dlu:grow"),},
						new RowSpec[] {
						RowSpec.decode("50dlu"),
						RowSpec.decode("fill:1dlu:grow"),
						RowSpec.decode("fill:1dlu:grow(5)"),}));
				
				JLabel lblCheatRooms = new JLabel("Cheat Rooms");
				lblCheatRooms.setFont(new Font("Tahoma", Font.BOLD, 15));
				roomPanel.add(lblCheatRooms, "1, 1, center, default");
				
				houseSelectBox = new JComboBox<House>();
				roomPanel.add(houseSelectBox, "1, 2, fill, top");
				fillHouseItems(houses);
				
				roomListPanel = new JPanel();
				roomListPanel.setOpaque(false);
				roomPanel.add(roomListPanel, "1, 3, fill, fill");
				
				final ItemListener roomListener = new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							House house = (House)houseSelectBox.getSelectedItem();
							Room room = comboBoxToRoomMap.get(e.getSource());
							Integer id = house.getRooms().indexOf(room);
							if (id != null && id != -1) {
								room.empty();
								String roomInfoId = (String)e.getItem();
								room =RoomInfoUtil.newRoom(roomInfoId);
								house.getRoomSlots().get(id).setRoom(room);
								room.setHouse(house);
					}
						}
					}
				};
				
				houseSelectBox.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						FormLayout fl = new FormLayout(new ColumnSpec[] {
								ColumnSpec.decode("pref:grow"),},
								new RowSpec[] {RowSpec.decode("pref:grow")});
						House house = (House)houseSelectBox.getSelectedItem();
						roomListPanel.removeAll();
						roomListPanel.setLayout(fl);
						comboBoxToRoomMap.clear();
						if (house != null) {
							for (int i = 0; i < house.getRooms().size(); i++) {
								Room room = house.getRooms().get(i);
								JComboBox<String> roomSelect = new JComboBox<>();
								fl.insertRow(i+1, RowSpec.decode("pref:none"));
								roomListPanel.add(roomSelect, "1,"+(i+1)+", fill, top");
								roomSelect.setBorder(new EmptyBorder(5, 5, 5, 5));
								roomSelect.setOpaque(false);
								comboBoxToRoomMap.put(roomSelect, room);
								for (RoomInfo roomInfo : RoomInfoUtil.getRoomInfos()) {
									roomSelect.addItem(roomInfo.getId());
									roomSelect.setSelectedItem(room.getRoomInfo().getId());
								}
								roomSelect.addItemListener(roomListener);
							}
						}
						roomListPanel.validate();
					}
				});
				
				
				TranslucentPanel itemPanel = new TranslucentPanel();
				add(itemPanel, "8, 1, 1, 2, fill, fill");
				itemPanel.setLayout(new FormLayout(new ColumnSpec[] {
						ColumnSpec.decode("1dlu:grow"),},
						new RowSpec[] {
						RowSpec.decode("50dlu"),
						RowSpec.decode("fill:1dlu:grow"),
						RowSpec.decode("50dlu"),}));
				
				JLabel lblCheatItems = new JLabel("Cheat items");
				lblCheatItems.setFont(new Font("Tahoma", Font.BOLD, 15));
				itemPanel.add(lblCheatItems, "1, 1");
				
				final JButton getItemButton = new JButton("Get item");
				Inventory inventory = new Inventory();
				for (Item item : Jasbro.getInstance().getItems().values()) {
					inventory.addItem(item);
				}
				final InventoryPanel itemSelectionPanel = new InventoryPanel(inventory);
				itemPanel.add(itemSelectionPanel, "1, 2, fill, fill");
				
				itemPanel.add(getItemButton, "1, 3, center, top");
				getItemButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Item item = itemSelectionPanel.getSelectedItem();
						if (item != null) {
							Jasbro.getInstance().getData().getInventory().addItem(item);
						}
					}
				});
				
				validate();
	}
	
	public void fillHouseItems(List<House> houses) {
		if (roomListPanel != null) {
			roomListPanel.removeAll();
		}
		houseSelectBox.removeAllItems();
		houseSelectBox.addItem(null);
		houseSelectBox.setSelectedIndex(0);
		for (House house : houses) {
			houseSelectBox.addItem(house);
		}
	}
	
}