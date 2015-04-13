/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jasbro.gui.objects.div;

import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.gui.DelegateMouseListener;
import jasbro.gui.GuiUtil;
import jasbro.gui.MyPanel;
import jasbro.texts.TextUtil;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 *
 * @author Azrael
 */
public class AttributePanel extends MyPanel {
    private Attribute attribute;
    private JLabel attributeNameLabel;
    private JLabel attributeValueLabel;

    /**
     * Creates new form AttributePanel
     */
    public AttributePanel() {
        setOpaque(false);
        FormLayout layout = new FormLayout(new ColumnSpec[] {
        		ColumnSpec.decode("default:grow"),
        		ColumnSpec.decode("default:grow"),
        		ColumnSpec.decode("3dlu:none"),},
        	new RowSpec[] {
        		RowSpec.decode("default:grow"),});
        setLayout(layout);
	    DelegateMouseListener listener = new DelegateMouseListener();
	    addMouseMotionListener(listener);
	    addMouseListener(listener);
	    setBorder(new EmptyBorder(0, 2, 0, 0));
	    getPreferredSize().width = 1;
        getMinimumSize().width = 1;

    }
    
    public AttributePanel(Attribute attribute) {
    	this();
        this.attribute = attribute;
        initComponents();
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
        initComponents();
    }
    
    private void initComponents() {
    	attributeNameLabel = new JLabel();
        attributeNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        attributeNameLabel.setOpaque(false);
        attributeNameLabel.setText(attribute.getNameResolved());
        attributeNameLabel.getPreferredSize().width = 1;
        attributeNameLabel.getMinimumSize().width = 1;
        attributeNameLabel.setFont(GuiUtil.DEFAULTBOLDFONT);
        add(attributeNameLabel, "1, 1, fill, fill");
    	
        attributeValueLabel = new JLabel();
        attributeValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        attributeValueLabel.setOpaque(false);        
        attributeValueLabel.getPreferredSize().width = 1;
        attributeValueLabel.getMinimumSize().width = 1;
        attributeValueLabel.setFont(GuiUtil.DEFAULTBOLDFONT);
        add(attributeValueLabel, "2, 1, fill, fill");
    }

	public JLabel getAttributeNameLabel() {
		return attributeNameLabel;
	}

	public JLabel getAttributeValueLabel() {
		return attributeValueLabel;
	}
	
	public void setNormalSize() {
		setMinimumSize(null);
		setPreferredSize(null);
		attributeNameLabel.setMinimumSize(null);
		attributeNameLabel.setPreferredSize(null);
		attributeValueLabel.setMinimumSize(null);
		attributeValueLabel.setPreferredSize(null);
	}
	
	@Override
	public void update() {
	    if (attribute.getAttributeType() instanceof EssentialAttributes) {
	        attributeNameLabel.setToolTipText(TextUtil.htmlPreformatted(
	                ((EssentialAttributes) attribute.getAttributeType()).getDescription()));
	    }
	    else if (attribute.getAttributeType() instanceof BaseAttributeTypes) {
	        attributeNameLabel.setToolTipText(TextUtil.htmlPreformatted(
                    ((BaseAttributeTypes) attribute.getAttributeType()).getDescription()));
	    }
	    
	    attributeValueLabel.setText(attribute.getValue()+"");
        int bonus = attribute.getBonus();
        String toolTip;
        Object attributes [] = {(int)attribute.getInternValue(), attribute.getMaxValue(), bonus};
        if (bonus == 0) {
            toolTip = TextUtil.t("ui.attributetooltip", attributes);
        }
        else {
            toolTip = TextUtil.t("ui.attributetooltip.bonus", attributes);
        }
        attributeValueLabel.setToolTipText(toolTip);
	}
}
