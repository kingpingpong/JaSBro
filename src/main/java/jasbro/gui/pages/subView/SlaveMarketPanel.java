package jasbro.gui.pages.subView;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.Ownership;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.world.market.AuctionHouse;
import jasbro.game.world.market.SlaveMarket;
import jasbro.game.world.market.Bidder.UiBidder;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SlaveMarketPanel extends JPanel {
	private JPanel hirePanel;
	private MyImage slaveImage;
	private Charakter selectedSlave = null;
	private JTextArea hintArea;
	
	public SlaveMarketPanel() {
		
		setOpaque(false);
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow(20)"),
				ColumnSpec.decode("default:grow"),
				ColumnSpec.decode("default:grow(18)"),
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("default:grow"),
				RowSpec.decode("default:grow(16)"),
				RowSpec.decode("default:grow(3)"),}));
		
		TranslucentPanel translucentPanel = new TranslucentPanel();
		translucentPanel.setPreferredSize(new Dimension(0, 0));
		add(translucentPanel, "2, 2, fill, fill");
		
		translucentPanel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("default:grow"), }, new RowSpec[] { RowSpec.decode("fill:20dlu"), RowSpec.decode("fill:pref:grow"), }));
		
		JLabel lblBuyGirl = new JLabel(TextUtil.t("ui.buyslave.title"));
		lblBuyGirl.setFont(new Font("Tahoma", Font.BOLD, 18));
		translucentPanel.add(lblBuyGirl, "1, 1");
		
		hirePanel = new JPanel();
		hirePanel.setOpaque(false);
		translucentPanel.add(hirePanel, "1, 2, fill, fill");
		
		slaveImage = new MyImage();
		add(slaveImage, "4, 1, 1, 2");
		
		hintArea = GuiUtil.getDefaultTextarea();				
		hintArea.setText(TextUtil.t("ui.buyslave.introduction"));
		hintArea.setFont(GuiUtil.DEFAULTBOLDFONT);
		
		TranslucentPanel controlPanel = new TranslucentPanel();
		add(controlPanel, "4, 3, fill, fill");
		controlPanel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("default:grow"), }, new RowSpec[] { RowSpec.decode("default:grow"), FormFactory.DEFAULT_ROWSPEC, }));
		controlPanel.add(hintArea, "1, 1, fill, fill");
		
		JButton btnHire = new JButton("Buy");
		btnHire.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object arguments[] = {selectedSlave.getName()};
				int price = 500 + (int) selectedSlave.calculateValue();
				if(price<500){price=500;}
				if(Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.DISCOUNTSLAVES))
					price/=2;
				if (selectedSlave != null && Jasbro.getInstance().getData().canAfford(price)) {
					selectedSlave.setOwnership(Ownership.OWNED);
					Jasbro.getInstance().getData().getCharacters().add(selectedSlave);
					Jasbro.getInstance().getData().spendMoney(price, TextUtil.t("slavemarket.bought", arguments));
					Jasbro.getInstance().getData().getSlaveMarket().getSlaves().remove(selectedSlave);
					new MessageScreen(TextUtil.t("ui.buyslave.bought", selectedSlave), ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, selectedSlave), selectedSlave.getBackground());
					Jasbro.getInstance().getData().getEventManager().notifyAll(new MyEvent(EventType.CHARACTERGAINED, selectedSlave));
					selectedSlave = null;
					slaveImage.setImage(null);
					initHireList();
				}
				else{
					new MessageScreen(TextUtil.t("ui.buyslave.cantafford", selectedSlave), ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, selectedSlave), selectedSlave.getBackground());
				}
			}
		});
		btnHire.setFont(GuiUtil.DEFAULTBOLDFONT);
		controlPanel.add(btnHire, "1, 2, center, bottom");
		
		initHireList();
	}
	
	public void initHireList() {
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
					slaveImage.setImage(ImageUtil.getInstance().getImageDataByTags(imageTags, selectedSlave.getImages()));
					if(selectedSlave != null) {
						String name = selectedSlave.getName();
						int price = 500 + (int) selectedSlave.calculateValue();
						if(price<500){price=500;}
						if(Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.DISCOUNTSLAVES))
							price/=2;
						hintArea.setText(TextUtil.t("ui.buyslave.costText", name, price));
					}
					repaint();
				}
			}
		};
		
		hirePanel.removeAll();
		SlaveMarket slaveMarket = Jasbro.getInstance().getData().getSlaveMarket();
		for (Charakter character : slaveMarket.getSlaves()) {
			CharacterShortView shortView = new CharacterShortView(character, false);
			hirePanel.add(shortView);
			shortView.addMouseListener(ml);
		}
	}
	
}