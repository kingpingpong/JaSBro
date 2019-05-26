package jasbro.gui.perks;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.traits.PerkHandler;
import jasbro.game.character.traits.SkillTree;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.MyEventListener;
import jasbro.gui.GuiUtil;
import jasbro.gui.RPGView;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class PerksPanel extends MyImage implements MyEventListener {
	private List<SkillTreeItemPanel> skillTreeItems = new ArrayList<SkillTreeItemPanel>();
	private SkillTreeItemPanel selectedSkillTree;
	private SkillTreePanel skillTreePanel;
	private Charakter character;
	private JLabel perkPointLabel;
	
	public PerksPanel(Charakter characterTmp) {
		this.character = characterTmp;
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("100dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("default:grow"),
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		setBackgroundImage(new ImageData("images/icons/perks/Old_Scroll_Texture_II_by_Isthar_art.jpg"));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setOpaque(false);
		scrollPane.setBorder(null);
		add(scrollPane, "1, 1, fill, fill");
		scrollPane.getViewport().setOpaque(false);
		
		JPanel skillTreeSelectionPanel = new JPanel();
		skillTreeSelectionPanel.setOpaque(false);
		scrollPane.setViewportView(skillTreeSelectionPanel);
		FormLayout layout = new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("default:none")
		});
		skillTreeSelectionPanel.setLayout(layout);
		
		MouseListener ml = new MyMouseListener();
		List<SkillTree> skillTrees = character.getSkillTrees();
		for (int i = 0; i < skillTrees.size(); i++) {
			SkillTree skillTree = skillTrees.get(i);
			SkillTreeItemPanel skillTreeItemPanel = new SkillTreeItemPanel(skillTree);
			layout.appendRow(RowSpec.decode("default:none"));
			skillTreeSelectionPanel.add(skillTreeItemPanel, "1,"+ (i+1));
			skillTreeItemPanel.addMouseListener(ml);
			skillTreeItems.add(skillTreeItemPanel);
			if (i == 0) {
				skillTreeItemPanel.setSelected(true);
				selectedSkillTree = skillTreeItemPanel;
			}
		}
		
		scrollPane = new JScrollPane();
		scrollPane.setOpaque(false);
		scrollPane.setBorder(null);
		add(scrollPane, "3, 1, 1, 5, fill, fill");
		scrollPane.getViewport().setOpaque(false);
		
		skillTreePanel = new SkillTreePanel();
		scrollPane.setViewportView(skillTreePanel);
		
		
		Object arguments[] = {character.getUnspentPerkPoints()};
		final MyImage perkPointImage = new MyImage();
		perkPointImage.setBackgroundImage(new ImageData("images/icons/perks/button75892304.png"));
		perkPointImage.setPreferredSize(new Dimension(-1, 40));
		perkPointLabel = new JLabel(TextUtil.t("ui.perkPointsRemaining", arguments));
		perkPointLabel.setFont(GuiUtil.DEFAULTHEADERFONT);
		perkPointLabel.setHorizontalAlignment(SwingConstants.CENTER);
		perkPointImage.setLayout(new GridLayout(1, 1));
		perkPointImage.add(perkPointLabel);
		
		add(perkPointImage, "1, 3, fill, fill");
		if (selectedSkillTree != null) {
			skillTreePanel.initSkillTree(selectedSkillTree.getSkillTree(), character);
		}
		character.addListener(this);
		
		Object arguments2[] = {100000};
		JButton btnResetPerks = new JButton(TextUtil.t("ui.resetPerks", arguments2));
		add(btnResetPerks, "1, 5");
		btnResetPerks.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog (Jasbro.getInstance().getGui(), TextUtil.t("ui.confirmResetPerks"), TextUtil.t("ui.confirmResetPerks.title"), JOptionPane.OK_CANCEL_OPTION) == 
						JOptionPane.OK_OPTION) {
					Jasbro.getInstance().getData().spendMoney(100000, "Reset perks");
					PerkHandler.resetPerks(character);
					skillTreePanel.initSkillTree(selectedSkillTree.getSkillTree(), character);
				}
			}
		});
		
		validate();
	}
	
	@Override
	public Dimension getPreferredSize() {
		RPGView view = Jasbro.getInstance().getGui();
		if (view != null) {
			int heightGui = view.getHeight();
			int widthGui = view.getWidth();
			return new Dimension(widthGui * 3 / 4, heightGui * 3 / 4);
		}
		else {
			return super.getPreferredSize();
		}
	}
	
	private class MyMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			handleClick(e);
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			handleClick(e);
		}
		
		public void handleClick(MouseEvent e) {
			for (SkillTreeItemPanel skillTreeItem : skillTreeItems) {
				skillTreeItem.setSelected(false);
			}
			selectedSkillTree = (SkillTreeItemPanel) e.getSource();
			selectedSkillTree.setSelected(true);
			skillTreePanel.initSkillTree(selectedSkillTree.getSkillTree(), character);
		}
	}
	
	@Override
	public void handleEvent(MyEvent e) {
		if (e.getType() == EventType.STATUSCHANGE) {
			Object arguments[] = {PerksPanel.this.character.getUnspentPerkPoints()};
			perkPointLabel.setText(TextUtil.t("ui.perkPointsRemaining", arguments));
			validate();
			repaint();
		}
	}
}