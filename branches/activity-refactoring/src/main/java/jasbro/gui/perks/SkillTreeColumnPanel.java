package jasbro.gui.perks;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SkillTreeColumnPanel extends JPanel {
    private FormLayout layout;
    
    public SkillTreeColumnPanel() {
        setMinimumSize(new Dimension(120, -1));
        layout = new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("default:grow"),
                ColumnSpec.decode("default:grow"),
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                FormFactory.UNRELATED_GAP_ROWSPEC,});
        setLayout(layout);
        setOpaque(false);
    }
    
    public void addPerkItem(PerkItemPanel perkItemPanel) {
        layout.appendRow(RowSpec.decode("default:grow"));
        add(perkItemPanel, "2,"+ (layout.getRowCount() + ", center, center"));
        perkItemPanel.setPreferredSize(new Dimension(100, 100));
        validate();
    }
    
}
