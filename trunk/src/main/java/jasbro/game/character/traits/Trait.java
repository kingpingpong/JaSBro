package jasbro.game.character.traits;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.battle.Attack;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.interfaces.MinObedienceModifier;
import jasbro.game.interfaces.Person;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public enum Trait implements MinObedienceModifier {
    LOVELY(200, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.CHARISMA, 0.3f)), 
    FIT(200, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.STAMINA, 0.3f)), 
    STRONG(200, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.STRENGTH, 0.3f)), 
    CLEVER(200, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.INTELLIGENCE, 0.3f)),
    LEADER(200, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.COMMAND, 0.3f)), 
    UGLY(-100, LOVELY, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.CHARISMA, -0.3f)), 
    UNFIT(-100, FIT, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.STAMINA, -0.3f)), 
    WEAK(-100, STRONG, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.STRENGTH, -0.3f)), 
    STUPID(-100, CLEVER, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.INTELLIGENCE, -0.3f)),
    FOLLOWER(-100, LEADER, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.COMMAND, -0.3f)), 
    
    FRIGID(-100, new TraitEffect.MultipleAttributeChangeInfluence(-0.05f, Sextype.values())),
    NATURAL(200, FRIGID, new TraitEffect.MultipleAttributeChangeInfluence(0.05f, Sextype.values())),
    NUMB(200, new TraitEffect.Numb()),
    SENSITIVE(-200, NUMB, new TraitEffect.Sensitive()),
    BITER(200, new TraitEffect.Biter()),
    SENSUALTONGUE(-200, BITER, new TraitEffect.SensualTongue()),  
    DEADFISH(200, new TraitEffect.DeadFish()),
    AMBITOUSLOVER(-200, DEADFISH, new TraitEffect.AmbitousLover()),   
    SINGLEMINDED(200, new TraitEffect.SingleMinded()),
    MULTIFACETED(-200, SINGLEMINDED, new TraitEffect.Multifaceted()),  
    // clumsy trait, don't let her touch those plates.
    CLUMSY(-300, new TraitEffect.MultipleAttributeChangeInfluence(-0.1f, SpecializationAttribute.COOKING,
            SpecializationAttribute.CLEANING, SpecializationAttribute.BARTENDING)),
    // helpful trait, girl is good at doing stuff
    HELPFUL(400, new TraitEffect.MultipleAttributeChangeInfluence(0.1f, SpecializationAttribute.COOKING,
            SpecializationAttribute.CLEANING, SpecializationAttribute.BARTENDING)),
    PERSONALMAID(200, new TraitEffect.MultipleAttributeChangeInfluence(0.3f, SpecializationAttribute.CLEANING)),	
    DIRTYDEVIL(-200, PERSONALMAID, new TraitEffect.MultipleAttributeChangeInfluence(-0.3f, SpecializationAttribute.CLEANING)),                                       

    RESTAURATEUR(200, new TraitEffect.MultipleAttributeChangeInfluence(0.3f, SpecializationAttribute.COOKING)),	
    COOKINGDISASTER(-200, RESTAURATEUR, new TraitEffect.MultipleAttributeChangeInfluence(-0.3f, SpecializationAttribute.COOKING)),  
    ALTRUISTIC(200, new TraitEffect.MultipleAttributeChangeInfluence(0.3f, SpecializationAttribute.WELLNESS,
            SpecializationAttribute.MEDICALKNOWLEDGE)), 
    SELFISH(-100, ALTRUISTIC, new TraitEffect.MultipleAttributeChangeInfluence(-0.3f, SpecializationAttribute.WELLNESS,
            SpecializationAttribute.MEDICALKNOWLEDGE)),
    FURFANATIC(200, new TraitEffect.MultipleAttributeChangeInfluence(0.3f, SpecializationAttribute.CATGIRL)),	
    PROHUMANIST(-200, FURFANATIC, new TraitEffect.MultipleAttributeChangeInfluence(-0.3f, SpecializationAttribute.CATGIRL)),  
    WILLINGSUBJECT(200, new TraitEffect.MultipleAttributeChangeInfluence(0.3f, Sextype.BONDAGE)),	
    RESISTANTVICTIM(-200, WILLINGSUBJECT, new TraitEffect.MultipleAttributeChangeInfluence(-0.3f, Sextype.BONDAGE)),    
            
    
	BIGBOOBS(150, new TraitEffect.BigBoobs()), 
	SMALLBOOBS(-50, new TraitEffect.SmallBoobs()), 
	LOLI(200, new TraitEffect.Loli()),
	SHOTA(200, new TraitEffect.Loli()),	
	FURRY(150, new TraitEffect.Furry()),	
	
	FEISTY(-300, new TraitEffect.Feisty()), 
	OBEDIENT(300, FEISTY, new TraitEffect.Obedient()), 
	NYMPHO(300, new TraitEffect.Nympho()),
	
	HEALTHY(400, new TraitEffect.AttributeMaxModifier(EssentialAttributes.HEALTH, 10)), 
	SICKLY(-200, HEALTHY, new TraitEffect.AttributeMaxModifier(EssentialAttributes.HEALTH, -10)), 
	PERSEVERING(400, new TraitEffect.AttributeMaxModifier(EssentialAttributes.ENERGY, 10)), 
	FLABBY(-300, PERSEVERING, new TraitEffect.AttributeMaxModifier(EssentialAttributes.HEALTH, -10)),

	OPENMINDED(-200, new TraitEffect.OpenMinded()), 
	RESERVED(200, OPENMINDED, new TraitEffect.Reserved()),	
    NIMBLEFINGERS(200, new TraitEffect.MultipleAttributeChangeInfluence(0.3f, SpecializationAttribute.PICKPOCKETING)),	
    GRUBBYMITTS(-200, NIMBLEFINGERS, new TraitEffect.MultipleAttributeChangeInfluence(-0.3f, SpecializationAttribute.PICKPOCKETING)),
	SHY(-200, new TraitEffect.Shy()), 
	UNINHIBITED(200, SHY, new TraitEffect.Uninhibited()),
	NICEBODY(100, new TraitEffect.Nicebody()), 
	STIFF(-100, new TraitEffect.AttributeChangeInfluence(SpecializationAttribute.STRIP, -0.3f)),
	SLUT(300, new TraitEffect.Slut()), 
	FRAGILE(-300, new TraitEffect.Fragile()),
	WILD(200, new TraitEffect.Wild()),
	TSUNDERE(-150, new TraitEffect.Tsundere()),
	ABSORPTION(100, new TraitEffect.Absorption()),
	LOYAL, RAPACIOUS(LOYAL),
	FORMERNOBLE(1000), RARESLAVE(1000), EXTREMELYRARESLAVE(100000), EXTREMELYRARESLAVE2(1000000),
    
	//Kinky Tree
	PERVERT(true,100, new Perks.Pervert()),
  	KINKY(true,100, new Perks.Kinky()),
  	MEATTOILET(true,100, new Perks.MeatToilet()),
  	SEXADDICT(true,100, new Perks.SexAddict()),
  	INSATIABLE(true, new Perks.AttributeMaxInfluence(EssentialAttributes.ENERGY, BaseAttributeTypes.STAMINA, 2)),
  	SUBMISSIVE(true, 300, new Perks.Submissive()),
  	MASOCHIST(true, 300, new Perks.Masochist()),
  	EXHIBITIONIST(true, 300, new Perks.Exhibitionist()),
  	SEXMANIAC(true, 300, new Perks.SexManiac()),
  	SEXFREAK(true, 400, new TraitEffect.MultipleAttributeChangeInfluence(0.9f, Sextype.GROUP, Sextype.MONSTER, Sextype.BONDAGE)),
  	CUMSLUT(true, 300, new Perks.CumSlut()),
  	LEATHERMISTRESS(true, 300),
  	PUBLICUSE(true,100), //I have no idea how I could do that... Soldiers and lower cost no energy and only count as 1/4th of an action. Groups ratings set to 0 (won't serve groups) 
  	BREEDER(true, 300, new Perks.Breeder()),
  	GANGBANGQUEEN(true, 300, new Perks.GangbangQueen()),
  	LEATHERQUEEN(true, 300, new Perks.LeatherQueen()),
  	FLESHTOY(true, 300, new Perks.FleshToy()),
  	WHATDOESNTKILLYOU(true, 300, new Perks.KillYou()),
  	
	//Mutant Tree
  	CURSEDBLOOD(true, new TraitEffect.AttributeMaxModifier(EssentialAttributes.HEALTH, 10)), //done
  	THEBIGGERTHEYARE(true,100, new Perks.TheBiggerTheyAre()), //done
  	FREAKY(true,400, new TraitEffect.MultipleAttributeChangeInfluence(0.2f, Sextype.MONSTER, SpecializationAttribute.GENETICADAPTABILITY)), //done
  	MONSTERHUNTER(true,100, new Perks.MonsterHunter()), //done
  	IVESEENWORSE(true,100, new Perks.IveSeenWorse()), // to stupid to get it to work...  Energy loss during whoring-40%, max customers+4. 20% chance to be kinda apathetic (satisfaction-100, flavor text)
  	MONSTERPEDIA(true, 200,new Perks.Monsterpedia()), //done
  	CONSUMEANDADAPT(true,1000), //customer satisfaction bonus is still missing
  	RULESOFNATURE(true, 200, new Perks.RulesOfNature()), //done
  	WHENTHEHUNTERBECOMESPREY(true, 200), //customer satisfaction bonus after a win is missing
  	MONSTERSOW(true,100, new Perks.MonsterSow()), //done
  	BEASTBREEDER(true, 300), // done / Thanks Teferus ^^
  	ADRENALINEADDICT(true, 300), //increased reward/customer satisfaction missing
  	TONIGHTWEDINEONMEAT(true, 300), // not started yet
  	PHEROMONES(true, 300), //done
  	NUMBERFORTYSEVEN(true, 300, new Perks.NumberFortySeven()), //done
  	PRIMALINSTINCTS(true, 300, new Perks.PrimalInstincts()), //done
  	LETSDOITLIKERABBITS(true, 300, new Perks.LetsDoItLikeRabbits()), //done
  	CUMCOMBS(true, 300, new Perks.CumCombs()), //done
  	OVIPOSITION(true, 300, new Perks.Oviposition()),
  	INHUMANPREGNANCY(true, 300, new Perks.InhumanPregnancy()), //done
  	FLUCTUATINGHORMONES(true, 300, new Perks.FluctuatingHormones()), //started, but not finished
  	INHERITANCE(true, 300),
  	TOPOFTHEFOODCHAIN(true, 300),
  	SHESALREADYFULL(true, 300),
  	ISWEARIMONTHEPILL(true, 300),
  	MOTHERLYWARMTH(true, 300),
  	HEARTOFTHESWARM(true, 300),
  	THEBEASTWITHIN(true, 300),
  	BEASTINHEAT(true, 300, new Perks.BeastInHeat()), //done
  	HIBERNATION(true, 300, new Perks.Hibernation()), //done
  	TRUECAT(true, new Perks.AttributeInfluence(SpecializationAttribute.CATGIRL, SpecializationAttribute.GENETICADAPTABILITY, 0.3f)), //done
  	RELENTLESSBEAST(true, 300, new Perks.RelentlessBeast()), //flavor text missing
  	FLAMEBREATH(true, 300, new Perks.FlameBreath()), //done
  	EXTRACTABLECLAWS(true, 100, new Perks.ExtractableClaws()), //done
    
  	//Maid tree		
  	PRACTICAL(true, new Perks.AttributeInfluence(SpecializationAttribute.CLEANING, BaseAttributeTypes.INTELLIGENCE, 5)),
  	DIETEXPERT(true, 300, new Perks.DietExpert()),
  	MOTHERLYCARE(true, 300, new Perks.MotherlyCare()),
  	TIMEMANIPULATION(true,100, new Perks.TimeManipulation()),
  	PROFESSIONAL(true,100, new Perks.Professional()),
  	CHEF(true,100, new Perks.Chef()),
  	COMPULSIVECLEANER(true, 300, new Perks.CompulsiveCleaner()),
  	ALWAYSIMPROVE(true, 300, new Perks.AlwaysImprove()),
  	HOUSEFAIRY(true, 300, new Perks.HouseFairy()),
  	WASHINGANDIRONING(true, 300, new Perks.WashingAndIroning()),
  	ELEGANT(true,1000, new Perks.Elegant()), 
  	CORDONBLEU(true, 300, new Perks.CordonBleu()),
  	
	//Whore Tree
  	ONENIGHT(true,100, new Perks.OneNight()),  	
  	SEDUCTRESS(true, new Perks.AttributeInfluence(BaseAttributeTypes.CHARISMA, SpecializationAttribute.SEDUCTION, 0.1f)),
  	ENDURANCE(true,100, new Perks.Endurance()),
  	CHATTY(true,100, new Perks.Chatty()),
  	COMPETITIVE(true,100, new Perks.Competitive()),
  	COUPLEMORE(true,100, new Perks.SimpleTraitEffect(2.d, null, CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT)),
  	SLOPPY(true,100, new Perks.Sloppy()),
  	SITBACK(true,100, new Perks.SitBack()),
  	FIRST(true,100, new Perks.MyFirst()),
  	WENCH(true, new Perks.AttributeInfluence(SpecializationAttribute.BARTENDING, SpecializationAttribute.SEDUCTION, 0.25f)), 
  	STYLISH(true, 100, new Perks.BeautySleep()),
  	JUSTYOUANDME(true,100, new Perks.YouAndMe()),
  	NUTBUSTER(true,1000), 
  	TAKEOURTIME(true,100, new Perks.OurTime()),
  	QUICKIE(true,100),
  	KEEPEMCOMING(true,100, new Perks.KeepEmComing()),
  	THENIGHTISSTILLYOUNG(true,100, new Perks.Renowed()),
  	NONNEGOCIABLE(true,100, new Perks.HighClass()),
  	STREETSMARTS(true,1000), THATGIRL(true,1000), 
  			
  	//Dancer Tree
  	PURE(true, new Perks.AttributeInfluence(SpecializationAttribute.STRIP, BaseAttributeTypes.CHARISMA, 2)),
    LEWD(true, new Perks.AttributeInfluence(SpecializationAttribute.STRIP, SpecializationAttribute.SEDUCTION, 0.3f)),
    CROWDLOVER(true,100, new Perks.CrowdLover()),
    REFINED(true,100, new Perks.Refined()),
    LASCIVIOUS(true,100, new Perks.Lascivious()),
    SMELLSLIKEKITTEN(true,100),
    TRENDY(true,100, new Perks.Trendy()),
    TANLINES(true,100, new Perks.TanLines()),
    SKINCARE(true,100, new Perks.SkinCare()),
    TEASER(true,100, new Perks.Teaser()),
    ACROBATICS(true,100, new Perks.Acrobatics()),
    NICEHIPS(true,100, new TraitEffect.AttributeMaxModifier(BaseAttributeTypes.CHARISMA, 10)),
    PERFECTCONDITION(true, 400, new TraitEffect.MultipleAttributeChangeInfluence(0.3f, BaseAttributeTypes.STAMINA, BaseAttributeTypes.CHARISMA)),
    ORIENTALCHARMS(true,100, new Perks.OrientalCharms()),
    THEUNTOUCHABLE(true,100, new Perks.TheUntouchable()),
	EXTRAS(true,100),
	HORNY(true,100, new Perks.Horny()),
	TARGETAUDIENCE(true,100, new Perks.TargetAudience()),
	
	//Bartender
	CLASSY(true, 200, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.CHARISMA, 0.3f)), 
	LIQUORMASTER(true,100, new Perks.LiquorMaster()),
	MULTITASKING(true,100, new Perks.Multitasking()),
	THECONFIDENT(true,100, new Perks.TheConfident()),
	FLIRTY(true,100),
	DATASS(true,100),
	CATMAID(true,100, new Perks.CatMaid()),
	UNDERTHETABLE(true,100),
	OUTGOING(true,100, new Perks.Outgoing()),
    
    //Veteran Tree
    SHARPSENSES(true,100, new Perks.SimpleTraitEffect(5.d, null, CalculatedAttribute.HIT, CalculatedAttribute.DODGE)),
    WEAPONMASTERY(true,100, new Perks.SimpleTraitEffect(null, 1.0, CalculatedAttribute.DAMAGE)),
    MINDOFTHEFIGHTER(true,100, new Perks.MindOfTheFighter()),
    ELEMENTALSTUDY(true,100, new Perks.ElementalStudy()),
    TOUGH(true,  new TraitEffect.AttributeMaxModifier(EssentialAttributes.HEALTH, 15)), 
    DISTRACTION(true,100, new Perks.Distraction()),
    PLUNDERER(true,100, new Perks.Plunderer()),
    LOSTARTS(true,100, new Perks.SimpleTraitEffect(10.d, null, CalculatedAttribute.HIT, CalculatedAttribute.DAMAGE, CalculatedAttribute.SPEED, CalculatedAttribute.CRITCHANCE)),
    VITALPOINTSPIERCING(true,100, new Perks.SimpleTraitEffect(15.d, null, CalculatedAttribute.CRITDAMAGEAMOUNT)),
    CASTTIME(true,100, new Perks.CastTime()),
    IRONBODY(true,100, new Perks.IronBody()),
    ETHERSHIELD(true,100, new Perks.EtherShield()),
    MKIIWALKER(true,100, new Perks.SimpleTraitEffect(40.d, null, CalculatedAttribute.BLOCKAMOUNT)),
    SHOWTIME(true,1000),
    
    //Thief tree
    GREEDY(true,100, new Perks.SimpleTraitEffect(25.d, null, CalculatedAttribute.STEALAMOUNTMODIFIER)),
    NIMBLEHANDS(true,100, new Perks.SimpleTraitEffect(20.d, null, CalculatedAttribute.STEALCHANCE)),
    PICKPOCKET(true,100, new Perks.SimpleTraitEffect(10.d, null, CalculatedAttribute.STEALITEMCHANCE)),
    ROGUE(true,100, new Perks.SimpleTraitEffect(4.d, null, CalculatedAttribute.CRITCHANCE, CalculatedAttribute.DAMAGE, CalculatedAttribute.DODGE)),
    LUPIN(true, new Perks.AttributeInfluence(SpecializationAttribute.PICKPOCKETING, BaseAttributeTypes.INTELLIGENCE, 2)),
    BANDIT(true, 400, new TraitEffect.MultipleAttributeChangeInfluence(0.3f, BaseAttributeTypes.STAMINA, BaseAttributeTypes.STRENGTH)),
    CONARTIST(true, new Perks.AttributeInfluence(SpecializationAttribute.PICKPOCKETING, BaseAttributeTypes.CHARISMA, 2.5f)),
    RESELLER(true,1000),
    LIAISONSDANGEREUSES(true, new Perks.AttributeInfluence(SpecializationAttribute.PICKPOCKETING, SpecializationAttribute.SEDUCTION, 0.3f)),
    NIGHTSHADE(true,100, new Perks.NightShade()), 
    SHADOWBODY(true,100, new Perks.ShadowBody()),
    DOUBLEVIE(true,100, new Perks.DoubleVie()),
    ASSASSIN(true,100, new Perks.SimpleTraitEffect(30.d, null, CalculatedAttribute.CRITDAMAGEAMOUNT)),
    PHANTOMTHIEF(true,100, new TraitEffect.AttributeMaxModifier(SpecializationAttribute.PICKPOCKETING, 300)),

    //Catgirl Tree
    GROOMING(true, new Perks.AttributeInfluence(BaseAttributeTypes.CHARISMA, SpecializationAttribute.CATGIRL, 0.1f)),
    STRAYCAT(true, new Perks.AttributeInfluence(BaseAttributeTypes.OBEDIENCE, SpecializationAttribute.CATGIRL, -0.1f)),
    DOMESTICATED(true, new Perks.AttributeInfluence(BaseAttributeTypes.OBEDIENCE, SpecializationAttribute.CATGIRL, 0.1f)),
    CATSANDRATS(true, new Perks.AttributeInfluence(BaseAttributeTypes.STAMINA, SpecializationAttribute.CATGIRL, 0.1f)),
    CUNNING(true, new Perks.AttributeInfluence(BaseAttributeTypes.INTELLIGENCE, SpecializationAttribute.CATGIRL, 0.1f)),
    PAWERFUL(true, new Perks.AttributeInfluence(BaseAttributeTypes.STRENGTH, SpecializationAttribute.CATGIRL, 0.1f)),
    ALLPURRPOSE(true, new Perks.AttributeInfluence(SpecializationAttribute.CLEANING, SpecializationAttribute.CATGIRL, 0.1f)),
    CATBURGLAR(true, new Perks.AttributeInfluence(SpecializationAttribute.PICKPOCKETING, SpecializationAttribute.CATGIRL, 0.1f)),
    WETPUSSY(true, new Perks.AttributeInfluence(SpecializationAttribute.SEDUCTION, SpecializationAttribute.CATGIRL, 0.1f)),
    CATTRACTIVE(true, new Perks.AttributeInfluence(SpecializationAttribute.ADVERTISING, SpecializationAttribute.CATGIRL, 0.1f)),
    CATWALK(true, new Perks.AttributeInfluence(SpecializationAttribute.STRIP, SpecializationAttribute.CATGIRL, 0.1f)),
    HIGHCATNESSRATING(true, 400, new TraitEffect.AttributeMaxModifier(SpecializationAttribute.CATGIRL, 96)), 
    AGILITY(true,100, new Perks.Agility()),
    NOCTURNAL(true,100, new Perks.Nocturnal()),
    CATNAP(true,100, new Perks.CatNap()),
    CATPUN(true,100, new Perks.CatPun()),
    CATINHEAT(true,100, new Perks.CatInHeat()),
    CATSAREASSHOLES(true,100),
    
    //Trainer / Slave tree
    CERTIFIEDTRAINER(true, 500, new TraitEffect.AllAttributeChangeInfluence(0.3f)),
    GOODSLAVE(true, 50, new Perks.GoodSlave()),
    BASICTRAINING1(true, 100, new TraitEffect.BaseAttributeChangeInfluence(0.1f)),
    BASICTRAINING2(true, 100, new TraitEffect.BaseAttributeChangeInfluence(0.1f)),
    BASICTRAINING3(true, 100, new TraitEffect.BaseAttributeChangeInfluence(0.1f)),
    SEXTRAINING1(true, 100, new TraitEffect.SexAttributeChangeInfluence(0.1f)),
    SEXTRAINING2(true, 100, new TraitEffect.SexAttributeChangeInfluence(0.1f)),
    SEXTRAINING3(true, 100, new TraitEffect.SexAttributeChangeInfluence(0.1f)),
    SPECIALIZATIONTRAINING1(true, 100, new TraitEffect.SpecializationAttributeChangeInfluence(0.1f)),
    SPECIALIZATIONTRAINING2(true, 100, new TraitEffect.SpecializationAttributeChangeInfluence(0.1f)),
    SPECIALIZATIONTRAINING3(true, 100, new TraitEffect.SpecializationAttributeChangeInfluence(0.1f)),
    PERFECTTRAINER(true, 500, new TraitEffect.AllAttributeChangeInfluence(0.3f)),
    PERFECTSLAVE(true, 500, new TraitEffect.AllAttributeChangeInfluence(0.3f)),
    TRAINING(true, 100, new TraitEffect.AllAttributeChangeInfluence(0.05f)),
    EFFECTIVETRAINER(true, 100, new Perks.EffectiveTrainer()),
    MOTIVATOR(true, 100, new Perks.Motivator()),
    RESPECTED(true, 100, new TraitEffect.InfluenceAttributeLoss(BaseAttributeTypes.COMMAND, -0.5f)),
    ONEOFUS(true, 100, new TraitEffect.CancelAttributeLoss(BaseAttributeTypes.COMMAND)),
    STRONGPRESENCE(true, 100, new Perks.SimpleTraitEffect(10d, null, CalculatedAttribute.CONTROL)),
    EYESEVERYWHERE(true, 100, new Perks.SimpleTraitEffect(20d, null, CalculatedAttribute.CONTROL)),
    SPYNETWORK(true, 100, new Perks.SimpleTraitEffect(50d, null, CalculatedAttribute.CONTROL)),
    CONTENTPERK(true, 100, new Perks.SimpleTraitEffect(3d, null, CalculatedAttribute.CONTROL)),
    TRUSTEDSLAVE(true, 100, new Perks.SimpleTraitEffect(10d, null, CalculatedAttribute.CONTROL)),
    
    //Sex tree
    DEBUTANTE(true, 100, new TraitEffect.SexAttributeChangeInfluence(0.1f)),
    SENSITIVECLIT(true, 100, new TraitEffect.AttributeChangeInfluence(Sextype.VAGINAL, 0.2f)),
    DEEPLOVE(true, 100, new TraitEffect.AttributeChangeInfluence(Sextype.ANAL, 0.2f)),
    LOVETHETASTE(true, 100, new TraitEffect.AttributeChangeInfluence(Sextype.ORAL, 0.2f)),
    AFLEURDEPEAU(true, 100, new TraitEffect.AttributeChangeInfluence(Sextype.FOREPLAY, 0.2f)),
    MEATBUNS(true, 100, new TraitEffect.AttributeChangeInfluence(Sextype.TITFUCK, 0.2f)),
    COZYCUNT(true, 100, new Perks.CozyCunt()),
    ROWDYRUMP(true, 100, new Perks.RowdyRump()),
    SLURPYSLURP(true, 100, new Perks.SlurpySlurp()),
    TOUCHYFEELY(true, 100, new Perks.TouchyFeely()),
    PUFFPUFF(true, 100, new Perks.PuffPuff()),
    BEDROOMPRINCESS(true, 100, new Perks.BedroomPrincess()),
    WETFORYOU(true, 100, new Perks.WetForYou()),
    BACKDOOROPEN(true, 100, new Perks.BackdoorOpen()),
    THIRSTY(true, 100, new Perks.Thirsty()),
    FEELMEUP(true, 100, new Perks.FeelMeUp()),
    COMETOMOMMY(true, 100, new Perks.ComeToMommy()),
    STEAMY(true, 100, new Perks.Steamy()),
    BEDGODDESS(true, 100, new Perks.BedGoddess()),
    
    //Advertising tree
    INITIATE(true, 100, new TraitEffect.AttributeChangeInfluence(SpecializationAttribute.ADVERTISING, 0.2f)),
    SAMPLINGTHEGOODS(true, 1000),//whore
    SHOWINGTHEGOODS(true, 1000),//strip
    SHOWOFF(true, 1000),//fighter
    CATCHY(true, 1000),//cat
    HEYGUYSBOOSE(true, 1000),//bartend
    SPIRITED(true, 100, new Perks.Spirited()),
    TARGETBUM(true, 100, new Perks.TargetBum()),
    TARGETPEASANT(true, 100, new Perks.TargetPeasants()),
    TARGETSOLDIER(true, 100, new Perks.TargetSoldiers()),
    TARGETBUSINESSMEN(true, 100, new Perks.TargetBusinessmen()),
    CONFIRMEDSALESPERSON(true, 100, new Perks.Salesperson()),
    SALESPROMOTION(true, new Perks.AttributeInfluence( SpecializationAttribute.ADVERTISING,BaseAttributeTypes.INTELLIGENCE, 0.1f)),
    WHATABOUTSOMEPUSSY(true, 100, new Perks.OverwriteVaginal()),
    WHATABOUTSOMEASS(true, 100, new Perks.OverwriteAnal()),
    WHATABOUTABLOWJOB(true, 100, new Perks.OverwriteOral()),
    WHATABOUTSOMECUDDLING(true, 100, new Perks.OverwriteForeplay()),
    WHATABOUTSOMEPUFFPUFF(true, 100, new Perks.OverwriteTitfuck()),
    WHATABOUTSOMEWEIRDSTUFF(true, 100, new Perks.OverwriteBondage()),
    WHATABOUTSOMEHARDCORESTUFF(true, 100, new Perks.OverwriteMonster()),
    NICHEMARKETINGNOBLE(true, 100, new Perks.TargetNobles()),
    NICHEMARKETINGLORD(true, 100, new Perks.TargetLords()),
    NICHEMARKETINGCELEBRITY(true, 100, new Perks.TargetCelebrities()),
    NICHEMARKETINGGROUPS(true, 100, new Perks.TargetGroups()),
    RECOGNIZED(true,100, new Perks.Recognized()),
    BUSINESSRELATIONS(true, 100),
    
  //Alchemist tree
    ERUDITE(true, 100, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.INTELLIGENCE, 0.3f)),
    SELFTAUGHT(true,100, new Perks.SelfTaught()),
    GATEOFTRUTH(true, 400, new TraitEffect.MultipleAttributeChangeInfluence(1.5f, SpecializationAttribute.PLANTKNOWLEDGE, SpecializationAttribute.ALCHEMY)),
    GREENTHUMB(true,100, new Perks.GreenThumb()),
    FLOWERARRANGEMENT(true,100),
    APHRODISIACS(true,100, new Perks.Aphrodisiacs()),
    CALMINGINCENCES(true,100, new Perks.RelaxingIncences()),
    
    // Nurse tree
    FIRSTAID(true, 100, new TraitEffect.AttributeChangeInfluence(SpecializationAttribute.MEDICALKNOWLEDGE, 0.2f)), 
    INDULGENCE(true, 100, new Perks.Indulgence()), 
    TANTRIC(true, 100, new Perks.Tantric()), 
    OILY(true, 100, new Perks.Oily()), 
    SOAPY(true, 100, new Perks.Soapy()), 
    MANUALLABOR(true, 100, new Perks.AttributeInfluence(SpecializationAttribute.WELLNESS, BaseAttributeTypes.STRENGTH, 2)), 
    BEAUTICIAN(true, 100), 
    EROTIC_MASSAGE(true, 100, new Perks.AttributeInfluence(SpecializationAttribute.WELLNESS, BaseAttributeTypes.CHARISMA, 0.25f)), 
    NAIAD(true, 100, new Perks.Naiad()), 
    DERMATOLOGIST(true, 100, new Perks.Dermatologist()), 
    DEEPBREATH(true, 100), 
    MEDIC(true, 100, new Perks.Medic()), 
    DOCTOR(true, 100, new Perks.AttributeInfluence(SpecializationAttribute.MEDICALKNOWLEDGE, BaseAttributeTypes.INTELLIGENCE, 0.5f)),
    COSMETICS(true, 100), 
    SEXTHERAPIST(true, 100, new Perks.AttributeInfluence(SpecializationAttribute.SEDUCTION, BaseAttributeTypes.INTELLIGENCE, 0.5f)), 
    SEXPERT(true, 100, new Perks.SeXpert());
    ;
    
	private boolean perk = false;
	private int valueModifier = 0;
	private Trait opposingTrait;
	private TraitEffect traitEffect;
	
	private Trait() {
		this(false, 0, null, null);
	}
	
	private Trait(final int valueModifier) {
		this(false, valueModifier);
	}
	
	private Trait(final Trait opposingTrait) {
		this(false, 0, opposingTrait, null);
	}
	
	private Trait(final boolean perk, final int valueModifier) {
		this(perk, valueModifier, null);
	}
	
	private Trait(final boolean perk, final TraitEffect traitEffect) {
		this(perk, 0, traitEffect);
	}
	
	private Trait(final int valueModifier, final TraitEffect traitEffect) {
		this(false, valueModifier, traitEffect);
	}
	
	private Trait(final boolean perk, final int valueModifier, final TraitEffect traitEffect) {
		this(perk, valueModifier, null, traitEffect);
	}
	
	private Trait(final int valueModifier, final Trait opposingTrait, final TraitEffect traitEffect) {
		this(false, valueModifier, opposingTrait, traitEffect);
	}
	
	private Trait(final boolean perk, final int valueModifier, final Trait opposingTrait, final TraitEffect traitEffect) {
		this.perk = perk;
		this.valueModifier = valueModifier;
		this.opposingTrait = opposingTrait;
		if(opposingTrait != null && opposingTrait.opposingTrait == null) {
			opposingTrait.opposingTrait = this;
		}
		this.traitEffect = traitEffect;
	}
    
    
	public void handleEvent(MyEvent e, Charakter character) {
	    if (traitEffect != null) {
	        traitEffect.handleEvent(e, character, this);
	    }
	}
	
	@Override
	public int getMinObedienceModified(int curMinObedience, Charakter character, RunningActivity activity) {
		if (traitEffect != null) {
		    return traitEffect.getMinObedienceModified(curMinObedience, character, activity);
		}
		return curMinObedience;
	}
	
	
	
	public boolean addTrait(Charakter character, List<Trait> traits) {
		Trait opposedTrait = null;
		for (Trait curTrait : traits) {
			if (isOpposed(curTrait)) {
				opposedTrait = curTrait;
				break;
			}
		}
		if (opposedTrait == null) {
			if (traitEffect != null) {
			    return traitEffect.addTrait(character);
			}
			else {
	            return true;
			}
		}
		else {
			character.removeTrait(opposedTrait);
			return false;
		}
	}
	
	public boolean removeTrait(Charakter character) {
        if (traitEffect != null) {
            return traitEffect.removeTrait(character);
        }
        else {
            return true;
        }
	}
	
	public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
        if (traitEffect != null) {
            return traitEffect.getAttributeModified(calculatedAttribute, currentValue, character);
        }
        else {
            return currentValue;
        }
	}
	
	public boolean isOpposed(Trait trait) {
		if (this == BIGBOOBS && (trait == LOLI || trait == SMALLBOOBS)) {
			return true;
		}
		else if ((this == LOLI || this == SMALLBOOBS) && trait == BIGBOOBS) {
			return true;
		}		
		return opposingTrait != null && trait == opposingTrait;
	}
    
    public static List<Trait> getBasicTraits() {
        List<Trait> traits = new ArrayList<Trait>();
        for (Trait trait : Trait.values()) {
            if (!trait.isPerk()) {
                traits.add(trait);
            }
        }
        return traits;
    }
	   
    public int getValueModifier() {
        return valueModifier;
    }
	
    public boolean isPerk() {
        return perk;
    }
    
    public String getText() {
        return TextUtil.t(this.toString());
    }
    
    public String getText(Person person) {
        return TextUtil.t(this.toString(), person);
    }
    
    public String getDescription() {
        return TextUtil.t(this.toString() + ".description");
    }
    
    public String getDescription(Person person) {
        return TextUtil.t(this.toString() + ".description", person);
    }
    
    public SkillTree getAssociatedSkillTree() {
        if (traitEffect != null) {
            return traitEffect.getSkillTree();
        }
        else {
            return null;
        }
    }
    
    public void modifyPossibleAttacks(List<Attack> attacks, Charakter character) {
        if (traitEffect != null) {
            traitEffect.modifyPossibleAttacks(attacks, character);
        }
    }
    
    public float getAttributeModifier(Attribute attribute) {
        if (traitEffect != null) {
            return traitEffect.getAttributeModifier(attribute);
        }
        else {
            return 0;
        }
    }
    
    public int modifyCustomerRating(int initialRating, Customer customer, BusinessMainActivity businessMainActivity) {
        if (traitEffect != null) {
            return traitEffect.modifyCustomerRating(initialRating, customer, businessMainActivity);
        }
        else {
            return initialRating;
        }
    }
}
