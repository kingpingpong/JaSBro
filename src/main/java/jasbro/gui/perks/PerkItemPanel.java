package jasbro.gui.perks;

import jasbro.game.character.Charakter;
import jasbro.game.character.traits.SkillTree;
import jasbro.game.character.traits.SkillTreeItem;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

public class PerkItemPanel extends MyImage {
    private SkillTreeItem perk;
    private Charakter character;
    private SkillTree skillTree;
    
    public PerkItemPanel(SkillTreeItem skillTreeItem, Charakter character, SkillTree skillTree) {
        this.character = character;
        this.skillTree = skillTree;
        this.perk = skillTreeItem;
        setCentered(true);
        setToolTipText(TextUtil.htmlPreformatted(perk.getPerk().getText(character) + "\n" + perk.getDescription(skillTree, character)));
        setImage(skillTreeItem.getIcon());
        if (character.getTraits().contains(skillTreeItem.getPerk())) {
            setBackgroundImage(new ImageData("images/icons/perks/magic_circle_of_spark_by_llunet1-d50er3t.png"));
        }
            
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setInsetX(getWidth() / 5);
                setInsetY(getHeight() / 5);
                repaint();
            }
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (perk.canLearn(PerkItemPanel.this.skillTree, PerkItemPanel.this.character)) {
                    Object arguments[] = {perk.getPerk().getText(), perk.getPerk().getDescription()};
                    if (JOptionPane.showConfirmDialog(PerkItemPanel.this.getParent().getParent().getParent(), 
                            TextUtil.t("ui.confirmPerk", arguments), 
                            TextUtil.t("ui.learnPerk", arguments), JOptionPane.YES_NO_OPTION) 
                            == JOptionPane.YES_OPTION) {
                        PerkItemPanel.this.character.addTrait(perk.getPerk());
                        setBackgroundImage(new ImageData("images/icons/perks/magic_circle_of_spark_by_llunet1-d50er3t.png"));
                        setToolTipText(TextUtil.htmlPreformatted(perk.getPerk().getText() + "\n" + 
                                perk.getDescription(PerkItemPanel.this.skillTree, PerkItemPanel.this.character)));
                    }
                }
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (perk.canLearn(PerkItemPanel.this.skillTree, PerkItemPanel.this.character)) {
                    Object arguments[] = {perk.getPerk().getText(), perk.getPerk().getDescription()};
                    if (JOptionPane.showConfirmDialog(PerkItemPanel.this.getParent().getParent().getParent(), 
                            TextUtil.t("ui.confirmPerk", arguments), 
                            TextUtil.t("ui.learnPerk", arguments), JOptionPane.YES_NO_OPTION) 
                            == JOptionPane.YES_OPTION) {
                        PerkItemPanel.this.character.addTrait(perk.getPerk());
                        setBackgroundImage(new ImageData("images/icons/perks/magic_circle_of_spark_by_llunet1-d50er3t.png"));
                        setToolTipText(TextUtil.htmlPreformatted(perk.getPerk().getText() + "\n" + 
                                perk.getDescription(PerkItemPanel.this.skillTree, PerkItemPanel.this.character)));
                    }
                }
                repaint();
            }
        });
    }

    public SkillTreeItem getPerk() {
        return perk;
    }

    public boolean canLearn() {
        return perk.canLearn(PerkItemPanel.this.skillTree, PerkItemPanel.this.character);
    }
}
