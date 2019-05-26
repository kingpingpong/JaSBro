package jasbro.gui.town;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.world.market.AuctionHouse;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.gui.pages.subView.CharacterShortView;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;

public class AuctionHouseMenu extends MyImage {
	private static final Map<Trait, String> TRAIT_OPTION_KEYS = new EnumMap<>(Trait.class);
	static {
		for (Trait t : Trait.values()) {
			TRAIT_OPTION_KEYS.put(t, String.format("auction.trait.%s.", t.toString().toLowerCase()));
		}
	}

	private MyImage girlImage;
	private MyImage auctioneerImage;
	private AuctionHouse auctionHouse;
	private Color backgroundColor = new Color(1.0f, 1.0f, 0.9f, 0.8f);
	private Charakter selectedSlave = null;
	private JPanel buyList;
	private JButton startAuctionButton;
	private JTextArea hintArea;
	private String behavior2;
	private String slaveJob;
	private String slaveJobText;
	private String furryText;
	private String proficiency;

	public AuctionHouseMenu() {
		setOpaque(false);
		setBackgroundImage(new ImageData("images/backgrounds/auctionHouse.jpg"));

		double width = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
		double height = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
		int widthRat = (int) (width/1280);
		int heightRat = (int) (height/720);
		int iconSize = (int) (65*width/1280);
		int backHomeBtnWidth = (int) (150*width/1280);
		int backHomeBtnHeight = (int) (50*width/1280);

		ImageIcon homeIcon1 = new ImageIcon("images/buttons/home.png");
		Image homeImage1 = homeIcon1.getImage().getScaledInstance( backHomeBtnWidth, backHomeBtnHeight,  java.awt.Image.SCALE_SMOOTH ) ;  
		homeIcon1 = new ImageIcon(homeImage1);

		ImageIcon homeIcon2 = new ImageIcon("images/buttons/home hover.png");
		Image homeImage2 = homeIcon2.getImage().getScaledInstance( backHomeBtnWidth, backHomeBtnHeight,  java.awt.Image.SCALE_SMOOTH ) ;  
		homeIcon2 = new ImageIcon(homeImage2);

		JButton homeButton = new JButton(homeIcon1);
		homeButton.setRolloverIcon(homeIcon2);
		homeButton.setPressedIcon(homeIcon1);
		homeButton.setBounds((int) (15*width/1280),(int) (550*height/720), backHomeBtnWidth, backHomeBtnHeight);
		homeButton.setBorderPainted(false); 
		homeButton.setContentAreaFilled(false); 
		homeButton.setFocusPainted(false); 
		homeButton.setOpaque(false);
		add(homeButton);
		homeButton.addActionListener(new  ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Jasbro.getInstance().getGui().showHouseManagementScreen();
			}
		});

		ImageIcon backIcon1 = new ImageIcon("images/buttons/back.png");
		Image backImage1 = backIcon1.getImage().getScaledInstance( backHomeBtnWidth, backHomeBtnHeight,  java.awt.Image.SCALE_SMOOTH ) ;  
		backIcon1 = new ImageIcon(backImage1);

		ImageIcon backIcon2 = new ImageIcon("images/buttons/back hover.png");
		Image backImage2 = backIcon2.getImage().getScaledInstance( backHomeBtnWidth, backHomeBtnHeight,  java.awt.Image.SCALE_SMOOTH ) ;  
		backIcon2 = new ImageIcon(backImage2);

		JButton backButton = new JButton(backIcon1);
		backButton.setRolloverIcon(backIcon2);
		backButton.setPressedIcon(backIcon1);
		backButton.setBounds((int) (15*width/1280),(int) (620*height/720), backHomeBtnWidth, backHomeBtnHeight);
		backButton.setBorderPainted(false); 
		backButton.setContentAreaFilled(false); 
		backButton.setFocusPainted(false); 
		backButton.setOpaque(false);
		add(backButton);
		backButton.addActionListener(new  ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Jasbro.getInstance().getGui().showSlaveMarketScreen();
			}
		});

		auctionHouse = Jasbro.getInstance().getData().getAuctionHouse();
		FormLayout formLayout = new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("1dlu:grow"),
				ColumnSpec.decode("1dlu:grow"),},
				new RowSpec[] {
						RowSpec.decode("default:grow"),
						RowSpec.decode("min:grow(20)"),
						RowSpec.decode("min:grow(40)"),
						RowSpec.decode("default:grow"),
						RowSpec.decode("30dlu"),});
		formLayout.setHonorsVisibility(false);
		setLayout(formLayout);

		auctioneerImage = new MyImage();
		if(Jasbro.getInstance().getData().getDay()%2 == 0){
			auctioneerImage.setImage(new ImageData("images/people/Runa and Rune 1.png"));			
		}
		else{
			auctioneerImage.setImage(new ImageData("images/people/Runa and Rune 2.png"));	
		}
		add(auctioneerImage, "1, 1, 1, 5, fill, fill");

		UIManager.put("TabbedPane.contentOpaque", false);
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(tabbedPane, "2, 2, fill, center");
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

		hintArea = GuiUtil.getDefaultTextarea();	
		hintArea.setText(TextUtil.t("ui.buyslave.introduction"));
		hintArea.setFont(GuiUtil.DEFAULTBOLDFONT);

		TranslucentPanel controlPanel = new TranslucentPanel();
		controlPanel.setBackground(backgroundColor);
		add(controlPanel, "2, 3, fill, fill");
		controlPanel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("default:grow"), }, new RowSpec[] { RowSpec.decode("default:grow"), FormFactory.DEFAULT_ROWSPEC, }));
		controlPanel.add(hintArea, "1, 1, fill, fill");

		JPanel sellPanel = new TranslucentPanel();
		tabbedPane.addTab("Sell slave", null, sellPanel, null);
		sellPanel.setBackground(backgroundColor);
		sellPanel.setBorder(new LineBorder(new Color(139, 69, 19), 1, false));
		sellPanel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("10px:grow"), }, new RowSpec[] { RowSpec.decode("fill:20dlu"), RowSpec.decode("fill:20dlu"), RowSpec.decode("fill:pref:grow"), }));

		JLabel lblSellGirl = new JLabel("Sell slave");
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
					sellDataPanel.validate();
					startAuctionButton.setEnabled(true);
					repaint();
				}
			}
		});

		startAuctionButton = new JButton("Start Auction");
		add(startAuctionButton, "2, 4, center, top");
		startAuctionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedSlave != null) {
					Charakter character = selectedSlave;
					auctionHouse.getSlaves().remove(selectedSlave);
					sellDataPanel.removeAll();
					characterSelect.removeItem(selectedSlave);
					selectedSlave = null;
					Jasbro.getInstance().getGui().startAuction(auctionHouse, character, false);
					initBuyList();
				}
			}
		});

		addMouseListener (new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					Jasbro.getInstance().getGui().showSlaveMarketScreen();
				}
			}		
		});

		initBuyList();
		validate();
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
					getAuctionText();
					startAuctionButton.setEnabled(true);

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

	public void getAuctionText() {
		String behavior1;

		String auctioneer1	= "Rune";	
		String auctioneer2 = "Runa";

		if(Jasbro.getInstance().getData().getDay()%2 == 0){
			behavior1 = "lewd";
			behavior2 = "mean";
		}
		else{
			behavior1 = "mean";
			behavior2 = "lewd";
		}

		String introductionText;

		getFurryText();
		getSpecializationText();

		if(selectedSlave.getSpecializations().contains(SpecializationType.FURRY)){
			introductionText = TextUtil.t(slaveJobText + "\n" +  furryText + "\n\n" +auctioneer1 + ": " + "auction.introduction." + behavior1, selectedSlave);
		}
		else{
			introductionText = TextUtil.t(slaveJobText + "\n\n" + auctioneer1 + ": " + "auction.introduction." + behavior1, selectedSlave);
		}



		int a = selectedSlave.getTraits().size();
		if(a > 3){
			a = 3;
		}

		String[] traitText = new String[3];
		List<String> traitOption = new ArrayList<String>();
		getTrait(traitOption);			


		Vector<String> vector = new Vector<String>();
		int check = 3;
		while (true) {		
			if (traitOption.size() < 3) {
				check = traitOption.size();		
			}
			if (traitOption.size() == 0) {
				break;

			}
			int choice = Util.getInt(0, traitOption.size());
			String temp = traitOption.get(choice);
			if (!vector.contains(temp)) {
				vector.add(temp);
			}
			if (vector.size() == check) break;
		}

		if(traitOption.size() != 0){
			for (int i = 0; i < vector.size(); i++) {
				String choice;
				if(i!=1){
					choice = behavior2;
				}
				else{
					choice = behavior1;
				}
				traitText[i] = TextUtil.t(vector.get(i) + choice, selectedSlave);
			}			
			if(vector.size() == 1){
				hintArea.setText(introductionText + "\n" + auctioneer2 + ": " + traitText[0]);
			}
			if(vector.size() == 2){
				hintArea.setText(introductionText + "\n" + auctioneer2 + ": " + traitText[0] + "\n" + auctioneer1 + ": " + traitText[1]);
			}
			if(vector.size() == 3){
				hintArea.setText(introductionText + "\n" + auctioneer2 + ": " + traitText[0] + "\n" + auctioneer1 + ": " + traitText[1] + "\n" + auctioneer2 + ": " + traitText[2] + "\n");
			}
		}
		else{
			hintArea.setText(introductionText + "\n" + auctioneer2 + ": " + TextUtil.t("auction.trait.notrait." + behavior2, selectedSlave));
		}
	}		

	void getFurryText() {
		/*if(selectedSlave.getSpecializations().contains(SpecializationType.FURRY)){
			if(selectedSlave.getTraits().contains(Trait.MORPHAQUATIC)){
				furryText = TextUtil.t("auction.furry.aquatic", selectedSlave);
			}
			else if(selectedSlave.getTraits().contains(Trait.AVIAN)){
				furryText = TextUtil.t("auction.furry.avian", selectedSlave);
			}
			else if(selectedSlave.getTraits().contains(Trait.CANINE)){
				furryText = TextUtil.t("auction.furry.canine", selectedSlave);
			}
			else if(selectedSlave.getTraits().contains(Trait.FELINE)){
				furryText = TextUtil.t("auction.furry.feline", selectedSlave);
			}
			else if(selectedSlave.getTraits().contains(Trait.INSECT)){
				furryText = TextUtil.t("auction.furry.insect", selectedSlave);
			}
			else if(selectedSlave.getTraits().contains(Trait.LAGOMORPH)){
				furryText = TextUtil.t("auction.furry.lagomorph", selectedSlave);
			}
			else if(selectedSlave.getTraits().contains(Trait.REPTILIAN)){
				furryText = TextUtil.t("auction.furry.reptilian", selectedSlave);
			}
			else if(selectedSlave.getTraits().contains(Trait.VULPINE)){
				furryText = TextUtil.t("auction.furry.vulpine", selectedSlave);
			}
			else{
				furryText = TextUtil.t("auction.furry.none", selectedSlave);
			}
		} */
	}

	void getSpecializationText() {		
		int skill = 0;		
		slaveJob = selectedSlave.getSlaveJob();
		if(slaveJob == "ClassyWhore"){
			getProficiency(selectedSlave.getFinalValue(SpecializationAttribute.SEDUCTION)+selectedSlave.getFinalValue(BaseAttributeTypes.CHARISMA),30, 60);
			slaveJobText = TextUtil.t("auction.slavejob.classywhore." + proficiency, selectedSlave);
		}
		else if(slaveJob == "CumBucket"){
			getProficiency(selectedSlave.getFinalValue(SpecializationAttribute.SEDUCTION)+selectedSlave.getFinalValue(BaseAttributeTypes.STAMINA),30, 60);
			slaveJobText = TextUtil.t("auction.slavejob.cumbucket." + proficiency, selectedSlave);
		}
		else if(slaveJob == "ServesDrunkards"){
			getProficiency(selectedSlave.getFinalValue(SpecializationAttribute.BARTENDING)+selectedSlave.getFinalValue(BaseAttributeTypes.INTELLIGENCE),30, 60);
			slaveJobText = TextUtil.t("auction.slavejob.servesdrunkards." + proficiency, selectedSlave);
		}
		else if(slaveJob == "ClassyBartender"){
			getProficiency(selectedSlave.getFinalValue(SpecializationAttribute.BARTENDING)+selectedSlave.getFinalValue(BaseAttributeTypes.CHARISMA),30, 60);
			slaveJobText = TextUtil.t("auction.slavejob.classybartender." + proficiency, selectedSlave);
		}
		else if(slaveJob == "GivesExtras"){
			getProficiency((int) (selectedSlave.getFinalValue(SpecializationAttribute.STRIP)+Util.getAverage(SpecializationType.SEX, selectedSlave)),30, 60);
			slaveJobText = TextUtil.t("auction.slavejob.givesextras." + proficiency, selectedSlave);
		}
		else if(slaveJob == "Performer"){
			getProficiency(selectedSlave.getFinalValue(SpecializationAttribute.STRIP)+selectedSlave.getFinalValue(BaseAttributeTypes.CHARISMA),30, 60);
			slaveJobText = TextUtil.t("auction.slavejob.performer." + proficiency, selectedSlave);
		}
		else if(slaveJob == "Brawler"){
			getProficiency(selectedSlave.getFinalValue(SpecializationAttribute.VETERAN)+selectedSlave.getFinalValue(BaseAttributeTypes.STRENGTH),30, 60);
			slaveJobText = TextUtil.t("auction.slavejob.brawler." + proficiency, selectedSlave);
		}
		else if(slaveJob == "Mage"){
			getProficiency(selectedSlave.getFinalValue(SpecializationAttribute.VETERAN)+selectedSlave.getFinalValue(BaseAttributeTypes.INTELLIGENCE),30, 60);
			slaveJobText = TextUtil.t("auction.slavejob.mage." + proficiency, selectedSlave);
		}
		else if(slaveJob == "BookWorm"){
			getProficiency(selectedSlave.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE)+selectedSlave.getFinalValue(BaseAttributeTypes.INTELLIGENCE),30, 60);
			slaveJobText = TextUtil.t("auction.slavejob.bookworm." + proficiency, selectedSlave);
		}
		else if(slaveJob == "DoesWhatShesAskedTo"){
			getProficiency(selectedSlave.getFinalValue(SpecializationAttribute.MAGIC)+selectedSlave.getFinalValue(BaseAttributeTypes.OBEDIENCE),30, 60);
			slaveJobText = TextUtil.t("auction.slavejob.doeswhatshesaskedto." + proficiency, selectedSlave);
		}
		else if(slaveJob == "FuckedByMonsters"){
			getProficiency(selectedSlave.getFinalValue(Sextype.MONSTER)+selectedSlave.getFinalValue(BaseAttributeTypes.STRENGTH),30, 60);
			slaveJobText = TextUtil.t("auction.slavejob.fuckedbymonsters." + proficiency, selectedSlave);
		}
		else if(slaveJob == "FuckedByGroup"){
			getProficiency(selectedSlave.getFinalValue(Sextype.GROUP)+selectedSlave.getFinalValue(BaseAttributeTypes.STAMINA),30, 60);
			slaveJobText = TextUtil.t("auction.slavejob.fuckedbygroup." + proficiency, selectedSlave);
		}
		else if(slaveJob == "Cosette"){
			getProficiency((int) (Util.getAverage(SpecializationType.MAID, selectedSlave)+selectedSlave.getFinalValue(BaseAttributeTypes.OBEDIENCE)),30, 60);
			slaveJobText = TextUtil.t("auction.slavejob.cosette." + proficiency, selectedSlave);
		}
		else if(slaveJob == "HeadMaid"){
			getProficiency((int) (Util.getAverage(SpecializationType.MAID, selectedSlave)+selectedSlave.getFinalValue(BaseAttributeTypes.CHARISMA)),30, 60);
			slaveJobText = TextUtil.t("auction.slavejob.headmaid." + proficiency, selectedSlave);
		}
		else{
			getProficiency(selectedSlave.getFinalValue(BaseAttributeTypes.OBEDIENCE),20, 40);
			slaveJobText = TextUtil.t("auction.slavejob.none." + proficiency, selectedSlave);
		}
	}

	void getProficiency(Integer skill, int a, int b){
		if(skill<=a){
			proficiency="low";
		}
		else if(skill>b){
			proficiency="high";
		}	
		else {
			proficiency="mid";
		}
	}


	void getTrait(List<String> traitOption) {
		for (Trait t : selectedSlave.getTraits()) {
			traitOption.add(TRAIT_OPTION_KEYS.get(t));
		}
	}
}	