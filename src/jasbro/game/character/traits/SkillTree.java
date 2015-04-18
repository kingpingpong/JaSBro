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
    TRAINER(new TrainerSkillTreeStart(), new ImageData("images/icons/perks/azula_on_the_iron_throne_by_kissyushka-d5y518a.jpg")),
    SLAVE(new SlaveSkillTreeStart(), new ImageData("images/icons/perks/leia_turner_by_valadaz.jpg")),
    FIGHTER(new WarriorSkillTreeStart(), new ImageData("images/icons/perks/warrior_by_Wen_Xaeroaaa.jpg")),
    MAID(new MaidTreeStart(), new ImageData("images/icons/perks/Superior_robo_waifu_maid.png")),
    THIEF(new ThiefTreeStart(), new ImageData("images/icons/perks/200px-NightingaleSentinel.png")),
    KINKYSEX(new KinkySexTreeStart(), new ImageData("images/icons/perks/women_bondage.png")),
    MUTANT(new MutantTreeStart(), new ImageData("images/icons/perks/monster.png")),    
    WHORE(new WhoreTreeStart(), new ImageData("images/icons/perks/whore.png")),
    BARTENDER(new BartenderTreeStart(), new ImageData("images/icons/perks/bartender.jpg")),
    SEX(new SexTreeStart(), new ImageData("images/icons/perks/sex.jpg")),
    MARKETINGEXPERT(new AdvertisingTreeStart(), new ImageData("images/icons/perks/marketing.jpg")),
    DANCER(new DancerTreeStart(), new ImageData("images/icons/perks/dance.jpg")),
    CATGIRL(new CatgirlTreeStart(), new ImageData("images/icons/perks/catgirl.jpg")),
    ALCHEMIST(new AlchemistTreeStart(), new ImageData("images/icons/perks/alchemy.png")),
    NURSE(new NurseTreeStart(), new ImageData("images/icons/perks/nurse.jpg"));
    
    
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
