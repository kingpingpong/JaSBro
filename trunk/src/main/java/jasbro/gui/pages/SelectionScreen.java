package jasbro.gui.pages;

import jasbro.Jasbro;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.MessageInterface;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SelectionScreen<T> extends JPanel implements MessageInterface {
	public SelectionScreen() {
	}
	private SelectionData<T> selectedOption;

	public SelectionData<T> select(List<SelectionData<T>> choices,
			ImageData image1, ImageData image2, ImageData background, String description) {
		setBackground(new Color(240, 230, 140));
		setOpaque(false);
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("1dlu:grow"),
				ColumnSpec.decode("1dlu:grow(6)"),
				ColumnSpec.decode("1dlu:grow"), }, new RowSpec[] {
				RowSpec.decode("1dlu:grow(3)"), RowSpec.decode("1dlu:grow"), }));

		JPanel imageArea = new JPanel();
		imageArea.setOpaque(false);
		add(imageArea, "1, 1, 3, 1, fill, fill");
		imageArea.setLayout(new GridLayout(1, 0, 0, 0));

		if (image2 == null) {
			MyImage imageLabel = new MyImage(image1);
			MyImage backgroundLabel = new MyImage(background);
			backgroundLabel.setBounds(0, 0, imageArea.getWidth(), imageArea.getHeight());
			imageArea.add(backgroundLabel, 0);
			backgroundLabel.setOpaque(true);
			backgroundLabel.setResize(true);

			backgroundLabel.setLayout(new GridLayout(1, 0, 0, 0));
			backgroundLabel.add(imageLabel);
		} else {
			MyImage imageAreaBackground = new MyImage();
			imageAreaBackground.addMouseListener(GuiUtil.DELEGATEMOUSELISTENER);
			imageAreaBackground.setBackgroundImage(background);
			imageArea.add(imageAreaBackground, 0);
			imageAreaBackground.setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("1dlu:grow(4)"),
					ColumnSpec.decode("1dlu:grow"),
					ColumnSpec.decode("1dlu:grow(4)"),
					FormFactory.RELATED_GAP_COLSPEC,},
				new RowSpec[] {
					RowSpec.decode("1dlu:grow"),}));

			MyImage myImage = new MyImage();
			myImage.setImage(image1);
			imageAreaBackground.add(myImage, "2, 1, fill, fill");
			
			myImage = new MyImage();
			myImage.setImage(image2);
			imageAreaBackground.add(myImage, "4, 1, fill, fill");
		}

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(240, 230, 140));
        scrollPane.getViewport().setOpaque(false);
		add(scrollPane, "2, 2, fill, fill");

		JPanel buttonPanel = new JPanel();
		buttonPanel.setFont(new Font("Tahoma", Font.PLAIN, 25));
		buttonPanel.setOpaque(false);
		FormLayout layout = new FormLayout(
				new ColumnSpec[] {
						ColumnSpec.decode("1dlu:grow"), },
				new RowSpec[] {
						RowSpec.decode("default:none"),
						RowSpec.decode("fill:default:grow"), });
		buttonPanel.setLayout(layout);
		scrollPane.setViewportView(buttonPanel);
		
		JTextArea descriptionArea = GuiUtil.getDefaultTextarea();
		descriptionArea.setText(description);
		descriptionArea.setOpaque(false);
		descriptionArea.setFont(GuiUtil.DEFAULTBOLDFONT);
		buttonPanel.add(descriptionArea, "1, 1, fill, top");

		for (int i = 0; i < choices.size(); i++) {
			final SelectionData<T> choice = choices.get(i);
			JButton choiceButton = new JButton(choice.getButtonText());
			choiceButton.setToolTipText(choice.getTooltipText());
			choiceButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selectedOption = choice;
					AccessController.doPrivileged(new PrivilegedAction<Void>() {

                        @Override
                        public Void run() {
                            Jasbro.getThreadpool().execute(new Runnable() {
                                public void run() {
                                    try {
                                        Thread.sleep(500); //Sleep shortly to give next message time to load
                                    } catch (InterruptedException e1) {
                                    }
                                    Jasbro.getInstance().getGui().removeLayer(SelectionScreen.this);
                                };
                            });
                            return null;
                        }
					    
					});

				}
			});
			
			if (!choice.isEnabled()) {
				choiceButton.setEnabled(false);
			}
			layout.insertRow(i + 2, RowSpec.decode("default:none"));
			buttonPanel.add(choiceButton, "1," + (i + 2) + ", fill, top");
		}

		Jasbro.getInstance().getGui().addMessage(this);
		
		do {
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
			}
		}
		while (selectedOption == null);

		return selectedOption;
	}
	
	@Override
	public boolean isPriorityMessage() {
		return true;
	}
	
	@Override
	public void init() {
	}

	@Override
	public void setMessageGroupObject(Object charcterGroupObject) {
	}

	@Override
	public Object getMessageGroupObject() {
		return null;
	}
}
