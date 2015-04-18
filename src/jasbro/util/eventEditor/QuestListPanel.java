package jasbro.util.eventEditor;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.world.customContent.CustomQuestTemplate;
import jasbro.texts.TextUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class QuestListPanel extends JPanel {
    private JList<CustomQuestTemplate> questList;
    private JTextField textField;
    
    public QuestListPanel(final QuestPanel questPanel) {
        setLayout(new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                FormFactory.DEFAULT_ROWSPEC,
                RowSpec.decode("default:grow"),}));
        
        JPanel newItemPanel = new JPanel();
        add(newItemPanel, "1, 1, fill, fill");
        newItemPanel.setLayout(new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("default:grow"),
                FormFactory.DEFAULT_COLSPEC,},
            new RowSpec[] {
                FormFactory.DEFAULT_ROWSPEC,}));
        
        textField = new JTextField();
        newItemPanel.add(textField, "1, 1, fill, default");
        textField.setColumns(10);
        
        JButton btnCreateNewItem = new JButton(TextUtil.t("eventEditor.createQuest"));
        btnCreateNewItem.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                String questId = textField.getText().trim();
                if (questId != null && !questId.equals("") && !Jasbro.getInstance().getCustomQuestTemplates().containsKey(questId) &&
                       Util.isValidFileName(questId)) {
                    CustomQuestTemplate quest = new CustomQuestTemplate(questId);
                    Jasbro.getInstance().getCustomQuestTemplates().put(questId, quest);
                    questPanel.setQuest(quest);
                    QuestListPanel.this.update();
                }
            }
        });
        newItemPanel.add(btnCreateNewItem, "2, 1");
        
        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, "1, 2, fill, fill");
        
        
        questList = new JList<CustomQuestTemplate>();
        scrollPane.setViewportView(questList);
        questList.addListSelectionListener(new ListSelectionListener() {         
            @Override
            public void valueChanged(ListSelectionEvent e) {
                questPanel.setQuest(questList.getSelectedValue());
            }
        });
        update();
    }


    public void update() {
        CustomQuestTemplate itemArray[] = new CustomQuestTemplate[Jasbro.getInstance().getCustomQuestTemplates().entrySet().size()];

        itemArray = Jasbro.getInstance().getCustomQuestTemplates().values().toArray(itemArray);
        Arrays.sort(itemArray, new Comparator<CustomQuestTemplate>() {
            @Override
            public int compare(CustomQuestTemplate o1, CustomQuestTemplate o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                }
                else if (o1 != null) {
                    return o1.getId().compareTo(o2.getId());
                }
                else {
                    return o2.getId().compareTo(null);
                }
            }
        });
        questList.setListData(itemArray);
        validate();
        repaint();
    }
}
