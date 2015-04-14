/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jasbro.gui.pages.subView;
import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.events.MyEvent;
import jasbro.game.housing.Room;
import jasbro.game.housing.RoomSlot;
import jasbro.game.interfaces.MyEventListener;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.locations.LocationType;
import jasbro.gui.GuiUtil;
import jasbro.gui.dnd.CanReceiveCharacterDrop;
import jasbro.gui.dnd.MyCharacterTransferHandler;
import jasbro.gui.dnd.ToleranceMouseListener;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 *
 * @author Azrael
 */
public class LocationPanel extends MyImage implements MyEventListener, CanReceiveCharacterDrop {

	private CharacterLocation characterLocation;
	private JPanel characterPanel;
	private JList<ActivityDetails> activitySelection;
	private JPanel activityPanel;
	private JLabel locationStatusLabel;
	private JPanel locationInfoPanel;
	private JComboBox<SelectionData<?>> selectionDataComboBox;
	private JScrollPane activityScrollPane;
	private ListSelectionListener myListener;

	/**
	 * Creates new form Room
	 */
	public LocationPanel() {
		setBorder(new LineBorder(new Color(139, 69, 19), 1, false));
		FormLayout formLayout = new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("1dlu:grow(10)"),
				ColumnSpec.decode("1dlu:grow(10)"),
				ColumnSpec.decode("1dlu:grow(10)"),
				ColumnSpec.decode("25dlu:grow"),},
				new RowSpec[] {
				RowSpec.decode("10dlu"),
				RowSpec.decode("1dlu:grow"),});
		setLayout(formLayout);

		locationStatusLabel = new LocationStatusLabel();
		locationStatusLabel.setOpaque(true);
		locationStatusLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		locationStatusLabel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(139, 69, 19)));
		locationStatusLabel.setBackground(new Color(255,255,204));
		add(locationStatusLabel, "1,1,3,1,fill,fill");

		locationInfoPanel = new JPanel();
		locationInfoPanel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(139, 69, 19)));
		locationInfoPanel.setBackground(new Color(255,255,204));
		add(locationInfoPanel, "4, 1, fill, fill");
		locationInfoPanel.setLayout(new GridLayout(1, 0, 2, 2));



		characterPanel = new JPanel();
		characterPanel.setOpaque(false);
		add(characterPanel, "1, 2, 3, 1, fill, fill");
		characterPanel.setLayout(new MigLayout("", "[center]", "[center]"));

		characterPanel.addHierarchyBoundsListener(new HierarchyBoundsAdapter() {
			@Override
			public void ancestorResized(HierarchyEvent e) {
				characterPanel.revalidate();
			}
		});

		activityPanel = new JPanel();
		activityPanel.setOpaque(false);
		activityPanel.setBackground(new Color(255, 255, 204));
		activityPanel.setBorder(new MatteBorder(0, 1, 0, 0, (Color) new Color(139, 69, 19)));
		add(activityPanel, "4, 2, fill, fill");
		activityPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("1px"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("1px"),},
				new RowSpec[] {
				RowSpec.decode("1px"),
				RowSpec.decode("top:default"),
				RowSpec.decode("2dlu:grow"),
				RowSpec.decode("top:10dlu"),
				RowSpec.decode("1px"),}));

		this.setBackground(new Color(1f,1,0.9f,0.5f));

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				redoLayout();
			}
		});
	}

	public LocationPanel(CharacterLocation characterLocation) {
		this();
		setCharacterLocation(characterLocation);
	}

	public CharacterLocation getCharacterLocation() {
		return characterLocation;
	}

	public void setCharacterLocation(CharacterLocation characterLocation) {
		this.characterLocation = characterLocation;
		characterLocation.addListener(this);
		init();
	}

	private void init() {
		characterPanel.removeAll();
		activityPanel.removeAll();
		if (characterLocation != null) {

			List<Charakter> characters = characterLocation.getCurrentUsage().getCharacters();

			int width = getWidth();
			if (width == 0) {
				width = Jasbro.getInstance().getGui().getWidth() / 5;
			}
			for (int i = 0; i < characters.size(); i++) {
				final Charakter character = characters.get(i);
				final CharacterShortView characterView = new CharacterShortView(character);


				if (characters.size() > 4 && i == 2) {
					characterPanel.add(characterView);
				}
				else {
					characterPanel.add(characterView);
				}

				characterView.setTransferHandler(new MyCharacterTransferHandler());

				ToleranceMouseListener tml = new ToleranceMouseListener() {
					public void mouseDragged(MouseEvent e) {
						super.mouseDragged(e);
						if (!e.isConsumed()) {
							TransferHandler handle = characterView.getTransferHandler();
							handle.exportAsDrag(characterView, e, TransferHandler.LINK);
						}
					}
				};
				characterView.addMouseMotionListener(tml);
				characterView.addMouseListener(tml);
			}



			activityScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			activityScrollPane.getVerticalScrollBar().setPreferredSize( new Dimension(0,-1) );  
			activityPanel.add(activityScrollPane, "2, 2");
			activityScrollPane.setOpaque(false);
			activityScrollPane.setBorder(null);
			activityScrollPane.getViewport().setOpaque(false);

			activitySelection = new JList<ActivityDetails>();
			activitySelection.setOpaque(false);
			activityScrollPane.setViewportView(activitySelection);
			activitySelection.setCellRenderer(new DefaultListCellRenderer() {
				@SuppressWarnings("rawtypes")
				public Component getListCellRendererComponent(JList list,
						Object value, int index, boolean isSelected,
						boolean hasFocus) {
					JLabel label = (JLabel) super.getListCellRendererComponent(
							list, value, index, isSelected, hasFocus);
					ActivityDetails details = (ActivityDetails) value;
					label.setText(details.getText());
					if (details.getDescription() != null) {
						label.setToolTipText(TextUtil.htmlPreformatted(details.getDescription()));
					}
					else {
						label.setToolTipText("");
					}
					label.setOpaque(true);
					label.setPreferredSize(label.getPreferredSize());
					if (!isSelected) {
						label.setBackground(new Color(255,255,204));
					}
					FontMetrics metrics = label.getFontMetrics(label.getFont());
					while (activitySelection.getWidth() != 0 && metrics.stringWidth(label.getText()) > activitySelection.getWidth()-4) {
						Font font = label.getFont().deriveFont(label.getFont().getSize() - 1.0f);
						label.setFont(font);
						metrics = label.getFontMetrics(label.getFont());
					}

					return label;
				}
			});
			activityScrollPane.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					activitySelection.setPreferredSize(null);
					Dimension preferredSize = activitySelection.getPreferredSize();
					preferredSize.width = activityScrollPane.getWidth()+5;
					activitySelection.setPreferredSize(preferredSize);
					repaint();
				}
			});

			myListener = new ListSelectionListener() {                
				@Override
				public void valueChanged(ListSelectionEvent e) {
					characterLocation.setSelectedActivityDetails(activitySelection.getSelectedValue());
					initSelectionComboBox();
				}
			};
			characterLocation.addListener(new MyEventListener() {
				private boolean updateInProgress = false;

				@Override
				public void handleEvent(MyEvent e) {
					if (!updateInProgress) {
						updateInProgress = true;
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								updateActivitySelectionList();
								repaint();
								updateInProgress = false;
							}
						});
					}
				}
			});
			updateActivitySelectionList();

			if (characterLocation instanceof RoomSlot && !((RoomSlot)characterLocation).isAvailable()) {
				setBackgroundImage(null);
				setImage(characterLocation.getImage());
			}
			else if (characterLocation == null || characterLocation instanceof RoomSlot) {
				setBackgroundImage(null);
			}
			else {
				setBackgroundImage(characterLocation.getImage());
			}
		}

		locationInfoPanel.removeAll();
		if (characterLocation.getDescription() != null) {
			MyImage infoImage = new MyImage(new ImageData("images/icons/info-pic.png"));
			infoImage.setToolTipText(TextUtil.htmlPreformatted(characterLocation.getDescription()));
			locationInfoPanel.add(infoImage);
		}

		setTransferHandler(new MyCharacterTransferHandler());

		initSelectionComboBox();

		redoLayout();
	}

	private void updateActivitySelectionList() {
		activitySelection.removeListSelectionListener(myListener);
		ActivityDetails selectedDetails = characterLocation.getSelectedActivityDetails();
		List<ActivityDetails> activityDetailList = characterLocation.getPossibleActivities();
		activitySelection.setListData(activityDetailList.toArray(new ActivityDetails[activityDetailList.size()]));
		activitySelection.setSelectedValue(selectedDetails, true);
		activitySelection.addListSelectionListener(myListener);
	}

	private void initSelectionComboBox() {
		List<SelectionData<?>> selectionDataList = characterLocation.getCurrentUsage().getType().getSelectionOptions(
				characterLocation.getCurrentUsage());
		if (selectionDataList != null) {
			if (selectionDataComboBox != null) {
				activityPanel.remove(selectionDataComboBox);
			}
			selectionDataComboBox = new JComboBox<SelectionData<?>>();
			activityPanel.add(selectionDataComboBox, "2, 4, fill, default");

			for (int i = 0; i < selectionDataComboBox.getComponentCount(); i++) 
			{
				if (selectionDataComboBox.getComponent(i) instanceof JComponent) {
					((JComponent) selectionDataComboBox.getComponent(i)).setBorder(GuiUtil.DEFAULTBORDER);
				}


				if (selectionDataComboBox.getComponent(i) instanceof AbstractButton) {
					((AbstractButton) selectionDataComboBox.getComponent(i)).setBorder(GuiUtil.DEFAULTBORDER);
				}
			}

			selectionDataComboBox.setRenderer(new DefaultListCellRenderer() {
				@SuppressWarnings("rawtypes")
				public Component getListCellRendererComponent(JList list,
						Object value, int index, boolean isSelected,
						boolean hasFocus) {
					JLabel label = (JLabel) super.getListCellRendererComponent(
							list, value, index, isSelected, hasFocus);
					SelectionData<?> selectionData = (SelectionData<?>) value;
					if (selectionData != null) {
						label.setText(selectionData.getShortText());
						if (selectionData.getTooltipText() != null) {
							label.setToolTipText(TextUtil.htmlPreformatted(selectionData.getButtonText() + "\n" + selectionData.getTooltipText()));
						}
						else {
							label.setToolTipText(TextUtil.htmlPreformatted(selectionData.getButtonText()));
						}
					}
					else {
						label.setText(" ");
					}
					FontMetrics metrics = label.getFontMetrics(label.getFont());
					while (label.getWidth() != 0 && metrics.stringWidth(label.getText()) > label.getWidth()) {
						Font font = label.getFont().deriveFont(label.getFont().getSize() - 1.0f);
						label.setFont(font);
						metrics = label.getFontMetrics(label.getFont());
					}                    
					return label;
				}
			});

			selectionDataComboBox.addItem(null);
			for (SelectionData<?> selectionData : selectionDataList) {
				selectionDataComboBox.addItem(selectionData);
			}
			selectionDataComboBox.addActionListener(new ActionListener() {                
				@Override
				public void actionPerformed(ActionEvent e) {
					SelectionData<?> selectionData = (SelectionData<?>)selectionDataComboBox.getSelectedItem();
					characterLocation.getCurrentUsage().setSelectedOption(selectionData);
					if (selectionData != null) {
						if (selectionData.getTooltipText() != null) {
							selectionDataComboBox.setToolTipText(TextUtil.htmlPreformatted(selectionData.getButtonText() + "\n" + selectionData.getTooltipText()));
						}
						else {
							selectionDataComboBox.setToolTipText(TextUtil.htmlPreformatted(selectionData.getButtonText()));
						}
					}
					else {
						selectionDataComboBox.setToolTipText("");
					}
				}
			});
			selectionDataComboBox.setSelectedItem(characterLocation.getCurrentUsage().getSelectedOption());
		}
		else {
			if (selectionDataComboBox != null) {
				activityPanel.remove(selectionDataComboBox);
			}
		}
		activityPanel.validate();
		activityPanel.repaint();
	}

	private void redoLayout() {
		int components = characterPanel.getComponents().length;

		if (getWidth() < 300 && components > 2) {
			if (components < 5) {
				characterPanel.setLayout(new MigLayout("insets 1", "[center]1", "[center]1"));

			}
			else {
				characterPanel.setLayout(new MigLayout("insets 1, wrap 3", "[center]1", "[center]1"));
			}                	
		}
		else {
			if (components < 5 || characterPanel.getWidth() > components * 80) {
				characterPanel.setLayout(new MigLayout("", "[center]", "[center]"));
			}
			else {
				characterPanel.setLayout(new MigLayout("wrap 3", "[center]", "[center]"));
			}
		}        
		characterPanel.validate();
		characterPanel.repaint();
	}

	@Override
	public synchronized void handleEvent(MyEvent e) {
		if (characterLocation.getCurrentUsage().getCharacters().size() != characterPanel.getComponents().length) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					init();
					validate();
					repaint();
				}
			});
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		if (getBackgroundImage() != null) {
			super.paintComponent(g);
		}
		else {
			g.setColor(getBackground());
			Insets insets = getInsets();
			g.fillRect(insets.left, insets.top, getWidth() - insets.right, getHeight() - insets.bottom);
			super.paintComponent(g);
		}
	}

	private class LocationStatusLabel extends JLabel {

		@Override
		protected void paintComponent(Graphics g) {
			if (characterLocation != null) {
				if(characterLocation.getLocationType()!=LocationType.BEACH
						&& characterLocation.getLocationType()!=LocationType.RESTAURANT
						&& characterLocation.getLocationType()!=LocationType.LIBRARY
						&& characterLocation.getLocationType()!=LocationType.STREETS
						&& characterLocation.getLocationType()!=LocationType.DUNGEON1
						&& characterLocation.getLocationType()!=LocationType.DUNGEON2
						&& characterLocation.getLocationType()!=LocationType.DUNGEON3
						&& characterLocation.getLocationType()!=LocationType.DUNGEON4){
					RoomSlot room = (RoomSlot) characterLocation;
					if(room.getDownTime()>0){
						setText(characterLocation.getName() + "   " + characterLocation.getAmountPeople() + " / " +characterLocation.getMaxPeople() + " Availables in :" + room.getDownTime()+ " days");} 

					else{
						setText(characterLocation.getName() + "   " + characterLocation.getAmountPeople() + " / " +characterLocation.getMaxPeople() + " ");}

				}
				else{setText(characterLocation.getName() + "   " + characterLocation.getAmountPeople() + " / " +characterLocation.getMaxPeople() + " ");}
			}
			super.paintComponent(g);
		}
	}
}
