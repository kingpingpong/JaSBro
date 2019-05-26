package jasbro.gui.objects.menus;

import jasbro.Jasbro;
import jasbro.gui.GuiUtil;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

@SuppressWarnings("serial")
public class PreferencePanel extends JPanel {
	private JSpinner saveSlotsSpinner;
	private JCheckBox cheatModeCheckbox;
	private JCheckBox protagonistIsPlayerCheckbox;
	private JCheckBox showLoliImagesCheckbox;
	private JCheckBox showBlackAndWhiteImagesCheckbox;
	private JCheckBox showFutaOnMaleImagesCheckbox;
	private JCheckBox show3dImagesCheckbox;
	private JCheckBox showCosplayCheckbox;
	private JCheckBox showTimeTakenCheckbox;
	private JCheckBox showSleepCheckbox;
	private JCheckBox newTownsceenCheckbox;
	private JLabel lblReloadItemData;
	private JButton btnReloadItems;
	private JLabel lblDevelopment;
	private JLabel lblImageFilter;
	private GuiPreferencePanel guiPreferencePanel;
	private JSpinner treeSpinner;
	private String resolutionWidth = "" + ConfigHandler.getSetting(Settings.RESOLUTIONWIDTH, 100);
	private String resolutionHeight = "" + ConfigHandler.getSetting(Settings.RESOLUTIONHEIGHT, 100);
	
	
	public PreferencePanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("10dlu"),
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("10dlu"),
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		guiPreferencePanel = new GuiPreferencePanel();
		add(guiPreferencePanel, "1, 2, 2, 1, fill, fill");
		
		JLabel protagonistIsPlayerLabel = new JLabel("Address protagonist as 'you'");
		add(protagonistIsPlayerLabel, "1, 3");
		
		protagonistIsPlayerCheckbox = new JCheckBox("", ConfigHandler.isProtagonistPlayer());
		add(protagonistIsPlayerCheckbox, "2, 3");
		
		JLabel cheatModeLabel = new JLabel(TextUtil.t("ui.preferences.cheatmode"));
		cheatModeLabel.setToolTipText(TextUtil.t("ui.preferences.cheatmode.tooltip"));
		add(cheatModeLabel, "1, 4, left, center");
		
		cheatModeCheckbox = new JCheckBox("", ConfigHandler.isCheat());
		add(cheatModeCheckbox, "2, 4, left, top");
		
		JLabel saveSlotLabel = new JLabel(TextUtil.t("ui.preferences.saveslots"));
		saveSlotLabel.setToolTipText(TextUtil.t("ui.preferences.saveslots.tooltip"));
		add(saveSlotLabel, "1, 5, left, center");
		
		saveSlotsSpinner = new JSpinner(new SpinnerNumberModel(ConfigHandler.getSetting(Settings.SAVESLOTS, 3), 1, Short.MAX_VALUE, 1));
		add(saveSlotsSpinner, "2, 5, left, default");
		
		lblImageFilter = new JLabel("Image filter");
		add(lblImageFilter, "1, 7");
		lblImageFilter.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
		
		JLabel showBlackAndWhiteImagesLabel = new JLabel(TextUtil.t("ui.preferences.showBlackAndWhiteImages"));
		add(showBlackAndWhiteImagesLabel, "1, 8, left, center");
		
		showBlackAndWhiteImagesCheckbox = new JCheckBox("", ConfigHandler.isShowBlackAndWhiteImages());
		add(showBlackAndWhiteImagesCheckbox, "2, 8, left, top");
		
		JLabel showLoliImagesLabel = new JLabel(TextUtil.t("ui.preferences.showLoliImages"));
		add(showLoliImagesLabel, "1, 9, left, center");
		
		showLoliImagesCheckbox = new JCheckBox("", ConfigHandler.isShowLoliImages());
		add(showLoliImagesCheckbox, "2, 9, left, top");
		
		JLabel showFutaOnMaleImagesLabel = new JLabel(TextUtil.t("ui.preferences.showFutaOnMaleImages"));
		add(showFutaOnMaleImagesLabel, "1, 10, left, center");
		
		showFutaOnMaleImagesCheckbox = new JCheckBox("", ConfigHandler.isShowFutaOnMaleImages());
		add(showFutaOnMaleImagesCheckbox, "2, 10, left, top");
		
		JLabel show3dImagesLabel = new JLabel(TextUtil.t("ui.preferences.show3dImages"));
		add(show3dImagesLabel, "1, 11, left, center");
		
		show3dImagesCheckbox = new JCheckBox("", ConfigHandler.isShow3dImages());
		add(show3dImagesCheckbox, "2, 11, left, top");
		
		JLabel showCosplayLabel = new JLabel(TextUtil.t("ui.preferences.showCosplayImages"));
		add(showCosplayLabel, "1, 12, left, center");
		
		showCosplayCheckbox = new JCheckBox("", ConfigHandler.isShowCosplay());
		add(showCosplayCheckbox, "2, 12, left, top");
		
		JLabel showSleepLabel = new JLabel(TextUtil.t("ui.preferences.showSleep"));
		add(showSleepLabel, "1, 13, left, center");
		
		showSleepCheckbox = new JCheckBox("", ConfigHandler.isShowSleep());
		add(showSleepCheckbox, "2, 13, left, top");
		
		lblDevelopment = new JLabel("Development");
		add(lblDevelopment, "1, 14");
		lblDevelopment.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
		
		lblReloadItemData = new JLabel("Reload items, quests, events, npcs (for testing purposes)");
		add(lblReloadItemData, "1, 15");
		
