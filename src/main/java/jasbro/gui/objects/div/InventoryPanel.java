package jasbro.gui.objects.div;

import jasbro.game.items.Inventory;
import jasbro.game.items.Inventory.ItemData;
import jasbro.game.items.Item;
import jasbro.game.items.UsableItem;
import jasbro.gui.GuiUtil;
import jasbro.texts.TextUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolTip;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class InventoryPanel extends TranslucentPanel {
	private Inventory inventory;
	private JList<ItemData> itemList;
	private boolean imageOnly;
	private boolean resized = false;
	private Item displayedItem;
	
	@SuppressWarnings("unused") //Only for windowmaker plugin
	private InventoryPanel() {
		this(null, false);
	}
	
	public InventoryPanel(Inventory inventory) {
		this(inventory, false);
	}
	
	public InventoryPanel(Inventory inventory, boolean imageOnlyTmp) {
		this.imageOnly = imageOnlyTmp;
		this.inventory = inventory;
		setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		
		itemList = new JList<ItemData>() {
			@Override
			public Point getToolTipLocation(MouseEvent event) {
				if (!imageOnly) {
					return super.getToolTipLocation(event);
				}
				else {
					return new Point(event.getComponent().getX() - 600, 
							event.getComponent().getY() - 100);
				}
			}
			
			@Override
			public JToolTip createToolTip() {
				JToolTip tooltip = super.createToolTip();
				if (displayedItem != null && displayedItem instanceof UsableItem) {
					tooltip.setBackground(new Color(24, 219, 92));
				}
				return tooltip;
			}
			
			@Override
			public int locationToIndex(Point location) {
				int index = super.locationToIndex(location);
				if (index != -1 && !getCellBounds(index, index).contains(location)) {
					return -1;
				}
				else {
					return index;
				}
			}
		};
		itemList.setOpaque(false);
		
		if (imageOnly) {
			itemList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			itemList.setVisibleRowCount(-1);
		}
		
		scrollPane.setViewportView(itemList);
		itemList.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				ListModel<ItemData> m = itemList.getModel();
				int index = itemList.locationToIndex(e.getPoint());
				if( index>-1 ) {
					Item item = m.getElementAt(index).getItem();
					displayedItem = item;
					if (item.getDescription() != null) {
						itemList.setToolTipText(TextUtil.htmlItem((item)));
					}
					else {
						itemList.setToolTipText("");
					}
				}
				else {
					itemList.setToolTipText("");
				}
			}
		});
		
		itemList.setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, 
					boolean cellHasFocus) {
				if (!imageOnly) {
					Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					((JComponent) component).setOpaque(false);
					
					ItemData itemData = (ItemData) value;
					JPanel panel = new JPanel();
					panel.setLayout(new FormLayout(
							new ColumnSpec[] { 
									ColumnSpec.decode("15dlu:none"), 
									ColumnSpec.decode("pref:grow(4)"), },
									new RowSpec[] { 
									RowSpec.decode("15dlu:none") }));
					
					panel.setOpaque(false);
					panel.add(new MyImage(itemData.getItem().getIcon()), "1, 1, fill, fill");
					panel.add(component, "2, 1, fill, fill");
					return panel;
				}
				else {
					ItemData itemData = (ItemData) value;
					MyImage image = new MyImage(itemData.getItem().getIcon());
					if (itemData.getAmount() > 1) {
						image.setLayout(new FormLayout(
								new ColumnSpec[] { 
										ColumnSpec.decode("default:grow"), 
										ColumnSpec.decode("default:grow"), },
										new RowSpec[] { 
										RowSpec.decode("default:grow"),
										RowSpec.decode("default:grow") }));
						JLabel amountLabel = new JLabel(""+itemData.getAmount());
						amountLabel.setFont(GuiUtil.DEFAULTLARGEBOLDFONT);
						amountLabel.setBackground(Color.WHITE);
						amountLabel.setOpaque(true);
						image.add(amountLabel, "2, 2, right, bottom");
					}
					
					Border border = null;
					if (cellHasFocus) {
						if (isSelected) {
							border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
						}
						if (border == null) {
							border = UIManager.getBorder("List.focusCellHighlightBorder");
						}
					} else {
						border = new EmptyBorder(1, 1, 1, 1);
					}
					image.setBorder(border);
					return image;
				}
			}
		});
		
		itemList.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				if (imageOnly && !resized) {
					int width = itemList.getWidth() - 10;
					itemList.setFixedCellHeight(width / 4);
					itemList.setFixedCellWidth(width / 4);
					resized = true;
				}
			};
		});
		
		
		updateView();
	}
	
	public void updateView() {
		resized = false;
		Item selectedItem = null;
		if (itemList.getSelectedValue() != null) {
			selectedItem = itemList.getSelectedValue().getItem();
		}
		ItemData itemArray[] = new ItemData[inventory.getItems().size()];
		itemList.setListData(inventory.getItems().toArray(itemArray));
		if (selectedItem != null) {
			for (ItemData itemData : itemArray) {
				if (itemData.getItem().equals(selectedItem)) {
					itemList.setSelectedValue(itemData, true);
					break;
				}
			}
		}
		repaint();
	}
	
	public Item getSelectedItem() {
		if (itemList.getSelectedValue() != null) {
			return ((ItemData)itemList.getSelectedValue()).getItem();
		}
		else {
			return null;
		}
	}
	
	public JList<ItemData> getItemList() {
		return itemList;
	}
	
	public void addListSelectionListener(ListSelectionListener listener) {
		itemList.addListSelectionListener(listener);
	}
}