package jasbro.gui;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class MyPanel extends JPanel {
	public void update() {
	}
	
	public void addSingle(JComponent component) {
		setLayout(new GridLayout(1, 1));
		add(component);
		getPreferredSize().height = component.getPreferredSize().height;
	}
}