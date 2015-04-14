package jasbro.gui.objects.div;

import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.events.EventManager;
import jasbro.gui.DelegateMouseListener;
import jasbro.texts.TextUtil;

import java.awt.GridLayout;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class CharacterConditionsPanel extends JPanel {
    @SuppressWarnings("unused")
    private final static Logger log = Logger.getLogger(EventManager.class);

    private Charakter character;
    private MyImage workView;
    private JPanel conditionIcons;

    /**
     * Create the panel.
     */
    public CharacterConditionsPanel() {
        DelegateMouseListener listener = new DelegateMouseListener();
        addMouseMotionListener(listener);
        addMouseListener(listener);
        setLayout(new FormLayout(new ColumnSpec[] {
        		ColumnSpec.decode("1dlu:grow"),},
        	new RowSpec[] {
        		RowSpec.decode("1dlu:grow"),
        		RowSpec.decode("1dlu:grow"),}));
        
        setOpaque(false);
        
        workView = new MyImage();
        workView.addMouseMotionListener(listener);
        workView.addMouseListener(listener);
        add(workView, "1, 1, fill, fill");
        
        conditionIcons = new JPanel();
        add(conditionIcons, "1, 2, fill, fill");
        conditionIcons.setOpaque(false);
        
    }

    public Charakter getCharacter() {
        return character;
    }

    public void setCharacter(Charakter character) {
        this.character = character;        
        init();
    }
    
    public void init() {
    	conditionIcons.removeAll();
    	
        if (character.getConditions().size() > 0) {
            List<Condition> conditions = character.getConditions();
            if (conditions.size() < 3) {
            	conditionIcons.setLayout(new GridLayout(1, 3, 5, 5));
            }
            else if (conditions.size() < 4) {
            	conditionIcons.setLayout(new GridLayout(1, 3, 2, 2));
            }
            else if (conditions.size() < 9) {
            	conditionIcons.setLayout(new GridLayout(2, 4, 1, 1));
            }
            else {
            	conditionIcons.setLayout(new GridLayout(0, 5, 1, 1));
            }
            do {
            	try {
		            conditionIcons.removeAll();
		            for (Condition condition : character.getConditions()) {
		            	MyImage myImage = new MyImage(condition.getIcon());
		            	conditionIcons.add(myImage);
		            	myImage.setToolTipText(TextUtil.htmlPreformatted(condition.getDescription()));
		            }
		            break;
            	}
            	catch (ConcurrentModificationException e) {
            		try {
						Thread.sleep(20);
					} catch (InterruptedException e1) {
					}
            	}
            }
            while(true);
        }
        update();
    }
    
    
    public void update() {
        if (character != null) {
            workView.setImage(character.getWarnImage());
            workView.setToolTipText(TextUtil.htmlPreformatted(character.getWarnString()));
        }
    }

}
