package jasbro.gui.character;

import jasbro.game.character.CharacterBase;
import jasbro.game.character.traits.Trait;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.texts.TextUtil;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class CharacterStartDescriptionPanel extends TranslucentPanel {
	public CharacterStartDescriptionPanel(CharacterBase characterBase) {
		setPreferredSize(null);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		JPanel mainPanel = new JPanel();		
		mainPanel.setOpaque(false);
		JScrollPane scrollPane = new JScrollPane(mainPanel);
		add(scrollPane);
		mainPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
    	JPanel traitPanel = new JPanel();
    	traitPanel.setPreferredSize(null);
    	FormLayout layout = new FormLayout(new ColumnSpec[] {
        		ColumnSpec.decode("1dlu:grow"),},
        	new RowSpec[] {
    			RowSpec.decode("default:none")
    		});
    	
    	traitPanel.setOpaque(false);
    	traitPanel.setLayout(layout);
    	mainPanel.add(traitPanel, "2, 2, fill, fill");
    	JLabel label = new JLabel(TextUtil.t("ui.traits"));
    	label.setFont(GuiUtil.DEFAULTBOLDFONT.deriveFont(GuiUtil.DEFAULTBOLDFONT.getSize() + 1f));
    	traitPanel.add(label, "1, 1, left, top");
    	for (int i = 0; i < characterBase.getTraits().size(); i++) {
    		Trait trait = characterBase.getTraits().get(i);
    		layout.appendRow(RowSpec.decode("min:none"));
    		JLabel traitLabel = new JLabel(trait.getText());
    		traitLabel.setToolTipText(trait.getDescription());
    		traitLabel.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
    		traitPanel.add(traitLabel, "1,"+(i+2)+", left, top");
    	}
		

        JTextArea descriptionTextArea = GuiUtil.getDefaultTextarea();
        JScrollPane scrollPaneDescription = new JScrollPane(descriptionTextArea);
        scrollPaneDescription.setBorder(null);
        descriptionTextArea.setText(characterBase.getFullDescription());
        descriptionTextArea.setFont(GuiUtil.DEFAULTBOLDFONT);
        mainPanel.add(scrollPaneDescription, "2, 4, 2, 1, fill, fill");
	}
}
