package jasbro.gui.pages.subView;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.Ownership;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.world.market.AuctionHouse;
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

public class TrainerBarPanel extends JPanel {
	private JPanel hirePanel;
	private MyImage trainerImage;
	private Charakter selectedTrainer = null;
	
	public TrainerBarPanel() {
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
		
		JLabel lblBuyGirl = new JLabel(TextUtil.t("ui.hiretrainer.title"));
		lblBuyGirl.setFont(new Font("Tahoma", Font.BOLD, 18));
		translucentPanel.add(lblBuyGirl, "1, 1");
		
		hirePanel = new JPanel();
		hirePanel.setOpaque(false);
		translucentPanel.add(hirePanel, "1, 2, fill, fill");
		
		trainerImage = new MyImage();
		add(trainerImage, "4, 1, 1, 2");
		
		JTextArea hintArea = GuiUtil.getDefaultTextarea();
		hintArea.setText(TextUtil.t("ui.hiretrainer.hint"));
		hintArea.setFont(GuiUtil.DEFAULTBOLDFONT);
		
		TranslucentPanel controlPanel = new TranslucentPanel();
		add(controlPanel, "4, 3, fill, fill");
		controlPanel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("default:grow"), }, new RowSpec[] { RowSpec.decode("default:grow"), FormFactory.DEFAULT_ROWSPEC, }));
		controlPanel.add(hintArea, "1, 1, fill, fill");
		
		JButton btnHire = new JButton("Hire");
		btnHire.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedTrainer != null) {
					selectedTrainer.setOwnership(Ownership.CONTRACT);
					Jasbro.getInstance().getData().getCharacters().add(selectedTrainer);
					Jasbro.getInstance().getData().getAuctionHouse().getTrainers().remove(selectedTrainer);
					new MessageScreen(TextUtil.t("ui.hiretrainer.hired", selectedTrainer), ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, selectedTrainer), selectedTrainer.getBackground());
					Jasbro.getInstance().getData().getEventManager().notifyAll(new MyEvent(EventType.CHARACTERGAINED, selectedTrainer));
					selectedTrainer = null;
					trainerImage.setImage(null);
					initHireList();
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
					selectedTrainer = (Charakter) shortView.getCharacter();
					List<ImageTag> imageTags = selectedTrainer.getBaseTags();
					imageTags.add(0, ImageTag.STANDARD);
					imageTags.add(1, ImageTag.CLEANED);
					trainerImage.setImage(ImageUtil.getInstance().getImageDataByTags(imageTags, selectedTrainer.getImages()));
					repaint();
				}
			}
		};
		
		hirePanel.removeAll();
		AuctionHouse auctionHouse = Jasbro.getInstance().getData().getAuctionHouse();
		for (Charakter character : auctionHouse.getTrainers()) {
			CharacterShortView shortView = new CharacterShortView(character, false);
			hirePanel.add(shortView);
			shortView.addMouseListener(ml);
		}
	}
	
}