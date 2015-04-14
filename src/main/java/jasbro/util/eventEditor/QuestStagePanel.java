package jasbro.util.eventEditor;

import jasbro.game.world.customContent.CustomQuestStage;
import jasbro.game.world.customContent.CustomQuestTemplate;
import jasbro.gui.DelegateMouseListener;
import jasbro.gui.GuiUtil;
import jasbro.texts.TextUtil;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.FlowLayout;

public class QuestStagePanel extends JPanel {
    private CustomQuestStage questStage;
    private CustomQuestTemplate customQuest;
    private JTextField titleTextField;
    private JTextArea descriptionTextArea;
    private JCheckBox showInQuestLogCheckBox;
    private boolean selected = false;
    private JPanel contentPanel;
    
    public QuestStagePanel(CustomQuestStage questStageTmp, CustomQuestTemplate customQuestTmp) {
        this.questStage = questStageTmp;
        this.customQuest = customQuestTmp;
        setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),
                FormFactory.RELATED_GAP_COLSPEC,},
            new RowSpec[] {
                FormFactory.RELATED_GAP_ROWSPEC,
                RowSpec.decode("default:grow"),
                FormFactory.RELATED_GAP_ROWSPEC,}));
        
        contentPanel = new JPanel();
        add(contentPanel, "2, 2, fill, fill");
        contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                RowSpec.decode("60dlu"),
                RowSpec.decode("default:grow"),}));
        
        showInQuestLogCheckBox = new JCheckBox(TextUtil.t("eventEditor.showInQuestLog"));
        contentPanel.add(showInQuestLogCheckBox, "1, 1, left, top");
        showInQuestLogCheckBox.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                questStage.setShowInQuestLog(showInQuestLogCheckBox.isSelected());
            }
        });
        
        JPanel panel = new JPanel();
        contentPanel.add(panel, "3, 1, fill, fill");
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        JLabel lblNewLabel = new JLabel(TextUtil.t("eventEditor.questStage") + ": " + 
                customQuest.getQuestStages().indexOf(questStage));
        panel.add(lblNewLabel);
        
        JLabel queststageTitleLabel = new JLabel(TextUtil.t("eventEditor.questStageTitle"));
        contentPanel.add(queststageTitleLabel, "1, 2, right, default");
        
        titleTextField = new JTextField();
        contentPanel.add(titleTextField, "3, 2, fill, default");
        titleTextField.setColumns(10);
        titleTextField.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                questStage.setTitle(titleTextField.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                questStage.setTitle(titleTextField.getText());
            }

            public void changedUpdate(DocumentEvent e) {
                questStage.setTitle(titleTextField.getText());
            }
        });        
        
        JLabel questStageDescription = new JLabel(TextUtil.t("eventEditor.questStageDescription"));
        contentPanel.add(questStageDescription, "1, 3, right, default");
        
        JScrollPane scrollPane = new JScrollPane();
        contentPanel.add(scrollPane, "3, 3, fill, fill");
        
        descriptionTextArea = new JTextArea();
        scrollPane.setViewportView(descriptionTextArea);
        descriptionTextArea.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                questStage.setDescription(descriptionTextArea.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                questStage.setDescription(descriptionTextArea.getText());
            }

            public void changedUpdate(DocumentEvent e) {
                questStage.setDescription(descriptionTextArea.getText());
            }
        });
        
        TriggerListPanel triggerListPanel = new TriggerListPanel(questStage);
        contentPanel.add(triggerListPanel, "1, 4, 3, 1, fill, fill");
        
        if (questStage != null) {
            showInQuestLogCheckBox.setSelected(questStage.isShowInQuestLog());
            titleTextField.setText(questStage.getTitle());
            descriptionTextArea.setText(questStage.getDescription());
            
        }
        
        setSelected(false);
        contentPanel.addMouseListener(GuiUtil.DELEGATEMOUSELISTENER);
        addMouseListener(new DelegateMouseListener() {
            @Override
            public void dispatch(MouseEvent e) {
                Container parent = e.getComponent().getParent();
                MouseEvent e2 = SwingUtilities.convertMouseEvent(e.getComponent(), e, e.getComponent().getParent());
                e2.setSource(QuestStagePanel.this);
                parent.dispatchEvent(e2);
            }
        });
    }
    
    public CustomQuestStage getQuestStage() {
        return questStage;
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            setBackground(Color.BLUE);
        }
        else {
            setBackground(Color.DARK_GRAY);
        }
        repaint();
    }
}
