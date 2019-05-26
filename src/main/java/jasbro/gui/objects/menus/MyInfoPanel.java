package jasbro.gui.objects.menus;

import jasbro.Jasbro;
import jasbro.game.GameData;
import jasbro.game.character.ControlData;
import jasbro.gui.MyPanel;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;

import javax.swing.JLabel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class MyInfoPanel extends MyPanel {
	private JLabel controlLabel;
	private JLabel moneyLabel;
	private MyImage controlIcon;
	private JLabel dayLabel;
	
	
	public MyInfoPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("1dlu:grow"),
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("10dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("10dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
				new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		
		controlLabel = new JLabel();
		add(controlLabel, "2, 1");
		controlLabel.setToolTipText(TextUtil.htmlPreformatted(TextUtil.t("CONTROL.description")));
		
		controlIcon = new MyImage(new ImageData("images/icons/control.png"));
		add(controlIcon, "3, 1, fill, fill");
		
		moneyLabel = new JLabel();
		add(moneyLabel, "5, 1");
		
		MyImage myImage = new MyImage(new ImageData("images/icons/cent.png"));
		add(myImage, "6, 1, fill, fill");
		
		dayLabel = new JLabel();
		add(dayLabel, "8, 1");
		
		update();
	}
	
	@Override
	public void update() {
		GameData data = Jasbro.getInstance().getData();
		
		if (data != null) {
			if (!data.getEventManager().isShiftInProgress()) {
				ControlData controlData = data.calculateControl();
				String controlText = TextUtil.t("ui.control", new Object[]{controlData.getControlUsed(), 
						controlData.getControlGenerated()});
				if (!controlLabel.getText().equals(controlText)) {
					controlLabel.setText(controlText);
				}
				
				String dayText = TextUtil.t("ui.day", new Object[]{data.getDay()});
				if (!dayLabel.getText().equals(dayText)) {
					dayLabel.setText(dayText);
				}
			}
			
			String moneyText = TextUtil.t("ui.gold", new Object[]{data.getMoney()});
			if (!moneyLabel.equals(moneyText)) {
				moneyLabel.setText(moneyText);
			}
			
			if (!isVisible()) {
				setVisible(true);
				getParent().validate();
			}
		}
		else {
			setVisible(false);
		}
		
		repaint();
	}
	
}