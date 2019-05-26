package jasbro.gui.pages;

import jasbro.Jasbro;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.game.housing.SecurityState;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.AdvertisingPanel;
import jasbro.gui.objects.div.EditRoomPanel;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.SecurityPanel;
import jasbro.gui.objects.div.TargetAudiencePanel;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.texts.TextUtil;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class HouseScreen extends MyImage {
	
	private List<JPanel> columnPanels = new ArrayList<JPanel>();
	
	/**
	 * Create the panel.
	 */
	public HouseScreen(final House house) {
		
		FormLayout layout = new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("3dlu:none"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("3dlu:none"),},
				new RowSpec[] {
				RowSpec.decode("3dlu:none"),
				RowSpec.decode("default:grow"),
				RowSpec.decode("3dlu:none"),});
		layout.setColumnGroups(new int[][]{ {2, 3, 5, 6}});
		setLayout(layout);
		
		setImage(house.getImage());
		setResize(true);
		
		//initiate column panels
		for (int i = 1; i < 6; i++) {
			JPanel panel = new JPanel();
			panel.setOpaque(false);
			panel.setBorder(GuiUtil.DEFAULTEMPTYBORDER);
			columnPanels.add(panel);
			panel.setLayout(new FormLayout(new ColumnSpec[] {
					ColumnSpec.decode("default:grow"),},
					new RowSpec[] {RowSpec.decode("min:grow(50)")}));
			add(panel, (i + 1) + ", 2, fill, fill");
		}
		
		//add elements
		final JTextField textField = new JTextField(house.getInternName());
		textField.getPreferredSize().width = 1;
		
		addToColumnPanel(textField, 0);
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				house.setInternName((textField.getText() + e.getKeyChar()).trim());
			}
		});
		
		JPanel infoPanel = new TranslucentPanel();
		infoPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("default:grow"),
				RowSpec.decode("default:grow"),}));
		infoPanel.setPreferredSize(null);
		JLabel label = new JLabel(TextUtil.t("type"));
		label.setBorder(new EmptyBorder(1, 1, 1, 1));
		infoPanel.add(label, "1,1");
		label.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
		JLabel label_1 = new JLabel(house.getHouseType().getText());
		label_1.setBorder(new EmptyBorder(1, 1, 1, 1));
		label_1.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
		infoPanel.add(label_1, "2, 1, right, default");
		
		label = new JLabel(TextUtil.t("fame"));
		label.setBorder(new EmptyBorder(1, 1, 1, 1));
		label.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
		infoPanel.add(label, "1,2");
		label_1 = new JLabel(house.getFame().getFameBuilding().getText());
		Object arguments[] = {house.getFame().getFame()};
		label_1.setToolTipText(TextUtil.t("formatted", arguments));
		label_1.setBorder(new EmptyBorder(1, 1, 1, 1));
		label_1.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
		infoPanel.add(label_1, "2, 2, right, default");
		
		
		for (Component component : infoPanel.getComponents()) {
			component.setFont(component.getFont().deriveFont(component.getFont().getSize() + 4f));
		}
		addToColumnPanel(infoPanel, 0);
		
		for (Room room : house.getRooms()) {
			addToColumnPanel(new EditRoomPanel(room), 0);
		}
		
		JButton btnClose = new JButton("Close");
		btnClose.getPreferredSize().width = 1;
		addToColumnPanel(btnClose, 0);
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Jasbro.getInstance().getGui().showHouseManagementScreen();
			}
		});
		
		addToColumnPanel(new TargetAudiencePanel(house), 4);
		
		addToColumnPanel(new AdvertisingPanel(house), 4);
		
		if(SecurityState.isImplemented())
			addToColumnPanel(new SecurityPanel(house), 4);
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				repaint();
			}
		});
		
		JPanel panel = new JPanel();//Todo find better workaround
		panel.setPreferredSize(new Dimension(10000, 10000));
		panel.setOpaque(false);
		addToColumnPanel(panel, 2);
		
		validate();
	}
	
	private void addToColumnPanel(Component component, int column) {
		if (column == 0 && columnPanels.get(column).getComponents().length > 12 && ! (component instanceof JButton)) {
			column = 1;
		}
		
		JPanel panel = columnPanels.get(column);
		FormLayout layout = (FormLayout) panel.getLayout();
		int row = layout.getRowCount();
		layout.insertRow(row, RowSpec.decode("default:grow"));
		layout.insertRow(row+1, RowSpec.decode("2dlu:none")); //Spaces
		panel.add(component, "1,"+(row)+", fill, top");
	}
	
}