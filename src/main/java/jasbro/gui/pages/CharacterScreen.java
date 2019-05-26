package jasbro.gui.pages;

import jasbro.Jasbro;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.Ownership;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.MyEventListener;
import jasbro.gui.GuiUtil;
import jasbro.gui.MyPanel;
import jasbro.gui.character.CharacterScreenCenterPanel;
import jasbro.gui.character.CharacterScreenInfoPanel;
import jasbro.gui.character.CharacterScreenInventoryPanel;
import jasbro.gui.character.CharacterScreenNamePanel;
import jasbro.gui.character.CharacterScreenOptionsPanel;
import jasbro.gui.character.SpecializationPanel;
import jasbro.gui.character.TraitPanel;
import jasbro.gui.objects.div.AllowedServicesPanel;
import jasbro.gui.objects.div.EquipmentSlotPanel;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class CharacterScreen extends MyImage implements MyEventListener {
	private Charakter character;
	private List<MyPanel> leftColumns = new ArrayList<MyPanel>();
	private List<MyPanel> rightColumns = new ArrayList<MyPanel>();
	
	@SuppressWarnings("unused") //for window editor plugin only
	private CharacterScreen() {
		this(new Charakter(null));
	}
	
	/**
	 * Create the panel.
	 */
	public CharacterScreen(final Charakter character) {
		this.character = character;
		
		setBackgroundImage(character.getBackground());
		setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character));
		
		//add elements
		leftColumns.add(new CharacterScreenNamePanel(character));
		leftColumns.add(new CharacterScreenInfoPanel(character));
		
		if (!character.getType().isChildType()) {
			leftColumns.add(new TraitPanel(character));
		}
		
		for (SpecializationType specializationType : character.getSpecializations()) {
			leftColumns.add(new SpecializationPanel(specializationType, this.character));
		}
		
		MyPanel closePanel = new MyPanel();
		JButton btnClose = new JButton("Close");
		closePanel.addSingle(btnClose);
		leftColumns.add(closePanel);
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Jasbro.getInstance().getGui().removeLayer(CharacterScreen.this);
			}
		});
		
		if (!character.getType().isChildType() && character.getOwnership() == Ownership.OWNED &&
				(character.getGender() != Gender.MALE || character.getTraits().contains(Trait.INHUMANPREGNANCY))) {
			rightColumns.add(new CharacterScreenOptionsPanel(character));
		}
		
		if (!character.getType().isChildType() && Jasbro.getInstance().getData().getCharacters().contains(character)) {
			rightColumns.add(new AllowedServicesPanel(character));
		}
		
		if (character.getType() == CharacterType.TRAINER && character != Jasbro.getInstance().getData().getProtagonist()
				&& Jasbro.getInstance().getData().getTrainers().contains(character)) {
			
			MyPanel firePanel = new MyPanel();
			firePanel.setPreferredSize(null);
			JButton fireButton = new JButton(TextUtil.t("ui.fire"));
			firePanel.addSingle(fireButton);
			rightColumns.add(firePanel);
			fireButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					int confirm = JOptionPane.showConfirmDialog(CharacterScreen.this,
							TextUtil.t("ui.firetrainer", character),
							"fire",
							JOptionPane.YES_NO_OPTION);
					if (confirm == 0) {
						new MessageData(TextUtil.t("ui.trainerfired", character), 
								ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, character), character.getBackground())
						.createMessageScreen();
						Jasbro.getInstance().getGui().removeLayer(CharacterScreen.this);
						Jasbro.getInstance().removeCharacter(character);
					}
				}
			});
		}
		
		if (Jasbro.getInstance().getData().getCharacters().contains(character)) {
			rightColumns.add(new EquipmentSlotPanel(character, this));
			rightColumns.add(new CharacterScreenInventoryPanel(character, this));
		}
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				setInsetX(getWidth() / 8);
				redoLayout();
			}
		});
		
		
		character.addListener(this);
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				update();
			}
		});
	}
	
	public void redoLayout() {
		removeAll();
		invalidate();
		
		FormLayout layout = new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("3dlu:none"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow(20)"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("3dlu:none"),},
				new RowSpec[] {
				RowSpec.decode("3dlu:none"),
				RowSpec.decode("default:grow"),
				RowSpec.decode("3dlu:none"),});
		layout.setColumnGroups(new int[][]{ {2, 3, 5, 6}});
		
		setLayout(layout);
		
		List<JPanel> columnPanels = new ArrayList<JPanel>();
		//initiate column panels
		for (int i = 1; i < 6; i++) {
			JPanel panel = new JPanel();
			panel.setOpaque(false);
			panel.setBorder(GuiUtil.DEFAULTEMPTYBORDER);
			columnPanels.add(panel);
			panel.setLayout(new FormLayout(new ColumnSpec[] {
					ColumnSpec.decode("default:grow"),},
					new RowSpec[] {RowSpec.decode("default:grow(200)")}));
			add(panel, (i + 1) + ", 2, fill, fill");
		}
		
		validate();
		
		if (columnPanels.get(0).getHeight() == 0) {
			System.gc();
			return; // this character screen is actually no longer in use
		}
		
		int columnSize = 5;
		int column = 0;
		for (JComponent component : leftColumns) {
			JPanel columnPanel = columnPanels.get(column);        	
			FormLayout columnLayout = (FormLayout) columnPanel.getLayout();
			
			if (columnLayout.preferredLayoutSize(CharacterScreen.this).height + component.getPreferredSize().height > 
			columnPanel.getHeight()) {
				column++;
				if (column == 2) {
					column++;
				}
				columnPanel = columnPanels.get(column);
				columnLayout = (FormLayout) columnPanel.getLayout();
			}
			
			if (component instanceof JButton && ((FormLayout)columnPanels.get(0).getLayout()).
					preferredLayoutSize(CharacterScreen.this).height + component.getPreferredSize().height <= 
					columnPanels.get(0).getHeight()) {
				addToColumnPanelTop(component, columnPanels.get(0));
			}
			else {
				addToColumnPanelTop(component, columnPanel);
			}
		}
		
		column = columnSize-1;
		for (JComponent component : rightColumns) {
			JPanel columnPanel = columnPanels.get(column);
			FormLayout columnLayout = (FormLayout) columnPanel.getLayout();
			
			if (columnLayout.minimumLayoutSize(CharacterScreen.this).height + component.getMinimumSize().height > 
			columnPanel.getHeight()) {
				column--;
				columnPanel = columnPanels.get(column);
				columnLayout = (FormLayout) columnPanel.getLayout();
			}
			if (column == columnSize-1) {
				addToColumnPanelTop(component, columnPanel);
			}
			else {
				addToColumnPanelBottom(component, columnPanel);
			}
		}
		
		addToColumnPanelBottom(new CharacterScreenCenterPanel(character, this), columnPanels.get(columnSize / 2));
		
		requestFocus();
		validate();
		repaint();
	}
	
	private void addToColumnPanelTop(Component component, JPanel panel) {
		FormLayout layout = (FormLayout) panel.getLayout();
		int row = layout.getRowCount();
		if (component instanceof CharacterScreenInventoryPanel) {
			layout.insertRow(row, RowSpec.decode("default:grow(999999)"));
			panel.add(component, "1," + (row) + ", fill, fill");
		}
		else {
			layout.insertRow(row, RowSpec.decode("default:grow"));
			layout.insertRow(row + 1, RowSpec.decode("2dlu:none")); // Spaces
			panel.add(component, "1," + (row) + ", fill, top");
		}
	}
	
	private void addToColumnPanelBottom(Component component, JPanel panel) {
		FormLayout layout = (FormLayout) panel.getLayout();
		int row = layout.getRowCount();
		layout.appendRow(RowSpec.decode("default:grow"));
		layout.appendRow(RowSpec.decode("2dlu:none")); // Spaces
		panel.add(component, "1," + (row + 1) + ", fill, top");
	}
	
	@Override
	public void handleEvent(MyEvent e) {
		if (e.getType() == EventType.STATUSCHANGE) {
			setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character));
			update();
		}
	}
	
	public void update() {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				for (MyPanel myPanel : leftColumns) {
					myPanel.update();
				}
				for (MyPanel myPanel : rightColumns) {
					myPanel.update();
				}
				redoLayout();
			}
		});
	}
}