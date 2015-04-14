package jasbro.util.itemEditor;

import jasbro.game.items.equipmentEffect.EquipmentEffect;
import jasbro.gui.DelegateMouseListener;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EquipmentEffectPanel extends JPanel {
	protected boolean selected = false;
	protected EquipmentEffect itemEffect;
	protected JPanel contentPanel;
	protected JPanel dataPanel;
	
	public EquipmentEffectPanel() {
		setBackground(Color.DARK_GRAY);
		setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.UNRELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.UNRELATED_GAP_ROWSPEC,
				RowSpec.decode("default:none"),
				RowSpec.decode("default:grow"),
				FormFactory.UNRELATED_GAP_ROWSPEC,}));
		
		contentPanel = new JPanel();
		contentPanel.addMouseListener(new DelegateMouseListener());
		add(contentPanel, "2, 3, fill, fill");
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		if (selected) {
			setBackground(Color.BLUE);
		}
		else {
			setBackground(Color.DARK_GRAY);
		}
		repaint();
	}

	public EquipmentEffect getItemEffect() {
		return itemEffect;
	}

	public void setItemEffect(EquipmentEffect usableItemEffect) {
		this.itemEffect = usableItemEffect;		
		try {
			Class<? extends JPanel> panelClass = getItemEffect().getType().getItemEffectPanelClass();
			if (panelClass != null) {
				dataPanel = panelClass.getConstructor(EquipmentEffect.class).newInstance(getItemEffect());
			}
			else {
				dataPanel = new JPanel();
				dataPanel.add(new JLabel(usableItemEffect.getName()));
			}
			add(dataPanel, "2, 2, fill, fill");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
		add(dataPanel, "2, 2, fill, fill");
	}
	
	public void addPanel(EquipmentEffectPanel usableItemEffectPanel) {
		contentPanel.add(usableItemEffectPanel);
	}
	
	public void removePanel(EquipmentEffectPanel usableItemEffectPanel) {
		contentPanel.remove(usableItemEffectPanel);
	}
	
	public boolean hasChildEffects() {
		return contentPanel.getComponents().length > 0;
	}
	
	public JPanel getDataPanel() {
		return dataPanel;
	}

}
