package jasbro.util.eventEditor;

import jasbro.game.world.customContent.CustomQuestStage;
import jasbro.game.world.customContent.CustomQuestTemplate;
import jasbro.game.world.customContent.EventAndQuestFileLoader;
import jasbro.texts.TextUtil;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class QuestEditorPanel extends JPanel {
    private CustomQuestTemplate customQuestTemplate;
    private QuestStagePanel selectedQuestStagePanel;
    private JPanel questStageMasterPanel;
    
    private JButton saveButton;
    
    
    public QuestEditorPanel(CustomQuestTemplate customQuestTemplateTmp) {
        this.customQuestTemplate = customQuestTemplateTmp;
        setLayout(new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("default:grow"),
                ColumnSpec.decode("90dlu"),},
            new RowSpec[] {
                RowSpec.decode("default:grow"),}));
        
        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, "1, 1, fill, fill");
        
        questStageMasterPanel = new JPanel();
        scrollPane.setViewportView(questStageMasterPanel);
        questStageMasterPanel.setLayout(new BoxLayout(questStageMasterPanel, BoxLayout.Y_AXIS));
        
        JPanel panel = new JPanel();
        add(panel, "2, 1, fill, fill");
        panel.setLayout(new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.UNRELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                RowSpec.decode("default:grow"),
                FormFactory.DEFAULT_ROWSPEC,}));
        
        saveButton = new JButton(TextUtil.t("eventEditor.save"));
        panel.add(saveButton, "1, 1");
        saveButton.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                EventAndQuestFileLoader.getInstance().save(customQuestTemplate);
            }
        });
        
        JButton btnAddQuestStage = new JButton(TextUtil.t("eventEditor.addQuestStage"));
        panel.add(btnAddQuestStage, "1, 3");
        btnAddQuestStage.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                CustomQuestStage newQuestStage = new CustomQuestStage();
                QuestEditorPanel.this.customQuestTemplate.getQuestStages().add(newQuestStage);
                addQuestStagePanel(newQuestStage);
                validate();
                repaint();
            }
        });
        
        JButton btnDeleteQuestStage = new JButton(TextUtil.t("eventEditor.deleteQuestStage"));
        btnDeleteQuestStage.setForeground(Color.RED);
        panel.add(btnDeleteQuestStage, "1, 5, right, default");
        btnDeleteQuestStage.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedQuestStagePanel != null) {                              
                    questStageMasterPanel.remove(selectedQuestStagePanel);
                    customQuestTemplate.getQuestStages().remove(selectedQuestStagePanel.getQuestStage());
                    selectedQuestStagePanel = null;
                    validate();
                    repaint();
                }
            }
        });
        
        for (CustomQuestStage customQuestStage : this.customQuestTemplate.getQuestStages()) {
            addQuestStagePanel(customQuestStage);
        }
        
        questStageMasterPanel.addMouseListener(new MyMouseListener());
    }
    
    public void addQuestStagePanel(CustomQuestStage customQuestStage) {
        QuestStagePanel questStagePanel = new QuestStagePanel(customQuestStage, customQuestTemplate);
        questStageMasterPanel.add(questStagePanel);
    }
    
    public void setSelected(QuestStagePanel questStagePanel) {
        if (selectedQuestStagePanel != null) {                              
            selectedQuestStagePanel.setSelected(false);
        }
        selectedQuestStagePanel = questStagePanel;
        questStagePanel.setSelected(true);

        validate();
        repaint();
    }
    
    private class MyMouseListener extends MouseAdapter {
        public void mouseClicked(java.awt.event.MouseEvent e) {
            if (e.getSource() instanceof QuestStagePanel) {
                QuestStagePanel newPanel = (QuestStagePanel) e.getSource();
                setSelected(newPanel);
            }
        };
    }
}
