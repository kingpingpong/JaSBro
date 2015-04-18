package jasbro.gui.objects.menus;

import jasbro.Jasbro;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Gender;
import jasbro.game.housing.House;
import jasbro.gui.CharacterFilterListModel.Filter;
import jasbro.texts.TextUtil;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class FilterMenu extends JPanel {
    private JTextField searchString;
    private JComboBox<House> houseSelectBox;
    private JComboBox<Gender> genderSelectBox;
    private JComboBox<CharacterType> typeSelectBox;
    
    
    public FilterMenu() {
        setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.UNRELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,}));
        Filter filter = Jasbro.getInstance().getGui().getFilteredModel().getFilter();
        
        searchString = new JTextField();
        add(searchString, "1, 1, 3, 1, fill, default");
        searchString.setColumns(10);
        searchString.setText(filter.getSearchString());
        
        JLabel lblNewLabel = new JLabel(TextUtil.t("ui.gender"));
        add(lblNewLabel, "1, 3, right, default");
        
        genderSelectBox = new JComboBox<Gender>();
        add(genderSelectBox, "3, 3, fill, default");
        genderSelectBox.addItem(null);
        for (Gender gender : Gender.values()) {
            genderSelectBox.addItem(gender);
        }
        genderSelectBox.setSelectedItem(filter.getGender());
        
        lblNewLabel = new JLabel(TextUtil.t("ui.type"));
        add(lblNewLabel, "1, 5, right, default");
        
        typeSelectBox = new JComboBox<CharacterType>();
        add(typeSelectBox, "3, 5, fill, default");
        typeSelectBox.addItem(null);
        for (CharacterType type : CharacterType.values()) {
            typeSelectBox.addItem(type);
        }
        typeSelectBox.setSelectedItem(filter.getType());
        
        lblNewLabel = new JLabel(TextUtil.t("ui.house"));
        add(lblNewLabel, "1, 7, right, default");
        
        houseSelectBox = new JComboBox<House>();
        add(houseSelectBox, "3, 7, fill, default");
        houseSelectBox.addItem(null);
        for (House house : Jasbro.getInstance().getData().getHouses()) {
            houseSelectBox.addItem(house);
        }
        houseSelectBox.setSelectedItem(filter.getHouse());
    }
    
    public Filter getFilter() {
        Filter filter = new Filter();
        filter.setSearchString(searchString.getText());
        filter.setHouse((House) houseSelectBox.getSelectedItem());
        filter.setGender((Gender) genderSelectBox.getSelectedItem());
        filter.setType((CharacterType) typeSelectBox.getSelectedItem());
        return filter;
    }

}
