package jasbro.gui.pages.subView;

import jasbro.Jasbro;
import jasbro.game.GameData;
import jasbro.game.character.CharacterSpawner;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class LaboratoryPanel extends JPanel {
	private JButton cloneButton;
	private JButton futanariButton;
	private JButton undoFutaButton;
	private JComboBox<Charakter> characterSelect;
	private JSpinner cloneAmountSpinner;
	private final static int clonePrice = 10000;
	
	public LaboratoryPanel() {
		init();
	}
	
	public void init() {
		removeAll();
		setOpaque(false);
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow(3)"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow(3)"),},
			new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(30, 0, 0, 0));
		contentPanel.setOpaque(false);
		contentPanel.setAutoscrolls(true);
		add(contentPanel, "2, 1, fill, fill");
		
		
		characterSelect = new JComboBox<Charakter>();
		characterSelect.addItem(null);
		for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
			characterSelect.addItem(character);
		}
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("right:default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:default:grow"),},
				new RowSpec[] {
				RowSpec.decode("top:pref:grow"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("top:40dlu"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("top:20dlu"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("top:20dlu"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("default:grow(2)"),}));
		
		contentPanel.add(characterSelect, "1, 2, 3, 1, fill, fill");
		
		cloneAmountSpinner = new JSpinner();
		cloneAmountSpinner.setModel(new SpinnerNumberModel(1, 1, 10000, 1));
		cloneAmountSpinner.setEnabled(false);
		contentPanel.add(cloneAmountSpinner,"1, 4, right, fill");
		
		cloneButton = new JButton(TextUtil.t("laboratory.clone", getSelectedCharacter(), new Object[]{clonePrice}));
		contentPanel.add(cloneButton, "3, 4, left, fill");		
		cloneButton.setEnabled(false);
		cloneButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getSelectedCharacter() != null) {
					GameData data = Jasbro.getInstance().getData();
					final int clones = (Integer) cloneAmountSpinner.getValue();
					int i=0;
					if (data.canAfford(clones * clonePrice)) {
						data.spendMoney(clones * clonePrice, "Clone");
						Charakter clone;
						do {
							clone = CharacterSpawner.create(getSelectedCharacter().getBase(), CharacterType.SLAVE);
							clone.setName(clone.getName() + TextUtil.t("laboratory.clonenameaddition", getSelectedCharacter()));
							data.getCharacters().add(clone);
							i++;
						} while (i < clones);
						if (clones == 1) {
							new MessageScreen(TextUtil.t("laboratory.clone.message", getSelectedCharacter()), 
									ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, clone), clone.getBackground());
						}
						else {
							Object arguments [] = {clones};
							new MessageScreen(TextUtil.t("laboratory.clone.message.multiple", getSelectedCharacter(), arguments), 
									ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, clone), clone.getBackground());
						}
						Charakter selected = getSelectedCharacter();
						init();
						characterSelect.setSelectedItem(selected);
					}
				}
			}
			
		});
		
		futanariButton = new JButton(TextUtil.t("laboratory.turnintofuta", getSelectedCharacter()));
		contentPanel.add(futanariButton, "1, 6, 3, 1");
		futanariButton.setEnabled(false);
		
		futanariButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getSelectedCharacter() != null) {
					GameData data = Jasbro.getInstance().getData();
					if (data.canAfford(100)) {
						data.spendMoney(100, "Futarization");
						getSelectedCharacter().setGender(Gender.FUTA);
						new MessageScreen(TextUtil.t("laboratory.turnintofuta.message", getSelectedCharacter()), 
								ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, getSelectedCharacter()), 
								getSelectedCharacter().getBackground());
						init();
					}
				}
			}
		});
		
		undoFutaButton = new JButton(TextUtil.t("laboratory.reversefuta", getSelectedCharacter()));
		undoFutaButton.setEnabled(false);
		contentPanel.add(undoFutaButton, "1, 8, 3, 1");
		undoFutaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getSelectedCharacter() != null) {
					GameData data = Jasbro.getInstance().getData();
					if (data.canAfford(100)) {
						data.spendMoney(100, "Defutarization");
						getSelectedCharacter().setGender(Gender.FEMALE);
						new MessageScreen(TextUtil.t("laboratory.reversefuta.message", getSelectedCharacter()), 
								ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, getSelectedCharacter()), 
								getSelectedCharacter().getBackground());
						init();
					}
				}
			}
		});
		
		characterSelect.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (getSelectedCharacter() != null && getSelectedCharacter().getGender() == Gender.FEMALE) {
						futanariButton.setEnabled(true);
					}
					else {
						futanariButton.setEnabled(false);
					}
					if (getSelectedCharacter() != null && getSelectedCharacter().getType() != CharacterType.TRAINER) {
						cloneButton.setEnabled(true);
						cloneAmountSpinner.setEnabled(true);
					}
					else {
						cloneButton.setEnabled(false);
						cloneAmountSpinner.setEnabled(false);
					}
					if (getSelectedCharacter() != null && getSelectedCharacter().getGender() == Gender.FUTA) {
						undoFutaButton.setEnabled(true);
					}
					else {
						undoFutaButton.setEnabled(false);
					}
				}
			}
		});
		validate();
	}
	
	public Charakter getSelectedCharacter() {
		return (Charakter) characterSelect.getSelectedItem();
	}
	
}