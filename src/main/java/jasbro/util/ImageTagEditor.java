package jasbro.util;

import jasbro.game.character.CharacterBase;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageTagGroup;
import jasbro.texts.TextUtil;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 *
 * @author Azrael
 */
public class ImageTagEditor extends javax.swing.JPanel {

    private ImageData image;
    private CharacterEditor characterEditor;
    private JTabbedPane imageDataTabbedPane;
    private JList<ImageData> imageList;
    private CharacterBase characterBase;


    public ImageTagEditor() {
    	setLayout(new FormLayout(new ColumnSpec[] {
    			ColumnSpec.decode("default:grow"),},
    		new RowSpec[] {
    			RowSpec.decode("default:grow"),}));
    	
    	imageDataTabbedPane = new JTabbedPane(JTabbedPane.TOP);
    	add(imageDataTabbedPane, "1, 1, fill, fill");
    }
    
    /**
     * Creates new form AttributeEditor
     */
    public ImageTagEditor(CharacterEditor characterEditor) {
    	this();
    	this.characterEditor = characterEditor;
    }

    private void addTagComponents() {


    	KeyAdapter myTagKeyListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				characterEditor.getImageList().dispatchEvent(e);
			}
        };
    	
        String rowBehavior = "grow";
        
        for (ImageTagGroup imageTagGroup : ImageTagGroup.values()) {        	
        	if (imageTagGroup.isStandardGroup()) {
            	JPanel curTabPanel = new JPanel();
            	FormLayout layout = new FormLayout(new ColumnSpec[] {
        				ColumnSpec.decode("1dlu:grow"),
        				ColumnSpec.decode("1dlu:grow"),
        				ColumnSpec.decode("1dlu:grow"),},
        			new RowSpec[] {
        				RowSpec.decode("pref:grow"),
        				RowSpec.decode("fill:default:grow"),});
            	curTabPanel.setLayout(layout);
            	imageDataTabbedPane.addTab(imageTagGroup.getText(), null, curTabPanel, null);
        		
        		int amount = 0;
        		for (final ImageTag imageTag : ImageTag.values()) {
        			if (imageTag.getImageTagGroup() == imageTagGroup) {
        				if (amount > 0 && amount % 18 == 0) {
                    		if (ImageTag.values().length - amount < 15) {
                    			rowBehavior = "none";
                    		}
                    		curTabPanel = new JPanel();
                    		layout = new FormLayout(new ColumnSpec[] {
                    				ColumnSpec.decode("1dlu:grow"),
                    				ColumnSpec.decode("1dlu:grow"),
                    				ColumnSpec.decode("1dlu:grow"),},
                    			new RowSpec[] {
                    				RowSpec.decode("pref:"  + rowBehavior ),
                    				RowSpec.decode("fill:default:grow"),});
                        	curTabPanel.setLayout(layout);
                        	imageDataTabbedPane.addTab(imageTagGroup.getText() + " " + (amount / 18 + 1), null, curTabPanel, null);
                    	}
                    	else if (amount > 0 && amount % 3 == 0) {
                    		layout.insertRow((amount%18) / 3 + 1, RowSpec.decode("pref:" + rowBehavior ));
                    	}
                    	
                        JCheckBox checkBox = new ImageTagCheckbox(imageTag, image.getTags().contains(imageTag));
                        curTabPanel.add(checkBox, (amount%3)+1 + ", " + ((amount%18) / 3 + 1) + ", fill, top" );
                        checkBox.addKeyListener(myTagKeyListener);
                        checkBox.addItemListener(new ItemListener() {
                            public void itemStateChanged(ItemEvent e) {
                                JCheckBox box = (JCheckBox)e.getSource();
                                if (box.isSelected()) {
                                	for (ImageData image : imageList.getSelectedValuesList()) {
                                        image.addTag(imageTag);
                                	}
                                }
                                else {
                                	for (ImageData image : imageList.getSelectedValuesList()) {
                                        image.removeTag(imageTag);
                                	}
                                }
                                if (characterBase != null) {
                                    characterBase.setChanged(true);
                                }
                            }
                        });
                        amount++;
        			}                	
                }
        	}
        }
        
        JPanel peopleImageTagPabel = new PeopleImageTagPanel(image, characterBase);
        imageDataTabbedPane.addTab(TextUtil.t("imagetag.people"), null, peopleImageTagPabel, null);
        
        JPanel curTabPanel = new JPanel();
        FormLayout layout = new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("1dlu:grow"),
				ColumnSpec.decode("1dlu:grow"),},
			new RowSpec[] {
				RowSpec.decode("pref:none"),
				RowSpec.decode("fill:default:grow"),});
    	curTabPanel.setLayout(layout);
    	imageDataTabbedPane.addTab("Other ", null, curTabPanel, null);
    	final JTextField customTextField = new JTextField(image.getCustomText());
    	curTabPanel.add(new JLabel("Custom text:"), "1, 1, fill, top");
    	curTabPanel.add(customTextField, "2, 1, fill, top");
    	customTextField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
            	for (ImageData image : imageList.getSelectedValuesList()) {
    				image.setCustomText(customTextField.getText());
            	}
            	if (characterBase != null) {
                    characterBase.setChanged(true);
            	}
			}
		});
        
        
        repaint();
    }

    public void setImage(ImageData image, CharacterBase characterBase) {
    	int selectedTab = imageDataTabbedPane.getSelectedIndex();
    	this.characterBase = characterBase;
    	if (this.image == null) {
    		selectedTab = 0;
    	}
        imageDataTabbedPane.removeAll();
        if (image != null) {
            this.image = image;
            addTagComponents();
            imageDataTabbedPane.setSelectedIndex(selectedTab);
        }
        validate();
    }

    public class ImageTagCheckbox extends JCheckBox {    	
    	public ImageTagCheckbox(ImageTag imageTag, boolean selected) {
    		setText(imageTag.getText());
    		setSelected(selected);
    		
    		String tooltip = imageTag.getDescription();
            if (imageTag == ImageTag.CLOTHED ||
                    imageTag == ImageTag.NAKED ||
                    imageTag == ImageTag.SLEEP ||
                    imageTag == ImageTag.MAID ||
                    imageTag == ImageTag.FUTA ||
                    imageTag == ImageTag.LESBIAN ||
                    imageTag == ImageTag.ORAL ||
                    imageTag == ImageTag.VAGINAL ||
                    imageTag == ImageTag.ANAL ||
                    imageTag == ImageTag.TITFUCK ||
                    imageTag == ImageTag.BONDAGE ||
                    imageTag == ImageTag.FOREPLAY ||
                    imageTag == ImageTag.GROUP ||
                    imageTag == ImageTag.SWIMSUIT ||
                    imageTag == ImageTag.ICON
                    ) {
                if (tooltip == null) {
                    tooltip = "";
                }
                tooltip += "\n" + TextUtil.t("imagetag.importantTag");
                setForeground(Color.decode("#085508"));
            }
    		if (imageTag.isExcludeTag() && imageTag.getImageTagGroup() != ImageTagGroup.FILTERTAGS) {
    		    if (tooltip == null) {
    		        tooltip = "";
    		    }
    		    tooltip += "\n\n" + TextUtil.t("imagetag.exclusiveTag");
                setForeground(Color.RED);
    		}
    		if (tooltip != null) {
                setText(getText() + " (i)");
                setToolTipText(TextUtil.htmlPreformatted(tooltip));
    		}
    	}
    }

	public void setImageList(JList<ImageData> imageList) {
		this.imageList = imageList;
	}
    
    
}
