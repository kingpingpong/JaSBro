package jasbro.gui.town;

import jasbro.Jasbro;
import jasbro.game.quests.Quest;
import jasbro.game.world.market.QuestManager;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.QuestPanel;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class QuestMenu extends MyImage {
	private JTabbedPane tabbedPane;
	private JPanel questPanel;
	private JPanel activeQuestsPanel;
	
	public QuestMenu() {
		setBackgroundImage(getTownImage());
		setOpaque(false);
		
		double width = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
		double height = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
		int widthRat = (int) (width/1280);
		int heightRat = (int) (height/720);
		int iconSize = (int) (65*width/1280);
		
		ImageIcon homeIcon1 = new ImageIcon("images/buttons/home.png");
		Image homeImage1 = homeIcon1.getImage().getScaledInstance( 230, 75,  java.awt.Image.SCALE_SMOOTH ) ;  
		homeIcon1 = new ImageIcon(homeImage1);
		
		ImageIcon homeIcon2 = new ImageIcon("images/buttons/home hover.png");
		Image homeImage2 = homeIcon2.getImage().getScaledInstance( 230, 75,  java.awt.Image.SCALE_SMOOTH ) ;  
		homeIcon2 = new ImageIcon(homeImage2);
		
		JButton homeButton = new JButton(homeIcon1);
		homeButton.setRolloverIcon(homeIcon2);
		homeButton.setPressedIcon(homeIcon1);
		homeButton.setBounds((int) (15*width/1280),(int) (550*height/720), 230, 75);
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
		Image backImage1 = backIcon1.getImage().getScaledInstance( 230, 75,  java.awt.Image.SCALE_SMOOTH ) ;  
		backIcon1 = new ImageIcon(backImage1);
		
		ImageIcon backIcon2 = new ImageIcon("images/buttons/back hover.png");
		Image backImage2 = backIcon2.getImage().getScaledInstance( 230, 75,  java.awt.Image.SCALE_SMOOTH ) ;  
		backIcon2 = new ImageIcon(backImage2);
		
		JButton backButton = new JButton(backIcon1);
		backButton.setRolloverIcon(backIcon2);
		backButton.setPressedIcon(backIcon1);
		backButton.setBounds((int) (15*width/1280),(int) (620*height/720), 230, 75);
		backButton.setBorderPainted(false); 
		backButton.setContentAreaFilled(false); 
		backButton.setFocusPainted(false); 
		backButton.setOpaque(false);
		add(backButton);
		backButton.addActionListener(new  ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Jasbro.getInstance().getGui().showSlaverGuildScreen();
			}
		});
		
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow(23)"),
				ColumnSpec.decode("default:grow(20)"),
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("pref:grow"),
				RowSpec.decode("pref:grow(8)"),
				RowSpec.decode("pref:grow"),}));
		
		MyImage teacherImage = new MyImage();
		teacherImage.setImage(new ImageData("images/people/secretary.png"));
		add(teacherImage, "1, 1, 1, 3, fill, fill");
		
		addMouseListener (new MouseAdapter(){
	        public void mouseClicked(MouseEvent e) {
	            if (SwingUtilities.isRightMouseButton(e)) {
	            	Jasbro.getInstance().getGui().showSlaverGuildScreen();
	            }
	        }		
		});
		
		UIManager.put("TabbedPane.contentOpaque", false);
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, "2, 2, fill, fill");
		
		{ //Panel for new quests
			JPanel panel = new TranslucentPanel();
			tabbedPane.addTab("Take Quest", null, panel, null);
			panel.setLayout(new FormLayout(new ColumnSpec[] {
					ColumnSpec.decode("default:grow"),},
					new RowSpec[] {
					RowSpec.decode("20dlu"),
					RowSpec.decode("default:grow"),}));
			
			JLabel lblTakeQuest = new JLabel("Take quest");
			lblTakeQuest.setFont(lblTakeQuest.getFont().deriveFont(15f));
			panel.add(lblTakeQuest, "1, 1");
			
			questPanel = new JPanel();
			questPanel.setOpaque(false);
			panel.add(questPanel, "1, 2, fill, fill");
			FormLayout layout = new FormLayout(new ColumnSpec[] {
					ColumnSpec.decode("center:min:grow"),
					ColumnSpec.decode("center:min:grow"),
					ColumnSpec.decode("center:min:grow"),
					ColumnSpec.decode("center:min:grow"),},
					new RowSpec[] {
					FormFactory.DEFAULT_ROWSPEC,});
			layout.setColumnGroups(new int[][]{ {1, 2, 3, 4}});
			questPanel.setLayout(layout);
		}
		
		{//Panel for existing quests
			JPanel panel = new TranslucentPanel();
			tabbedPane.addTab("Active quests", null, panel, null);
			
			panel.setLayout(new FormLayout(new ColumnSpec[] {
					ColumnSpec.decode("default:grow"),},
					new RowSpec[] {
					RowSpec.decode("20dlu"),
					RowSpec.decode("default:grow"),}));
			
			JLabel lblTakeQuest = new JLabel("Active quests");
			lblTakeQuest.setFont(lblTakeQuest.getFont().deriveFont(15f));
			panel.add(lblTakeQuest, "1, 1");
			
			JScrollPane scrollPane = new JScrollPane();
			panel.add(scrollPane, "1, 2, fill, fill");
			scrollPane.setOpaque(false);
			scrollPane.getViewport().setOpaque(false);
			
			activeQuestsPanel = new JPanel();
			activeQuestsPanel.setOpaque(false);
			scrollPane.setViewportView(activeQuestsPanel);
			FormLayout layout = new FormLayout(new ColumnSpec[] {
					ColumnSpec.decode("center:min:grow"),
					ColumnSpec.decode("center:min:grow"),
					ColumnSpec.decode("center:min:grow"),
					ColumnSpec.decode("center:min:grow"),},
					new RowSpec[] {
					FormFactory.DEFAULT_ROWSPEC,});
			layout.setColumnGroups(new int[][]{ {1, 2, 3, 4}});
			activeQuestsPanel.setLayout(layout);
		}
		
		initQuestPanel();
		initActiveQuestsPanel();
	}
	
	public void initQuestPanel() {
		questPanel.removeAll();
		
		final QuestManager questManager = Jasbro.getInstance().getData().getQuestManager();
		FormLayout layout = (FormLayout) questPanel.getLayout();
		List<Quest> quests = questManager.getPossibleQuests();
		for (int i = 0; i < quests.size(); i++) {
			final Quest quest = quests.get(i);
			QuestPanel panel = new QuestPanel();
			panel.init(quest);
			if (i%4 == 0) {
				layout.appendRow(FormFactory.DEFAULT_ROWSPEC);
			}
			questPanel.add(panel, (i%4+1) + ", " + (i/4+1) + ", fill, fill");
			
			JButton questButton = new JButton(TextUtil.t("quest.accept"));
			panel.add(questButton, "1, 3, fill, fill");
			questButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					questManager.activateQuest(quest);
					initQuestPanel();
					initActiveQuestsPanel();
				}
			});
		}
		validate();
		repaint();
	}
	
	
	public void initActiveQuestsPanel() {
		activeQuestsPanel.removeAll();
		
		final QuestManager questManager = Jasbro.getInstance().getData().getQuestManager();
		FormLayout layout = (FormLayout) activeQuestsPanel.getLayout();
		List<Quest> quests = questManager.getActiveQuests();
		for (Quest quest : questManager.getInactiveQuests()) {
			if (quest.showInQuestLog()) {
				quests.add(quest);
			}
		}
		
		for (int i = 0; i < quests.size(); i++) {
			final Quest quest = quests.get(i);
			QuestPanel panel = new QuestPanel();
			panel.init(quest);
			if (i%4 == 0) {
				layout.appendRow(FormFactory.DEFAULT_ROWSPEC);
			}
			activeQuestsPanel.add(panel, (i%4+1) + ", " + (i/4+1) + ", fill, fill");
			
			if (quest.canFinishEarly()) {
				JButton finishQuestButton = new JButton(TextUtil.t("quest.finish"));
				panel.add(finishQuestButton, "1, 3, fill, fill");
				finishQuestButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						quest.finish();
						initQuestPanel();
						initActiveQuestsPanel();
					}
				});
			}	
		}
		validate();
		repaint();
	}
	
	public ImageData getTownImage() {
		switch(Jasbro.getInstance().getData().getTime()) {
		case AFTERNOON:
			return new ImageData("images/backgrounds/slaverguild afternoon.jpg");
		case NIGHT:
			return new ImageData("images/backgrounds/hiretrainer morning.jpg");
		default:
			return new ImageData("images/backgrounds/hiretrainer morning.jpg");
		}
	}
}