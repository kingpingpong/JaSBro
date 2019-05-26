package jasbro.util.eventEditor.triggerRequirementPanels;

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

import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.gui.GuiUtil;

public class TriggerRequirementPanel extends JPanel {
	private final static Logger log = LogManager.getLogger(TriggerRequirementPanel.class);
	private TriggerRequirement triggerRequirement;
	private JPanel contentPanel;
	private JPanel subRequirementsPanel;
	private boolean selected = false;
	
	
	public TriggerRequirementPanel(TriggerRequirement triggerRequirementTmp, MouseListener mouseListener) {
		this.triggerRequirement = triggerRequirementTmp;
		addMouseListener(mouseListener);
		setBackground(Color.GRAY);
		setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.PREF_COLSPEC,
				FormFactory.PREF_COLSPEC,
				FormFactory.PREF_COLSPEC,
				ColumnSpec.decode("1dlu"),},
				new RowSpec[] {
				RowSpec.decode("1dlu"),
				RowSpec.decode("pref:grow"),
				RowSpec.decode("1dlu"),}));
		
		JLabel lblNewLabel = new JLabel(triggerRequirementTmp.getType().getText());
		add(lblNewLabel, "1, 2");
		
		
		if (triggerRequirement != null) {
			try {
				Class<? extends JPanel> panelClass = triggerRequirement.getType().getEventPanelClass();
				if (panelClass != null) {
					contentPanel = panelClass.getConstructor(TriggerRequirement.class).newInstance(triggerRequirement);
				} else {
					contentPanel = new JPanel();
				}
				contentPanel.setOpaque(false);
				contentPanel.addMouseListener(GuiUtil.DELEGATEMOUSELISTENER);
				add(contentPanel, "2, 2, fill, fill");
				
			} catch (Exception ex) {
				log.error("Error when creating sub panel", ex);
			}
		}
		
		
		subRequirementsPanel = new JPanel();
		add(subRequirementsPanel, "3, 2, fill, fill");
		subRequirementsPanel.setLayout(new BoxLayout(subRequirementsPanel, BoxLayout.Y_AXIS));
		
		for (TriggerRequirement requirement : triggerRequirement.getSubRequirements()) {
			TriggerRequirementPanel triggerRequirementPanel = new TriggerRequirementPanel(requirement, mouseListener);
			subRequirementsPanel.add(triggerRequirementPanel);
			triggerRequirementPanel.addMouseListener(mouseListener);
		}
	}
	
	public void addPanel(TriggerRequirementPanel triggerRequirementPanel) {
		subRequirementsPanel.add(triggerRequirementPanel);
	}
	
	public void removePanel(TriggerRequirementPanel triggerRequirementPanel) {
		subRequirementsPanel.remove(triggerRequirementPanel);
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
	
	public boolean isSelected() {
		return selected;
	}
	
	public JPanel getSubRequirementsPanel() {
		return subRequirementsPanel;
	}
	
	public TriggerRequirement getTriggerRequirement() {
		return triggerRequirement;
	}
	
	
}