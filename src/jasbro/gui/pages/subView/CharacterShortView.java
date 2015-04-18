package jasbro.gui.pages.subView;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.interfaces.MyEventListener;
import jasbro.gui.DelegateMouseListener;
import jasbro.gui.GuiUtil;
import jasbro.gui.RPGView;
import jasbro.gui.objects.div.CharacterConditionsPanel;
import jasbro.gui.objects.div.IconAttributePanel;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.VerticalAttributeBar;
import jasbro.gui.pages.CharacterScreen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 *
 * @author Azrael
 */
public class CharacterShortView extends javax.swing.JPanel implements MyEventListener {
    private Charakter character;
    private List<IconAttributePanel> attributePanels;
    private JPanel attributePane;
    private MyImage characterIcon;
    private JPanel barPanel;
    private VerticalAttributeBar energyBar;
    private VerticalAttributeBar healthBar;
    private CharacterConditionsPanel characterConditionsPanel;

    /**
     * Creates new form CharacterShortView
     */
    public CharacterShortView() {
        setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
        initComponents();           
    }
    
    public CharacterShortView(Charakter character) {
        this();
        setCharacter(character);
    }
    
    public CharacterShortView(Charakter character, boolean conditionsPanel) {
        this(character);
        if (!conditionsPanel) {
        	this.characterConditionsPanel.setVisible(false);
        }
    }
    
    private void initComponents() {
        characterIcon = new jasbro.gui.objects.div.MyImage();
        attributePane = new javax.swing.JPanel();
        
        attributePanels = new ArrayList<IconAttributePanel>();

        setBackground(new java.awt.Color(232, 203, 142));
        setMinimumSize(new java.awt.Dimension(12, 24));
        setPreferredSize(new java.awt.Dimension(120, 240));
        setLayout(new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("1dlu:grow"),
                ColumnSpec.decode("1dlu:grow"),},
            new RowSpec[] {
                RowSpec.decode("1dlu:grow"),
                RowSpec.decode("1dlu:grow(3)"),}));

        characterIcon.setMaximumSize(new java.awt.Dimension(99999, 999999));
        characterIcon.setMinimumSize(new java.awt.Dimension(20, 20));
        characterIcon.setPreferredSize(new java.awt.Dimension(20, 20));
        add(characterIcon, "1, 1, fill, fill");
        
        characterConditionsPanel = new CharacterConditionsPanel();
        add(characterConditionsPanel, "2, 1, fill, fill");
        
        barPanel = new JPanel();        
        barPanel.setOpaque(false);
        add(barPanel, "1, 2, fill, fill");
        barPanel.setLayout(new GridLayout(0, 2, 0, 0));
        
        energyBar = new VerticalAttributeBar();
        barPanel.add(energyBar);
        
        healthBar = new VerticalAttributeBar();
        barPanel.add(healthBar);

       

        attributePane.setMinimumSize(new java.awt.Dimension(20, 60));
        attributePane.setOpaque(false);
        attributePane.setPreferredSize(new java.awt.Dimension(20, 60));
        FormLayout layout = new FormLayout(new ColumnSpec[] {
        		ColumnSpec.decode("default:grow"),},
        	new RowSpec[] {
        		RowSpec.decode("default:grow(20)"),
        		RowSpec.decode("default:grow"),
        		RowSpec.decode("default:grow(20)"),
        		RowSpec.decode("default:grow"),
        		RowSpec.decode("default:grow(20)"),
        		RowSpec.decode("default:grow"),
        		RowSpec.decode("default:grow(20)"),
        		RowSpec.decode("default:grow"),
        		RowSpec.decode("default:grow(20)")
        		});
        attributePane.setLayout(layout);
        layout.setRowGroups(new int[][]{ {1, 3, 5, 7, 9}});

        for (int i = 0; i < 5; i++) {
        	IconAttributePanel attributePanel = new IconAttributePanel();
        	attributePanels.add(attributePanel);
        	attributePane.add(attributePanel, "1, " + ((i*2)+1) + ", fill, fill");
        }
        
        add(attributePane, "2, 2, fill, fill");
        
        DelegateMouseListener listener = new DelegateMouseListener();
        characterIcon.addMouseMotionListener(listener);
        characterIcon.addMouseListener(listener);  
        attributePane.addMouseMotionListener(listener);
        attributePane.addMouseListener(listener);  
        barPanel.addMouseMotionListener(listener);
        barPanel.addMouseListener(listener);  

        
        
        DelegateMouseListener iconDelegateMouseListener = new DelegateMouseListener() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		if (!(Jasbro.getInstance().getGui().getLayerPane().getComponent(0) instanceof CharacterScreen)) {
        			Jasbro.getInstance().getGui().showCharacterView(character);
        		}
        	}
        };
        characterIcon.addMouseListener(iconDelegateMouseListener);
        characterIcon.addMouseMotionListener(iconDelegateMouseListener);
        
        setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
        
        addComponentListener(new ComponentAdapter() {
        	@Override
        	public void componentResized(ComponentEvent e) {
        		CharacterShortView.this.updateFontSize();
        	}
		});

    }
    
	public Charakter getCharacter() {
        return character;
    }

    public final void setCharacter(Charakter character) {
        characterIcon.setToolTipText(character.getName());
        characterIcon.setImage(character.getIcon());
        
        healthBar.setAttribute(character.getAttribute(EssentialAttributes.HEALTH));
        energyBar.setAttribute(character.getAttribute(EssentialAttributes.ENERGY));
        
        characterConditionsPanel.setCharacter(character);
        character.addListener(this);
        
        this.character = character;
        
        updateAttributeDisplay();
        characterConditionsPanel.update();
        validate();
        repaint();
    }
    
    public void updateAttributeDisplay() {
        if (character != null) {
            List<AttributeType> attributeList;
            if (character.getSpecializations().size() > 0) {
                attributeList = character.getSpecializations().iterator().next().getAssociatedAttributes();
            }
            else {
                attributeList = new ArrayList<AttributeType>();
                attributeList.add(BaseAttributeTypes.CHARISMA);
                attributeList.add(BaseAttributeTypes.INTELLIGENCE);
                attributeList.add(BaseAttributeTypes.STAMINA);
                attributeList.add(BaseAttributeTypes.STRENGTH);
            }
            for (int i = 0; i < attributeList.size() && i < attributePanels.size(); i++) {
                IconAttributePanel attributePanel = attributePanels.get(i);
                attributePanel.setAttribute(character.getAttribute(attributeList.get(i)));
            }
            updateFontSize();

        }
    }
    
    public void updateFontSize() {
        Font font = getFont();
        int maxFontSize = 16;
        for (IconAttributePanel attributePanel : attributePanels) {
            int curMaxFontSize = attributePanel.getMaxFittingFontSize(font);
            if (curMaxFontSize < maxFontSize) {
                maxFontSize = curMaxFontSize;
            }
        }
        
        if (maxFontSize > 14) {
            maxFontSize = 14;
        }
        font = font.deriveFont((float)maxFontSize);
        
        for (IconAttributePanel attributePanel : attributePanels) {
            attributePanel.setFont(font);
        }
        repaint();
	}

    
    @Override
    public Dimension getPreferredSize() {
        RPGView view = Jasbro.getInstance().getGui();
        if (view != null) {
            int heightGui = view.getHeight();        
            return new Dimension(heightGui/7, heightGui/3);
        }
        else {
            return super.getPreferredSize();
        }
    }

    @Override
    public void handleEvent(MyEvent e) {
        if (e.getType() == EventType.STATUSCHANGE) {
            setCharacter(character);
        }
    }
}
