package jasbro.game.character.traits;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.interfaces.AttributeType;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillTreeItem {

    private Trait perk;
    private List<SkillTreeItem> nextItems = new ArrayList<SkillTreeItem>();
    private List<PerkRequirement> perkRequirements  = new ArrayList<PerkRequirement>();
    private List<PerkRequirement> perkUnRequirements = new ArrayList<PerkRequirement>();
    private ImageData icon;
    private List<SkillTreeItem> parentItems = new ArrayList<SkillTreeItem>();

    public SkillTreeItem(ImageData icon) {
        this.icon = icon;
    }
	
	public SkillTreeItem(Trait perk, ImageData icon, SkillTreeItem... nextItems) {
		this(icon);
		this.perk = perk;
		for (SkillTreeItem skillTreeItem : nextItems) {
			this.nextItems.add(skillTreeItem);
		}
	}
	
	public boolean add(Charakter character) {
		if (checkRequirementsMet(character)) {
			character.addTrait(perk);
			return true;
		} else {
			return false;
		}
	}
	
	public void remove(Charakter character) {
		character.removeTrait(perk);
	}
	
	public List<PerkRequirement> getPerkRequirements() {
		return perkRequirements;
	}
	
    public List<PerkRequirement> getPerkUnRequirements() {
        return perkUnRequirements;
    }

    public void setPerkRequirements(List<PerkRequirement> perkRequirements) {
        this.perkRequirements = perkRequirements;
    }
    public void setPerkUnRequirements(List<PerkRequirement> perkRequirements) {
        this.perkRequirements = perkRequirements;
    }
	
	public Trait getPerk() {
		return perk;
	}
	
	public List<SkillTreeItem> getNextItems() {
		return nextItems;
	}
	
	public boolean isParentItem(SkillTreeItem skillTreeItem) {
		return parentItems.contains(skillTreeItem);
	}
	
	public void addParentItem(SkillTreeItem parentItem) {
		parentItems.add(parentItem);
	}
	
	public boolean isParentLearned(Charakter character) {
		if (getParentItems().size() == 0) {
			return true;
		}
		List<Trait> characterTraits = character.getTraits();
		for (SkillTreeItem parentItem : parentItems) {
			if (characterTraits.contains(parentItem.getPerk())) {
				return true;
			}
		}
		return false;
	}
	
	public List<SkillTreeItem> getParentItems() {
		return parentItems;
	}
	
	
	
	
	
	//Requirements to learn perks
	public interface PerkRequirement extends Serializable {
		public boolean isRequirementMet(Charakter character);
		
		public String getRequirementDescription();
	}
	
	//Normal attribute requirement is automatic!
	public class AttributeRequirement implements PerkRequirement {
		private AttributeType attributeType;
		private int amount;
		
		public AttributeRequirement(AttributeType attributeType, int amount) {
			this.attributeType = attributeType;
			this.amount = amount;
		}
		
		@Override
		public boolean isRequirementMet(Charakter character) {
			if (character.getAttribute(attributeType).getInternValue() >= amount) {
				return true;
			} else {
				return false;
			}
		}
		
		@Override
		public String getRequirementDescription() {
			return attributeType.getText() + ": " + amount;
		}
	}
	
	public class PerkNotPresentRequirement implements PerkRequirement {
		private Trait perk;
		
		public PerkNotPresentRequirement(Trait perk) {
			this.perk = perk;
		}
		
		@Override
		public boolean isRequirementMet(Charakter character) {
			if (character.getTraits().contains(perk)) {
				return false;
			} else {
				return true;
			}
		}
		
		@Override
		public String getRequirementDescription() {
			return "Can not have: " + perk.getText();
		}
	}
	
	public class RequiresTraitRequirement implements PerkRequirement {
		private Trait perk;
		
		public RequiresTraitRequirement(Trait perk) {
			this.perk = perk;
		}
		
		@Override
		public boolean isRequirementMet(Charakter character) {
			if (character.getTraits().contains(perk)) {
				return true;
			} else {
				return false;
			}
		}
		
		@Override
		public String getRequirementDescription() {
			return "Requires: " + perk.getText();
		}
	}
	
	public static void link(Trait perk1, Trait perk2, Map<Trait, SkillTreeItem> map) {
		map.get(perk1).getNextItems().add(map.get(perk2));
		map.get(perk2).addParentItem(map.get(perk1));
	}
	
	public static void create(Trait perk, Map<Trait, SkillTreeItem> map, ImageData icon) {
		map.put(perk, new SkillTreeItem(perk, icon));
	}
	
	public ImageData getIcon() {
		return icon;
	}
	
	public boolean canLearn(SkillTree skillTree, Charakter character) {
		if (character.getUnspentPerkPoints() < 1) {
			return false;
		}
		
		if (!isParentLearned(character)) {
			return false;
		}
		
		SpecializationType specializationType = PerkHandler.getConnectedSpecializationType(skillTree);
		if (specializationType != null) {
			int baseRequirement = PerkHandler.getBaseRequirement(this, skillTree, character);
			if (baseRequirement > 0) {
				if (Util.getAverage(specializationType, character) < baseRequirement) {
					return false;
				}
			}
		}
		
		return checkRequirementsMet(character);
	}
	
    public boolean checkRequirementsMet(Charakter character) {
        for (PerkRequirement perkRequirement : getPerkRequirements()) {
            if (!perkRequirement.isRequirementMet(character)) {
                return false;
            }
        }
        for (PerkRequirement perkRequirement : getPerkUnRequirements()) {
        	if (perkRequirement.isRequirementMet(character)) {
        		return false;
        	}
        }
        return true;
    }
	
	public String getDescription(SkillTree skillTree, Charakter character) {
		String text = perk.getDescription(character) + "\n";
		if (!character.getTraits().contains(perk)) {
			SpecializationType specializationType = PerkHandler.getConnectedSpecializationType(skillTree);
			if (specializationType != null) {
				int baseRequirement = PerkHandler.getBaseRequirement(this, skillTree, character);
				if (baseRequirement > 0) {
					Object arguments[] = {baseRequirement, skillTree.getText(), ((int) Util.getAverage(specializationType, character))};
					text += TextUtil.t("perks.baseRequirement", arguments) + "\n";
				}
			}
			
			for (PerkRequirement perkRequirement : getPerkRequirements()) {
				if (!perkRequirement.isRequirementMet(character)) {
					text +=  perkRequirement.getRequirementDescription();
				}
			}
		}
		return text;
	}
	
	public static class SexTreeStart extends SkillTreeItem {
		public SexTreeStart() {
			super(Trait.DEBUTANTE, new ImageData("images/icons/perks/debutante.png"));
			Map<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
			map.put(Trait.DEBUTANTE, this);
			
			create(Trait.SENSITIVECLIT, map, new ImageData("images/icons/perks/sensitiveclit.png"));
			create(Trait.DEEPLOVE, map, new ImageData("images/icons/perks/deeplove.png"));
			create(Trait.LOVETHETASTE, map, new ImageData("images/icons/perks/lovethetaste.png"));
			create(Trait.AFLEURDEPEAU, map, new ImageData("images/icons/perks/afleurdepeau.png"));
			create(Trait.MEATBUNS, map, new ImageData("images/icons/perks/meatbuns.png"));
			
			create(Trait.COZYCUNT, map, new ImageData("images/icons/perks/cozycunt.png"));
			create(Trait.ROWDYRUMP, map, new ImageData("images/icons/perks/rowdyrump.png"));
			create(Trait.SLURPYSLURP, map, new ImageData("images/icons/perks/slurpyslurp.png"));
			create(Trait.TOUCHYFEELY, map, new ImageData("images/icons/perks/touchyfeely.png"));
			create(Trait.PUFFPUFF, map, new ImageData("images/icons/perks/puffpuff.png"));
			
			create(Trait.BEDROOMPRINCESS, map, new ImageData("images/icons/perks/bedroomprincess.png"));
			
			create(Trait.WETFORYOU, map, new ImageData("images/icons/perks/wetforyou.png"));
			create(Trait.BACKDOOROPEN, map, new ImageData("images/icons/perks/backdooropen.png"));
			create(Trait.THIRSTY, map, new ImageData("images/icons/perks/thirsty.png"));
			create(Trait.FEELMEUP, map, new ImageData("images/icons/perks/feelmeup.png"));
			create(Trait.COMETOMOMMY, map, new ImageData("images/icons/perks/cometomommy.png"));
			
			create(Trait.STEAMY, map, new ImageData("images/icons/perks/steamy.png"));
			create(Trait.BEDGODDESS, map, new ImageData("images/icons/perks/bedgoddess.png"));
			
			
			link(Trait.DEBUTANTE, Trait.SENSITIVECLIT, map);
			link(Trait.DEBUTANTE, Trait.DEEPLOVE, map);
			link(Trait.DEBUTANTE, Trait.LOVETHETASTE, map);
			link(Trait.DEBUTANTE, Trait.AFLEURDEPEAU, map);
			link(Trait.DEBUTANTE, Trait.MEATBUNS, map);
			
			link(Trait.SENSITIVECLIT, Trait.COZYCUNT, map);
			link(Trait.DEEPLOVE, Trait.ROWDYRUMP, map);
			link(Trait.LOVETHETASTE, Trait.SLURPYSLURP, map);
			link(Trait.AFLEURDEPEAU, Trait.TOUCHYFEELY, map);
			link(Trait.MEATBUNS, Trait.PUFFPUFF, map);
			
			link(Trait.COZYCUNT, Trait.BEDROOMPRINCESS, map);
			link(Trait.ROWDYRUMP, Trait.BEDROOMPRINCESS, map);
			link(Trait.SLURPYSLURP, Trait.BEDROOMPRINCESS, map);
			link(Trait.TOUCHYFEELY, Trait.BEDROOMPRINCESS, map);
			link(Trait.PUFFPUFF, Trait.BEDROOMPRINCESS, map);
			
			link(Trait.BEDROOMPRINCESS, Trait.WETFORYOU, map);
			link(Trait.BEDROOMPRINCESS, Trait.BACKDOOROPEN, map);
			link(Trait.BEDROOMPRINCESS, Trait.THIRSTY, map);
			link(Trait.BEDROOMPRINCESS, Trait.FEELMEUP, map);
			link(Trait.BEDROOMPRINCESS, Trait.COMETOMOMMY, map);
			
			link(Trait.WETFORYOU, Trait.STEAMY, map);
			link(Trait.WETFORYOU, Trait.BEDGODDESS, map);
			link(Trait.BACKDOOROPEN, Trait.STEAMY, map);
			link(Trait.BACKDOOROPEN, Trait.BEDGODDESS, map);
			link(Trait.THIRSTY, Trait.STEAMY, map);
			link(Trait.THIRSTY, Trait.BEDGODDESS, map);
			link(Trait.FEELMEUP, Trait.STEAMY, map);
			link(Trait.FEELMEUP, Trait.BEDGODDESS, map);
			link(Trait.COMETOMOMMY, Trait.STEAMY, map);
			link(Trait.COMETOMOMMY, Trait.BEDGODDESS, map);
			
			
			
		}
	}
	
	public static class AdvertisingTreeStart extends SkillTreeItem {
		public AdvertisingTreeStart() {
			super(Trait.INITIATE, new ImageData("images/icons/perks/beast-eye.png"));
			Map<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
			map.put(Trait.INITIATE, this);
			
			create(Trait.SAMPLINGTHEGOODS, map, new ImageData("images/icons/perks/samplingthegoods.png"));
			create(Trait.SHOWINGTHEGOODS, map, new ImageData("images/icons/perks/showingthegoods.png"));
			create(Trait.SHOWOFF, map, new ImageData("images/icons/perks/showoff.png"));
			create(Trait.CATCHY, map, new ImageData("images/icons/perks/catchy.png"));
			create(Trait.HEYGUYSBOOSE, map, new ImageData("images/icons/perks/heyguysboose.png"));
			
			create(Trait.SPIRITED, map, new ImageData("images/icons/perks/spirited.png"));
			
			create(Trait.TARGETBUM, map, new ImageData("images/icons/perks/targetbum.png"));
			create(Trait.TARGETPEASANT, map, new ImageData("images/icons/perks/targetpeasant.png"));
			create(Trait.TARGETSOLDIER, map, new ImageData("images/icons/perks/targetsoldier.png"));
			create(Trait.TARGETBUSINESSMEN, map, new ImageData("images/icons/perks/targetbusinessmen.png"));
			create(Trait.CONFIRMEDSALESPERSON, map, new ImageData("images/icons/perks/confirmedsalesperson.png"));
			
			create(Trait.SALESPROMOTION, map, new ImageData("images/icons/perks/salespromotion.png"));
			
			create(Trait.RECOGNIZED, map, new ImageData("images/icons/perks/recognised.png"));
			
			create(Trait.NICHEMARKETINGNOBLE, map, new ImageData("images/icons/perks/nichemarketingnoble.png"));
			create(Trait.NICHEMARKETINGLORD, map, new ImageData("images/icons/perks/nichemarketinglord.png"));
			create(Trait.NICHEMARKETINGCELEBRITY, map, new ImageData("images/icons/perks/nichemarketingcelebrity.png"));
			create(Trait.NICHEMARKETINGGROUPS, map, new ImageData("images/icons/perks/nichemarketinggroup.png"));
			create(Trait.BUSINESSRELATIONS, map, new ImageData("images/icons/perks/businessrelations.png"));
			
			
			link(Trait.INITIATE, Trait.SAMPLINGTHEGOODS, map);
			link(Trait.INITIATE, Trait.SHOWINGTHEGOODS, map);
			link(Trait.INITIATE, Trait.SHOWOFF, map);
			link(Trait.INITIATE, Trait.CATCHY, map);
			link(Trait.INITIATE, Trait.HEYGUYSBOOSE, map);
			
			link(Trait.SAMPLINGTHEGOODS, Trait.SPIRITED, map);
			link(Trait.SHOWINGTHEGOODS, Trait.SPIRITED, map);
			link(Trait.SHOWOFF, Trait.SPIRITED, map);
			link(Trait.CATCHY, Trait.SPIRITED, map);
			link(Trait.HEYGUYSBOOSE, Trait.SPIRITED, map);
			
			link(Trait.SPIRITED, Trait.TARGETBUM, map);
			link(Trait.SPIRITED, Trait.TARGETPEASANT, map);
			link(Trait.SPIRITED, Trait.TARGETSOLDIER, map);
			link(Trait.SPIRITED, Trait.TARGETBUSINESSMEN, map);
			link(Trait.SPIRITED, Trait.CONFIRMEDSALESPERSON, map);
			
			link(Trait.TARGETBUM, Trait.SALESPROMOTION, map);
			link(Trait.TARGETPEASANT, Trait.SALESPROMOTION, map);
			link(Trait.TARGETSOLDIER, Trait.SALESPROMOTION, map);
			link(Trait.TARGETBUSINESSMEN, Trait.SALESPROMOTION, map);
			link(Trait.CONFIRMEDSALESPERSON, Trait.SALESPROMOTION, map);
			
			link(Trait.SALESPROMOTION, Trait.RECOGNIZED, map);
			link(Trait.SALESPROMOTION, Trait.RECOGNIZED, map);
			link(Trait.SALESPROMOTION, Trait.RECOGNIZED, map);
			link(Trait.SALESPROMOTION, Trait.RECOGNIZED, map);
			link(Trait.SALESPROMOTION, Trait.RECOGNIZED, map);
			link(Trait.SALESPROMOTION, Trait.RECOGNIZED, map);
			link(Trait.SALESPROMOTION, Trait.RECOGNIZED, map);
			

			link(Trait.RECOGNIZED, Trait.NICHEMARKETINGNOBLE, map);
			link(Trait.RECOGNIZED, Trait.NICHEMARKETINGLORD, map);
			link(Trait.RECOGNIZED, Trait.NICHEMARKETINGCELEBRITY, map);
			link(Trait.RECOGNIZED, Trait.NICHEMARKETINGGROUPS, map);
			link(Trait.RECOGNIZED, Trait.BUSINESSRELATIONS, map);
		}
	}
	
	
	public static class WarriorSkillTreeStart extends SkillTreeItem {
		public WarriorSkillTreeStart() {
			super(Trait.SHARPSENSES, new ImageData("images/icons/perks/sharp_senses.png"));
			Map<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
			map.put(Trait.SHARPSENSES, this);
			
			create(Trait.WEAPONMASTERY, map, new ImageData("images/icons/perks/weapon_mastery.png"));
			create(Trait.TOUGH, map, new ImageData("images/icons/perks/tough.png"));
			create(Trait.ELEMENTALSTUDY, map, new ImageData("images/icons/perks/battlemage.png"));
			
			create(Trait.MINDOFTHEFIGHTER, map, new ImageData("images/icons/perks/mind_of_the_fighter.png"));
			create(Trait.IRONBODY, map, new ImageData("images/icons/perks/iron_body.png"));
			create(Trait.ETHERSHIELD, map, new ImageData("images/icons/perks/ether_sheild.png"));
			
			create(Trait.SHOWTIME, map, new ImageData("images/icons/perks/showtime.png"));
			create(Trait.PLUNDERER, map, new ImageData("images/icons/perks/plunderer.png"));
			
			create(Trait.VITALPOINTSPIERCING, map, new ImageData("images/icons/perks/vital_points_piercing.png"));
			create(Trait.MKIIWALKER, map, new ImageData("images/icons/perks/mxii_walker.png"));
			create(Trait.CASTTIME, map, new ImageData("images/icons/perks/long_cast_time.png"));            
			create(Trait.DISTRACTION, map, new ImageData("images/icons/perks/distraction.png"));
			
			create(Trait.LOSTARTS, map, new ImageData("images/icons/perks/lost_arts.png"));
			
			link(Trait.SHARPSENSES, Trait.WEAPONMASTERY, map);
			link(Trait.SHARPSENSES, Trait.TOUGH, map);
			link(Trait.SHARPSENSES, Trait.ELEMENTALSTUDY, map);
			
			link(Trait.WEAPONMASTERY, Trait.MINDOFTHEFIGHTER, map);
			link(Trait.TOUGH, Trait.IRONBODY, map);
			link(Trait.ELEMENTALSTUDY, Trait.ETHERSHIELD, map);
			
			link(Trait.MINDOFTHEFIGHTER, Trait.SHOWTIME, map);
			link(Trait.IRONBODY, Trait.SHOWTIME, map);
			link(Trait.ETHERSHIELD, Trait.SHOWTIME, map);
			link(Trait.MINDOFTHEFIGHTER, Trait.PLUNDERER, map);
			link(Trait.IRONBODY, Trait.PLUNDERER, map);
			link(Trait.ETHERSHIELD, Trait.PLUNDERER, map);
			
			link(Trait.SHOWTIME, Trait.VITALPOINTSPIERCING, map);
			link(Trait.SHOWTIME, Trait.MKIIWALKER, map);
			link(Trait.SHOWTIME, Trait.CASTTIME, map);
			link(Trait.SHOWTIME, Trait.DISTRACTION, map);
			link(Trait.PLUNDERER, Trait.VITALPOINTSPIERCING, map);
			link(Trait.PLUNDERER, Trait.MKIIWALKER, map);
			link(Trait.PLUNDERER, Trait.CASTTIME, map);
			link(Trait.PLUNDERER, Trait.DISTRACTION, map);
			
			link(Trait.VITALPOINTSPIERCING, Trait.LOSTARTS, map);
			link(Trait.MKIIWALKER, Trait.LOSTARTS, map);
			link(Trait.CASTTIME, Trait.LOSTARTS, map);
			link(Trait.DISTRACTION, Trait.LOSTARTS, map);
			
			
		}
	}
	
	public static class WhoreTreeStart extends SkillTreeItem {
		public WhoreTreeStart() {
			super(Trait.SEDUCTRESS, new ImageData("images/icons/perks/charm.png"));
			Map<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
			map.put(Trait.SEDUCTRESS, this);
			create(Trait.ENDURANCE, map, new ImageData("images/icons/perks/burning-passion.png"));
			create(Trait.CHATTY, map, new ImageData("images/icons/perks/conversation.png"));
			create(Trait.FIRST, map, new ImageData("images/icons/perks/love-song.png"));
			
			create(Trait.COMPETITIVE, map, new ImageData("images/icons/perks/love-howl.png"));
			create(Trait.STYLISH, map, new ImageData("images/icons/perks/gems.png"));
			
			create(Trait.COUPLEMORE, map, new ImageData("images/icons/perks/backup.png"));
			create(Trait.WENCH, map, new ImageData("images/icons/perks/pretty-fangs.png"));
			create(Trait.JUSTYOUANDME, map, new ImageData("images/icons/perks/lips.png"));
			
			create(Trait.NUTBUSTER, map, new ImageData("images/icons/perks/terror.png"));
			create(Trait.QUICKIE, map, new ImageData("images/icons/perks/sands-of-time.png"));
			create(Trait.SITBACK, map, new ImageData("images/icons/perks/chemical-bolt.png"));            
			create(Trait.NONNEGOCIABLE, map, new ImageData("images/icons/perks/cash.png"));
			create(Trait.TAKEOURTIME, map, new ImageData("images/icons/perks/time-trap.png"));
			
			create(Trait.STREETSMARTS, map, new ImageData("images/icons/perks/seated-mouse.png"));
			create(Trait.KEEPEMCOMING, map, new ImageData("images/icons/perks/minions.png"));
			create(Trait.THENIGHTISSTILLYOUNG, map, new ImageData("images/icons/perks/moon.png"));
			
			create(Trait.THATGIRL, map, new ImageData("images/icons/perks/paper-lantern.png"));
			create(Trait.SLOPPY, map, new ImageData("images/icons/perks/dozen.png"));
			create(Trait.ONENIGHT, map, new ImageData("images/icons/perks/star-swirl.png"));
			
			link(Trait.SEDUCTRESS, Trait.ENDURANCE, map);
			link(Trait.SEDUCTRESS, Trait.CHATTY, map);
			link(Trait.SEDUCTRESS, Trait.FIRST, map);
			
			link(Trait.ENDURANCE, Trait.COMPETITIVE, map);
			link(Trait.CHATTY, Trait.COMPETITIVE, map);
			link(Trait.CHATTY, Trait.STYLISH, map);
			link(Trait.FIRST, Trait.STYLISH, map);
			
			link(Trait.COMPETITIVE, Trait.COUPLEMORE, map);
			link(Trait.COMPETITIVE, Trait.WENCH, map);
			link(Trait.STYLISH, Trait.WENCH, map);
			link(Trait.STYLISH, Trait.JUSTYOUANDME, map);
			
			link(Trait.COUPLEMORE, Trait.NUTBUSTER, map);
			link(Trait.COUPLEMORE, Trait.QUICKIE, map);
			link(Trait.COUPLEMORE, Trait.SITBACK, map);
			link(Trait.WENCH, Trait.SITBACK, map);
			link(Trait.JUSTYOUANDME, Trait.SITBACK, map);
			link(Trait.JUSTYOUANDME, Trait.NONNEGOCIABLE, map);
			link(Trait.JUSTYOUANDME, Trait.TAKEOURTIME, map);
			
			
			link(Trait.NUTBUSTER, Trait.STREETSMARTS, map);
			link(Trait.QUICKIE, Trait.STREETSMARTS, map);
			link(Trait.SITBACK, Trait.KEEPEMCOMING, map);
			link(Trait.TAKEOURTIME, Trait.THENIGHTISSTILLYOUNG, map);
			link(Trait.NONNEGOCIABLE, Trait.THENIGHTISSTILLYOUNG, map);
			
			link(Trait.STREETSMARTS, Trait.THATGIRL, map);
			link(Trait.KEEPEMCOMING, Trait.SLOPPY, map);
			link(Trait.THENIGHTISSTILLYOUNG, Trait.ONENIGHT, map);
			
		}
	}
	
	public static class MaidTreeStart extends SkillTreeItem {
		public MaidTreeStart() {
			super(Trait.PROFESSIONAL, new ImageData("images/icons/perks/fairy-wand.png"));
			Map<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
			map.put(Trait.PROFESSIONAL, this);
			create(Trait.CORDONBLEU, map, new ImageData("images/icons/perks/meat-cleaver.png"));
			create(Trait.COMPULSIVECLEANER, map, new ImageData("images/icons/perks/powder.png"));
			create(Trait.DIETEXPERT, map, new ImageData("images/icons/perks/aubergine.png"));
			create(Trait.CHEF, map, new ImageData("images/icons/perks/salt-shaker.png"));
			create(Trait.PRACTICAL, map, new ImageData("images/icons/perks/pizza-cutter.png"));
			create(Trait.HOUSEFAIRY, map, new ImageData("images/icons/perks/fairy-wand.png"));
			create(Trait.MOTHERLYCARE, map, new ImageData("images/icons/perks/shining-heart.png"));
			create(Trait.ELEGANT, map, new ImageData("images/icons/perks/gem-pendant.png"));
			create(Trait.WASHINGANDIRONING, map, new ImageData("images/icons/perks/water-drop.png"));
			create(Trait.TIMEMANIPULATION, map, new ImageData("images/icons/perks/hourglass.png"));
			create(Trait.ALWAYSIMPROVE, map, new ImageData("images/icons/perks/divergence.png"));
			create(Trait.CULINARYDELIGHTS, map, new ImageData("images/icons/perks/culinarydelights.png"));
			
			link(Trait.PROFESSIONAL, Trait.CORDONBLEU, map);
			link(Trait.PROFESSIONAL, Trait.COMPULSIVECLEANER, map);
			
			link(Trait.CORDONBLEU, Trait.DIETEXPERT, map);
			link(Trait.CORDONBLEU, Trait.CHEF, map);
			
			link(Trait.COMPULSIVECLEANER, Trait.PRACTICAL, map);
			link(Trait.COMPULSIVECLEANER, Trait.HOUSEFAIRY, map);
			
			link(Trait.DIETEXPERT, Trait.MOTHERLYCARE, map);
			
			link(Trait.CHEF, Trait.MOTHERLYCARE, map);
			
			link(Trait.PRACTICAL, Trait.WASHINGANDIRONING, map);
			link(Trait.HOUSEFAIRY, Trait.WASHINGANDIRONING, map);
			
			link(Trait.WASHINGANDIRONING, Trait.ELEGANT, map);
			link(Trait.MOTHERLYCARE, Trait.ELEGANT, map);
			
			link(Trait.ELEGANT, Trait.CULINARYDELIGHTS, map);
			link(Trait.ELEGANT, Trait.ALWAYSIMPROVE, map);
			link(Trait.ELEGANT, Trait.TIMEMANIPULATION, map);
		}
	}
	
	public static class ThiefTreeStart extends SkillTreeItem {
		public ThiefTreeStart() {
			super(Trait.GREEDY, new ImageData("images/icons/perks/greedy.png"));
			Map<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
			map.put(Trait.GREEDY, this);
			
			create(Trait.NIMBLEHANDS, map, new ImageData("images/icons/perks/nimble_hands.png"));
			create(Trait.PICKPOCKET, map, new ImageData("images/icons/perks/nimblehands.png"));
			create(Trait.ROGUE, map, new ImageData("images/icons/perks/rogue.png"));
			
			create(Trait.LUPIN, map, new ImageData("images/icons/perks/lupin.png"));
			
			create(Trait.CONARTIST, map, new ImageData("images/icons/perks/con_artist.png"));
			create(Trait.RESELLER, map, new ImageData("images/icons/perks/reseller.png"));
			create(Trait.BANDIT, map, new ImageData("images/icons/perks/bandit.png"));
			
			create(Trait.LIAISONSDANGEREUSES, map, new ImageData("images/icons/perks/liaisons_dangereuses.png"));
			
			create(Trait.NIGHTSHADE, map, new ImageData("images/icons/perks/nightshade.png"));
			create(Trait.ASSASSIN, map, new ImageData("images/icons/perks/assassin.png"));
			
			create(Trait.SHADOWBODY, map, new ImageData("images/icons/perks/shadow_body.png"));
			create(Trait.PHANTOMTHIEF, map, new ImageData("images/icons/perks/phantom_theif.png"));
			create(Trait.DOUBLEVIE, map, new ImageData("images/icons/perks/doublevie.png"));
			
			link(Trait.GREEDY, Trait.NIMBLEHANDS, map);
			link(Trait.GREEDY, Trait.PICKPOCKET, map);
			link(Trait.GREEDY, Trait.ROGUE, map);
			
			link(Trait.NIMBLEHANDS, Trait.LUPIN, map);
			link(Trait.PICKPOCKET, Trait.LUPIN, map);
			link(Trait.ROGUE, Trait.LUPIN, map);
			
			link(Trait.LUPIN, Trait.CONARTIST, map);
			link(Trait.LUPIN, Trait.RESELLER, map);
			link(Trait.LUPIN, Trait.BANDIT, map);
			
			link(Trait.CONARTIST, Trait.LIAISONSDANGEREUSES, map);
			link(Trait.RESELLER, Trait.LIAISONSDANGEREUSES, map);
			link(Trait.BANDIT, Trait.LIAISONSDANGEREUSES, map);
			
			link(Trait.LIAISONSDANGEREUSES, Trait.NIGHTSHADE, map);
			link(Trait.LIAISONSDANGEREUSES, Trait.ASSASSIN, map);
			
			link(Trait.NIGHTSHADE, Trait.PHANTOMTHIEF, map);
			link(Trait.NIGHTSHADE, Trait.DOUBLEVIE, map);
			link(Trait.ASSASSIN, Trait.DOUBLEVIE, map);
			link(Trait.ASSASSIN, Trait.SHADOWBODY, map);
		}
	}
	
	public static class KinkySexTreeStart extends SkillTreeItem {
		public KinkySexTreeStart() {
			super(Trait.KINKY, new ImageData("images/icons/perks/two-shadows.png"));
			Map<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
			map.put(Trait.KINKY, this);
			
			create(Trait.PERVERT, map, new ImageData("images/icons/perks/mad-scientist.png"));
			create(Trait.SUBMISSIVE, map, new ImageData("images/icons/perks/oppression.png"));
			create(Trait.EXHIBITIONIST, map, new ImageData("images/icons/perks/shouting.png"));
			
			create(Trait.SEXFREAK, map, new ImageData("images/icons/perks/imp-laugh.png"));
			
			create(Trait.SEXADDICT, map, new ImageData("images/icons/perks/sex_addict.png"));
			//create(Trait.MASOCHIST, map, new ImageData("images/icons/perks/handcuffs.png"));
			create(Trait.SEXMANIAC, map, new ImageData("images/icons/perks/mouth-watering.png")); 
			
			create(Trait.CUMSLUT, map, new ImageData("images/icons/perks/spill.png"));
			create(Trait.FLESHTOY, map, new ImageData("images/icons/perks/spill.png"));
			
			create(Trait.MEATTOILET, map, new ImageData("images/icons/perks/meat.png"));            
			create(Trait.PUBLICUSE, map, new ImageData("images/icons/perks/backup.png"));
			create(Trait.GANGBANGQUEEN, map, new ImageData("images/icons/perks/dozen.png"));
			//create(Trait.LEATHERMISTRESS, map, new ImageData("images/icons/perks/slavery-whip.png"));
			//create(Trait.LEATHERQUEEN, map, new ImageData("images/icons/perks/leather_queen.png"));
			create(Trait.BREEDER, map, new ImageData("images/icons/perks/shining-heart.png"));
			create(Trait.INSATIABLE, map, new ImageData("images/icons/perks/swallow.png"));
			
			//create(Trait.FROMDUSKTILLDAWN, map, new ImageData("images/icons/perks/swallow.png"));
			
			create(Trait.ANYPLACE, map, new ImageData("images/icons/perks/swallow.png"));
			//create(Trait.WHATDOESNTKILLYOU, map, new ImageData("images/icons/perks/what_doesnt_kill_you.png"));
			create(Trait.MONSTERSOW, map, new ImageData("images/icons/perks/brain-freeze.png"));
			
			link(Trait.KINKY, Trait.PERVERT, map);
			link(Trait.KINKY, Trait.SUBMISSIVE, map);
			link(Trait.KINKY, Trait.EXHIBITIONIST, map);
			
			link(Trait.PERVERT, Trait.SEXFREAK, map);
			link(Trait.SUBMISSIVE, Trait.SEXFREAK, map);
			link(Trait.EXHIBITIONIST, Trait.SEXFREAK, map);
			
			link(Trait.SEXFREAK, Trait.SEXADDICT, map);
			//link(Trait.SEXFREAK, Trait.MASOCHIST, map);
			link(Trait.SEXFREAK, Trait.SEXMANIAC, map);
			
			link(Trait.SEXADDICT, Trait.CUMSLUT, map);
			//link(Trait.MASOCHIST, Trait.CUMSLUT, map);
			//link(Trait.MASOCHIST, Trait.FLESHTOY, map);
			link(Trait.SEXMANIAC, Trait.FLESHTOY, map);
			
			link(Trait.SEXMANIAC, Trait.ANYPLACE, map);
			link(Trait.SEXADDICT, Trait.ANYPLACE, map);
			
			//link(Trait.CUMSLUT, Trait.MEATTOILET, map);
			link(Trait.CUMSLUT, Trait.GANGBANGQUEEN, map);
			link(Trait.CUMSLUT, Trait.PUBLICUSE, map);
			//link(Trait.FLESHTOY, Trait.LEATHERMISTRESS, map);
			//link(Trait.FLESHTOY, Trait.LEATHERQUEEN, map);
			link(Trait.FLESHTOY, Trait.MEATTOILET, map);
			link(Trait.FLESHTOY, Trait.BREEDER, map);
			
			link(Trait.ANYPLACE, Trait.PUBLICUSE, map);
			link(Trait.ANYPLACE, Trait.MEATTOILET, map);
			
			
			//link(Trait.MEATTOILET, Trait.INSATIABLE, map);
			link(Trait.PUBLICUSE, Trait.INSATIABLE, map);
			link(Trait.GANGBANGQUEEN, Trait.INSATIABLE, map);
			
			
			
			link(Trait.MEATTOILET, Trait.MONSTERSOW, map);
			link(Trait.BREEDER, Trait.MONSTERSOW, map);
			
			//link(Trait.LEATHERMISTRESS, Trait.WHATDOESNTKILLYOU, map);
			//link(Trait.LEATHERQUEEN, Trait.WHATDOESNTKILLYOU, map);
			//link(Trait.BREEDER, Trait.WHATDOESNTKILLYOU, map);
			
		}
	}
	
	public static class CatgirlTreeStart extends SkillTreeItem {
		public CatgirlTreeStart() {
			super(Trait.CATSAREASSHOLES, new ImageData("images/icons/perks/beast-eye.png"));
			Map<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
			map.put(Trait.CATSAREASSHOLES, this);
			create(Trait.DOMESTICATED, map, new ImageData("images/icons/perks/meat-cleaver.png"));
			create(Trait.STRAYCAT, map, new ImageData("images/icons/perks/powder.png"));
			create(Trait.CATWALK, map, new ImageData("images/icons/perks/aubergine.png"));
			create(Trait.WETPUSSY, map, new ImageData("images/icons/perks/salt-shaker.png"));
			create(Trait.NOCTURNAL, map, new ImageData("images/icons/perks/pizza-cutter.png"));
			//create(Trait.AGILITY, map, new ImageData("images/icons/perks/fairy-wand.png"));
			create(Trait.CATSANDRATS, map, new ImageData("images/icons/perks/shining-heart.png"));
			create(Trait.CUNNING, map, new ImageData("images/icons/perks/gem-pendant.png"));
			create(Trait.PAWERFUL, map, new ImageData("images/icons/perks/water-drop.png"));
			create(Trait.GROOMING, map, new ImageData("images/icons/perks/hourglass.png"));
			create(Trait.CATNAP, map, new ImageData("images/icons/perks/divergence.png"));
			create(Trait.ALLPURRPOSE, map, new ImageData("images/icons/perks/divergence.png"));
			create(Trait.CATTRACTIVE, map, new ImageData("images/icons/perks/divergence.png"));
			create(Trait.CATBURGLAR, map, new ImageData("images/icons/perks/divergence.png"));
			//create(Trait.CATPUN, map, new ImageData("images/icons/perks/divergence.png"));
			//create(Trait.CATINHEAT, map, new ImageData("images/icons/perks/divergence.png"));
			create(Trait.HIGHCATNESSRATING, map, new ImageData("images/icons/perks/divergence.png"));
			
			link(Trait.CATSAREASSHOLES, Trait.DOMESTICATED, map);
			link(Trait.CATSAREASSHOLES, Trait.STRAYCAT, map);
			
			
			link(Trait.DOMESTICATED, Trait.CATWALK, map);
			link(Trait.DOMESTICATED, Trait.WETPUSSY, map);
			
			link(Trait.STRAYCAT, Trait.NOCTURNAL, map);
			//link(Trait.STRAYCAT, Trait.AGILITY, map);
			
			link(Trait.WETPUSSY, Trait.CATSANDRATS, map);
			link(Trait.NOCTURNAL, Trait.CATSANDRATS, map);
			
			link(Trait.CATWALK, Trait.CUNNING, map); 
			//link(Trait.AGILITY, Trait.CUNNING, map);
			
			link(Trait.NOCTURNAL, Trait.PAWERFUL, map);
			//link(Trait.AGILITY, Trait.PAWERFUL, map);
			
			link(Trait.CATWALK, Trait.GROOMING, map);
			link(Trait.WETPUSSY, Trait.GROOMING, map);
			
			
			
			link(Trait.CUNNING, Trait.CATNAP, map);
			link(Trait.PAWERFUL, Trait.CATNAP, map);
			link(Trait.GROOMING, Trait.CATNAP, map);
			link(Trait.CATSANDRATS, Trait.CATNAP, map);
			
			//link(Trait.CUNNING, Trait.CATPUN, map);
			//link(Trait.PAWERFUL, Trait.CATPUN, map);
			//link(Trait.GROOMING, Trait.CATPUN, map);
			//link(Trait.CATSANDRATS, Trait.CATPUN, map);
			
			link(Trait.CATNAP, Trait.ALLPURRPOSE, map);
			link(Trait.CATNAP, Trait.CATTRACTIVE, map);
			link(Trait.CATNAP, Trait.CATBURGLAR, map);
			
			//link(Trait.CATPUN, Trait.ALLPURRPOSE, map);
			//link(Trait.CATPUN, Trait.CATTRACTIVE, map);
			//link(Trait.CATPUN, Trait.CATBURGLAR, map);
			
			//link(Trait.ALLPURRPOSE, Trait.CATINHEAT, map);
			//link(Trait.CATTRACTIVE, Trait.CATINHEAT, map);
			//link(Trait.CATBURGLAR, Trait.CATINHEAT, map);
			link(Trait.ALLPURRPOSE, Trait.HIGHCATNESSRATING, map);
			link(Trait.CATTRACTIVE, Trait.HIGHCATNESSRATING, map);
			link(Trait.CATBURGLAR, Trait.HIGHCATNESSRATING, map);
			
			
		}
	}
	
	public static class TrainerSkillTreeStart extends SkillTreeItem {
		public TrainerSkillTreeStart() {
			super(Trait.CERTIFIEDTRAINER, new ImageData("images/icons/perks/scroll-unfurled.png"));
			Map<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
			map.put(Trait.CERTIFIEDTRAINER, this);
			create(Trait.EFFECTIVETRAINER, map, new ImageData("images/icons/perks/pyromaniac.png"));
			create(Trait.MOTIVATOR, map, new ImageData("images/icons/perks/sing.png"));
			create(Trait.TRAINING, map, new ImageData("images/icons/perks/open-book.png"));
			create(Trait.PERFECTTRAINER, map, new ImageData("images/icons/perks/stone-throne.png"));   
			
			create(Trait.BASICTRAINING1, map, new ImageData("images/icons/perks/atomic-slashes.png"));
			create(Trait.BASICTRAINING2, map, new ImageData("images/icons/perks/atomic-slashes.png"));
			create(Trait.BASICTRAINING3, map, new ImageData("images/icons/perks/atomic-slashes.png"));
			create(Trait.SEXTRAINING1, map, new ImageData("images/icons/perks/flame.png"));
			create(Trait.SEXTRAINING2, map, new ImageData("images/icons/perks/flame.png"));
			create(Trait.SEXTRAINING3, map, new ImageData("images/icons/perks/flame.png"));
			create(Trait.SPECIALIZATIONTRAINING1, map, new ImageData("images/icons/perks/spectacles.png"));
			create(Trait.SPECIALIZATIONTRAINING2, map, new ImageData("images/icons/perks/spectacles.png"));
			create(Trait.SPECIALIZATIONTRAINING3, map, new ImageData("images/icons/perks/spectacles.png"));
			
			create(Trait.RESPECTED, map, new ImageData("images/icons/perks/back-forth.png"));
			create(Trait.ONEOFUS, map, new ImageData("images/icons/perks/back-forth.png"));
			
			create(Trait.STRONGPRESENCE, map, new ImageData("images/icons/perks/six-eyes.png"));
			create(Trait.EYESEVERYWHERE, map, new ImageData("images/icons/perks/six-eyes.png"));
			create(Trait.SPYNETWORK, map, new ImageData("images/icons/perks/six-eyes.png"));
			
			map.get(Trait.ONEOFUS).getPerkRequirements().add(new RequiresTraitRequirement(Trait.RESPECTED));
			
			
			link(Trait.CERTIFIEDTRAINER, Trait.RESPECTED, map);
			link(Trait.CERTIFIEDTRAINER, Trait.SEXTRAINING1, map);
			link(Trait.CERTIFIEDTRAINER, Trait.BASICTRAINING1, map);
			link(Trait.CERTIFIEDTRAINER, Trait.SPECIALIZATIONTRAINING1, map);
			link(Trait.CERTIFIEDTRAINER, Trait.STRONGPRESENCE, map);
			
			
			link(Trait.RESPECTED, Trait.SEXTRAINING2, map);
			link(Trait.RESPECTED, Trait.BASICTRAINING2, map);
			link(Trait.SEXTRAINING1, Trait.SEXTRAINING2, map);
			link(Trait.SEXTRAINING1, Trait.BASICTRAINING2, map);
			link(Trait.BASICTRAINING1, Trait.BASICTRAINING2, map);
			link(Trait.SPECIALIZATIONTRAINING1, Trait.BASICTRAINING2, map);
			link(Trait.SPECIALIZATIONTRAINING1, Trait.SPECIALIZATIONTRAINING2, map);
			link(Trait.STRONGPRESENCE, Trait.BASICTRAINING2, map);
			link(Trait.STRONGPRESENCE, Trait.SPECIALIZATIONTRAINING2, map);
			
			link(Trait.SEXTRAINING2, Trait.ONEOFUS, map);
			link(Trait.SEXTRAINING2, Trait.TRAINING, map);
			link(Trait.BASICTRAINING2, Trait.TRAINING, map);
			link(Trait.BASICTRAINING2, Trait.MOTIVATOR, map);
			link(Trait.BASICTRAINING2, Trait.EFFECTIVETRAINER, map);
			link(Trait.SPECIALIZATIONTRAINING2, Trait.EFFECTIVETRAINER, map);
			link(Trait.SPECIALIZATIONTRAINING2, Trait.EYESEVERYWHERE, map);
			
			link(Trait.ONEOFUS, Trait.SEXTRAINING3, map);
			link(Trait.ONEOFUS, Trait.BASICTRAINING3, map);
			link(Trait.TRAINING, Trait.SEXTRAINING3, map);
			link(Trait.TRAINING, Trait.BASICTRAINING3, map);
			link(Trait.MOTIVATOR, Trait.BASICTRAINING3, map);
			link(Trait.EFFECTIVETRAINER, Trait.BASICTRAINING3, map);
			link(Trait.EFFECTIVETRAINER, Trait.SPECIALIZATIONTRAINING3, map);
			link(Trait.EYESEVERYWHERE, Trait.SPECIALIZATIONTRAINING3, map);
			link(Trait.EYESEVERYWHERE, Trait.BASICTRAINING3, map);
			
			link(Trait.SEXTRAINING3, Trait.PERFECTTRAINER, map);
			link(Trait.BASICTRAINING3, Trait.PERFECTTRAINER, map);
			link(Trait.BASICTRAINING3, Trait.SPYNETWORK, map);
			link(Trait.SPECIALIZATIONTRAINING3, Trait.SPYNETWORK, map);
		}
	}
	
	public static class LegacySkillTreeStart extends SkillTreeItem {
		public LegacySkillTreeStart() {
			super(Trait.GENIUS, new ImageData("images/icons/perks/brain.png"));
			Map<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
			map.put(Trait.GENIUS, this);
			create(Trait.LEGACYWHORE, map, new ImageData("images/icons/perks/charm.png"));
			create(Trait.LEGACYSTRIPPER, map, new ImageData("images/icons/perks/spirited.png"));
			create(Trait.LEGACYMASSEUR, map, new ImageData("images/icons/perks/sleight_of_hands.png"));
			create(Trait.LEGACYNONE, map, new ImageData("images/icons/perks/stone-throne.png"));
			create(Trait.LEGACYADVENTURER, map, new ImageData("images/icons/perks/lost_arts.png"));
			create(Trait.LEGACYBARTENDER, map, new ImageData("images/icons/perks/liquor_master.png"));
			create(Trait.LEGACYMAID, map, new ImageData("images/icons/perks/salt-shaker.png"));
			create(Trait.LEGACYWHORE2, map, new ImageData("images/icons/perks/pyromaniac.png"));
			create(Trait.LEGACYSTRIPPER2, map, new ImageData("images/icons/perks/sing.png"));
			create(Trait.LEGACYMASSEUR2, map, new ImageData("images/icons/perks/open-book.png"));
			create(Trait.LEGACYNONE2, map, new ImageData("images/icons/perks/stone-throne.png"));
			create(Trait.LEGACYADVENTURER2, map, new ImageData("images/icons/perks/stone-throne.png"));
			create(Trait.LEGACYBARTENDER2, map, new ImageData("images/icons/perks/stone-throne.png"));
			create(Trait.LEGACYMAID2, map, new ImageData("images/icons/perks/stone-throne.png"));
			
			create(Trait.HIDDENLIBRARY, map, new ImageData("images/icons/perks/book-aura.png"));
			
			create(Trait.TOUGHERMISSIONS1, map, new ImageData("images/icons/perks/diamond-hard.png"));
			create(Trait.TOUGHERMISSIONS2, map, new ImageData("images/icons/perks/diamond-hard.png"));
			create(Trait.TOUGHERMISSIONS3, map, new ImageData("images/icons/perks/diamond-hard.png"));
			create(Trait.TOUGHERMISSIONS4, map, new ImageData("images/icons/perks/diamond-hard.png"));
			
			create(Trait.DISCOUNTLIBRARY, map, new ImageData("images/icons/perks/cash.png"));
			create(Trait.DISCOUNTSCHOOL, map, new ImageData("images/icons/perks/cash.png"));
			create(Trait.DISCOUNTSHOPS, map, new ImageData("images/icons/perks/cash.png"));
			create(Trait.DISCOUNTSLAVES, map, new ImageData("images/icons/perks/cash.png"));
			
			create(Trait.BENEFACTORSTREETS, map, new ImageData("images/icons/perks/top-hat.png"));
			create(Trait.BENEFACTORSHOPS, map, new ImageData("images/icons/perks/top-hat.png"));			
			create(Trait.BENEFACTORCARPENTERS, map, new ImageData("images/icons/perks/top-hat.png"));
			create(Trait.BENEFACTORSLAVEMARKET, map, new ImageData("images/icons/perks/top-hat.png"));
			
			
			map.get(Trait.LEGACYWHORE).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYSTRIPPER));
			map.get(Trait.LEGACYWHORE).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYMASSEUR));
			map.get(Trait.LEGACYWHORE).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYNONE));
			map.get(Trait.LEGACYWHORE).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYADVENTURER));
			map.get(Trait.LEGACYWHORE).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYBARTENDER));
			map.get(Trait.LEGACYWHORE).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYMAID));
			
			map.get(Trait.LEGACYSTRIPPER).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYWHORE));
			map.get(Trait.LEGACYSTRIPPER).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYMASSEUR));
			map.get(Trait.LEGACYSTRIPPER).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYNONE));
			map.get(Trait.LEGACYSTRIPPER).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYADVENTURER));
			map.get(Trait.LEGACYSTRIPPER).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYBARTENDER));
			map.get(Trait.LEGACYSTRIPPER).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYMAID));
			
			map.get(Trait.LEGACYNONE).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYSTRIPPER));
			map.get(Trait.LEGACYNONE).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYMASSEUR));
			map.get(Trait.LEGACYNONE).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYWHORE));
			map.get(Trait.LEGACYNONE).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYADVENTURER));
			map.get(Trait.LEGACYNONE).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYBARTENDER));
			map.get(Trait.LEGACYNONE).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYMAID));
			
			map.get(Trait.LEGACYADVENTURER).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYSTRIPPER));
			map.get(Trait.LEGACYADVENTURER).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYMASSEUR));
			map.get(Trait.LEGACYADVENTURER).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYNONE));
			map.get(Trait.LEGACYADVENTURER).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYWHORE));
			map.get(Trait.LEGACYADVENTURER).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYBARTENDER));
			map.get(Trait.LEGACYADVENTURER).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYMAID));
			
			map.get(Trait.LEGACYBARTENDER).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYSTRIPPER));
			map.get(Trait.LEGACYBARTENDER).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYMASSEUR));
			map.get(Trait.LEGACYBARTENDER).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYNONE));
			map.get(Trait.LEGACYBARTENDER).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYWHORE));
			map.get(Trait.LEGACYBARTENDER).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYADVENTURER));
			map.get(Trait.LEGACYBARTENDER).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYMAID));
			
			map.get(Trait.LEGACYMAID).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYSTRIPPER));
			map.get(Trait.LEGACYMAID).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYMASSEUR));
			map.get(Trait.LEGACYMAID).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYNONE));
			map.get(Trait.LEGACYMAID).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYWHORE));
			map.get(Trait.LEGACYMAID).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYBARTENDER));
			map.get(Trait.LEGACYMAID).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYADVENTURER));
			
			link(Trait.GENIUS, Trait.LEGACYWHORE, map);
			link(Trait.GENIUS, Trait.LEGACYSTRIPPER, map);
			link(Trait.GENIUS, Trait.LEGACYMASSEUR, map);
			//link(Trait.GENIUS, Trait.LEGACYNONE, map);
			link(Trait.GENIUS, Trait.LEGACYADVENTURER, map);
			link(Trait.GENIUS, Trait.LEGACYBARTENDER, map);
			link(Trait.GENIUS, Trait.LEGACYMAID, map);
			
			
			link(Trait.LEGACYWHORE, Trait.HIDDENLIBRARY, map);
			link(Trait.LEGACYSTRIPPER, Trait.HIDDENLIBRARY, map);
			link(Trait.LEGACYMASSEUR, Trait.HIDDENLIBRARY, map);
			link(Trait.LEGACYADVENTURER, Trait.HIDDENLIBRARY, map);
			link(Trait.LEGACYBARTENDER, Trait.HIDDENLIBRARY, map);
			link(Trait.LEGACYMAID, Trait.HIDDENLIBRARY, map); 
			
			link(Trait.HIDDENLIBRARY, Trait.TOUGHERMISSIONS1, map); 
			link(Trait.HIDDENLIBRARY, Trait.DISCOUNTLIBRARY, map); 
			link(Trait.HIDDENLIBRARY, Trait.BENEFACTORSTREETS, map); 
			
			link(Trait.TOUGHERMISSIONS1, Trait.TOUGHERMISSIONS2, map); 
			link(Trait.TOUGHERMISSIONS2, Trait.TOUGHERMISSIONS3, map); 
			link(Trait.TOUGHERMISSIONS3, Trait.TOUGHERMISSIONS4, map); 
			
			link(Trait.DISCOUNTLIBRARY, Trait.DISCOUNTSCHOOL, map); 
			link(Trait.DISCOUNTSCHOOL, Trait.DISCOUNTSHOPS, map); 
			link(Trait.DISCOUNTSHOPS, Trait.DISCOUNTSLAVES, map); 
			
			link(Trait.BENEFACTORSTREETS, Trait.BENEFACTORSHOPS, map); 
			link(Trait.BENEFACTORSHOPS, Trait.BENEFACTORSLAVEMARKET, map); 
			link(Trait.BENEFACTORSLAVEMARKET, Trait.BENEFACTORCARPENTERS, map); 
		}
	}
	
	public static class BartenderTreeStart extends SkillTreeItem {
		public BartenderTreeStart() {
			super(Trait.CLASSY, new ImageData("images/icons/perks/classy.png"));
			Map<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
			map.put(Trait.CLASSY, this);
			
			create(Trait.NIGHTSHIFT, map, new ImageData("images/icons/perks/night-sky.png"));
			
			create(Trait.MULTITASKING, map, new ImageData("images/icons/perks/multitasking.png"));
			create(Trait.OUTGOING, map, new ImageData("images/icons/perks/outgoing.png"));
			
			create(Trait.THECONFIDENT, map, new ImageData("images/icons/perks/theconfident.png"));
			create(Trait.CATMAID, map, new ImageData("images/icons/perks/cat_maid.png"));
			
			create(Trait.DATASS, map, new ImageData("images/icons/perks/dat_ass.png"));
			create(Trait.FLIRTY, map, new ImageData("images/icons/perks/flirty.png"));
			
			create(Trait.LIQUORMASTER, map, new ImageData("images/icons/perks/liquor_master.png"));
			create(Trait.UNDERTHETABLE, map, new ImageData("images/icons/perks/under_the_table.png"));
			
			link(Trait.CLASSY, Trait.NIGHTSHIFT, map);
			
			link(Trait.NIGHTSHIFT, Trait.OUTGOING, map);
			link(Trait.NIGHTSHIFT, Trait.MULTITASKING, map);
			
			link(Trait.OUTGOING, Trait.DATASS, map);
			link(Trait.OUTGOING, Trait.FLIRTY, map);
			
			link(Trait.MULTITASKING, Trait.THECONFIDENT, map);
			link(Trait.MULTITASKING, Trait.CATMAID, map);
			
			link(Trait.FLIRTY, Trait.UNDERTHETABLE, map);
			link(Trait.DATASS, Trait.UNDERTHETABLE, map);
			
			link(Trait.CATMAID, Trait.LIQUORMASTER, map);
			link(Trait.THECONFIDENT, Trait.LIQUORMASTER, map);
			
			
		}
	}
	
	public static class SlaveSkillTreeStart extends SkillTreeItem {
		public SlaveSkillTreeStart() {
			super(Trait.GOODSLAVE, new ImageData("images/icons/perks/spiked-collar.png"));
			Map<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
			map.put(Trait.GOODSLAVE, this);
			create(Trait.PERFECTSLAVE, map, new ImageData("images/icons/perks/spiked-collar.png"));
			create(Trait.TRAINING, map, new ImageData("images/icons/perks/open-book.png"));
			
			create(Trait.BASICTRAINING1, map, new ImageData("images/icons/perks/atomic-slashes.png"));
			create(Trait.BASICTRAINING2, map, new ImageData("images/icons/perks/atomic-slashes.png"));
			create(Trait.BASICTRAINING3, map, new ImageData("images/icons/perks/atomic-slashes.png"));
			create(Trait.SEXTRAINING1, map, new ImageData("images/icons/perks/flame.png"));
			create(Trait.SEXTRAINING2, map, new ImageData("images/icons/perks/flame.png"));
			create(Trait.SEXTRAINING3, map, new ImageData("images/icons/perks/flame.png"));
			create(Trait.SPECIALIZATIONTRAINING1, map, new ImageData("images/icons/perks/spectacles.png"));
			create(Trait.SPECIALIZATIONTRAINING2, map, new ImageData("images/icons/perks/spectacles.png"));
			create(Trait.SPECIALIZATIONTRAINING3, map, new ImageData("images/icons/perks/spectacles.png"));
			
			create(Trait.CONTENTPERK, map, new ImageData("images/icons/perks/sleepy.png"));
			create(Trait.TRUSTEDSLAVE, map, new ImageData("images/icons/perks/sleepy.png"));
			
			link(Trait.GOODSLAVE, Trait.SEXTRAINING1, map);
			link(Trait.GOODSLAVE, Trait.BASICTRAINING1, map);
			link(Trait.GOODSLAVE, Trait.SPECIALIZATIONTRAINING1, map);
			
			link(Trait.SEXTRAINING1, Trait.SEXTRAINING2, map);
			link(Trait.SEXTRAINING1, Trait.BASICTRAINING2, map);
			link(Trait.BASICTRAINING1, Trait.BASICTRAINING2, map);
			link(Trait.SPECIALIZATIONTRAINING1, Trait.BASICTRAINING2, map);
			link(Trait.SPECIALIZATIONTRAINING1, Trait.SPECIALIZATIONTRAINING2, map);
			
			link(Trait.SEXTRAINING2, Trait.TRAINING, map);
			link(Trait.BASICTRAINING2, Trait.TRAINING, map);
			link(Trait.BASICTRAINING2, Trait.CONTENTPERK, map);
			link(Trait.SPECIALIZATIONTRAINING2, Trait.CONTENTPERK, map);
			
			link(Trait.TRAINING, Trait.SEXTRAINING3, map);
			link(Trait.TRAINING, Trait.BASICTRAINING3, map);
			link(Trait.CONTENTPERK, Trait.BASICTRAINING3, map);
			link(Trait.CONTENTPERK, Trait.SPECIALIZATIONTRAINING3, map);
			
			link(Trait.SEXTRAINING3, Trait.PERFECTSLAVE, map);
			link(Trait.BASICTRAINING3, Trait.PERFECTSLAVE, map);
			link(Trait.BASICTRAINING3, Trait.TRUSTEDSLAVE, map);
			link(Trait.SPECIALIZATIONTRAINING3, Trait.TRUSTEDSLAVE, map);
			
		}
	}
	
	public static class DancerTreeStart extends SkillTreeItem {
		public DancerTreeStart() {
			super(Trait.NICEHIPS, new ImageData("images/icons/perks/lotus.png"));
			Map<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
			map.put(Trait.NICEHIPS, this);
			
			create(Trait.LEWD, map, new ImageData("images/icons/perks/beams-aura.png"));
			create(Trait.PURE, map, new ImageData("images/icons/perks/aura.png"));
			
			create(Trait.TEASER, map, new ImageData("images/icons/perks/wrapped-heart.png"));
			create(Trait.LASCIVIOUS, map, new ImageData("images/icons/perks/white-cat.png"));
			create(Trait.REFINED, map, new ImageData("images/icons/perks/flowers.png"));
			create(Trait.ORIENTALCHARMS, map, new ImageData("images/icons/perks/fragrance.png"));
			create(Trait.SMELLSLIKEKITTEN, map, new ImageData("images/icons/perks/smellslikekitten.png"));
			
			create(Trait.ACROBATICS, map, new ImageData("images/icons/perks/beanstalk.png"));
			create(Trait.PERFECTCONDITION, map, new ImageData("images/icons/perks/barefoot.png"));
			create(Trait.CROWDLOVER, map, new ImageData("images/icons/perks/dark-squad.png"));
			create(Trait.HORNY, map, new ImageData("images/icons/perks/love-song.png"));
			
			create(Trait.TRENDY, map, new ImageData("images/icons/perks/scale-mail.png"));
			create(Trait.SKINCARE, map, new ImageData("images/icons/perks/pollen-dust.png"));
			
			create(Trait.TARGETAUDIENCE, map, new ImageData("images/icons/perks/reticule.png"));
			create(Trait.TANLINES, map, new ImageData("images/icons/perks/burn.png"));
			
			create(Trait.THEUNTOUCHABLE, map, new ImageData("images/icons/perks/heart-tower.png"));
			
			create(Trait.EXTRAS, map, new ImageData("images/icons/perks/rose.png"));
			
			link(Trait.NICEHIPS, Trait.PURE, map);
			link(Trait.NICEHIPS, Trait.LEWD, map);
			
			link(Trait.PURE, Trait.TEASER, map);
			link(Trait.PURE, Trait.LASCIVIOUS, map);
			link(Trait.PURE, Trait.REFINED, map);
			link(Trait.PURE, Trait.ORIENTALCHARMS, map);
			
			link(Trait.ORIENTALCHARMS, Trait.SMELLSLIKEKITTEN, map);
			link(Trait.ACROBATICS, Trait.SMELLSLIKEKITTEN, map);
			link(Trait.SMELLSLIKEKITTEN, Trait.EXTRAS, map);
			link(Trait.SMELLSLIKEKITTEN, Trait.THEUNTOUCHABLE, map);
			
			
			link(Trait.LEWD, Trait.ACROBATICS, map);
			link(Trait.LEWD, Trait.PERFECTCONDITION, map);
			link(Trait.LEWD, Trait.CROWDLOVER, map);
			link(Trait.LEWD, Trait.HORNY, map);
			
			link(Trait.TEASER, Trait.TRENDY, map);
			link(Trait.LASCIVIOUS, Trait.TRENDY, map);
			
			link(Trait.REFINED, Trait.SKINCARE, map);
			link(Trait.ORIENTALCHARMS, Trait.SKINCARE, map);
			
			link(Trait.ACROBATICS, Trait.TARGETAUDIENCE, map);
			link(Trait.PERFECTCONDITION, Trait.TARGETAUDIENCE, map);
			
			link(Trait.CROWDLOVER, Trait.TANLINES, map);
			link(Trait.HORNY, Trait.TANLINES, map);
			
			link(Trait.TANLINES, Trait.EXTRAS, map);
			link(Trait.TRENDY, Trait.EXTRAS, map);
			
			link(Trait.TARGETAUDIENCE, Trait.THEUNTOUCHABLE, map);
			link(Trait.SKINCARE, Trait.THEUNTOUCHABLE, map);
			
		}
	}
	
	public static class NurseTreeStart extends SkillTreeItem {
		public NurseTreeStart() {
			super(Trait.BENEVOLENT, new ImageData("images/icons/perks/first_aid.png"));
			Map<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
			map.put(Trait.BENEVOLENT, this);
			
			create(Trait.BREWER, map, new ImageData("images/icons/perks/potion-ball.png"));
			
			create(Trait.TANTRIC, map, new ImageData("images/icons/perks/tantric.png"));
			create(Trait.MAGICALHEALING, map, new ImageData("images/icons/perks/heart-bottle.png"));
			create(Trait.LOVEANDCARE, map, new ImageData("images/icons/perks/love-song.png"));
			
			create(Trait.ONSENPRINCESS, map, new ImageData("images/icons/perks/naiad.png"));
			create(Trait.OILY, map, new ImageData("images/icons/perks/oily.png"));
			create(Trait.CASTCURE, map, new ImageData("images/icons/perks/health-increase.png"));
			create(Trait.FIRSTAID, map, new ImageData("images/icons/perks/first_aid.png"));
			create(Trait.COSMETICS, map, new ImageData("images/icons/perks/cosmetics.png"));
			
			create(Trait.SEXPERT, map, new ImageData("images/icons/perks/cheerful.png"));
			create(Trait.BLESSEDAURA, map, new ImageData("images/icons/perks/beams-aura.png"));
			

			
			
			link(Trait.BENEVOLENT, Trait.BREWER, map);
			
			link(Trait.BREWER, Trait.TANTRIC, map);
			link(Trait.BREWER, Trait.MAGICALHEALING, map);
			link(Trait.BREWER, Trait.LOVEANDCARE, map);
			
			link(Trait.TANTRIC, Trait.ONSENPRINCESS, map);
			link(Trait.TANTRIC, Trait.OILY, map);
			
			link(Trait.MAGICALHEALING, Trait.CASTCURE, map);
			link(Trait.MAGICALHEALING, Trait.FIRSTAID, map);
			
			link(Trait.LOVEANDCARE, Trait.COSMETICS, map);
			
			link(Trait.ONSENPRINCESS, Trait.SEXPERT, map);
			link(Trait.OILY, Trait.SEXPERT, map);
			link(Trait.COSMETICS, Trait.SEXPERT, map);
			
			link(Trait.CASTCURE, Trait.BLESSEDAURA, map);
			link(Trait.FIRSTAID, Trait.BLESSEDAURA, map);
			link(Trait.COSMETICS, Trait.BLESSEDAURA, map);


			
		}
		
	}
	
	public static class AlchemistTreeStart extends SkillTreeItem {
		public AlchemistTreeStart() {
			super(Trait.ERUDITE, new ImageData("images/icons/perks/pointy-hat.png"));
			Map<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
			map.put(Trait.ERUDITE, this);
			
			create(Trait.SELFTAUGHT, map, new ImageData("images/icons/perks/bookmark.png"));
			create(Trait.GATEOFTRUTH, map, new ImageData("images/icons/perks/magic-gate.png"));
			
			create(Trait.GREENTHUMB, map, new ImageData("images/icons/perks/pine-tree.png"));
			create(Trait.APHRODISIACS, map, new ImageData("images/icons/perks/drink-me.png"));
			create(Trait.FLOWERARRANGEMENT, map, new ImageData("images/icons/perks/beautician.png"));
			create(Trait.CALMINGINCENCES, map, new ImageData("images/icons/perks/lotus-flower.png"));
			
			create(Trait.DARKRITUAL, map, new ImageData("images/icons/perks/monsterpedia.png"));
			create(Trait.PERSONNALOFFERING, map, new ImageData("images/icons/perks/eclipse.png"));
			create(Trait.MANAFLOW, map, new ImageData("images/icons/perks/goo-explosion.png"));
			create(Trait.ETHERWALKER, map, new ImageData("images/icons/perks/wave-crest.png"));
			
			link(Trait.ERUDITE, Trait.SELFTAUGHT, map);
			link(Trait.ERUDITE, Trait.GATEOFTRUTH, map);
			
			link(Trait.SELFTAUGHT, Trait.GREENTHUMB, map);
			link(Trait.SELFTAUGHT, Trait.FLOWERARRANGEMENT, map);
			link(Trait.SELFTAUGHT, Trait.APHRODISIACS, map);
			link(Trait.SELFTAUGHT, Trait.CALMINGINCENCES, map);
			
			link(Trait.GATEOFTRUTH, Trait.DARKRITUAL, map);
			link(Trait.GATEOFTRUTH, Trait.PERSONNALOFFERING, map);
			link(Trait.GATEOFTRUTH, Trait.MANAFLOW, map);
			link(Trait.GATEOFTRUTH, Trait.ETHERWALKER, map);
			
		}
		
	}  

    
	public static class FurryTreeStart extends SkillTreeItem {
		public FurryTreeStart() {
			super(Trait.BESTIALFEATURES, new ImageData("images/icons/perks/bestialfeatures.png"));
			Map<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
			map.put(Trait.BESTIALFEATURES, this);
            
			create(Trait.FELINE, map, new ImageData("images/icons/perks/feline.png"));
			create(Trait.LAGOMORPH, map, new ImageData("images/icons/perks/lagomorph.png")); 
			
			/**
			 * Making all skills exclude each other
			 */

			

			
			
			map.get(Trait.FELINE).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LAGOMORPH));
			
			map.get(Trait.LAGOMORPH).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.FELINE));

			
			link(Trait.BESTIALFEATURES, Trait.FELINE, map);
			link(Trait.BESTIALFEATURES, Trait.LAGOMORPH, map);
			//link(Trait.BESTIALFEATURES, Trait.ARACHNID, map);
		}    
	}
    
	public static class DominatrixTreeStart extends SkillTreeItem {
		public DominatrixTreeStart() {
			super(Trait.GIVEANDTAKE, new ImageData("images/icons/perks/recycle.png"));
			Map<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
			map.put(Trait.GIVEANDTAKE, this);
            
			create(Trait.CRUELMASTER, map, new ImageData("images/icons/perks/crowned-heart.png"));
			create(Trait.HITMEHARDER, map, new ImageData("images/icons/perks/internal-injury.png"));
			create(Trait.PLEASURETHROUGHPAIN, map, new ImageData("images/icons/perks/ifrit.png"));
			create(Trait.PLEASUREANDPAIN, map, new ImageData("images/icons/perks/beastblood.png"));
			create(Trait.PLEASUREINPAIN, map, new ImageData("images/icons/perks/bleeding-heart.png"));
			create(Trait.MYWHIPISALLINEED, map, new ImageData("images/icons/perks/whip.png"));
			create(Trait.SADIST, map, new ImageData("images/icons/perks/what_doesnt_kill_you.png"));
			create(Trait.GOODMASTERSARETHEBESTSUBS, map, new ImageData("images/icons/perks/swap 2.png"));
			create(Trait.MASOCHIST, map, new ImageData("images/icons/perks/manacles.png"));
			create(Trait.LEATHERQUEEN, map, new ImageData("images/icons/perks/leather_queen.png"));
			create(Trait.EVERYONEBEHAVE, map, new ImageData("images/icons/perks/shouting.png"));
			create(Trait.AGGRESSIVEADVERTISEMENT, map, new ImageData("images/icons/perks/paranoia.png"));
			create(Trait.SQUEALANDHEAL, map, new ImageData("images/icons/perks/level-four-advanced.png"));
			create(Trait.LICKITUP, map, new ImageData("images/icons/perks/slurpyslurp.png"));
			create(Trait.LEATHERMISTRESS, map, new ImageData("images/icons/perks/slavery-whip.png"));
			create(Trait.WHATDOESNTKILLYOU, map, new ImageData("images/icons/perks/screaming.png"));
			create(Trait.MASTERANDSLAVE, map, new ImageData("images/icons/perks/chained-heart.png"));
            
			link(Trait.GIVEANDTAKE, Trait.CRUELMASTER, map);
			link(Trait.GIVEANDTAKE, Trait.HITMEHARDER, map);
            
			link(Trait.CRUELMASTER, Trait.PLEASURETHROUGHPAIN, map);
			link(Trait.CRUELMASTER, Trait.PLEASUREANDPAIN, map);
			link(Trait.HITMEHARDER, Trait.PLEASUREANDPAIN, map);
			link(Trait.HITMEHARDER, Trait.PLEASUREINPAIN, map);
            
			link(Trait.PLEASURETHROUGHPAIN, Trait.MYWHIPISALLINEED, map);
			link(Trait.PLEASURETHROUGHPAIN, Trait.SADIST, map);
			link(Trait.PLEASUREANDPAIN, Trait.SADIST, map);
			link(Trait.PLEASUREANDPAIN, Trait.GOODMASTERSARETHEBESTSUBS, map);
			link(Trait.PLEASUREANDPAIN, Trait.MASOCHIST, map);
			link(Trait.PLEASUREINPAIN, Trait.MASOCHIST, map);
			link(Trait.PLEASUREINPAIN, Trait.LEATHERQUEEN, map);
            
			link(Trait.MYWHIPISALLINEED, Trait.EVERYONEBEHAVE, map);
			link(Trait.SADIST, Trait.EVERYONEBEHAVE, map);
			link(Trait.SADIST, Trait.AGGRESSIVEADVERTISEMENT, map);
			link(Trait.GOODMASTERSARETHEBESTSUBS, Trait.AGGRESSIVEADVERTISEMENT, map);
			link(Trait.GOODMASTERSARETHEBESTSUBS, Trait.SQUEALANDHEAL, map);
			link(Trait.MASOCHIST, Trait.SQUEALANDHEAL, map);
			link(Trait.MASOCHIST, Trait.LICKITUP, map);
			link(Trait.LEATHERQUEEN, Trait.LICKITUP, map);
            
			link(Trait.EVERYONEBEHAVE, Trait.LEATHERMISTRESS, map);
			link(Trait.AGGRESSIVEADVERTISEMENT, Trait.LEATHERMISTRESS, map);
            
			link(Trait.AGGRESSIVEADVERTISEMENT, Trait.MASTERANDSLAVE, map);
			link(Trait.SQUEALANDHEAL, Trait.MASTERANDSLAVE, map);
            
			link(Trait.SQUEALANDHEAL, Trait.WHATDOESNTKILLYOU, map);
			link(Trait.LICKITUP, Trait.WHATDOESNTKILLYOU, map);
		}
	}
    

