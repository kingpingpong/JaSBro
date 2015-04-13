/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jasbro.gui.pages.subView;

import jasbro.game.housing.House;
import jasbro.game.interfaces.AreaInterface;
import jasbro.game.world.CharacterLocation;
import jasbro.gui.objects.div.MyImage;

import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * 
 * @author Azrael
 */
public class AreaPanel extends MyImage {
    private AreaInterface area;
    private JPanel locationPanel;

    /**
     * Creates new form HouseScreen
     */
    public AreaPanel() {
        setLayout(new GridLayout(0, 1, 5, 5));
        
        locationPanel = new JPanel();
        locationPanel.setOpaque(false);
        add(locationPanel);
    }
    
    private void init() {
        locationPanel.removeAll();
        setBackgroundImage(area.getImage());
        if (area != null) {        	
            if (area.getLocationAmount() < 7) {
                locationPanel.setLayout(new GridLayout(2, 3));
            }
            else if (area.getLocationAmount() < 9) {
                locationPanel.setLayout(new GridLayout(3, 3));
            }
            else {
                locationPanel.setLayout(new GridLayout(0, 4));
            }
            
        	GridLayout layout = (GridLayout) locationPanel.getLayout();
            if (!(area instanceof House)) {
            	layout.setHgap(0);
            	layout.setVgap(0);
            	setBorder(null);
            }
            else {
                setBorder(new EmptyBorder(10, 10, 10, 10));
            	layout.setHgap(10);
            	layout.setVgap(10);
            }
            
            
            for (CharacterLocation location : area.getLocations()) {
            	locationPanel.add(new LocationPanel(location));
            } 
            if (area.getLocationAmount() < 3) {
                locationPanel.add(Box.createGlue());
                locationPanel.add(Box.createGlue());
            }
        }
    }
    
    public AreaInterface getArea() {
        return area;
    }

    public void setArea(AreaInterface area) {
        this.area = area;
        init();
    }
}
