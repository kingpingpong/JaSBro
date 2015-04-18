package jasbro.gui.objects.menus;

import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class GuiPreferencePanel extends JPanel {

    private JCheckBox hideArrowKeysCheckbox;
    private JCheckBox useSystemLookAndFellCheckbox;
    private JCheckBox showNumbersOnBarsCheckbox;

    public GuiPreferencePanel() {
        setLayout(new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("default:grow"),
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                FormFactory.UNRELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.UNRELATED_GAP_ROWSPEC,}));
        
        JLabel useSystemLFLabel = new JLabel(TextUtil.htmlPreformatted("Use System Look and Feel\n(Requires restarting the game)"));
        add(useSystemLFLabel, "1, 2");

        useSystemLookAndFellCheckbox = new JCheckBox("", ConfigHandler.isUseSystemLookAndFeel());
        add(useSystemLookAndFellCheckbox, "2, 2");
        
        JLabel showNumbersOnBarsLabel = new JLabel("Show Numbers on Attribute bars");
        add(showNumbersOnBarsLabel, "1, 3");

        showNumbersOnBarsCheckbox = new JCheckBox("", ConfigHandler.isShowNumbersOnBars());
        add(showNumbersOnBarsCheckbox, "2, 3");

        JLabel hideArrowKeysLabel = new JLabel("Character Screen: Hide navigation arrows");
        add(hideArrowKeysLabel, "1, 4");

        hideArrowKeysCheckbox = new JCheckBox("", ConfigHandler.isHideArrowKeys());
        add(hideArrowKeysCheckbox, "2, 4");
    }

    public JCheckBox getHideArrowKeysCheckbox() {
        return hideArrowKeysCheckbox;
    }

    public JCheckBox getUseSystemLookAndFellCheckbox() {
        return useSystemLookAndFellCheckbox;
    }

    public JCheckBox getShowNumbersOnBarsCheckbox() {
        return showNumbersOnBarsCheckbox;
    }
    
    
}
