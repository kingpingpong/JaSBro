package jasbro.util.eventEditor;

import jasbro.Jasbro;
import jasbro.game.items.Item;
import jasbro.game.world.customContent.ImageSelection;
import jasbro.game.world.customContent.ImageSelection.ImageLocation;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TPath;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class ImageSelectionPanel extends JPanel {
	private ImageSelection imageSelection;
	private JTextField textField;
	private JTextField textField_1;
	private JPanel backgroundPanel;
	private JTabbedPane tabbedPane;
	private MyImage previewImage;
	private JComboBox<ImageTag> imageTagComboBox;
	private JPanel imageByNamePanel;
	
	private WorldEvent worldEvent;
	private Item item;
	private ActionListener deleteActionListener;
	
	public ImageSelectionPanel(Item item) {
		this.item = item;
		if (item.getImageSelection() != null) {
			init(item.getImageSelection());
		}
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public ImageSelectionPanel(final WorldEvent worldEvent, 
			ImageSelection imageSelectionTmp, ActionListener deleteActionListener) {
		this.worldEvent = worldEvent;
		this.deleteActionListener = deleteActionListener;
		init(imageSelectionTmp);
	}
	
	public void init(ImageSelection imageSelectionTmp) {
		this.imageSelection = imageSelectionTmp;
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("50dlu:grow"),
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		imageByNamePanel = new JPanel();
		
		if (item != null) {
			add(imageByNamePanel, "1, 1, fill, fill");
		}
		else {
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			add(tabbedPane, "1, 1, fill, fill");
			
			tabbedPane.addTab("Image by name", null, imageByNamePanel, null);
		}
		
		imageByNamePanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JLabel lblNewLabel_1 = new JLabel(TextUtil.t("eventEditor.image"));
		imageByNamePanel.add(lblNewLabel_1, "1, 1, right, default");
		
		final JComboBox<String> targetImageComboBox = new JComboBox<String>();
		imageByNamePanel.add(targetImageComboBox, "2, 1, fill, default");
		targetImageComboBox.setPreferredSize(new Dimension(650, 25));
		targetImageComboBox.setPrototypeDisplayValue("a");
		targetImageComboBox.setEditable(true);
		
		targetImageComboBox.setSelectedItem(imageSelection.getImage());
		targetImageComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				imageSelection.setImage((String)targetImageComboBox.getSelectedItem());
				if (previewImage != null) {
					try {
						if (item != null) {
							previewImage.setImage(imageSelection.getImageData(item));
						}
						else {
							previewImage.setImage(imageSelection.getImageData(worldEvent));
						}
						validate();
						repaint();
					}
					catch (Exception ex) {
					}
				}
			}
		});
		
		JLabel lblNewLabel_2 = new JLabel(TextUtil.t("eventEditor.imageLocation"));
		imageByNamePanel.add(lblNewLabel_2, "1, 2, right, default");
		
		final JComboBox<ImageLocation> comboBox = new JComboBox<ImageLocation>();
		imageByNamePanel.add(comboBox, "2, 2, fill, default");
		for (ImageLocation imageLocation : ImageLocation.values()) {
			comboBox.addItem(imageLocation);
		}
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				imageSelection.setImageLocation((ImageLocation) comboBox.getSelectedItem());
				Jasbro.getThreadpool().execute(new Runnable() {
					
					@Override
					public void run() {
						String selectedItem = (String)targetImageComboBox.getSelectedItem();
						targetImageComboBox.removeAllItems();
						targetImageComboBox.addItem(selectedItem);
						TFile parentFolder;
						if (imageSelection.getImageLocation() == ImageLocation.LOCAL) {
							if (worldEvent != null) {
								parentFolder = worldEvent.getFile().getParentFile();
							}
							else {
								parentFolder = item.getFile().getParentFile();
							}
						}
						else {
							parentFolder = new TFile("images");
						}
						
						List<String> images;
						if (item != null && imageSelection.getImageLocation() == ImageLocation.GLOBAL) {
							images = listImages(parentFolder, new TFile("images/icons/items"), 10);
						}
						else {
							images = listImages(parentFolder, parentFolder, 10);
						}
						
						
						for (String image : images) {
							targetImageComboBox.addItem(image);
						}
						validate();
						repaint();
					}
				});
			}
		});
		comboBox.setSelectedItem(imageSelection.getImageLocation());
		
		previewImage = new MyImage();
		validate();
		if (item == null) { //there should be enough space in the event editor
			imageByNamePanel.add(previewImage, "3, 1, 1, 2 fill, fill");
		}
		else {
			imageByNamePanel.add(previewImage, "1, 3, 2, 1, fill, fill");
		}
		
		if (item == null) {
			JPanel imageByCharacterPanel = new JPanel();
			tabbedPane.addTab("Image from character", null, imageByCharacterPanel, null);
			imageByCharacterPanel.setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.DEFAULT_COLSPEC,
					ColumnSpec.decode("default:grow"),},
					new RowSpec[] {
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,}));
			
			JLabel lblNewLabel = new JLabel(TextUtil.t("eventEditor.targetCharacter"));
			imageByCharacterPanel.add(lblNewLabel, "1, 1, right, default");
			
			textField = new JTextField();
			imageByCharacterPanel.add(textField, "2, 1, fill, default");
			textField.setText(imageSelection.getTarget());
			textField.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					imageSelection.setTarget(textField.getText());
				}
				
				public void removeUpdate(DocumentEvent e) {
					imageSelection.setTarget(textField.getText());
				}
				
				public void changedUpdate(DocumentEvent e) {
					imageSelection.setTarget(textField.getText());
				}
			});
			
			JLabel lblNewLabel_4 = new JLabel(TextUtil.t("imagetag"));
			imageByCharacterPanel.add(lblNewLabel_4, "1, 2, right, default");
			
			imageTagComboBox = new JComboBox<ImageTag>();
			imageByCharacterPanel.add(imageTagComboBox, "2, 2, fill, default");
			for (ImageTag imageTag : ImageTag.values()) {
				imageTagComboBox.addItem(imageTag);
			}
			imageTagComboBox.setSelectedItem(imageSelection.getImageTags().get(0));
			imageTagComboBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					imageSelection.getImageTags().remove(0);
					imageSelection.getImageTags().add(0, (ImageTag)imageTagComboBox.getSelectedItem());
				}
			});
			
			backgroundPanel = new JPanel();
			tabbedPane.addTab(TextUtil.t("eventEditor.characterBackground"), null, backgroundPanel, null);
			backgroundPanel.setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.DEFAULT_COLSPEC,
					ColumnSpec.decode("default:grow"),},
					new RowSpec[] {
					FormFactory.DEFAULT_ROWSPEC,}));
			tabbedPane.addChangeListener(new MyChangeListener(backgroundPanel));
			
			
			JLabel lblNewLabel_3 = new JLabel(TextUtil.t("eventEditor.targetCharacter"));
			backgroundPanel.add(lblNewLabel_3, "1, 1, right, default");
			
			textField_1 = new JTextField();
			backgroundPanel.add(textField_1, "2, 1, fill, default");
			textField_1.setText(imageSelection.getTarget());
			textField_1.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					imageSelection.setTarget(textField_1.getText());
				}
				
				public void removeUpdate(DocumentEvent e) {
					imageSelection.setTarget(textField_1.getText());
				}
				
				public void changedUpdate(DocumentEvent e) {
					imageSelection.setTarget(textField_1.getText());
				}
			});
			
			if (imageSelection.isBackground()) {
				tabbedPane.setSelectedComponent(backgroundPanel);
			}
			else if (imageSelection.getImage() == null) {
				tabbedPane.setSelectedIndex(1); //TODO improve
			}
		}
		
		if (deleteActionListener != null) {
			JButton deleteButton = new JButton(TextUtil.t("eventEditor.delete"));
			deleteButton.setForeground(Color.RED);
			add(deleteButton, "1, 2, left, default");
			deleteButton.addActionListener(deleteActionListener);
		}
		
	}
	
	private class MyChangeListener implements ChangeListener {
		private JPanel backgroundPanel;
		
		public MyChangeListener(JPanel backgroundPanel) {
			this.backgroundPanel = backgroundPanel;
		}
		
		@Override
		public void stateChanged(ChangeEvent e) {
			if (tabbedPane.getSelectedComponent() != imageByNamePanel) {
				imageSelection.setImage(null);
			}
			if (tabbedPane.getSelectedComponent() == backgroundPanel) {
				imageSelection.setBackground(true);
			}
			else {
				imageSelection.setBackground(false);
			}
		}
	}
	
	private List<String> listImages(TFile origFolder, TFile curFolder, int depth) {
		TFile files[] = curFolder.listFiles();
		List<String> images = new ArrayList<String>();
		
		for (TFile curFile : files) {
			if (ImageUtil.getInstance().isImage(curFile)) {
				String relativePath = new TPath(origFolder)
				.relativize(new TPath(curFile)).toString().replace('\\', '/');
				images.add(relativePath);
			} 
			else if (curFile.isDirectory() && depth > 0) {
				images.addAll(listImages(origFolder, curFile, depth-1));
			}
			else {
				// not implemented
			}
		}
		return images;
	}
}