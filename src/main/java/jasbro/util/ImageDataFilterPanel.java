package jasbro.util;

import jasbro.gui.ImageDataFilterListModel;
import jasbro.gui.ImageDataFilterListModel.Filter;
import jasbro.gui.objects.div.MyButton;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.texts.TextUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class ImageDataFilterPanel extends JPanel {
    private JTextField searchField;
    private ImageDataFilterListModel listModel;
    private JCheckBox chckbxShowOnlyUntagged;
    private JComboBox<ImageTag> comboBox;
    
    public ImageDataFilterPanel(ImageDataFilterListModel listModelTemp) {
        this.listModel = listModelTemp;
        setLayout(new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                FormFactory.RELATED_GAP_ROWSPEC,
                RowSpec.decode("default:grow"),
                RowSpec.decode("default:grow"),
                FormFactory.RELATED_GAP_ROWSPEC,}));
        
        JPanel topPanel = new JPanel();
        add(topPanel, "1, 2, fill, fill");
        topPanel.setLayout(new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("default:grow"),
                ColumnSpec.decode("15dlu"),},
            new RowSpec[] {
                RowSpec.decode("default:grow"),}));
        final String searchTerm = TextUtil.t("ui.search");
        
        MyButton resetFilterButton = new MyButton("", new ImageData("images/icons/x.png"), new ImageData("images/icons/x.png"));
        topPanel.add(resetFilterButton, "2, 1, fill, fill");
        resetFilterButton.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                chckbxShowOnlyUntagged.setSelected(false);
                comboBox.setSelectedItem(null);
                searchField.setText("");
                listModel.setFilter(new Filter());
                repaint();
            }
        });

        searchField = new JTextField();
        topPanel.add(searchField, "1, 1, fill, fill");
        
        searchField.addFocusListener (new FocusListener () {
            public void focusGained (FocusEvent e) {
                if (searchField.getText().equals (searchTerm)) searchField.setText ("");
            }

            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) searchField.setText (searchTerm);
            }
        });
        searchField.getDocument().addDocumentListener (new DocumentListener () {
            public void changedUpdate(DocumentEvent e) {
                if (!searchField.getText().equals(searchTerm)) {
                    listModel.getFilter().setSearchString(searchField.getText());
                    listModel.filter();
                }
            }

            public void insertUpdate(DocumentEvent e) {
                changedUpdate (e);
            }

            public void removeUpdate(DocumentEvent e) {
                changedUpdate (e);
            }
        });
        
        JPanel bottomPanel = new JPanel();
        add(bottomPanel, "1, 3, fill, fill");
        bottomPanel.setLayout(new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("default:grow"),
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                RowSpec.decode("default:grow"),}));
        
        chckbxShowOnlyUntagged = new JCheckBox("Show only untagged");
        bottomPanel.add(chckbxShowOnlyUntagged, "1, 1");
        chckbxShowOnlyUntagged.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {
                listModel.getFilter().setNoTagsOnly(chckbxShowOnlyUntagged.isSelected());;
                listModel.filter();
            }
        });
        
        comboBox = new JComboBox<ImageTag>();
        comboBox.addItem(null);
        for (ImageTag imageTag : ImageTag.values()) {
            if (imageTag.getImageTagGroup().isStandardGroup()) {
                comboBox.addItem(imageTag);
            }
        }
        bottomPanel.add(comboBox, "2, 1, fill, default");
        comboBox.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                listModel.getFilter().setImageTag((ImageTag) comboBox.getSelectedItem());
                listModel.filter();
            }
        });
        
    }

    public ImageDataFilterListModel getListModel() {
        return listModel;
    }

    public void setListModel(ImageDataFilterListModel listModel) {
        this.listModel = listModel;
    }

    
}
