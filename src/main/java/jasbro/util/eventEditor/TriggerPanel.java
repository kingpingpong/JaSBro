package jasbro.util.eventEditor;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import jasbro.Jasbro;
import jasbro.game.world.customContent.CustomQuestStage;
import jasbro.game.world.customContent.Trigger;
import jasbro.game.world.customContent.Trigger.TriggerType;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirementType;
import jasbro.texts.TextUtil;
import jasbro.util.eventEditor.triggerRequirementPanels.TriggerRequirementPanel;

public class TriggerPanel extends JPanel {
	private final static Logger log = LogManager.getLogger(TriggerPanel.class);
	
	private Trigger trigger;
	private CustomQuestStage customQuestStage;
	private JComboBox<String> eventComboBox;
	private TriggerRequirementPanel selectedRequirementPanel;
	private JPanel triggerRequirementPanel;
	private WorldEvent worldEvent;
	private JPanel customActivityPanel;
	private JTextField textField;
	
	/**
	 * @wbp.parser.constructor
	 */
	public TriggerPanel(Trigger triggerTmp, WorldEvent worldEvent) {
		this.worldEvent = worldEvent;
		init(triggerTmp);
	}
	
	public TriggerPanel(Trigger triggerTmp, CustomQuestStage customQuestStage) {
		this.customQuestStage = customQuestStage;
		init(triggerTmp);
	}
	
