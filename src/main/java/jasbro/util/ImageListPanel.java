package jasbro.util;

import jasbro.game.interfaces.HasImagesInterface;
import jasbro.gui.ImageDataFilterListModel;
import jasbro.gui.pictures.ImageData;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class ImageListPanel extends JPanel {
	private JList<ImageData> imageList;
	private ImageDataFilterListModel imageDataFilterListModel;
	private EditorInterface editor;
	
	public ImageListPanel(EditorInterface editorTmp) {
		this.editor = editorTmp;
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
				new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		imageDataFilterListModel = new ImageDataFilterListModel();  
		JPanel filterPanel = new ImageDataFilterPanel(imageDataFilterListModel);
		add(filterPanel, "1, 1");
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "1, 2, fill, fill");
		
		
		imageList = new JList<ImageData>(imageDataFilterListModel);
		scrollPane.setViewportView(imageList);
		
		imageList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		imageList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
				imageListValueChanged(evt);
			}
		});
		imageDataFilterListModel.addListDataListener(new ListDataListener() {
			@Override
			public void intervalRemoved(ListDataEvent e) {
			}
			
			@Override
			public void intervalAdded(ListDataEvent e) {
			}
			
			@Override
			public void contentsChanged(ListDataEvent e) {
				if (imageDataFilterListModel.getSize() == 0) {
					editor.setNoImageSelected(true);
					imageList.setSelectedValue(null, true);
				}
				else {
					editor.setNoImageSelected(false);
					imageList.setSelectedIndex(0);
					editor.changeCurrentImage(imageList.getSelectedValue());
				}
			}
		});
		
		imageList.setCellRenderer(new ListCellRenderer<ImageData>() {
			private BasicComboBoxRenderer renderer = new BasicComboBoxRenderer();
			@Override
			public Component getListCellRendererComponent(JList<? extends ImageData> list, ImageData value, int index,
					boolean isSelected, boolean cellHasFocus) {
				BasicComboBoxRenderer component = (BasicComboBoxRenderer)renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				component.setText(value.getFilename());
				return component;
			}
		});
	}
	
	public void addListSelectionListener(ListSelectionListener listSelectionListener) {
		imageList.addListSelectionListener(listSelectionListener);
	}
	
	private void imageListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_imageListValueChanged
		editor.changeCurrentImage(imageList.getSelectedValue());
	}
	
	public JList<ImageData> getImageList() {
		return imageList;
	}
	
	public ImageData getSelectedImage() {
		return imageList.getSelectedValue();
	}
	
	public void filter() {
		imageDataFilterListModel.filter();
	}
	
	public void setImageObject(HasImagesInterface imageObject) {
		imageDataFilterListModel.setBase(imageObject);
		imageList.setSelectedIndex(0);
	}
}