package jasbro.gui.objects.div;

import jasbro.game.events.business.BuildingAdvertising;
import jasbro.game.housing.House;
import jasbro.gui.GuiUtil;
import jasbro.texts.TextUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.border.Border;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class AdvertisingPanel extends TranslucentPanel {
	
	public AdvertisingPanel(House house) {
		setPreferredSize(null);
		setMinimumSize(null);
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblNewLabel = new JLabel(TextUtil.t("advertisement"));
		lblNewLabel.setFont(GuiUtil.DEFAULTLARGEBOLDFONT);
		lblNewLabel.setToolTipText(TextUtil.html(TextUtil.t("advertisement.info")));
		add(lblNewLabel, "1, 1");
		
		final BuildingAdvertising advertising = house.getAdvertising();
		Border defaultCheckboxBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);
		
		final JCheckBox flyerCheckbox = new JCheckBox(TextUtil.t("advertisement.flyers"));
		flyerCheckbox.setBorder(defaultCheckboxBorder);
		flyerCheckbox.setOpaque(false);
		flyerCheckbox.setFont(GuiUtil.DEFAULTBOLDFONT);
		flyerCheckbox.setSelected(advertising.isFlyers());
		flyerCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				advertising.setFlyers(flyerCheckbox.isSelected());
			}
		});
		add(flyerCheckbox, "1, 2");
		
		final JCheckBox barkerCheckbox = new JCheckBox(TextUtil.t("advertisement.barkers"));
		barkerCheckbox.setBorder(defaultCheckboxBorder);
		barkerCheckbox.setOpaque(false);
		barkerCheckbox.setFont(GuiUtil.DEFAULTBOLDFONT);
		barkerCheckbox.setSelected(advertising.isBarkers());
		barkerCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				advertising.setBarkers(barkerCheckbox.isSelected());
			}
		});
		add(barkerCheckbox, "1, 3");
		
		final JCheckBox posterCheckbox = new JCheckBox(TextUtil.t("advertisement.posters"));
		posterCheckbox.setBorder(defaultCheckboxBorder);
		posterCheckbox.setOpaque(false);
		posterCheckbox.setFont(GuiUtil.DEFAULTBOLDFONT);
		posterCheckbox.setSelected(advertising.isPosters());
		posterCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				advertising.setPosters(posterCheckbox.isSelected());
			}
		});
		add(posterCheckbox, "1, 4");
		
		final JCheckBox newspaperCheckbox = new JCheckBox(TextUtil.t("advertisement.newspapers"));
		newspaperCheckbox.setBorder(defaultCheckboxBorder);
		newspaperCheckbox.setOpaque(false);
		newspaperCheckbox.setFont(GuiUtil.DEFAULTBOLDFONT);
		newspaperCheckbox.setSelected(advertising.isNewspapers());
		newspaperCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				advertising.setNewspapers(newspaperCheckbox.isSelected());
			}
		});
		add(newspaperCheckbox, "1, 5");
		
		final JCheckBox sponsorshipCheckbox = new JCheckBox(TextUtil.t("advertisement.sponsorship"));
		sponsorshipCheckbox.setBorder(defaultCheckboxBorder);
		sponsorshipCheckbox.setOpaque(false);
		sponsorshipCheckbox.setFont(GuiUtil.DEFAULTBOLDFONT);
		sponsorshipCheckbox.setSelected(advertising.isSponsorship());
		sponsorshipCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				advertising.setSponsorship(sponsorshipCheckbox.isSelected());
			}
		});
		add(sponsorshipCheckbox, "1, 6");
		
	}
	
}