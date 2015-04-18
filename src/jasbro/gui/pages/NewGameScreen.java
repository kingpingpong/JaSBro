/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jasbro.gui.pages;
import jasbro.Jasbro;
import jasbro.game.character.CharacterBase;
import jasbro.gui.character.CharacterStartDescriptionPanel;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 *
 * @author Azrael
 */
public class NewGameScreen extends MyImage {

    private MyImage trainerImage;
    private MyImage slaveImage;
    private ImageData background = new ImageData("images/backgrounds/sky.jpg");
    private CharacterBase selectedTrainer;
    private CharacterBase selectedSlave;
    private JPanel descriptionPanel1;
    private JPanel descriptionPanel2;
    
    public NewGameScreen() {
        setBackgroundImage(background);
        setLayout(new FormLayout(new ColumnSpec[] {
        		ColumnSpec.decode("1dlu:grow"),
        		ColumnSpec.decode("1dlu:grow(3)"),
        		ColumnSpec.decode("1dlu:grow(3)"),
        		ColumnSpec.decode("1dlu:grow"),},
        	new RowSpec[] {
        		RowSpec.decode("1dlu:grow(3)"),
        		RowSpec.decode("1dlu:grow"),}));
        
        JPanel panel_1 = new JPanel();
        panel_1.setOpaque(false);
        add(panel_1, "1, 1, 1, 2, fill, fill");
        panel_1.setLayout(new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("1dlu:grow"),},
            new RowSpec[] {
                RowSpec.decode("20dlu"),
                RowSpec.decode("1dlu:grow"),}));
        
        JLabel lblNewLabel_1 = new JLabel("Select your Trainer");
        lblNewLabel_1.setBackground(new Color(255,255,204));
        lblNewLabel_1.setOpaque(true);
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
        panel_1.add(lblNewLabel_1, "1, 1, fill, center");
        
        JScrollPane scrollPane_1 = new JScrollPane();
        panel_1.add(scrollPane_1, "1, 2, fill, fill");
        
        final JList<CharacterBase> trainerList = new JList<CharacterBase>();
        trainerList.setModel(new AbstractListModel<CharacterBase>() {
            @Override
            public int getSize() {
                return Jasbro.getInstance().getTrainerBases().size();
            }

            @Override
            public CharacterBase getElementAt(int index) {
                return Jasbro.getInstance().getTrainerBases().get(index);
            }
        });
        trainerList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
            	if (trainerList.getSelectedValue() != selectedTrainer) {
            		selectedTrainer = trainerList.getSelectedValue();
                    ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, selectedTrainer.getImages());
                    trainerImage.setImage(image);
                    remove(descriptionPanel1);
                    descriptionPanel1 = new CharacterStartDescriptionPanel(selectedTrainer);
                    add(descriptionPanel1, "2, 2, fill, fill");
                    validate();
                    NewGameScreen.this.repaint();
            	}
            }
        });
        scrollPane_1.setViewportView(trainerList);
        
        trainerImage = new MyImage();
        trainerImage.setMaximumSize(new Dimension(9999999, 99999999));
        add(trainerImage, "2, 1, fill, fill");
        
        slaveImage = new MyImage();
        slaveImage.setMaximumSize(new Dimension(999999999, 999999999));
        add(slaveImage, "3, 1, fill, fill");
        
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        add(panel, "4, 1, 1, 2, fill, fill");
        panel.setLayout(new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("1dlu:grow"),},
            new RowSpec[] {
                RowSpec.decode("20dlu"),
                RowSpec.decode("1dlu:grow"),
                RowSpec.decode("20dlu"),}));
        
        JLabel lblNewLabel = new JLabel("Select your first girl");
        lblNewLabel.setBackground(new Color(255,255,204));
        lblNewLabel.setOpaque(true);
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblNewLabel, "1, 1");
        
        JScrollPane scrollPane = new JScrollPane();
        panel.add(scrollPane, "1, 2, fill, fill");
        
        final JList<CharacterBase> slaveList = new JList<CharacterBase>();
        scrollPane.setViewportView(slaveList);
        slaveList.setModel(new AbstractListModel<CharacterBase>() {
            @Override
            public int getSize() {
                return Jasbro.getInstance().getSlaveBases().size();
            }

            @Override
            public CharacterBase getElementAt(int index) {
                return Jasbro.getInstance().getSlaveBases().get(index);
            }
        });
        
        slaveList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
            	if (slaveList.getSelectedValue() != selectedSlave) {
            		selectedSlave = slaveList.getSelectedValue();
                    ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, selectedSlave.getImages());
                    slaveImage.setImage(image);
                    remove(descriptionPanel2);
                    descriptionPanel2 = new CharacterStartDescriptionPanel(selectedSlave);
                    add(descriptionPanel2, "3, 2, fill, fill");
                    validate();
                    NewGameScreen.this.repaint();
            	}
            }
        });
        
        JButton startbutton = new JButton("Start");
        panel.add(startbutton, "1, 3, fill, center");
        startbutton.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                Jasbro.getInstance().startNewGame(trainerList.getSelectedValue(), slaveList.getSelectedValue());
            }
        });
        
        descriptionPanel1 = new TranslucentPanel();
        add(descriptionPanel1, "2, 2, fill, fill");
        
        descriptionPanel2 = new TranslucentPanel();
        add(descriptionPanel2, "3, 2, fill, fill");
        
        //Init pictures
        slaveList.setSelectedIndex(0);        
        trainerList.setSelectedIndex(0);
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, slaveList.getSelectedValue().getImages());
        slaveImage.setImage(image);
        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, trainerList.getSelectedValue().getImages());
        trainerImage.setImage(image);
    }
}
