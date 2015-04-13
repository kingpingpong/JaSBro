package jasbro.gui.character;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.gui.objects.div.MyButton;
import jasbro.gui.pages.CharacterScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.util.ConfigHandler;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class CharacterScreenCenterPanel extends JPanel {
    private MyButton leftButton;
    private MyButton rightButton;
    
    public CharacterScreenCenterPanel(final Charakter character, final CharacterScreen parent) {
        setLayout(new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("default:grow"),
                ColumnSpec.decode("30dlu:none"),
                ColumnSpec.decode("default:grow(30)"),
                ColumnSpec.decode("30dlu:none"),
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                RowSpec.decode("default:grow(15)"),
                RowSpec.decode("30dlu:none"),
                RowSpec.decode("default:grow"),}));
        setPreferredSize(new Dimension(9999, 9999));
        setOpaque(false);
        
        final List<Charakter> characters = Jasbro.getInstance().getData().getCharacters();
        if (characters.contains(character)) {
            if (characters.indexOf(character) > 0) {
                leftButton = new MyButton("", new ImageData("images/icons/arrow_left.png"), 
                        new ImageData("images/icons/arrow_left.png"));
                
                if (!ConfigHandler.isHideArrowKeys()) {
                    add(leftButton, "2, 2, fill, fill");
                }
                else {
                    add(leftButton, "2, 2, left, center");
                }
                
                leftButton.addActionListener(new ActionListener() {                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Jasbro.getInstance().getGui().showCharacterView(characters.get(characters.indexOf(character) - 1));
                        Jasbro.getInstance().getGui().removeLayer(parent);
                    }
                });
                leftButton.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "pressed");
                leftButton.getActionMap().put("pressed", new AbstractAction() {                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Jasbro.getInstance().getGui().showCharacterView(characters.get(characters.indexOf(character) - 1));
                        Jasbro.getInstance().getGui().removeLayer(parent);
                    }
                });
            }

            if (characters.indexOf(character) < characters.size() - 1) {
                rightButton = new MyButton("", new ImageData("images/icons/arrow_right.png"), 
                        new ImageData("images/icons/arrow_right.png"));
                
                
                if (!ConfigHandler.isHideArrowKeys()) {
                    add(rightButton, "4, 2, fill, fill");
                }
                else {
                    add(rightButton, "4, 2, left, center");
                }
                rightButton.addActionListener(new ActionListener() {                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Jasbro.getInstance().getGui().showCharacterView(characters.get(characters.indexOf(character) + 1));
                        Jasbro.getInstance().getGui().removeLayer(parent);
                    }
                });
                rightButton.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "pressed");
                rightButton.getActionMap().put("pressed", new AbstractAction() {                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Jasbro.getInstance().getGui().showCharacterView(characters.get(characters.indexOf(character) + 1));
                        Jasbro.getInstance().getGui().removeLayer(parent);
                    }
                });
            }
        }       
    }
}
