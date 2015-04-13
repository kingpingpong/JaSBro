package jasbro.util.eventEditor.effectPanels;

import jasbro.game.world.customContent.ImageSelection;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventSimpleMessage;
import jasbro.texts.TextUtil;
import jasbro.util.eventEditor.EventEditor;
import jasbro.util.eventEditor.ImageSelectionPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EventEffectSimpleMessagePanel extends JPanel {
    private WorldEventSimpleMessage worldEventEffect;
    private JPanel imagesPanel;
    private WorldEvent worldEvent;
    
    public EventEffectSimpleMessagePanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
        setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.DEFAULT_COLSPEC,
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                RowSpec.decode("80dlu"),
                FormFactory.DEFAULT_ROWSPEC,
                RowSpec.decode("default:grow"),
                RowSpec.decode("default:grow"),}));
        this.worldEventEffect = (WorldEventSimpleMessage)worldEventEffectTmp;
        this.worldEvent = worldEventTmp;
        
        final JTextArea textArea = new JTextArea();
        final JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText(worldEventEffect.getMessage());
        textArea.setEditable(true);
        textArea.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                worldEventEffect.setMessage(textArea.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                worldEventEffect.setMessage(textArea.getText());
            }

            public void changedUpdate(DocumentEvent e) {
                worldEventEffect.setMessage(textArea.getText());
            }
        });
        add(scrollPane, "1, 1, 2, 1, fill, fill");
        
        final JCheckBox importanceCheckbox = new JCheckBox(TextUtil.t("eventEditor.importantMessage"));
        add(importanceCheckbox, "1, 2");
        importanceCheckbox.setToolTipText(TextUtil.t("eventEditor.importantMessage.title"));
        importanceCheckbox.setSelected(worldEventEffect.isImportantMessage());
        importanceCheckbox.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                worldEventEffect.setImportantMessage(importanceCheckbox.isSelected());
            }
        });
        
        
        JButton btnNewButton = new JButton(TextUtil.t("eventEditor.addImage"));
        add(btnNewButton, "2, 2");
        btnNewButton.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                worldEventEffect.getImages().add(new ImageSelection());
                updateImagesPanel();
            }
        });
        
        imagesPanel = new JPanel();
        add(imagesPanel, "1, 3, 2, 1, fill, fill");
        imagesPanel.setLayout(new BoxLayout(imagesPanel, BoxLayout.Y_AXIS));
        
        JLabel lblNewLabel = new JLabel(TextUtil.t("eventEditor.background"));
        add(lblNewLabel, "1, 4");
        
        ImageSelectionPanel imageSelectionPanel = new ImageSelectionPanel(worldEvent, 
                worldEventEffect.getBackground(), null);
        add(imageSelectionPanel, "2, 4, fill, fill");
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                scrollPane.getVerticalScrollBar().setValue(0);
            }
        });
        
        updateImagesPanel();
    }
    
    public void updateImagesPanel() {
        imagesPanel.removeAll();
        
        for (final ImageSelection imageSelection : worldEventEffect.getImages()) {
            ImageSelectionPanel selectionPanel = new ImageSelectionPanel(worldEvent, 
                    imageSelection, new ActionListener() {
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    worldEventEffect.getImages().remove(imageSelection);
                    updateImagesPanel();
                }
            });
            imagesPanel.add(selectionPanel);
        }
        
        EventEditor.getInstance().validate();
        EventEditor.getInstance().repaint();
    }
}
