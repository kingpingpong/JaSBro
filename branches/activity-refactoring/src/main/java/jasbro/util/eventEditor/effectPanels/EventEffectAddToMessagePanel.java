package jasbro.util.eventEditor.effectPanels;

import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.effects.WorldEventAddToMessage;
import jasbro.texts.TextUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
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

public class EventEffectAddToMessagePanel extends JPanel {
    private WorldEventAddToMessage worldEventEffect;

    public EventEffectAddToMessagePanel(WorldEventEffect worldEventEffectTmp, WorldEvent worldEventTmp) {
        setLayout(new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                FormFactory.DEFAULT_ROWSPEC,
                RowSpec.decode("80dlu"),}));
        this.worldEventEffect = (WorldEventAddToMessage)worldEventEffectTmp;
        
        final JCheckBox chckbxNewCheckBox = new JCheckBox(TextUtil.t("eventEditor.makePriorityMessage"));
        add(chckbxNewCheckBox, "1, 1");
        chckbxNewCheckBox.setSelected(worldEventEffect.isChangeToPriorityMessage());
        chckbxNewCheckBox.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                worldEventEffect.setChangeToPriorityMessage(chckbxNewCheckBox.isSelected());
            }
        });
        
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, "1, 2, fill, fill");
        
        final JTextArea textArea = new JTextArea();
        scrollPane.setViewportView(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText(worldEventEffect.getText());
        textArea.setEditable(true);
        textArea.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                worldEventEffect.setText(textArea.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                worldEventEffect.setText(textArea.getText());
            }

            public void changedUpdate(DocumentEvent e) {
                worldEventEffect.setText(textArea.getText());
            }
        });
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                scrollPane.getVerticalScrollBar().setValue(0);
            }
        });
    }
}
