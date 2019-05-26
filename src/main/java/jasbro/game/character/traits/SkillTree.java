package jasbro.game.character.traits;

import jasbro.game.character.traits.SkillTreeItem.AdvertisingTreeStart;
import jasbro.game.character.traits.SkillTreeItem.AlchemistTreeStart;
import jasbro.game.character.traits.SkillTreeItem.BartenderTreeStart;
import jasbro.game.character.traits.SkillTreeItem.CatgirlTreeStart;
import jasbro.game.character.traits.SkillTreeItem.DancerTreeStart;
import jasbro.game.character.traits.SkillTreeItem.KinkySexTreeStart;
import jasbro.game.character.traits.SkillTreeItem.MaidTreeStart;
import jasbro.game.character.traits.SkillTreeItem.MutantTreeStart;
import jasbro.game.character.traits.SkillTreeItem.NurseTreeStart;
import jasbro.game.character.traits.SkillTreeItem.SexTreeStart;
import jasbro.game.character.traits.SkillTreeItem.SlaveSkillTreeStart;
import jasbro.game.character.traits.SkillTreeItem.ThiefTreeStart;
import jasbro.game.character.traits.SkillTreeItem.TrainerSkillTreeStart;
import jasbro.game.character.traits.SkillTreeItem.WarriorSkillTreeStart;
import jasbro.game.character.traits.SkillTreeItem.WhoreTreeStart;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;

public enum SkillTree {
    TRAINER(new TrainerSkillTreeStart(), new ImageData("images/icons/perks/trainer.png")),
    SLAVE(new SlaveSkillTreeStart(), new ImageData("images/icons/perks/slave.png")),
    FIGHTER(new WarriorSkillTreeStart(), new ImageData("images/icons/perks/warrior.png")),
    MAID(new MaidTreeStart(), new ImageData("images/icons/perks/maid.png")),
    THIEF(new ThiefTreeStart(), new ImageData("images/icons/perks/thief.png")),
    KINKYSEX(new KinkySexTreeStart(), new ImageData("images/icons/perks/kinky.png")),
    MUTANT(new MutantTreeStart(), new ImageData("images/icons/perks/monster.png")),    
    WHORE(new WhoreTreeStart(), new ImageData("images/icons/perks/whore.png")),
    BARTENDER(new BartenderTreeStart(), new ImageData("images/icons/perks/bartender.png")),
    SEX(new SexTreeStart(), new ImageData("images/icons/perks/sex.png")),
    MARKETINGEXPERT(new AdvertisingTreeStart(), new ImageData("images/icons/perks/marketing.png")),
    DANCER(new DancerTreeStart(), new ImageData("images/icons/perks/dancer.png")),
    CATGIRL(new CatgirlTreeStart(), new ImageData("images/icons/perks/catgirl.png")),
    ALCHEMIST(new AlchemistTreeStart(), new ImageData("images/icons/perks/alchemy.png")),
    NURSE(new NurseTreeStart(), new ImageData("images/icons/perks/nurse.png"));
    
    
    private SkillTreeItem firstItem;
    private ImageData icon;
    
    private SkillTree(SkillTreeItem firstItem, ImageData icon) {
        this.firstItem = firstItem;
        this.icon = icon;
    }
    
    public SkillTreeItem getFirstItem() {
        return firstItem;
    }

    public ImageData getIcon() {
        return icon;
    }

    public String getText() {
        return TextUtil.t(this.toString());
    }

    


    
}
