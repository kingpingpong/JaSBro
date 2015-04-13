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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class PreferencePanel extends JPanel {
    private JSpinner saveSlotsSpinner;
    private JCheckBox cheatModeCheckbox;
    private JCheckBox protagonistIsPlayerCheckbox;
    private JCheckBox showLoliImagesCheckbox;
    private JCheckBox showBlackAndWhiteImagesCheckbox;
    private JCheckBox showFutaOnMaleImagesCheckbox;
    private JCheckBox show3dImagesCheckbox;
    private JCheckBox showCosplayCheckbox;
    private JCheckBox showSleepCheckbox;
    private JLabel lblReloadItemData;
    private JButton btnReloadItems;
    private JLabel lblDevelopment;
    private JLabel lblImageFilter;
    private GuiPreferencePanel guiPreferencePanel;

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
        ConfigHandler.setShowCosplay(showCosplayCheckbox.isSelected());
        ConfigHandler.setShowCosplay(showCosplayCheckbox.isSelected());
        ConfigHandler.setShowSleep(showSleepCheckbox.isSelected());
        ConfigHandler.setHideArrowKeys(guiPreferencePanel.getHideArrowKeysCheckbox().isSelected());
        ConfigHandler.setProtagonistIsPlayer(protagonistIsPlayerCheckbox.isSelected());        
        ConfigHandler.setUseSystemLookAndFeel(guiPreferencePanel.getUseSystemLookAndFellCheckbox().isSelected());
        ConfigHandler.setShowNumbersOnBars(guiPreferencePanel.getShowNumbersOnBarsCheckbox().isSelected());
    }
}
