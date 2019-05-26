package jasbro.gui.pages.subView;

import jasbro.Jasbro;
import jasbro.game.quests.Quest;
import jasbro.game.world.market.QuestManager;
import jasbro.gui.objects.div.QuestPanel;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.texts.TextUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class TrainerGuildPanel extends JPanel {
	private JTabbedPane tabbedPane;
	private JPanel questPanel;
	private JPanel activeQuestsPanel;
	
	public TrainerGuildPanel() {
		setOpaque(false);
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow(8)"),
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("pref:grow"),
				RowSpec.decode("pref:grow(8)"),
				RowSpec.decode("pref:grow"),}));
		
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
}