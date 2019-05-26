package jasbro.util.eventEditor.effectPanels;

import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventChangeMoney;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EventEffectChangeMoneyPanel extends JPanel {
	
	private WorldEventChangeMoney worldEventEffect;
	
	public EventEffectChangeMoneyPanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		this.worldEventEffect = (WorldEventChangeMoney)worldEventEffectTmp; 
		
		
		final JSpinner spinner = new JSpinner(new SpinnerNumberModel(0L, 0L,
				Long.MAX_VALUE, 1L));
		spinner.setValue(worldEventEffect.getMoneyModifier());
		add(spinner, "1, 1, fill, top");
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				double tmp = (Double)spinner.getValue();
				worldEventEffect.setMoneyModifier((long) tmp);
			}
		});
	}
	
}