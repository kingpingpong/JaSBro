package jasbro.util.eventEditor;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.world.customContent.WorldEvent;
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

public class EventListPanel extends JPanel {
    private JList<WorldEvent> eventList;
    private JTextField textField;
    
    public EventListPanel(final EventPanel eventPanel) {
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
        
        JButton btnCreateNewItem = new JButton(TextUtil.t("eventEditor.createEvent"));
        btnCreateNewItem.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                String eventId = textField.getText().trim();
                if (eventId != null && !eventId.equals("") && !Jasbro.getInstance().getWorldEvents().containsKey(eventId) &&
                        Util.isValidFileName(eventId)) {
                    WorldEvent event = new WorldEvent(eventId);
                    Jasbro.getInstance().getWorldEvents().put(eventId, event);
                    eventPanel.setEvent(event);
                    EventListPanel.this.update();
                }
            }
        });
        newItemPanel.add(btnCreateNewItem, "2, 1");
        
        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, "1, 2, fill, fill");
        
        
        eventList = new JList<WorldEvent>();
        scrollPane.setViewportView(eventList);
        eventList.addListSelectionListener(new ListSelectionListener() {         
            @Override
            public void valueChanged(ListSelectionEvent e) {
                eventPanel.setEvent(eventList.getSelectedValue());
            }
        });
        update();
    }


    public void update() {
        WorldEvent itemArray[] = new WorldEvent[Jasbro.getInstance().getWorldEvents().entrySet().size()];

        itemArray = Jasbro.getInstance().getWorldEvents().values().toArray(itemArray);
        Arrays.sort(itemArray, new Comparator<WorldEvent>() {
            @Override
            public int compare(WorldEvent o1, WorldEvent o2) {
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
        eventList.setListData(itemArray);
        validate();
        repaint();
    }
}