	public void init(Trigger triggerTmp) {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("left:default"),
				ColumnSpec.decode("default:grow"),
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
				new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,}));
		this.trigger = triggerTmp;
		
		JLabel label = new JLabel(TextUtil.t("eventEditor.triggerType"));
		add(label, "1, 1, left, center");        
		final JComboBox<TriggerType> triggerTypeComboBox = new JComboBox<TriggerType>();
		add(triggerTypeComboBox, "2, 1, fill, top");
		for (TriggerType triggerType : TriggerType.values()) {
			triggerTypeComboBox.addItem(triggerType);
		}
		triggerTypeComboBox.setSelectedItem(trigger.getTriggerType());
		triggerTypeComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				trigger.setTriggerType((TriggerType)triggerTypeComboBox.getSelectedItem());
				if (trigger.getTriggerType() == TriggerType.CUSTOMACTIVITY) {
					customActivityPanel.setVisible(true);
				}
				else {
					customActivityPanel.setVisible(false);
					trigger.setActivityDescription(null);
					trigger.setActivityLabel(null);
				}
			}
		});
		
		
		JButton btnNewButton = new JButton(TextUtil.t("eventEditor.deleteTrigger"));
		btnNewButton.setForeground(Color.RED);
		btnNewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (customQuestStage != null) {
					customQuestStage.removeTrigger(trigger);
				}
				else {
					worldEvent.getTriggers().remove(trigger);
				}
				Container parent = getParent();
				parent.remove(TriggerPanel.this);
				parent.validate();
				parent.repaint();
			}
		});
		add(btnNewButton, "3, 1, 2, 1, right, top");
		
		
		{
			customActivityPanel = new JPanel();
			add(customActivityPanel, "1, 2, 4, 1, fill, fill");
			customActivityPanel.setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.DEFAULT_COLSPEC,
					ColumnSpec.decode("default:grow"),
					FormFactory.DEFAULT_COLSPEC,
					ColumnSpec.decode("default:grow"),},
					new RowSpec[] {
					FormFactory.DEFAULT_ROWSPEC,}));
			
			JLabel lblNewLabel_2 = new JLabel(TextUtil.t("eventEditor.activityName"));
			customActivityPanel.add(lblNewLabel_2, "1, 1, right, default");
			
			textField = new JTextField();
			customActivityPanel.add(textField, "2, 1, fill, default");
			textField.setText(trigger.getActivityLabel());
			textField.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					trigger.setActivityLabel(textField.getText());
				}
				
				public void removeUpdate(DocumentEvent e) {
					trigger.setActivityLabel(textField.getText());
				}
				
				public void changedUpdate(DocumentEvent e) {
					trigger.setActivityLabel(textField.getText());
				}
			});
			
			
			JLabel lblNewLabel_3 = new JLabel(TextUtil.t("eventEditor.activityDescription"));
			customActivityPanel.add(lblNewLabel_3, "3, 1, right, default");
			
			final JTextArea textArea = new JTextArea();
			final JScrollPane scrollPane = new JScrollPane(textArea);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textArea.setText(trigger.getActivityDescription());
			textArea.setEditable(true);
			textArea.getDocument().addDocumentListener(new DocumentListener() {
				
				public void insertUpdate(DocumentEvent e) {
					trigger.setActivityDescription(textArea.getText());
				}
				
				public void removeUpdate(DocumentEvent e) {
					trigger.setActivityDescription(textArea.getText());
				}
				
				public void changedUpdate(DocumentEvent e) {
					trigger.setActivityDescription(textArea.getText());
				}
			});
			customActivityPanel.add(scrollPane, "4, 1, fill, fill");
			customActivityPanel.setVisible(trigger.getTriggerType() == TriggerType.CUSTOMACTIVITY);
		}
		
		
		JLabel lblNewLabel_1 = new JLabel(TextUtil.t("eventEditor.requirements"));
		add(lblNewLabel_1, "1, 3, left, default");
		
		final JComboBox<TriggerRequirementType> requirementComboBox = new JComboBox<TriggerRequirementType>();
		add(requirementComboBox, "2, 3, fill, default");
		for (TriggerRequirementType triggerRequirementType : TriggerRequirementType.values()) {
			requirementComboBox.addItem(triggerRequirementType);
		}
		
		JButton addRequirementButton = new JButton(TextUtil.t("eventEditor.addRequirement"));
		add(addRequirementButton, "3, 3");
		addRequirementButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					TriggerRequirement triggerRequirement = ((TriggerRequirementType)requirementComboBox.getSelectedItem()).
							getRequirementClass().newInstance();
					TriggerRequirementPanel triggerPanel = new TriggerRequirementPanel(triggerRequirement, new MyMouseListener());
					
					if (triggerRequirementPanel.getComponents().length == 0) {
						triggerRequirementPanel.add(triggerPanel);
						trigger.setRequirement(triggerRequirement);
						setSelected(triggerPanel);
					}
					else {
						if (selectedRequirementPanel != null && 
								selectedRequirementPanel.getTriggerRequirement().canAddRequirement(triggerRequirement)) {
							selectedRequirementPanel.getTriggerRequirement().getSubRequirements().add(triggerRequirement);
							selectedRequirementPanel.addPanel(triggerPanel);
							setSelected(triggerPanel);
						}
					}
					
					Container parent = getParent();
					parent.validate();
					parent.repaint();
				}
				catch (Exception ex) {
					log.error("Error when creating trigger requirement panel", ex);
				}
			}
		});
		
		JButton deleteRequirementButton = new JButton(TextUtil.t("eventEditor.deleteRequirement"));
		add(deleteRequirementButton, "4, 3");
		deleteRequirementButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedRequirementPanel != null) {
					Container parent = selectedRequirementPanel.getParent();
					if (parent == triggerRequirementPanel) {
						trigger.setRequirement(null);
						triggerRequirementPanel.remove(selectedRequirementPanel);
					}
					else {
						parent.remove(selectedRequirementPanel);
						while (!(parent instanceof TriggerRequirementPanel)) {
							parent = parent.getParent();
						}
						((TriggerRequirementPanel)parent).getTriggerRequirement().getSubRequirements().remove(selectedRequirementPanel.getTriggerRequirement());
					}
					selectedRequirementPanel = null;
					
					parent = getParent();
					parent.validate();
					parent.repaint();
				}
			}
		});
		
		triggerRequirementPanel = new JPanel();
		add(triggerRequirementPanel, "1, 4, 4, 1, fill, fill");
		triggerRequirementPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		if (customQuestStage != null) {
			JLabel lblNewLabel = new JLabel(TextUtil.t("eventEditor.triggerEvent"));
			add(lblNewLabel, "1, 5, right, default");
			
			eventComboBox = new JComboBox<String>();
			add(eventComboBox, "2, 5, fill, default");
			eventComboBox.setEditable(true);
			
			List<String> eventIds = new ArrayList<String>(Jasbro.getInstance().getWorldEvents().keySet());
			Collections.sort(eventIds);
			for (String worldEvent : eventIds) {
				eventComboBox.addItem(worldEvent);
			}
			
			eventComboBox.setSelectedItem(customQuestStage.getEventName(trigger));
			eventComboBox.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					customQuestStage.getTriggerToWorldEventMap().put(trigger, (String)eventComboBox.getSelectedItem());
				}
			});
		}
		
		
		if (trigger.getRequirement() != null) {
			addTriggerRequirementPanel(trigger.getRequirement());
		}
	}
	
	public void addTriggerRequirementPanel(TriggerRequirement triggerRequirement) {
		TriggerRequirementPanel triggerRequirementPanel = new TriggerRequirementPanel(triggerRequirement, new MyMouseListener());
		this.triggerRequirementPanel.add(triggerRequirementPanel);
	}
	
	public void setSelected(TriggerRequirementPanel triggerRequirementPanel) {
		if (selectedRequirementPanel != null) {
			selectedRequirementPanel.setSelected(false);
		}
		selectedRequirementPanel = triggerRequirementPanel;
		triggerRequirementPanel.setSelected(true);
		
		validate();
		repaint();
	}
	
	private class MyMouseListener extends MouseAdapter {
		public void mouseClicked(java.awt.event.MouseEvent e) {
			if (e.getSource() instanceof TriggerRequirementPanel) {
				TriggerRequirementPanel newPanel = (TriggerRequirementPanel) e.getSource();
				setSelected(newPanel);
			}
		};
	}
}