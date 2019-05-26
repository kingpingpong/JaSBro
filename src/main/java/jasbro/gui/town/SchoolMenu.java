package jasbro.gui.town;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.world.market.CharacterSchool;
import jasbro.game.world.market.CharacterSchool.Training;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SchoolMenu extends MyImage {
	private CharacterSchool school = new CharacterSchool();
	private Charakter selectedCharacter;
	private List<Training> possibleTraining;
	private JPanel trainingPanel;
	private JScrollPane scrollPane;
	private JComboBox<Charakter> characterSelect;
	private JCheckBox chckbxShowUnavailable;
	
	public SchoolMenu() {
		setBackgroundImage(new ImageData("images/backgrounds/school.jpg"));
		setOpaque(false);
		
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
		add(homeButton);
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
		add(backButton);
		backButton.addActionListener(new  ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Jasbro.getInstance().getGui().showTownScreen();
			}
		});
		
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow(23)"),
				ColumnSpec.decode("default:grow(20)"),
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("default:grow"),
				RowSpec.decode("default:grow(20)"),
				RowSpec.decode("default:grow"),}));
		
		addMouseListener (new MouseAdapter(){
	        public void mouseClicked(MouseEvent e) {
	            if (SwingUtilities.isRightMouseButton(e)) {
	            	Jasbro.getInstance().getGui().showTownScreen();
	            }
	        }		
		});
		
		MyImage teacherImage = new MyImage();
		teacherImage.setImage(new ImageData("images/people/realtor.png"));
		add(teacherImage, "1, 1, 1, 3, fill, fill");
		
		JPanel schoolPanel = new TranslucentPanel();
		add(schoolPanel, "2, 2, fill, fill");
		schoolPanel.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
		schoolPanel.setBorder(GuiUtil.DEFAULTBORDER);
		schoolPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("5dlu"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("5dlu"),},
				new RowSpec[] {
				RowSpec.decode("5dlu"),
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("5dlu"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("5dlu"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("5dlu"),
				RowSpec.decode("default:grow"),
				RowSpec.decode("5dlu"),}));
		
		characterSelect = new JComboBox<Charakter>();
		characterSelect.addKeyListener(new MyKeyListener());
		characterSelect.updateUI(); //Hack to get custom keylistener to be first in line
		
		characterSelect.addItem(null);
		for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
			characterSelect.addItem(character);
		}
		characterSelect.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Charakter charakter = (Charakter)characterSelect.getSelectedItem();
					selectedCharacter = charakter;
					updateTraining();
					javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() { 
							scrollPane.getVerticalScrollBar().setValue(0);
						}
					});
				}
			}
		});
		
		JLabel lblNewLabel = new JLabel(TextUtil.t("school"));
		lblNewLabel.setFont(GuiUtil.DEFAULTBOLDFONT);
		schoolPanel.add(lblNewLabel, "2, 2");
		schoolPanel.add(characterSelect,"2, 4");
		
		
		
		scrollPane = new JScrollPane();
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		
		chckbxShowUnavailable = new JCheckBox("Show unavailable");
		chckbxShowUnavailable.setOpaque(false);
		schoolPanel.add(chckbxShowUnavailable, "2, 6");
		chckbxShowUnavailable.setSelected(ConfigHandler.isShowUnavailableSchool());
		chckbxShowUnavailable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ConfigHandler.setShowUnavailableSchool(chckbxShowUnavailable.isSelected());
				updateTraining();
			}
		});
		
		schoolPanel.add(scrollPane, "2, 8, fill, fill");
		
		trainingPanel = new JPanel();
		trainingPanel.setOpaque(false);
		scrollPane.setViewportView(trainingPanel);
		
		FormLayout layout = new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("center:min:grow"),
				ColumnSpec.decode("center:min:grow"),},
				new RowSpec[] {
				RowSpec.decode("default:grow"),});
		layout.setColumnGroups(new int[][]{ {1, 2}});
		trainingPanel.setLayout(layout);
		validate();
	}
	
	public void updateTraining() {
		if (selectedCharacter == null) {
			possibleTraining = new ArrayList<CharacterSchool.Training>();
		}
		else {
			if (ConfigHandler.isShowUnavailableSchool()) {
				possibleTraining = school.getTrainingOpportunities(selectedCharacter);
			}
			else {
				possibleTraining = school.getTrainingOpportunitiesHideUnavailable(selectedCharacter);
			}
		}
		trainingPanel.removeAll();
		FormLayout layout = (FormLayout)trainingPanel.getLayout();
		for (int i = 0; i < possibleTraining.size(); i++) {
			Training training = possibleTraining.get(i);
			if (i % 2 == 0) {
				layout.insertRow(i/2+1, RowSpec.decode("default:none"));
			}
			trainingPanel.add(new TrainingInfoPanel(training), (i % 2 + 1) +", " + (i/2+1) + ", fill, fill");
		}		
		revalidate();
		repaint();
	}
	
	
	private class TrainingInfoPanel extends TranslucentPanel {
		private Training training;
		
		public TrainingInfoPanel(Training training) {
			this.training = training;
			setPreferredSize(null);
			getPreferredSize().width = 0;
			setBackground(new Color(1.0f,1.0f,0.9f,0.8f));
			setBorder(GuiUtil.DEFAULTEMPTYBORDER);
			setLayout(new FormLayout(new ColumnSpec[] {
					ColumnSpec.decode("default:grow"),},
					new RowSpec[] {
					RowSpec.decode("default:grow"),
					RowSpec.decode("default:grow"),
					RowSpec.decode("default:none"),}));
				
			JTextArea nameField = GuiUtil.getDefaultTextarea();;
			nameField.setText(training.getName());
			
			nameField.setFont(new Font("Tahoma", Font.BOLD, 18));
			add(nameField,"1,1");
			
			JTextArea messageField = GuiUtil.getDefaultTextarea();
			messageField.setText(training.getDescription());
			add(messageField,"1,2");
			
			Object arguments[] = {training.getPrice()};
			JButton buyButton = new JButton(TextUtil.t("school.pay", arguments));
			add(buyButton,"1,3, fill, bottom");
			if (!Jasbro.getInstance().getData().canAfford(training.getPrice()) || 
					!training.fulfillsRequirements()) {
				buyButton.setEnabled(false);
			}
			buyButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					TrainingInfoPanel.this.training.apply();
					SchoolMenu.this.updateTraining();
				}
			});
		}
	}
	
	private class MyKeyListener extends KeyAdapter {
		public void keyPressed( KeyEvent e ) {
			int curIndex = characterSelect.getSelectedIndex();
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				if (curIndex == -1) {
					curIndex = 0;
				}
				if (curIndex + 1 < characterSelect.getItemCount()) {
					characterSelect.hidePopup();
					characterSelect.setSelectedIndex(curIndex + 1);
				}
				e.consume();
			} 
			else if (e.getKeyCode() == KeyEvent.VK_UP) {
				if (curIndex - 1 >= 0) {
					characterSelect.hidePopup();
					characterSelect.setSelectedIndex(curIndex - 1);
				}
				e.consume();
			}
		}
	}
}