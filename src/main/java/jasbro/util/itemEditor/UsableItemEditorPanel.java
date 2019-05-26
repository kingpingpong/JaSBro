package jasbro.util.itemEditor;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import jasbro.game.items.UsableItem;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.game.items.usableItemEffects.UsableItemEffectContainer;
import jasbro.game.items.usableItemEffects.UsableItemEffectType;

public class UsableItemEditorPanel extends JPanel {
	private final static Logger log = LogManager.getLogger(UsableItemEditorPanel.class);
	private UsableItem item;
	private UsableItemEffectPanel selectedEffectPanel;
	private JComboBox<UsableItemEffectType> effectTypeComboBox;
	
	public UsableItemEditorPanel(UsableItem curItem) {
		this.item = curItem;
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow(8)"),
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		
		final JPanel effectPanel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(effectPanel);
		
		add(scrollPane, "1, 1, 1, 2, fill, fill");
		effectPanel.setLayout(new GridLayout(1, 1, 0, 0));
		
		JPanel panel = new JPanel();
		add(panel, "3, 1, fill, fill");
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		effectTypeComboBox = new JComboBox<UsableItemEffectType>();
		panel.add(effectTypeComboBox, "1, 1, fill, default");
		for (UsableItemEffectType itemEffectType : UsableItemEffectType.values()) {
			effectTypeComboBox.addItem(itemEffectType);
		}
		
		JButton btnAdd = new JButton("Add");
		panel.add(btnAdd, "1, 2");
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					UsableItemEffectType usableItemEffectType = (UsableItemEffectType)effectTypeComboBox.getSelectedItem();
					UsableItemEffect usableItemEffect = usableItemEffectType.getItemEffectClass().newInstance();
					
					if (effectPanel.getComponents().length != 0) {
						if (! (selectedEffectPanel.getItemEffect() instanceof UsableItemEffectContainer)) {
							return;
						}
						else {
							selectedEffectPanel.getItemEffect().getSubEffects().add(usableItemEffect);
						}
					}
					else {
						selectedEffectPanel = null;
						item.setItemEffect(usableItemEffect);
					}
					
					final UsableItemEffectPanel newPanel = new UsableItemEffectPanel();
					newPanel.setItemEffect(usableItemEffect);
					
					if (selectedEffectPanel != null) {
						selectedEffectPanel.setSelected(false);
						selectedEffectPanel.addPanel(newPanel);
					}
					else {
						effectPanel.add(newPanel);
					}
					selectedEffectPanel = newPanel;
					newPanel.setSelected(true);
					validate();
					repaint();
					
					newPanel.addMouseListener(new MyMouseListener());
				}
				catch (Exception ex) {
					log.error("Error", ex);
				}
			}
		});
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!selectedEffectPanel.hasChildEffects()) {
					Container component = selectedEffectPanel.getParent();
					while (component != UsableItemEditorPanel.this
							&& !(component instanceof UsableItemEffectPanel)) {
						component = component.getParent();
					}
					
					if (component != UsableItemEditorPanel.this) {
						UsableItemEffectPanel usableItemEffectPanel = (UsableItemEffectPanel) component;
						usableItemEffectPanel.setSelected(true);
						usableItemEffectPanel.getItemEffect().getSubEffects().remove(selectedEffectPanel.getItemEffect());
						usableItemEffectPanel.removePanel(selectedEffectPanel);
						selectedEffectPanel = usableItemEffectPanel;
					}
					else {
						selectedEffectPanel = null;
						effectPanel.removeAll();
						item.setItemEffect(null);
					}
					validate();
					repaint();
				}
			}
		});
		panel.add(btnDelete, "1, 3");
		
		if (item.getItemEffect() != null) {
			try {
				UsableItemEffectPanel usableItemEffectPanel = new UsableItemEffectPanel();
				usableItemEffectPanel.setItemEffect(item.getItemEffect());
				initEffectPanel(usableItemEffectPanel);
				effectPanel.add(usableItemEffectPanel);
				usableItemEffectPanel.setSelected(true);
				usableItemEffectPanel.addMouseListener(new MyMouseListener());
				selectedEffectPanel = usableItemEffectPanel;
			}
			catch (Exception e) {
				log.error("Error while initializing item effect panel", e);
			}
			
		}
		
		add(new SpawnDataPanel(curItem), "3, 2, fill, fill");
		
		validate();
		repaint();
	}
	
	private void initEffectPanel(UsableItemEffectPanel itemEffectPanel) 
			throws InstantiationException, IllegalAccessException {
		UsableItemEffect effect = itemEffectPanel.getItemEffect();
		for (UsableItemEffect subEffect : effect.getSubEffects()) {
			UsableItemEffectPanel usableItemEffectPanel = new UsableItemEffectPanel();
			usableItemEffectPanel.addMouseListener(new MyMouseListener());
			usableItemEffectPanel.setItemEffect(subEffect);
			itemEffectPanel.addPanel(usableItemEffectPanel);
			initEffectPanel(usableItemEffectPanel);
		}
	}
	
	private class MyMouseListener extends MouseAdapter {
		public void mouseClicked(java.awt.event.MouseEvent e) {
			if (selectedEffectPanel != null) {
				selectedEffectPanel.setSelected(false);
			}
			UsableItemEffectPanel newPanel = (UsableItemEffectPanel) e.getSource();
			newPanel.setSelected(true);
			selectedEffectPanel = newPanel;
			
			validate();
			repaint();
		};
	}
	
}