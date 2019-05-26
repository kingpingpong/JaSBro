package jasbro.gui.perks;

import jasbro.game.character.traits.SkillTree;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.MyImage;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class SkillTreeItemPanel extends MyImage {
	
	private SkillTree skillTree;
	
	public SkillTreeItemPanel(SkillTree skillTree) {
		this.skillTree = skillTree;
		setOpaque(false);
		setImage(skillTree.getIcon());
		setPreferredSize(new Dimension(-1, 150));
		setBackground(GuiUtil.TRANSPARENTCOLOR);
		setCentered(true);
		setToolTipText(skillTree.getText());
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				setInsetX(getWidth() / 12);
				setInsetY(getHeight() / 12);
				repaint();
			}
		});
		
	}
	
	public void setSelected(boolean selected) {
		if (selected) {
			setBackground(GuiUtil.SELECTEDTRANSPARENTCOLOR);
		}
		else {
			setBackground(GuiUtil.TRANSPARENTCOLOR);
		}
		repaint();
	}
	
	
	@Override
	public boolean isOpaque() {
		return false;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(getBackground());
		Insets insets = getInsets();
		g.fillRect(insets.left, insets.top, getWidth() - insets.right, getHeight() - insets.bottom);
		super.paintComponent(g);
	}
	
	public SkillTree getSkillTree() {
		return skillTree;
	}
	
	
}