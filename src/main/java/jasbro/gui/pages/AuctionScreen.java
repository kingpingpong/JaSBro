package jasbro.gui.pages;

import jasbro.Jasbro;
import jasbro.game.world.market.Auction;
import jasbro.game.world.market.Auction.AuctionGui;
import jasbro.game.world.market.Bidder.UiBidder;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class AuctionScreen extends JPanel implements AuctionGui {
	private Auction auction;
	private JTextArea messageField;
	private UiBidder playerBidder;
	private JScrollPane messageScrollPane;
	private JButton leaveEarlyButton;
	
	public AuctionScreen(Auction auction) {
		setBackground(Color.WHITE);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		this.auction = auction;
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("1dlu:grow(2)"),
				ColumnSpec.decode("1dlu:grow"),},
				new RowSpec[] {
				RowSpec.decode("1dlu:grow"),
				RowSpec.decode("fill:30dlu"),}));
			
		MyImage girlImage = new MyImage();
		add(girlImage, "2, 1, fill, fill");
		girlImage.setOpaque(false);
		List<ImageTag> imageTags = auction.getSlave().getBaseTags();
		imageTags.add(0, ImageTag.NAKED);
		imageTags.add(1, ImageTag.CLEANED);
		girlImage.setImage(ImageUtil.getInstance().getImageDataByTags(imageTags, auction.getSlave().getImages()));
		girlImage.setBackgroundImage(new ImageData("images/backgrounds/auction.jpg"));
		
		messageScrollPane = new JScrollPane();
		add(messageScrollPane, "3, 1, fill, fill");
		
		messageField = new JTextArea();
		messageField.setLineWrap(true);
		messageField.setWrapStyleWord(true);
		messageField.setEditable(false);
		messageField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		messageScrollPane.setViewportView(messageField);
		
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					playerBidder.bid(AuctionScreen.this.auction.getMaxBid() + Integer.parseInt(e.getActionCommand()));
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		
		leaveEarlyButton = new JButton(TextUtil.t("ui.auction.leaveEarly"));
		leaveEarlyButton.setEnabled(false);
		leaveEarlyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AuctionScreen.this.auction.setAbort(true);
			}
		});
		add(leaveEarlyButton, "2, 2, left, center");
		
		JPanel bidPanel = new JPanel();
		add(bidPanel, "3, 2, fill, fill");
		bidPanel.setBorder(GuiUtil.DEFAULTBORDER);
		bidPanel.setBackground(GuiUtil.DEFAULTTRANSPARENTCOLOR);
		bidPanel.setAutoscrolls(true);
		bidPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("3dlu:grow"),
				ColumnSpec.decode("15dlu:grow"),
				ColumnSpec.decode("15dlu:grow"),},
				new RowSpec[] {
				RowSpec.decode("10dlu:grow"),
				RowSpec.decode("10dlu:grow"),}));
		
		JButton btnBidMore = new JButton("Bid 1 more");
		bidPanel.add(btnBidMore, "1, 1");
		btnBidMore.setActionCommand("1");
		btnBidMore.addActionListener(al);
		
		btnBidMore = new JButton("Bid 10 more");
		bidPanel.add(btnBidMore, "2, 1");
		btnBidMore.setActionCommand("10");
		btnBidMore.addActionListener(al);
		
		btnBidMore = new JButton("Bid 100 more");
		bidPanel.add(btnBidMore, "3, 1");
		btnBidMore.setActionCommand("100");
		btnBidMore.addActionListener(al);
		
		btnBidMore = new JButton("Bid 1000 more");
		bidPanel.add(btnBidMore, "1, 2");
		btnBidMore.setActionCommand("1000");
		btnBidMore.addActionListener(al);
		
		btnBidMore = new JButton("Bid 10000 more");
		bidPanel.add(btnBidMore, "2, 2");
		btnBidMore.setActionCommand("10000");
		btnBidMore.addActionListener(al);
		
		btnBidMore = new JButton("Bid 100000 more");
		bidPanel.add(btnBidMore, "3, 2");
		btnBidMore.setActionCommand("100000");
		btnBidMore.addActionListener(al);
		
		playerBidder = new UiBidder();
		playerBidder.setAuction(auction);
		
		validate();
	}
	
	@Override
	public void update() {
		messageField.setText(auction.getMessages());
		if (auction.isOwnSlave() || auction.getMaxBidder() instanceof UiBidder) {
			leaveEarlyButton.setEnabled(false);
		}
		else {
			leaveEarlyButton.setEnabled(true);
		}
		
		JScrollBar vertical = messageScrollPane.getVerticalScrollBar();
		vertical.setValue( vertical.getMaximum() );
		repaint();
	}
	
	@Override
	public void close() {
		Jasbro.getInstance().getGui().removeLayer(this);
	} 
	
	@Override
	public void setVisible(boolean visibility) {
		if (visibility == false) {
			auction.setAbort(true);
		}
		super.setVisible(visibility);
	}
}