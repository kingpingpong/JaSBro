package jasbro.gui.character;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.world.market.CharacterSchool;
import jasbro.game.world.market.CharacterSchool.SpecializationTraining;
import jasbro.game.world.market.CharacterSchool.Training;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.AttributePanel;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SpecializationPanel extends TranslucentPanel {
	private Charakter character;
	private SpecializationType specialisationType;
	private FormLayout layout;
	private Map<AttributeType, AttributePanel> panelMap;
	private JPanel warnPanel;
	private MyImage warning;
	
	public SpecializationPanel(SpecializationType specializationType, Charakter character) {
		this.specialisationType = specializationType;
		this.character = character;
		panelMap = new HashMap<AttributeType, AttributePanel>();
		
		setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
		setBorder(GuiUtil.DEFAULTBORDER);
		layout = new FormLayout(new ColumnSpec[] {
		        FormFactory.DEFAULT_COLSPEC,
		        ColumnSpec.decode("default:grow"),
		        ColumnSpec.decode("default:grow(3)")},
		    new RowSpec[] {
		        RowSpec.decode("default:grow"),});
		setLayout(layout);
		setPreferredSize(null);
		getPreferredSize().width = 1;
		getMinimumSize().width = 200;
		init();
	}
	
	public void init() {
		removeAll();

		JLabel label = new JLabel(specialisationType.getText());
		add(label, "1, 1, fill, top");
		label.setFont(GuiUtil.DEFAULTLARGEBOLDFONT);
		label.setOpaque(false);
		
		warnPanel = new JPanel();
		warnPanel.setOpaque(false);
		add(warnPanel, "2, 1, fill, fill");
		warnPanel.setLayout(new FormLayout(new ColumnSpec[] {
		        ColumnSpec.decode("left:default:grow"),},
		    new RowSpec[] {
		        RowSpec.decode("default:grow"),}));
		
		for (int i = 0; i < specialisationType.getAssociatedAttributes().size(); i++) {
			AttributeType attributeType = specialisationType.getAssociatedAttributes().get(i);
			Attribute attribute = character.getAttribute(attributeType);
			AttributePanel attributePanel = new AttributePanel(attribute);
			layout.appendRow(RowSpec.decode("default:grow"));
			add(attributePanel, "1,"+(i+2)+", 3, 1, fill, top");
			panelMap.put(attributeType, attributePanel);
		}
		update();
	}
	
	@Override
	public void update() {
	    warnPanel.removeAll();
	    boolean maxed = false;
        for (int i = 0; i < specialisationType.getAssociatedAttributes().size(); i++) {
            AttributeType attributeType = specialisationType.getAssociatedAttributes().get(i);
            panelMap.get(attributeType).update();
            if (character.getAttribute(attributeType).isMaxed()) {
                maxed = true;
            }
        }
	    
        if (specialisationType == SpecializationType.FIGHTER) {
            float damage = ((int) (character.getDamage() * 100)) / 100f;
            Object arguments[] = { damage, character.getArmorValue(), character.getArmor(), character.getCritChance(), character.getCritDamageBonus(), character.getBlockChance(), character.getBlockAmount(), character.getDodge() };
            setToolTipText(TextUtil.htmlPreformatted(TextUtil.t("combatDataTooltip", arguments)));
        } else if (specialisationType == SpecializationType.THIEF) {
            Object arguments[] = { character.getStealChance(), character.getStealAmountModifier(), character.getStealItemChance() };
            setToolTipText(TextUtil.htmlPreformatted(TextUtil.t("thiefDataTooltip", arguments)));
        }

        
        else if (specialisationType == SpecializationType.BARTENDER) {
        	float f= 10 + character.getFinalValue(SpecializationAttribute.BARTENDING) / 3;
        	if(character.getTraits().contains(Trait.MULTITASKING)){
        		f+=character.getFinalValue(BaseAttributeTypes.INTELLIGENCE)/4;
        	}
        	int i = (int) f;
 
            Object arguments[] = {i  };
            setToolTipText(TextUtil.htmlPreformatted(TextUtil.t("bartenderDataTooltip", arguments)));
        }

        if (maxed) {
            warning = new MyImage(new ImageData("images/icons/Exclamation_Mark.png"));
            warning.setPreferredSize(null);
            if (!specialisationType.isTeachable()) {
                warning.setToolTipText(TextUtil.t("school.attributeMaxed.cantTeach"));
            }
            else {
                Jasbro.getThreadpool().execute(new SchoolShortcutRunnable());
            }
            warnPanel.add(warning, "1,1, fill, fill");
        }
        else {
            warning = null;
        }
	}
	
	private class SchoolShortcutRunnable implements Runnable {

        @Override
        public void run() {
            final MyImage warning = SpecializationPanel.this.warning;
            final CharacterSchool characterSchool = new CharacterSchool();
            List<Training> trainingPossibilities = characterSchool.getTrainingOpportunities(character);
            
            for (Training training : trainingPossibilities) {
                if (training instanceof SpecializationTraining) {
                    final SpecializationTraining specializationTraining = (SpecializationTraining) training;
                    if (specializationTraining.getSpecializationType() == specialisationType) {
                        warning.setToolTipText(TextUtil.htmlPreformatted(TextUtil.t("school.attributeMaxed") +
                                "\n\n" + specializationTraining.getName() +"\n" +
                                specializationTraining.getDescription() + "\n" +
                                TextUtil.t("stats.money", new Object[]{specializationTraining.getPrice()})));
                        
                        if (specializationTraining.fulfillsRequirements() && 
                                Jasbro.getInstance().getData().getMoney() >= specializationTraining.getPrice()) {
                            warning.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    characterSchool.getTrainingOpportunitiesHideUnavailable(character);
                                    String text = TextUtil.t("school.attributeMaxed.shortCut", character) +
                                            "\n\n" + specializationTraining.getName() +"\n" +
                                            specializationTraining.getDescription() + "\n" +
                                            TextUtil.t("stats.money", new Object[]{specializationTraining.getPrice()});
                                    int confirm = JOptionPane.showConfirmDialog(SpecializationPanel.this,
                                            text,
                                            "fire",
                                            JOptionPane.YES_NO_OPTION);
                                    if (confirm == 0) {
                                        specializationTraining.apply();
                                        update();
                                        repaint();
                                    }
                                }
                            });
                        }
                        break;
                    }
                }
            }
            
            

        }
	    
	}
}
