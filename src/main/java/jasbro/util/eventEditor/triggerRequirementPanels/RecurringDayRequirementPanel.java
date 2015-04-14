package jasbro.util.eventEditor.triggerRequirementPanels;

import jasbro.game.world.customContent.requirements.RecurringDayRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.texts.TextUtil;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class RecurringDayRequirementPanel extends JPanel {
	private RecurringDayRequirement triggerRequirement;

	public RecurringDayRequirementPanel(TriggerRequirement triggerRequirementTmp) {
		this.triggerRequirement = (RecurringDayRequirement)triggerRequirementTmp;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		add(new JLabel(TextUtil.t("eventEditor.each")));
		
        final JSpinner spinner = new JSpinner();
        spinner.setValue(triggerRequirement.getEveryXDays());
        add(spinner);
        spinner.addChangeListener(new ChangeListener() {                
            @Override
            public void stateChanged(ChangeEvent e) {
                triggerRequirement.setEveryXDays((int)spinner.getValue());
            }
        });
        
        add(new JLabel(TextUtil.t("eventEditor.daysOffset")));
        
        final JSpinner spinner2 = new JSpinner();
        spinner2.setValue(triggerRequirement.getOffset());
        add(spinner2);
        spinner2.addChangeListener(new ChangeListener() {                
            @Override
            public void stateChanged(ChangeEvent e) {
                triggerRequirement.setOffset((int)spinner2.getValue());
            }
        });
	}
}
