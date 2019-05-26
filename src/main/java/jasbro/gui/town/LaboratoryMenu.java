package jasbro.gui.town;

import jasbro.Jasbro;
import jasbro.game.GameData;
import jasbro.game.character.CharacterSpawner;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.traits.Trait;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class LaboratoryMenu extends MyImage {
	private JButton cloneButton;
	private JButton futanariButton;
	private JButton undoFutaButton;
	private JComboBox<Charakter> characterSelect;
	private JSpinner cloneAmountSpinner;
	private final static int clonePrice = 10000;
	
	public LaboratoryMenu() {
		removeAll();
		init();
	}
	
	public void init() {
		removeAll();
		setBackgroundImage(new ImageData("images/backgrounds/laboratory.jpg"));
		setOpaque(false);
		setVisible(true);
		
		double width = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
		double height = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
		int backHomeBtnWidth = (int) (150*width/1280);
		int backHomeBtnHeight = (int) (50*width/1280);
		
		ImageIcon homeIcon1 = new ImageIcon("images/buttons/home.png");
		Image homeImage1 = homeIcon1.getImage().getScaledInstance( backHomeBtnWidth, backHomeBtnHeight,  java.awt.Image.SCALE_SMOOTH ) ;  
		homeIcon1 = new ImageIcon(homeImage1);
		
		ImageIcon homeIcon2 = new ImageIcon("images/buttons/home hover.png");
		Image homeImage2 = homeIcon2.getImage().getScaledInstance( backHomeBtnWidth, backHomeBtnHeight,  java.awt.Image.SCALE_SMOOTH ) ;  
		homeIcon2 = new ImageIcon(homeImage2);
		
		JButton homeButton = new JButton(homeIcon1);
		homeButton.setRolloverIcon(homeIcon2);
		homeButton.setPressedIcon(homeIcon1);
		homeButton.setBounds((int) (15*width/1280),(int) (550*height/720), backHomeBtnWidth, backHomeBtnHeight);
		homeButton.setBorderPainted(false); 
		homeButton.setContentAreaFilled(false); 
		homeButton.setFocusPainted(false); 
		homeButton.setOpaque(false);
	    try {
			add(homeButton);
	    } catch (java.lang.NullPointerException e) {
	        // We need a way to remove this error
	    }
		homeButton.addActionListener(new  ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Jasbro.getInstance().getGui().showHouseManagementScreen();
			}
		});
		
		ImageIcon backIcon1 = new ImageIcon("images/buttons/back.png");
		Image backImage1 = backIcon1.getImage().getScaledInstance( backHomeBtnWidth, backHomeBtnHeight,  java.awt.Image.SCALE_SMOOTH ) ;  
		backIcon1 = new ImageIcon(backImage1);
		
		ImageIcon backIcon2 = new ImageIcon("images/buttons/back hover.png");
		Image backImage2 = backIcon2.getImage().getScaledInstance( backHomeBtnWidth, backHomeBtnHeight,  java.awt.Image.SCALE_SMOOTH ) ;  
		backIcon2 = new ImageIcon(backImage2);
		
		JButton backButton = new JButton(backIcon1);
		backButton.setRolloverIcon(backIcon2);
		backButton.setPressedIcon(backIcon1);
		backButton.setBounds((int) (15*width/1280),(int) (620*height/720), backHomeBtnWidth, backHomeBtnHeight);
		backButton.setBorderPainted(false); 
		backButton.setContentAreaFilled(false); 
		backButton.setFocusPainted(false); 
		backButton.setOpaque(false);
	    try {
			add(backButton);
	    } catch (java.lang.NullPointerException e) {
	    	// We need a way to remove this error
	    }
		backButton.addActionListener(new  ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Jasbro.getInstance().getGui().showAlchemist();
			}
		});
		
		addMouseListener (new MouseAdapter(){
	        public void mouseClicked(MouseEvent e) {
	            if (SwingUtilities.isRightMouseButton(e)) {
	            	Jasbro.getInstance().getGui().showAlchemist();
	            }
	        }		
		});
		
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
							clone.removeTrait(Trait.RARESLAVE);
							clone.removeTrait(Trait.FORMERNOBLE);
							clone.removeTrait(Trait.EXTREMELYRARESLAVE);
							clone.removeTrait(Trait.EXTREMELYRARESLAVE2);
							clone.addTrait(Trait.CLONE);
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