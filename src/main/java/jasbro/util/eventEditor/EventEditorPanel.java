package jasbro.util.eventEditor;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import jasbro.game.world.customContent.EventAndQuestFileLoader;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import jasbro.game.world.customContent.effects.WorldEventEffectContainer;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import jasbro.util.eventEditor.effectPanels.EventEffectPanel;

public class EventEditorPanel extends JPanel {
	private final static Logger log = LogManager.getLogger(EventEditorPanel.class);
	
	private WorldEvent worldEvent;
	private EventEffectPanel selectedEventEffectPanel;
	private JPanel eventEffectMasterPanel;
	
	private JPanel mainPanel;
	
	private JButton saveButton;
	private TriggerListPanel triggerListPanel;
	
	
	public EventEditorPanel(WorldEvent worldEventTmp) {
		this.worldEvent = worldEventTmp;
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow(6)"),}));
		
		JPanel buttonPanel = new JPanel();
		add(buttonPanel, "1, 1, fill, fill");
		buttonPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("12dlu"),
				ColumnSpec.decode("12dlu"),
				ColumnSpec.decode("20dlu"),
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		
		final JComboBox<WorldEventEffectType> worldEventComboBox = new JComboBox<WorldEventEffectType>();
		buttonPanel.add(worldEventComboBox, "1, 1");
		
		JButton btnAddEventEffect = new JButton(TextUtil.t("eventEditor.addEventEffect"));
		buttonPanel.add(btnAddEventEffect, "2, 1");
		btnAddEventEffect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					EventEffectPanel effectPanel = new EventEffectPanel(
							((WorldEventEffectType)worldEventComboBox.getSelectedItem()).getEventClass().newInstance(),
							worldEvent, new MyMouseListener());
					
					if (eventEffectMasterPanel.getComponents().length == 0) {
						eventEffectMasterPanel.add(effectPanel);
						worldEvent.getEffects().add(effectPanel.getWorldEventEffect());
						setSelected(effectPanel);
					}
					else {
						if (selectedEventEffectPanel != null && 
								selectedEventEffectPanel.getWorldEventEffect().canAddSubEffect()) {
							((WorldEventEffectContainer)selectedEventEffectPanel.getWorldEventEffect()).addEffect(effectPanel.getWorldEventEffect());
							selectedEventEffectPanel.addPanel(effectPanel);
							setSelected(effectPanel);
						}
					}
					
					validate();
					repaint();
				}
				catch (Exception ex) {
					log.error("Error when creating event effect panel", ex);
				}
			}
		});
		
		MyImage effectUp = new MyImage(new ImageData("images/icons/arrow_up.png"));
		buttonPanel.add(effectUp, "4, 1, fill, fill");
		effectUp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (selectedEventEffectPanel != null) {
					EventEffectPanel parentEffectPanel = 
							(EventEffectPanel)SwingUtilities.getAncestorOfClass(EventEffectPanel.class, selectedEventEffectPanel);
					
					if (parentEffectPanel != null) {
						WorldEventEffect selectedEventEffect = selectedEventEffectPanel.getWorldEventEffect();
						WorldEventEffect parentEventEffect = parentEffectPanel.getWorldEventEffect();
						int index = parentEventEffect.getSubEffects().indexOf(selectedEventEffect);
						if (index > 0) {
							index--;
							parentEventEffect.getSubEffects().remove(selectedEventEffect);
							parentEventEffect.getSubEffects().add(index, selectedEventEffect);
							parentEffectPanel.getSubeffectPanel().remove(selectedEventEffectPanel);
							parentEffectPanel.getSubeffectPanel().add(selectedEventEffectPanel, index);                            
							validate();
							repaint();
						}
					}
				}
			}
		});
		
		MyImage effectDown = new MyImage(new ImageData("images/icons/arrow_down.png"));
		buttonPanel.add(effectDown, "5, 1, fill, fill");
		effectDown.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (selectedEventEffectPanel != null) {
					EventEffectPanel parentEffectPanel = 
							(EventEffectPanel)SwingUtilities.getAncestorOfClass(EventEffectPanel.class, selectedEventEffectPanel);
					
					if (parentEffectPanel != null) {
						WorldEventEffect selectedEventEffect = selectedEventEffectPanel.getWorldEventEffect();
						WorldEventEffect parentEventEffect = parentEffectPanel.getWorldEventEffect();
						int index = parentEventEffect.getSubEffects().indexOf(selectedEventEffect);
						if (index < parentEventEffect.getSubEffects().size()-1) {
							index++;
							parentEventEffect.getSubEffects().remove(selectedEventEffect);
							parentEventEffect.getSubEffects().add(index, selectedEventEffect);
							parentEffectPanel.getSubeffectPanel().remove(selectedEventEffectPanel);
							parentEffectPanel.getSubeffectPanel().add(selectedEventEffectPanel, index);                            
							validate();
							repaint();
						}
					}
				}
			}
		});
		
		JButton btnDeleteEventEffect = new JButton(TextUtil.t("eventEditor.deleteEventEffect"));
		buttonPanel.add(btnDeleteEventEffect, "7, 1");
		btnDeleteEventEffect.setForeground(Color.RED);
		btnDeleteEventEffect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedEventEffectPanel != null) {
					Container parent = selectedEventEffectPanel.getParent();
					if (parent == eventEffectMasterPanel) {
						worldEvent.getEffects().remove(selectedEventEffectPanel.getWorldEventEffect());
						eventEffectMasterPanel.remove(selectedEventEffectPanel);
					}
					else {
						parent.remove(selectedEventEffectPanel);
						while (!(parent instanceof EventEffectPanel)) {
							parent = parent.getParent();
						}
						((EventEffectPanel)parent).getWorldEventEffect().getSubEffects().remove(selectedEventEffectPanel.getWorldEventEffect());
					}
					selectedEventEffectPanel = null;
					validate();
					repaint();
				}
			}
		});
		
		saveButton = new JButton(TextUtil.t("eventEditor.save"));
		buttonPanel.add(saveButton, "8, 1, right, default");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EventAndQuestFileLoader.getInstance().save(worldEvent);
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "1, 2, fill, fill");
		
		mainPanel = new JPanel();
		scrollPane.setViewportView(mainPanel);
		mainPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		triggerListPanel = new TriggerListPanel(worldEvent);
		mainPanel.add(triggerListPanel, "1, 1, left, fill");
		
		eventEffectMasterPanel = new JPanel();
		eventEffectMasterPanel.setLayout(new BoxLayout(eventEffectMasterPanel, BoxLayout.Y_AXIS));
		mainPanel.add(eventEffectMasterPanel, "1, 3, fill, fill");
		for (WorldEventEffectType worldEventEffectType : WorldEventEffectType.values()) {
			worldEventComboBox.addItem(worldEventEffectType);
		}
		
		for (WorldEventEffect worldEventEffect : this.worldEvent.getEffects()) {
			addEventEffectPanel(worldEventEffect);
		}
	}
	
	public void addEventEffectPanel(WorldEventEffect worldEventEffect) {
		EventEffectPanel eventEffectPanel = new EventEffectPanel(worldEventEffect, worldEvent, new MyMouseListener());
		eventEffectMasterPanel.add(eventEffectPanel);
	}
	
	public void setSelected(EventEffectPanel eventEffectPanel) {
		if (selectedEventEffectPanel != null) {
			selectedEventEffectPanel.setSelected(false);
		}
		selectedEventEffectPanel = eventEffectPanel;
		eventEffectPanel.setSelected(true);
		
		validate();
		repaint();
	}
	
	private class MyMouseListener extends MouseAdapter {
		public void mouseClicked(java.awt.event.MouseEvent e) {
			if (e.getSource() instanceof EventEffectPanel) {
				EventEffectPanel newPanel = (EventEffectPanel) e.getSource();
				setSelected(newPanel);
			}
		};
	}
	
}