/*
    public static class MutantTreeStart extends SkillTreeItem {
        public MutantTreeStart() {
            super(Trait.CURSEDBLOOD, new ImageData("images/icons/perks/beastblood.png"));
            Map<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
            map.put(Trait.CURSEDBLOOD, this);
            
            create(Trait.THEBIGGERTHEYARE, map, new ImageData("images/icons/perks/thebiggertheyare.png"));
            create(Trait.FREAKY, map, new ImageData("images/icons/perks/pyromaniac.png"));
            create(Trait.MONSTERHUNTER, map, new ImageData("images/icons/perks/monsterhunter.png"));           
            create(Trait.IVESEENWORSE, map, new ImageData("images/icons/perks/iveseenworse.png"));
            create(Trait.MONSTERPEDIA, map, new ImageData("images/icons/perks/monsterpedia.png"));
            create(Trait.FASTMETABOLISM, map, new ImageData("images/icons/perks/meat.png"));
            create(Trait.WHENTHEHUNTERBECOMESPREY, map, new ImageData("images/icons/perks/when_the_hunter_become_prey.png"));            
            create(Trait.MONSTERSOW, map, new ImageData("images/icons/perks/brain-freeze.png"));
            create(Trait.BEASTBREEDER, map, new ImageData("images/icons/perks/beastbreeder.png"));
            create(Trait.CONSUMEANDADAPT, map, new ImageData("images/icons/perks/consumeandadapt.png"));            
            create(Trait.ADRENALINEADDICT, map, new ImageData("images/icons/perks/adrenalineaddict.png")); 
            create(Trait.NUMBERFORTYSEVEN, map, new ImageData("images/icons/perks/number_47.png"));
            create(Trait.RULESOFNATURE, map, new ImageData("images/icons/perks/rulesofnature.png"));
            create(Trait.PRIMALINSTINCTS, map, new ImageData("images/icons/perks/primal_instincts.png"));
            create(Trait.PHEROMONES, map, new ImageData("images/icons/perks/burning-passion.png"));
            create(Trait.LETSDOITLIKERABBITS, map, new ImageData("images/icons/perks/letsdoitlikerabbits.png"));
            create(Trait.CUMCOMBS, map, new ImageData("images/icons/perks/cumcombs.png"));
            create(Trait.OVIPOSITION, map, new ImageData("images/icons/perks/oviposition.png"));
            create(Trait.INHUMANPREGNANCY, map, new ImageData("images/icons/perks/inhumanpregnancy.png"));
            create(Trait.FLUCTUATINGHORMONES, map, new ImageData("images/icons/perks/fluctuatinghormones.png"));
            create(Trait.INHERITANCE, map, new ImageData("images/icons/perks/inheritance.png"));
            create(Trait.TOPOFTHEFOODCHAIN, map, new ImageData("images/icons/perks/topofthefoodchain.png"));
            create(Trait.HONEYLACTATION, map, new ImageData("images/icons/perks/shesalreadyfull.png"));
            create(Trait.BESTIALFEATURES, map, new ImageData("images/icons/perks/iswearimonthepill.png"));
            create(Trait.RAINBOWFEATHERS, map, new ImageData("images/icons/perks/motherlywarmth.png"));
            create(Trait.HEARTOFTHESWARM, map, new ImageData("images/icons/perks/heartoftheswarm.png"));
            create(Trait.THEBEASTWITHIN, map, new ImageData("images/icons/perks/thebeastwithin.png"));
            create(Trait.BEASTINHEAT, map, new ImageData("images/icons/perks/beastinheat.png"));
            create(Trait.HIBERNATION, map, new ImageData("images/icons/perks/hibernation.png"));
            create(Trait.TRUECAT, map, new ImageData("images/icons/perks/truecat.png"));
            create(Trait.RELENTLESSBEAST, map, new ImageData("images/icons/perks/parmecia.png"));
            create(Trait.EXTRACTABLECLAWS, map, new ImageData("images/icons/perks/claws.png"));
            create(Trait.FLAMEBREATH, map, new ImageData("images/icons/perks/draconiclungs.png"));
            
            link(Trait.CURSEDBLOOD, Trait.LETSDOITLIKERABBITS, map);
            link(Trait.CURSEDBLOOD, Trait.FREAKY, map);
            link(Trait.CURSEDBLOOD, Trait.MONSTERHUNTER, map);  
            
            link(Trait.LETSDOITLIKERABBITS, Trait.CUMCOMBS, map);
            link(Trait.LETSDOITLIKERABBITS, Trait.RAINBOWFEATHERS, map);
            
            link(Trait.FREAKY, Trait.PHEROMONES, map);
            //link(Trait.FREAKY, Trait.CUMCOMBS, map);
            link(Trait.FREAKY, Trait.THEBEASTWITHIN, map);
            link(Trait.FREAKY, Trait.RAINBOWFEATHERS, map);

            link(Trait.MONSTERHUNTER, Trait.THEBEASTWITHIN, map); 
            link(Trait.MONSTERHUNTER, Trait.FASTMETABOLISM, map);  
            
            link(Trait.CUMCOMBS, Trait.INHUMANPREGNANCY, map);
            
            link(Trait.RAINBOWFEATHERS, Trait.INHUMANPREGNANCY, map);
            link(Trait.RAINBOWFEATHERS, Trait.TRUECAT, map);
            //link(Trait.CUMCOMBS, Trait.TRUECAT, map);
            link(Trait.PHEROMONES, Trait.TRUECAT, map);
            link(Trait.PHEROMONES, Trait.RELENTLESSBEAST, map);
            
            link(Trait.THEBEASTWITHIN, Trait.MONSTERPEDIA, map);
            link(Trait.THEBEASTWITHIN, Trait.RELENTLESSBEAST, map); 
            
            link(Trait.FASTMETABOLISM, Trait.MONSTERPEDIA, map);
            
 
            link(Trait.TRUECAT, Trait.BESTIALFEATURES, map);
            link(Trait.TRUECAT, Trait.IVESEENWORSE, map);
         
            link(Trait.RELENTLESSBEAST, Trait.IVESEENWORSE, map);
            link(Trait.RELENTLESSBEAST, Trait.THEBIGGERTHEYARE, map);
            
            link(Trait.INHUMANPREGNANCY, Trait.BEASTBREEDER, map);
            link(Trait.INHUMANPREGNANCY, Trait.OVIPOSITION, map);
            link(Trait.INHUMANPREGNANCY, Trait.BESTIALFEATURES, map);
            
            link(Trait.MONSTERPEDIA, Trait.THEBIGGERTHEYARE, map); 
            link(Trait.MONSTERPEDIA, Trait.TOPOFTHEFOODCHAIN, map); 
            link(Trait.MONSTERPEDIA, Trait.PRIMALINSTINCTS, map);
 
            link(Trait.BEASTBREEDER, Trait.INHERITANCE, map);
            link(Trait.OVIPOSITION, Trait.INHERITANCE, map);
            
            link(Trait.OVIPOSITION, Trait.FLUCTUATINGHORMONES, map);
            link(Trait.BESTIALFEATURES, Trait.FLUCTUATINGHORMONES, map);
            
            link(Trait.IVESEENWORSE, Trait.HIBERNATION, map);
            link(Trait.IVESEENWORSE, Trait.CONSUMEANDADAPT, map);
            
            link(Trait.BESTIALFEATURES, Trait.HIBERNATION, map);
            
            link(Trait.HIBERNATION, Trait.MONSTERSOW, map);
            
            link(Trait.THEBIGGERTHEYARE, Trait.RULESOFNATURE, map);
            link(Trait.THEBIGGERTHEYARE, Trait.CONSUMEANDADAPT, map);
            
            link(Trait.TOPOFTHEFOODCHAIN, Trait.RULESOFNATURE, map);     

            link(Trait.CONSUMEANDADAPT, Trait.EXTRACTABLECLAWS, map);     
            
            link(Trait.EXTRACTABLECLAWS, Trait.FLAMEBREATH, map);                   
            
            link(Trait.TOPOFTHEFOODCHAIN, Trait.WHENTHEHUNTERBECOMESPREY, map);
            link(Trait.PRIMALINSTINCTS, Trait.WHENTHEHUNTERBECOMESPREY, map);
   
            link(Trait.INHERITANCE, Trait.HONEYLACTATION, map); 
            link(Trait.FLUCTUATINGHORMONES, Trait.HONEYLACTATION, map);           
            
            link(Trait.RULESOFNATURE, Trait.ADRENALINEADDICT, map); 
            link(Trait.WHENTHEHUNTERBECOMESPREY, Trait.ADRENALINEADDICT, map); 
 
            link(Trait.HONEYLACTATION, Trait.HEARTOFTHESWARM, map);            
            
            link(Trait.ADRENALINEADDICT, Trait.NUMBERFORTYSEVEN, map); 
 
            link(Trait.MONSTERSOW, Trait.BEASTINHEAT, map);  
        }    
    }
  */   
}