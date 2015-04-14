package jasbro.gui.character;

import jasbro.Jasbro;
import jasbro.game.character.CharacterStuffCounter.CounterNames;
import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.AttributePanel;
import jasbro.gui.objects.div.MyButton;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.gui.perks.PerksPanel;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

public class CharacterScreenInfoPanel extends TranslucentPanel {
    private Charakter character;
    private MyButton perkButton;
    private JLabel typeLabel;
    private JLabel genderLabel;
    private JLabel fameLabel;
    
    public CharacterScreenInfoPanel(Charakter characterTmp) {
        this.character = characterTmp;
        setLayout(new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("default:grow"),
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                RowSpec.decode("default:grow"),
                RowSpec.decode("default:grow"),
                RowSpec.decode("default:grow"),
                FormFactory.PREF_ROWSPEC,
                FormFactory.PREF_ROWSPEC,
                FormFactory.PREF_ROWSPEC,
                RowSpec.decode("default:grow"),
                RowSpec.decode("default:grow"),
                RowSpec.decode("default:grow"),}));
        setPreferredSize(null);
        
        JLabel label = new JLabel(TextUtil.t("type"));
        label.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
        add(label, "1, 1");
        typeLabel = new JLabel();
        typeLabel.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
        add(typeLabel, "2, 1, right, default");
        
        label = new JLabel(TextUtil.t("gender"));
        label.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
        add(label, "1, 2");
        genderLabel = new JLabel();
        genderLabel.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
        add(genderLabel, "2, 2, right, default");
        
        label = new JLabel(TextUtil.t("fame"));
        label.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
        add(label, "1, 3");
        fameLabel = new JLabel();
        fameLabel.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
        add(fameLabel, "2, 3, right, default");
        
        
        if (character.getCounter().get(CounterNames.CHILDREN) > 0) {
            label = new JLabel(TextUtil.t("ui.children"));
            label.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
            add(label, "1, 4");
            JLabel childLabel = new JLabel(""+character.getCounter().get(CounterNames.CHILDREN));
            childLabel.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
            add(childLabel, "2, 4, right, default");
        }
        
        if (character.getAgeProgressionData().getNameMother() != null) {
            label = new JLabel(TextUtil.t("ui.mother"));
            label.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
            add(label, "1, 5");
            JLabel childLabel = new JLabel(character.getAgeProgressionData().getNameMother());
            childLabel.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
            add(childLabel, "2, 5, right, default");
        }
        
        if (character.getAgeProgressionData().getNameFather() != null) {
            label = new JLabel(TextUtil.t("ui.mother"));
            label.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
            add(label, "1, 6");
            JLabel childLabel = new JLabel(character.getAgeProgressionData().getNameFather());
            childLabel.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
            add(childLabel, "2, 6, right, default");
        }
        
        AttributePanel attributePanel = new AttributePanel(character.getAttribute(EssentialAttributes.HEALTH));
        add(attributePanel, "1, 7, 2, 1, fill, top");
        attributePanel.setNormalSize();
        attributePanel.update();
        attributePanel = new AttributePanel(character.getAttribute(EssentialAttributes.ENERGY));
        add(attributePanel, "1, 8, 2, 1, fill, top");
        attributePanel.setNormalSize();
        attributePanel.update();
        
        if (Jasbro.getInstance().getData().getCharacters().contains(character)) {
            perkButton = new MyButton("", new ImageData("images/icons/perks/button75892304.png"), 
                    new ImageData("images/icons/perks/button76812194.png"));
            perkButton.setMinimumSize(new Dimension(-1, 30));
            add(perkButton, "1, 9, 2, 1, fill, fill");
            perkButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFrame frame = Jasbro.getInstance().getGui();
                    JDialog dialog = new JDialog(Jasbro.getInstance().getGui(), "Perks", true);
                    dialog.getContentPane().add(new PerksPanel(character));
                    dialog.setResizable(true);
                    dialog.pack();
                    dialog.setLocationRelativeTo(frame);
                    dialog.validate();
                    dialog.setVisible(true);
                }
            });
            

        }
        
        for (Component component : getComponents()) {
            component.setFont(component.getFont().deriveFont(component.getFont().getSize() + 4f));
        }
        
        update();
    }
    
    public void update() {
        typeLabel.setText(character.getType().getText());
        genderLabel.setText(character.getGender().getText());
        fameLabel.setText(TextUtil.t("formatted", new Object[]{character.getFame().getFameCharacter().getText()}));
        fameLabel.setToolTipText(character.getFame().getFame()+"");
        
        if (perkButton != null) {
            int amountUnspent = character.getUnspentPerkPoints();
            if (amountUnspent > 0) {
                Object arguments[] = {amountUnspent};
                perkButton.setText(TextUtil.t("ui.perkButtonUnspent", arguments));
            }
            else {
                perkButton.setText(TextUtil.t("ui.perkButton"));
            }
            if (character.getSkillTrees().size() == 0) {
                perkButton.setEnabled(false);
            }
            else {
                perkButton.setEnabled(true);
            }
        }     
        

    }
}
