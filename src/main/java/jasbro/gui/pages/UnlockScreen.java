package jasbro.gui.pages;

import jasbro.Jasbro;
import jasbro.game.interfaces.UnlockObject;
import jasbro.game.world.Unlocks.FameUnlock;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.UnlockObjectDisplay;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class UnlockScreen extends MyImage {
	public UnlockScreen() {
		setBackgroundImage(new ImageData("images/backgrounds/sky.jpg"));
		
		JScrollPane unlockScrollPane = new JScrollPane();
		unlockScrollPane.setOpaque(false);
		unlockScrollPane.getViewport().setOpaque(false);
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel hintLabel = new JLabel(TextUtil.t("ui.unlockHint", new Object[] {Jasbro.getInstance().getData().getProtagonist().getFame().getFame()}));
		hintLabel.setFont(GuiUtil.DEFAULTLARGEBOLDFONT);
		add(hintLabel, "1, 2, center, center");
		add(unlockScrollPane, "1, 3, fill, fill");
		
		JPanel unlockPanel = new JPanel();
		unlockPanel.setOpaque(false);
		unlockScrollPane.setViewportView(unlockPanel);
		FormLayout layout = new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {});
		unlockPanel.setLayout(layout);
		
		JPanel closeButtonPanel = new JPanel();
		add(closeButtonPanel, "1, 4, fill, fill");
		closeButtonPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JButton closeButton = new JButton(TextUtil.t("ui.close"));
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Jasbro.getInstance().getGui().showHouseManagementScreen();
			}
		});
		closeButtonPanel.add(closeButton, "2, 2");
		closeButtonPanel.setOpaque(false);
		
		List<FameUnlock> fameUnlockObjects = Jasbro.getInstance().getData().getUnlocks().getFameUnlockObjects();
		int i = 0;
		for (; i < fameUnlockObjects.size(); i++) {
			FameUnlock fameUnlock = fameUnlockObjects.get(i);
			UnlockObjectDisplay panel = new UnlockObjectDisplay(fameUnlock);
			if (i%2 == 0) {
				layout.appendRow( RowSpec.decode("80dlu"));
			}
			unlockPanel.add(panel, (i%3+1) + ", " + (i/3+1) + ", fill, fill");
		}
		
		List<UnlockObject> unlocks = Jasbro.getInstance().getData().getUnlocks().getUnlockedObjectsInternal();
		for (UnlockObject unlockObject : unlocks) {
			UnlockObjectDisplay panel = new UnlockObjectDisplay(unlockObject);
			if (i%2 == 0) {
				layout.appendRow( RowSpec.decode("80dlu"));
			}
			unlockPanel.add(panel, (i%3+1) + ", " + (i/3+1) + ", fill, fill");
			i++;
		}
	}
	
}