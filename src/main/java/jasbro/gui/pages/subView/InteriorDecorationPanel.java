package jasbro.gui.pages.subView;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import jasbro.Jasbro;
import jasbro.game.housing.House;
import jasbro.game.housing.RoomInfo;
import jasbro.game.housing.RoomPlanning;
import jasbro.game.housing.RoomSlot;
import jasbro.gui.GuiUtil;
import jasbro.texts.TextUtil;

public class InteriorDecorationPanel extends JPanel {
	private Logger log = LogManager.getLogger(InteriorDecorationPanel.class);
	private RoomPlanning roomPlanning;
	private JLabel costLabel;
	
	public InteriorDecorationPanel() {
		setOpaque(false);
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("pref:grow"),
				ColumnSpec.decode("1dlu:grow(8)"),
				ColumnSpec.decode("pref:grow"),
				ColumnSpec.decode("1dlu:grow(8)"),
				ColumnSpec.decode("pref:grow"),},
			new RowSpec[] {
				RowSpec.decode("default:grow"),
				RowSpec.decode("default:grow(20)"),
				RowSpec.decode("default:grow"),}));
		
		JPanel housePanel = new JPanel();
		add(housePanel, "2, 2, fill, fill");
		housePanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				RowSpec.decode("default:grow"),
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),
				RowSpec.decode("default:grow"),
				RowSpec.decode("default:grow"),
				RowSpec.decode("default:grow(40)"),}));
		housePanel.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
		housePanel.setBorder(GuiUtil.DEFAULTBORDER);
		
		JLabel lblNewLabel = new JLabel(TextUtil.t("ui.interiordeco"));
		lblNewLabel.setFont(GuiUtil.DEFAULTBOLDFONT);
		housePanel.add(lblNewLabel, "1, 4");
		
		final JComboBox<House> houseSelectBox = new JComboBox<House>();
		housePanel.add(houseSelectBox, "1, 6, fill, default");
		houseSelectBox.addItem(null);
		houseSelectBox.setSelectedIndex(0);
		for (House house : Jasbro.getInstance().getData().getHouses()) {
			houseSelectBox.addItem(house);
		}
		
		final JPanel roomPanel = new JPanel();
		roomPanel.setOpaque(false);
		housePanel.add(roomPanel, "1, 8, fill, default");
		
		final ItemListener roomListener = new ItemListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void itemStateChanged(ItemEvent e) {
				try {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						JComboBox<RoomInfo> jComboBox = (JComboBox<RoomInfo>) e.getSource();
						int id = Integer.parseInt(jComboBox.getActionCommand());
						roomPlanning.getNewRooms().remove(id);
						RoomInfo roomInfo = (RoomInfo)jComboBox.getSelectedItem();
						roomPlanning.getNewRooms().add(id, roomInfo);
						jComboBox.setToolTipText(roomInfo.getDescription());
						updateCostLabel();
					}
				}
				catch (Exception ex) {
					log.error("Error", ex);
				}
			}
		};
		
		
		houseSelectBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				FormLayout fl = new FormLayout(new ColumnSpec[] {
						ColumnSpec.decode("default:grow"),},
						new RowSpec[] {RowSpec.decode("default:grow")});
				House house = (House)houseSelectBox.getSelectedItem();
				roomPanel.removeAll();
				roomPanel.setLayout(fl);
				roomPlanning = null;
				if (house != null) {
					roomPlanning = new RoomPlanning(house);
					int i = -1;
					for (RoomSlot roomSlot : house.getRoomSlots()) {
						i++;
						JComboBox<RoomInfo> roomSelect = new JComboBox<>();
						roomSelect.setRenderer(new DefaultListCellRenderer() {
							@SuppressWarnings("rawtypes")
							public Component getListCellRendererComponent(JList list,
									Object value, int index, boolean isSelected,
									boolean hasFocus) {
								JLabel label = (JLabel) super.getListCellRendererComponent(
										list, value, index, isSelected, hasFocus);
								RoomInfo roomInfo = (RoomInfo) value;
								label.setText(roomInfo.getText());
								label.setToolTipText(roomInfo.getDescription());
								label.setForeground(Color.BLACK);
								label.setOpaque(false);
								return label;
							}
						});
						fl.insertRow(i+1, RowSpec.decode("default:none"));
						roomPanel.add(roomSelect, "1,"+(i+1)+", fill, top");
						roomSelect.setBorder(new EmptyBorder(2, 2, 2, 2));
						roomSelect.setActionCommand(i+"");
						roomSelect.setOpaque(false);
						
						boolean actualRoomTypeAdded = false;
						for (RoomInfo roomInfo : Jasbro.getInstance().getData().getUnlocks().getAvailableRoomTypes()) {
							// FIXME glue code to keep things working for now
							if (roomInfo.fitsInSlot(roomSlot.getSlotType())) {
								roomSelect.addItem(roomInfo);
								if (roomInfo.getId().equals(roomSlot.getRoom().getRoomInfo().getId())) {
									roomSelect.setSelectedItem(roomInfo);
									roomSelect.setToolTipText(roomInfo.getDescription());
									actualRoomTypeAdded = true;
								}
							}
						}
						
						if (!actualRoomTypeAdded) {
							roomSelect.addItem(roomSlot.getRoom().getRoomInfo());
							roomSelect.setSelectedItem(roomSlot.getRoom().getRoomInfo());
							roomSelect.setToolTipText(roomSlot.getRoom().getRoomInfo().getDescription());
						}
						
						roomSelect.addItemListener(roomListener);
					}
				}
				roomPanel.validate();
				roomPanel.repaint();
			}
		});
		
		
		
		
		
		
		
		
		JPanel controlPanel = new JPanel();
		controlPanel.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
		controlPanel.setBorder(GuiUtil.DEFAULTBORDER);
		add(controlPanel, "4, 2, fill, fill");
		controlPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("20dlu"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("20dlu"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		costLabel = new JLabel("Cost changes: 0");
		controlPanel.add(costLabel, "1, 1");
		
		JButton btnReset = new JButton("Reset");
		controlPanel.add(btnReset, "1, 3");
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (roomPlanning != null) {
					roomPlanning.reset();
					int index = houseSelectBox.getSelectedIndex();
					houseSelectBox.setSelectedIndex(0);
					houseSelectBox.setSelectedIndex(index);
					updateCostLabel();
				}
			}
		});
		
		JButton btnBuyChanges = new JButton("Buy changes");
		controlPanel.add(btnBuyChanges, "1, 5");
		btnBuyChanges.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (roomPlanning != null && Jasbro.getInstance().getData().canAfford(roomPlanning.getCosts())) {
					roomPlanning.adoptRoomLayout();
					updateCostLabel();
				}
			}
		});
	}
	
	public void updateCostLabel() {
		if (roomPlanning != null) {
			costLabel.setText("Cost changes: " + roomPlanning.getCosts());
		}
		else {
			costLabel.setText("Cost changes: 0");
		}
		repaint();
	}
}