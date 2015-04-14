package jasbro.gui.pages.subView;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.world.market.AuctionHouse;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class AuctionHousePanel extends JPanel {
    private MyImage girlImage;
    private AuctionHouse auctionHouse;
    private Color backgroundColor = new Color(1.0f, 1.0f, 0.9f, 0.8f);
    private Charakter selectedSlave = null;
    private JPanel buyList;
    private JButton startAuctionButton;

    public AuctionHousePanel() {
        setOpaque(false);
        auctionHouse = Jasbro.getInstance().getData().getAuctionHouse();
        FormLayout formLayout = new FormLayout(new ColumnSpec[] {
                ColumnSpec.decode("1dlu:grow"),
                ColumnSpec.decode("1dlu:grow"),},
            new RowSpec[] {
                RowSpec.decode("default:grow"),
                RowSpec.decode("min:grow(10)"),
                RowSpec.decode("default:grow"),
                RowSpec.decode("30dlu"),});
        formLayout.setHonorsVisibility(false);
        setLayout(formLayout);

        UIManager.put("TabbedPane.contentOpaque", false);
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(tabbedPane, "1, 2, fill, fill");
        tabbedPane.setOpaque(false);

        JPanel buyPanel = new TranslucentPanel();
        tabbedPane.addTab("Buy girl", null, buyPanel, null);
        buyPanel.setBackground(backgroundColor);
        buyPanel.setBorder(new LineBorder(new Color(139, 69, 19), 1, false));
        buyPanel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("10px:grow"), }, new RowSpec[] { RowSpec.decode("fill:20dlu"), RowSpec.decode("fill:pref:grow"), }));

        JLabel lblBuyGirl = new JLabel("Buy slave");
        lblBuyGirl.setFont(new Font("Tahoma", Font.BOLD, 18));
        buyPanel.add(lblBuyGirl, "1, 1");

        buyList = new JPanel();
        buyList.setOpaque(false);
        buyPanel.add(buyList, "1, 2, fill, fill");

        JPanel sellPanel = new TranslucentPanel();
        tabbedPane.addTab("Sell slave", null, sellPanel, null);
        sellPanel.setBackground(backgroundColor);
        sellPanel.setBorder(new LineBorder(new Color(139, 69, 19), 1, false));
        sellPanel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("10px:grow"), }, new RowSpec[] { RowSpec.decode("fill:20dlu"), RowSpec.decode("fill:20dlu"), RowSpec.decode("fill:pref:grow"), }));

        JLabel lblSellGirl = new JLabel("Sell girl");
        lblSellGirl.setFont(new Font("Tahoma", Font.BOLD, 18));
        sellPanel.add(lblSellGirl, "1, 1, left, center");

        final JComboBox<Charakter> characterSelect = new JComboBox<Charakter>();
        characterSelect.addItem(null);
        for (Charakter character : Jasbro.getInstance().getData().getSlavesForSale()) {
            characterSelect.addItem(character);
        }
        sellPanel.add(characterSelect, "1, 2, left, center");

        final JPanel sellDataPanel = new JPanel();
        sellDataPanel.setOpaque(false);
        sellPanel.add(sellDataPanel, "1, 3, fill, fill");
        sellDataPanel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("center:pref:grow"), }, new RowSpec[] { RowSpec.decode("pref:grow"), }));

        characterSelect.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Charakter slave = (Charakter) characterSelect.getSelectedItem();
                    selectedSlave = slave;
                    sellDataPanel.removeAll();
                    sellDataPanel.add(new CharacterShortView(slave), "1,1");
                    List<ImageTag> imageTags = selectedSlave.getBaseTags();
                    imageTags.add(0, ImageTag.NAKED);
                    imageTags.add(1, ImageTag.CLEANED);
                    girlImage.setImage(ImageUtil.getInstance().getImageDataByTags(imageTags, selectedSlave.getImages()));
                    sellDataPanel.validate();
                    startAuctionButton.setEnabled(true);
                    repaint();
                }
            }
        });

        startAuctionButton = new JButton("Start Auction");
        add(startAuctionButton, "1, 4, right, default");
        startAuctionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedSlave != null) {
                    Charakter character = selectedSlave;
                    auctionHouse.getSlaves().remove(selectedSlave);
                    girlImage.setImage(null);
                    sellDataPanel.removeAll();
                    characterSelect.removeItem(selectedSlave);
                    selectedSlave = null;
                    Jasbro.getInstance().getGui().startAuction(auctionHouse, character, false);
                    initBuyList();
                }
            }
        });
        initBuyList();
        validate();

        girlImage = new MyImage();
        add(girlImage, "2, 1, 1, 4");
    }

    public void initBuyList() {
        MouseListener ml = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                mouseAction(e);
            }

            public void mouseAction(MouseEvent e) {
                if (!e.isConsumed()) {
                    e.consume();
                    CharacterShortView shortView = (CharacterShortView) e.getSource();
                    selectedSlave = (Charakter) shortView.getCharacter();
                    List<ImageTag> imageTags = selectedSlave.getBaseTags();
                    imageTags.add(0, ImageTag.NAKED);
                    imageTags.add(1, ImageTag.CLEANED);
                    girlImage.setImage(ImageUtil.getInstance().getImageDataByTags(imageTags, selectedSlave.getImages()));
                    
                    if (Jasbro.getInstance().getData().calculateControl().getDiff() - selectedSlave.getObedience() < 0) {
                        startAuctionButton.setEnabled(false);
                    }
                    else {
                        startAuctionButton.setEnabled(true);
                    }
                    repaint();
                }
            }
        };

        buyList.removeAll();
        for (Charakter character : auctionHouse.getSlaves()) {
            CharacterShortView shortView = new CharacterShortView(character, false);
            buyList.add(shortView);
            shortView.addMouseListener(ml);
        }
    }

}
