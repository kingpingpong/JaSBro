package jasbro.util.eventEditor.effectPanels;

import java.awt.Color;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.gui.GuiUtil;

public class EventEffectPanel extends JPanel {
	private final static Logger log = LogManager.getLogger(EventEffectPanel.class);
	
	private boolean selected = false;
	private WorldEventEffect worldEventEffect;
	private JPanel subeffectPanel;
	private JPanel dataPanel;
	private WorldEvent worldEvent;
	
	public EventEffectPanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp, MouseListener mouseListener) {
		this.worldEventEffect = worldEventEffectTmp;
		this.worldEvent = worldEventTmp;
		setBackground(Color.GRAY);
		setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.UNRELATED_GAP_COLSPEC,},
				new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.UNRELATED_GAP_ROWSPEC,}));
		
		subeffectPanel = new JPanel();
		subeffectPanel.setOpaque(false);
		subeffectPanel.addMouseListener(GuiUtil.DELEGATEMOUSELISTENER);
		add(subeffectPanel, "2, 3, fill, fill");
		BoxLayout boxLayout = new BoxLayout(subeffectPanel, BoxLayout.Y_AXIS);
		subeffectPanel.setLayout(boxLayout);
		
		add(new JLabel(worldEventEffect.getType().getText() + " "), "2, 1");
		
		if (worldEventEffect != null) {
			try {
				Class<? extends JPanel> panelClass = worldEventEffect.getType().getEventPanelClass();
				if (panelClass != null) {
					dataPanel = panelClass.getConstructor(WorldEventEffect.class, WorldEvent.class).
							newInstance(getWorldEventEffect(), worldEvent);
				} else {
					dataPanel = new JPanel();
				}
				dataPanel.setOpaque(false);
				dataPanel.addMouseListener(GuiUtil.DELEGATEMOUSELISTENER);
				add(dataPanel, "2, 2, fill, fill");
				
			} catch (Exception ex) {
				log.error("Error when creating sub panel", ex);
			}
			
			addMouseListener(mouseListener);
			for (WorldEventEffect effect : worldEventEffect.getSubEffects()) {
				EventEffectPanel eventEffectPanel = new EventEffectPanel(effect, worldEvent, mouseListener);
				subeffectPanel.add(eventEffectPanel);
				eventEffectPanel.addMouseListener(mouseListener);
			}
		}
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
		if (selected) {
			setBackground(Color.BLUE);
		} else {
			setBackground(Color.GRAY);
		}
		repaint();
	}
	
	public WorldEventEffect getWorldEventEffect() {
		return worldEventEffect;
	}
	
	
	public void addPanel(EventEffectPanel eventEffectPanel) {
		subeffectPanel.add(eventEffectPanel);
	}
	
	public void removePanel(EventEffectPanel eventEffectPanel) {
		subeffectPanel.remove(eventEffectPanel);
	}
	
	public JPanel getSubeffectPanel() {
		return subeffectPanel;
	}
	
	public JPanel getDataPanel() {
		return dataPanel;
	}
	
}