		btnReloadItems = new JButton("Reload All foreign files");
		btnReloadItems.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Jasbro.getInstance().setItems(null);
				Jasbro.getInstance().getData().getUnlocks().init();
				Jasbro.getInstance().setCustomQuestTemplates(null);
				Jasbro.getInstance().setWorldEvents(null);
				Jasbro.getInstance().getData().getQuestManager().setInactiveQuests(null);
				Jasbro.getInstance().setEnemyTemplates(null);
			}
		});
		add(btnReloadItems, "2, 15");
		
		JLabel treeLabel = new JLabel(TextUtil.t("ui.preferences.trees"));
		treeLabel.setToolTipText(TextUtil.t("ui.preferences.trees.tooltip"));
		add(treeLabel, "1, 16, left, center");
		
		treeSpinner = new JSpinner(new SpinnerNumberModel(ConfigHandler.getSetting(Settings.TREES, 100), 1, Short.MAX_VALUE, 1));
		add(treeSpinner, "2, 16, left, default");
		
		treeSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Jasbro.maxTrees = (int) ((JSpinner) e.getSource()).getValue();
			}
		});
		
		JLabel resolutionLabel = new JLabel(TextUtil.t("ui.preferences.resolution"));
		resolutionLabel.setToolTipText(TextUtil.t("ui.preferences.resolution.tooltip"));
		add(resolutionLabel, "1, 17, left, center");
		
		String[] resolutionChoice = { "1280x720", "1366x768", "1600x900", "1920x1080", "2560x1440"};
		final Integer[] resHeight = {720, 768, 900, 1080, 1440};
		final Integer[] resWidth = {1280, 1366, 1600, 1920, 2560};
		final JComboBox<String> resolutionCB = new JComboBox<String>(resolutionChoice);
		int index = 0;
		if(resolutionWidth.equals("1280")) index = 0;
		if(resolutionWidth.equals("1366")) index = 1;
		if(resolutionWidth.equals("1600")) index = 2;
		if(resolutionWidth.equals("1920")) index = 3;
		if(resolutionWidth.equals("2560")) index = 4;
		resolutionCB.setSelectedIndex(index);
		
		resolutionWidth = resWidth[index].toString();
        resolutionHeight = resHeight[index].toString();
		
		add(resolutionCB, "2, 17, left, default");
		
		
		
		resolutionCB.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        int res = resolutionCB.getSelectedIndex();
		        resolutionWidth = resWidth[res].toString();
		        resolutionHeight = resHeight[res].toString();
		    }
		});
		
		JLabel newTownscreenLabel = new JLabel(TextUtil.t("ui.preferences.showTownscreenNew"));
		add(newTownscreenLabel, "1, 18, left, center");
		
		newTownsceenCheckbox = new JCheckBox("", ConfigHandler.isTownScreenNew());
		add(newTownsceenCheckbox, "2, 18, left, top");
		
		JLabel showTimeTakenLabel = new JLabel(TextUtil.t("ui.preferences.showTimeTaken"));
		add(showTimeTakenLabel, "1, 19, left, center");
		
		showTimeTakenCheckbox = new JCheckBox("", ConfigHandler.isShowTimeTaken());
		add(showSleepCheckbox, "2, 19, left, top");
		
	}
	
	public void applyChanges() throws FileNotFoundException, IOException {
		if (ConfigHandler.changeSetting(Settings.SAVESLOTS, saveSlotsSpinner.getValue().toString())) {
			Jasbro.getInstance().getGui().getMainMenuBar().rebuildSaveMenu();
		}
		ConfigHandler.setCheat(cheatModeCheckbox.isSelected());
		ConfigHandler.setShowBlackAndWhiteImages(showBlackAndWhiteImagesCheckbox.isSelected());
		ConfigHandler.setShowLoliImages(showLoliImagesCheckbox.isSelected());
		ConfigHandler.setShowFutaOnMaleImages(showFutaOnMaleImagesCheckbox.isSelected());
		ConfigHandler.setShow3DImages(show3dImagesCheckbox.isSelected());
		ConfigHandler.setTownScreenNew(newTownsceenCheckbox.isSelected());
		ConfigHandler.setShowCosplay(showCosplayCheckbox.isSelected());
		ConfigHandler.setShowCosplay(showCosplayCheckbox.isSelected());
		ConfigHandler.setShowSleep(showSleepCheckbox.isSelected());
		ConfigHandler.setShowTimeTaken(showTimeTakenCheckbox.isSelected());
		ConfigHandler.setHideArrowKeys(guiPreferencePanel.getHideArrowKeysCheckbox().isSelected());
		ConfigHandler.setProtagonistIsPlayer(protagonistIsPlayerCheckbox.isSelected());        
		ConfigHandler.setUseSystemLookAndFeel(guiPreferencePanel.getUseSystemLookAndFellCheckbox().isSelected());
		ConfigHandler.setShowNumbersOnBars(guiPreferencePanel.getShowNumbersOnBarsCheckbox().isSelected());
		ConfigHandler.changeSetting(Settings.TREES, treeSpinner.getValue().toString());
		ConfigHandler.changeResolution(Settings.RESOLUTIONHEIGHT, resolutionHeight);
		ConfigHandler.changeResolution(Settings.RESOLUTIONWIDTH, resolutionWidth);
		Jasbro.getInstance().getGui().changeResolution(new Integer(resolutionWidth), new Integer(resolutionHeight));
	}
}