package jasbro.gui.objects.div;
import jasbro.Jasbro;
import jasbro.game.interfaces.UnlockObject;
import jasbro.game.world.Unlocks.FameUnlock;
import jasbro.gui.GuiUtil;
import jasbro.texts.TextUtil;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class UnlockObjectDisplay extends JPanel {
    private MyImage myImage;
    private UnlockObject unlockObject;
    private Long requiredFame;
    private TranslucentPanel contentPanel;
    private JLabel titleLabel;
    private JLabel fameLabel;
    
    public UnlockObjectDisplay() {
        init();
    }
    
    public UnlockObjectDisplay(UnlockObject unlockObject) {
        this.unlockObject = unlockObject;
        init();
    }
    
    public UnlockObjectDisplay(FameUnlock fameUnlock) {
        this.unlockObject = fameUnlock.getUnlockObject();
        this.requiredFame = fameUnlock.getRequiredFame();
        init();
    }
        
    public void init() {
        setOpaque(false);
        
        setLayout(new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("default:grow"),
                ColumnSpec.decode("125dlu"),
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                RowSpec.decode("default:grow"),
                RowSpec.decode("70dlu"),
                RowSpec.decode("default:grow")}));
        
        contentPanel = new TranslucentPanel();
        contentPanel.setLayout(new GridLayout(1, 1));
        add(contentPanel, "2, 2, fill, fill");
        
        myImage = new MyImage();
        contentPanel.add(myImage);
        myImage.setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),
                FormFactory.RELATED_GAP_COLSPEC,},
            new RowSpec[] {
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                RowSpec.decode("default:grow"),
                FormFactory.RELATED_GAP_ROWSPEC,}));
        
        titleLabel = new JLabel("New label");
        myImage.add(titleLabel, "2, 2");
        myImage.setBackgroundImage(unlockObject.getImage());
        
        titleLabel.setFont(GuiUtil.DEFAULTHEADERFONT);
        titleLabel.setText(unlockObject.getText());
        
        if (requiredFame != null) {
            fameLabel = new JLabel(TextUtil.t("ui.requiredFame", new Object[]{requiredFame}));
            fameLabel.setFont(GuiUtil.DEFAULTLARGEBOLDFONT);
            myImage.add(fameLabel, "2, 4");
        }
        
        if (unlockObject.getDescription() != null && !unlockObject.getDescription().trim().equals("")) {
            myImage.setToolTipText(TextUtil.htmlPreformatted(unlockObject.getDescription()));
        }
        
        setGrayScale(!Jasbro.getInstance().getData().getUnlocks().getUnlockedObjects().contains(unlockObject));
    }
    
    public void setGrayScale(boolean grayScale) {
        myImage.setGrayscale(grayScale);
        if (grayScale) {
            titleLabel.setForeground(Color.RED);
            if (fameLabel != null) {
                fameLabel.setForeground(Color.RED);
            }
        }
        else {
            titleLabel.setForeground(Color.BLACK);
            if (fameLabel != null) {
                fameLabel.setForeground(Color.BLACK);
            }
        }
        repaint();
    }
}
