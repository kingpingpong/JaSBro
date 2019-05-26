package jasbro.util.enemyEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import jasbro.game.world.customContent.npc.ComplexEnemyTemplate;
import jasbro.game.world.customContent.npc.NpcFileLoader;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
import jasbro.util.EditorInterface;
import jasbro.util.ImageListPanel;
import jasbro.util.ImageTagEditor;

public class EnemyEditorPanel extends JPanel implements EditorInterface {
	private final static Logger log = LogManager.getLogger(EnemyEditorPanel.class);
	
	private ComplexEnemyTemplate enemyTemplate;
	private MyImage imageDisplay;
	private ImageTagEditor imageTagEditor;
	private boolean on = false;
	private ImageListPanel imageListPanel;
	private JButton saveButton;
	private EnemyDataEditorPanel enemyDataEditorPanel;
	
	public EnemyEditorPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("180dlu:none"),
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(splitPane, "1, 1, fill, fill");
		
		EnemyListPanel enemyListPanel = new EnemyListPanel(this);
		splitPane.setLeftComponent(enemyListPanel);
		
		imageListPanel = new ImageListPanel(this);
		splitPane.setRightComponent(imageListPanel);
		splitPane.setDividerLocation(300);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(splitPane_1, "2, 1, fill, fill");
		
		imageDisplay = new MyImage();
		splitPane_1.setLeftComponent(imageDisplay);
		imageDisplay.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		
		saveButton = new JButton("Save");
		imageDisplay.add(saveButton, "1, 1, right, bottom");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (enemyTemplate != null) {
					NpcFileLoader.getInstance().save(enemyTemplate);
				}
			}
		});
		
		JSplitPane splitPane_2 = new JSplitPane();
		splitPane_1.setRightComponent(splitPane_2);
		
		imageTagEditor = new ImageTagEditor();
		splitPane_2.setLeftComponent(imageTagEditor);
		
		enemyDataEditorPanel = new EnemyDataEditorPanel();
		splitPane_2.setRightComponent(enemyDataEditorPanel);
		
		splitPane_2.setDividerLocation(650);
		splitPane_1.setDividerLocation(450);
	}
	
	public ComplexEnemyTemplate getEnemyTemplate() {
		return enemyTemplate;
	}
	
	public void setEnemyTemplate(ComplexEnemyTemplate enemyTemplate) {
		this.enemyTemplate = enemyTemplate;
		
		enemyDataEditorPanel.setEnemyTemplate(enemyTemplate);
		
		imageListPanel.setImageObject(enemyTemplate);
		if (this.enemyTemplate.getImages().size() > 0) {
			changeCurrentImage(enemyTemplate.getImages().get(0));
		}
		else {
			setNoImageSelected(true);
		}
	}
	
	@Override
	public void setNoImageSelected(boolean selected) {
		imageTagEditor.setEnabled(! on);
		if (on) {
			imageDisplay.setImage(null);
		}
		repaint();
	}
	
	@Override
	public void changeCurrentImage(ImageData selectedValue) {
		try {
			imageTagEditor.setImage(imageListPanel.getSelectedImage(), null);
			imageDisplay.setImage(imageListPanel.getSelectedImage());
			repaint();
		}catch (Exception e) {
			log.error("Error on changing image", e);
		}
	}
	
	
}