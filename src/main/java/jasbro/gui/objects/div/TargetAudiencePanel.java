package jasbro.gui.objects.div;

import jasbro.game.character.Gender;
import jasbro.game.housing.House;
import jasbro.gui.GuiUtil;
import jasbro.texts.TextUtil;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class TargetAudiencePanel extends TranslucentPanel {
	private House house;
	private JSlider maleAudienceSlider;
	private JSlider femaleAudienceSlider;
	private JSlider futaAudienceSlider;
	private boolean valueUpdating;
	
	public TargetAudiencePanel(House house) {
		setPreferredSize(null);
		
		this.house = house;
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblNewLabel = new JLabel(TextUtil.t("ui.targetAudience"));
		lblNewLabel.setFont(GuiUtil.DEFAULTBOLDFONT);
		add(lblNewLabel, "2, 2");
		
		JLabel lblNewLabel_1 = new JLabel(TextUtil.t("ui.percentMale"));
		add(lblNewLabel_1, "2, 3");
		
		maleAudienceSlider = new JSlider();
		maleAudienceSlider.setOpaque(false);
		maleAudienceSlider.setMinimumSize(new Dimension(1, 1));
		maleAudienceSlider.setMinimum(0);
		maleAudienceSlider.setMaximum(100);
		maleAudienceSlider.setMajorTickSpacing(10);
		maleAudienceSlider.setPaintTicks(true);
		maleAudienceSlider.setPaintLabels(true);
		maleAudienceSlider.setPaintTrack(true);
		maleAudienceSlider.setSnapToTicks(true);
		maleAudienceSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (!valueUpdating) {
					if (maleAudienceSlider.getValue() + 7 <= TargetAudiencePanel.this.house.getSpawnData().getPercentMale()) {
						TargetAudiencePanel.this.house.getSpawnData().decreaseChance(Gender.MALE);
					}
					else if (maleAudienceSlider.getValue() - 7 >= TargetAudiencePanel.this.house.getSpawnData().getPercentMale()) {
						TargetAudiencePanel.this.house.getSpawnData().increaseChance(Gender.MALE);
					}
					update();
				}
			}
		});
		add(maleAudienceSlider, "2, 4, fill, fill");
		
		JLabel lblNewLabel_2 = new JLabel(TextUtil.t("ui.percentFemale"));
		add(lblNewLabel_2, "2, 5");
		
		femaleAudienceSlider = new JSlider();
		femaleAudienceSlider.setOpaque(false);
		femaleAudienceSlider.setMinimumSize(new Dimension(1, 1));
		femaleAudienceSlider.setMinimum(0);
		femaleAudienceSlider.setMaximum(100);
		femaleAudienceSlider.setMajorTickSpacing(10);
		femaleAudienceSlider.setPaintTicks(true);
		femaleAudienceSlider.setPaintLabels(true);
		femaleAudienceSlider.setPaintTrack(true);
		femaleAudienceSlider.setSnapToTicks(true);
		femaleAudienceSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (!valueUpdating) {
					if (femaleAudienceSlider.getValue() + 7 <= TargetAudiencePanel.this.house.getSpawnData().getPercentFemale()) {
						TargetAudiencePanel.this.house.getSpawnData().decreaseChance(Gender.FEMALE);
					}
					else if (femaleAudienceSlider.getValue() - 7 >= TargetAudiencePanel.this.house.getSpawnData().getPercentFemale()) {
						TargetAudiencePanel.this.house.getSpawnData().increaseChance(Gender.FEMALE);
					}
					update();
				}
			}
		});
		add(femaleAudienceSlider, "2, 6");
		
		JLabel lblNewLabel_3 = new JLabel(TextUtil.t("ui.percentFuta"));
		add(lblNewLabel_3, "2, 7");
		
		futaAudienceSlider = new JSlider();
		futaAudienceSlider.setOpaque(false);
		futaAudienceSlider.setMinimumSize(new Dimension(1, 1));
		futaAudienceSlider.setMinimum(0);
		futaAudienceSlider.setMaximum(100);
		futaAudienceSlider.setMajorTickSpacing(10);
		futaAudienceSlider.setPaintTicks(true);
		futaAudienceSlider.setPaintLabels(true);
		futaAudienceSlider.setPaintTrack(true);
		futaAudienceSlider.setSnapToTicks(true);
		futaAudienceSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (!valueUpdating) {
					if (futaAudienceSlider.getValue() + 7 <= TargetAudiencePanel.this.house.getSpawnData().getPercentFuta()) {
						TargetAudiencePanel.this.house.getSpawnData().decreaseChance(Gender.FUTA);
					}
					else if (futaAudienceSlider.getValue() - 7 >= TargetAudiencePanel.this.house.getSpawnData().getPercentFuta()) {
						TargetAudiencePanel.this.house.getSpawnData().increaseChance(Gender.FUTA);
					}
					update();
				}
			}
		});
		add(futaAudienceSlider, "2, 8");
		update();
	}
	
	public void update() {
		valueUpdating = true;
		maleAudienceSlider.setValue(house.getSpawnData().getPercentMale());
		femaleAudienceSlider.setValue(house.getSpawnData().getPercentFemale());
		futaAudienceSlider.setValue(house.getSpawnData().getPercentFuta());
		valueUpdating = false;
		repaint();
	}
	
}