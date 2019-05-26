package jasbro.gui.objects.div;

import jasbro.gui.GuiUtil;
import jasbro.gui.MyPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

public class TranslucentPanel extends MyPanel {
	
	public TranslucentPanel() {
		setBorder(GuiUtil.DEFAULTBORDER);
		setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
		setPreferredSize(new Dimension(1, 1));
	}
	
	@Override
	public boolean isOpaque() {
		return false;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(getBackground());
		Insets insets = getInsets();
		g.fillRect(insets.left, insets.top, getWidth() - insets.right, getHeight() - insets.bottom);
		super.paintComponent(g);
	}
}