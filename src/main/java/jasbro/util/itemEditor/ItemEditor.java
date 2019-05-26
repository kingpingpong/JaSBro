package jasbro.util.itemEditor;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import jasbro.game.items.Item;

public class ItemEditor extends JFrame {
	private final static Logger log = LogManager.getLogger(ItemEditor.class);
	
	private JPanel contentPane;
	private ItemEditorPanel itemEditorPanel;
	private ItemListPanel itemList;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ItemEditor frame = new ItemEditor();
					frame.setVisible(true);
				} catch (Exception e) {
					log.error("Error on creating frame", e);
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public ItemEditor() {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
				log.error("Uncaught Exception", e);
			}
		});
		ToolTipManager.sharedInstance().setDismissDelay(10000);
		ToolTipManager.sharedInstance().setInitialDelay(100);
		
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow(12)"),},
			new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		
		itemList = new ItemListPanel(this);
		contentPane.add(itemList, "1, 1, fill, fill");
	}
	
	public void setItem(Item item) {
		if (itemEditorPanel != null) {
			contentPane.remove(itemEditorPanel);
		}
		if (item != null) {
			itemEditorPanel = new ItemEditorPanel(item, this);
			contentPane.add(itemEditorPanel, "3, 1, fill, fill");
		}
		else {
			itemList.updateList();
		}
		contentPane.validate();
		contentPane.repaint();
	}
	
}