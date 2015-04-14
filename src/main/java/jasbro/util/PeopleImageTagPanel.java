package jasbro.util;

import jasbro.game.character.CharacterBase;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageTagGroup;
import jasbro.texts.TextUtil;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class PeopleImageTagPanel extends JPanel {
	@SuppressWarnings("unused")
	private PeopleImageTagPanel() {
		this(null, null);
	}
	
	public PeopleImageTagPanel(final ImageData image, final CharacterBase characterBase) {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblNewLabel = new JLabel(TextUtil.t("imagetag.characterfuta"));
		add(lblNewLabel, "1, 1, right, default");
		
		final JComboBox<ImageTag> characterFutaCombobox = new JComboBox<ImageTag>();
		add(characterFutaCombobox, "2, 1, fill, default");
		characterFutaCombobox.addItem(null);
		for (ImageTag imageTag : ImageTag.getImageTags(ImageTagGroup.CHARACTERGENDER)) {
			characterFutaCombobox.addItem(imageTag);
			if (image.getTags().contains(imageTag)) {
				characterFutaCombobox.setSelectedItem(imageTag);
			}
		}
		characterFutaCombobox.setRenderer(new DefaultListCellRenderer() {
			@SuppressWarnings("rawtypes")
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean hasFocus) {
				JLabel label = (JLabel) super.getListCellRendererComponent(
						list, value, index, isSelected, hasFocus);
				ImageTag imageTag = (ImageTag) value;
				if (imageTag != null) {
					label.setText(imageTag.getText());
					label.setToolTipText(imageTag.getDescription());
				}
				else {
					label.setText(" ");
				}
				return label;
			}
        });
		
		characterFutaCombobox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				ImageTag imageTag = (ImageTag)characterFutaCombobox.getSelectedItem();
				for (ImageTag curImageTag : ImageTag.getImageTags(ImageTagGroup.CHARACTERGENDER)) {
					image.getTags().remove(curImageTag);
				}
				characterFutaCombobox.setToolTipText("");
				if (imageTag != null) {
					image.addTag(imageTag);
					characterFutaCombobox.setToolTipText(imageTag.getDescription());
				}
				characterBase.setChanged(true);
                repaint();
			} 
		});
		
		
		//Dominant / Submissive Position
		lblNewLabel = new JLabel(TextUtil.t("imagetag.dominantPosition"));
		lblNewLabel.setToolTipText(TextUtil.t("imagetag.dominantPosition.description"));
		add(lblNewLabel, "1, 2, right, default");
		
		final JComboBox<ImageTag> dominantPositionCombobox = new JComboBox<ImageTag>();
		add(dominantPositionCombobox, "2, 2, fill, default");
		dominantPositionCombobox.addItem(null);
		for (ImageTag imageTag : ImageTag.getImageTags(ImageTagGroup.DOMINANCE)) {
			dominantPositionCombobox.addItem(imageTag);
			if (image.getTags().contains(imageTag)) {
				dominantPositionCombobox.setSelectedItem(imageTag);
			}
		}
		dominantPositionCombobox.setRenderer(new DefaultListCellRenderer() {
			@SuppressWarnings("rawtypes")
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean hasFocus) {
				JLabel label = (JLabel) super.getListCellRendererComponent(
						list, value, index, isSelected, hasFocus);
				ImageTag imageTag = (ImageTag) value;
				if (imageTag != null) {
					label.setText(imageTag.getText());
					label.setToolTipText(imageTag.getDescription());
				}
				else {
					label.setText(" ");
				}
				return label;
			}
        });
		
		dominantPositionCombobox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				ImageTag imageTag = (ImageTag)dominantPositionCombobox.getSelectedItem();
				for (ImageTag curImageTag : ImageTag.getImageTags(ImageTagGroup.DOMINANCE)) {
					image.getTags().remove(curImageTag);
				}
				dominantPositionCombobox.setToolTipText("");
				if (imageTag != null) {
					image.addTag(imageTag);
					dominantPositionCombobox.setToolTipText(imageTag.getDescription());
				}
				characterBase.setChanged(true);
                repaint();
			} 
		});
		
		
		//Gender of other people
		lblNewLabel = new JLabel(TextUtil.t("imagetag.genderOthers"));
		lblNewLabel.setToolTipText(TextUtil.t("imagetag.genderOthers.description"));
		add(lblNewLabel, "1, 3, right, default");
		
		final JComboBox<ImageTag> genderOthersCombobox = new JComboBox<ImageTag>();
		add(genderOthersCombobox, "2, 3, fill, default");
		genderOthersCombobox.addItem(null);
		for (ImageTag imageTag : ImageTag.getImageTags(ImageTagGroup.OTHERGENDER)) {
			genderOthersCombobox.addItem(imageTag);
			if (image.getTags().contains(imageTag)) {
				genderOthersCombobox.setSelectedItem(imageTag);
			}
		}		
		genderOthersCombobox.setRenderer(new DefaultListCellRenderer() {
			@SuppressWarnings("rawtypes")
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean hasFocus) {
				JLabel label = (JLabel) super.getListCellRendererComponent(
						list, value, index, isSelected, hasFocus);
				ImageTag imageTag = (ImageTag) value;
				if (imageTag != null) {
					label.setText(imageTag.getText());
					label.setToolTipText(imageTag.getDescription());
				}
				else {
					label.setText(" ");
				}
				return label;
			}
        });
		
		genderOthersCombobox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				ImageTag imageTag = (ImageTag)genderOthersCombobox.getSelectedItem();
				for (ImageTag curImageTag : ImageTag.getImageTags(ImageTagGroup.OTHERGENDER)) {
					image.getTags().remove(curImageTag);
				}
				genderOthersCombobox.setToolTipText("");
				if (imageTag != null) {
					image.addTag(imageTag);
					genderOthersCombobox.setToolTipText(imageTag.getDescription());
				}
				characterBase.setChanged(true);
                repaint();
			} 
		});
		
		
		//Amount other people
		lblNewLabel = new JLabel(TextUtil.t("imagetag.amountOthers"));
		lblNewLabel.setToolTipText(TextUtil.t("imagetag.amountOthers.description"));
		add(lblNewLabel, "1, 4, right, default");
		
		final JComboBox<ImageTag> amountOthersCombobox = new JComboBox<ImageTag>();
		add(amountOthersCombobox, "2, 4, fill, default");
		amountOthersCombobox.addItem(null);
		for (ImageTag imageTag : ImageTag.getImageTags(ImageTagGroup.AMOUNTPEOPLE)) {
			amountOthersCombobox.addItem(imageTag);
			if (image.getTags().contains(imageTag)) {
				amountOthersCombobox.setSelectedItem(imageTag);
			}
		}		
		amountOthersCombobox.setRenderer(new DefaultListCellRenderer() {
			@SuppressWarnings("rawtypes")
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean hasFocus) {
				JLabel label = (JLabel) super.getListCellRendererComponent(
						list, value, index, isSelected, hasFocus);
				ImageTag imageTag = (ImageTag) value;
				if (imageTag != null) {
					label.setText(imageTag.getText());
					label.setToolTipText(imageTag.getDescription());
				}
				else {
					label.setText(" ");
				}
				return label;
			}
        });
		
		amountOthersCombobox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				ImageTag imageTag = (ImageTag)amountOthersCombobox.getSelectedItem();
				for (ImageTag curImageTag : ImageTag.getImageTags(ImageTagGroup.AMOUNTPEOPLE)) {
					image.getTags().remove(curImageTag);
				}
				amountOthersCombobox.setToolTipText("");
				if (imageTag != null) {
					image.addTag(imageTag);
					amountOthersCombobox.setToolTipText(imageTag.getDescription());
				}
				characterBase.setChanged(true);
                repaint();
			} 
		});
	}

}
