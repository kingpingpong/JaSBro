/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jasbro.gui.pages;

import jasbro.Jasbro;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.MessageData;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.MessageInterface;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import org.apache.log4j.Logger;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 *
 * @author Azrael
 */
public class MessageScreen extends JPanel implements MessageInterface {
    @SuppressWarnings("unused")
    private final static Logger log = Logger.getLogger(MessageScreen.class);
    
    private MyImage imageAreaBackground;
    private String text;
    private List<ImageData> images;
    private ImageData background;
    private JLabel attributeModificationArea;
    private boolean priorityMessage;
    private Object messageGroupObject;
    private JPanel buttonPanel;
    private MessageData messageData;
    private boolean initialized = false;
    
    //Constructor for Gui designer
    @SuppressWarnings("unused")
	private MessageScreen() {
    	init();
    }    
    
    public MessageScreen(String text, ImageData image, ImageData background) {
        getImages().add(image);
    	initVariables(text, background, false);
        Jasbro.getInstance().getGui().addMessage(this);
    }
    	
    public MessageScreen(String text, ImageData image, ImageData background, boolean priorityMessage) {
    	this(text, image, background);
    	this.priorityMessage = priorityMessage;
	}
    
    public MessageScreen(MessageData messageData) {
        this.messageData = messageData;
        Jasbro.getInstance().getGui().addMessage(this);
        messageGroupObject = messageData.getMessageGroupObject();
        this.priorityMessage = messageData.isPriorityMessage();
    }
    
    public void initVariables(String text, ImageData background, boolean priorityMessage) {
        this.text = text;
        this.background = background;
        this.priorityMessage = priorityMessage;
    }

	public synchronized void init() {
	    if (!initialized) {
	        initialized = true;
	        if (messageData != null) {
	            initVariables(messageData.getMessage(), messageData.getBackground(), 
	                    messageData.isPriorityMessage());
	            images = messageData.getImages();
	            messageGroupObject = messageData.getMessageGroupObject();
	        }
	        
	        setBackground(new Color(240, 230, 140));
	        setOpaque(false);
	        setLayout(new FormLayout(new ColumnSpec[] {
	                ColumnSpec.decode("1dlu:grow"),
	                ColumnSpec.decode("1dlu:grow(6)"),
	                ColumnSpec.decode("1dlu:grow"), }, 
	                new RowSpec[] {
	                RowSpec.decode("1dlu:grow(3)"),
	                RowSpec.decode("1dlu:grow"), }));
	        
	        imageAreaBackground = new MyImage();
	        imageAreaBackground.setBackgroundImage(background);
	        imageAreaBackground.setFocusable(true);
	        add(imageAreaBackground, "1, 1, 3, 1, fill, fill");
	        FormLayout layout;
	        if (images.size() == 1) {
	            layout = new FormLayout(new ColumnSpec[] {
	                    ColumnSpec.decode("1dlu:grow"),
	                    ColumnSpec.decode("1dlu:grow(6)"),
	                    ColumnSpec.decode("1dlu:grow"), }, new RowSpec[] {
	                    RowSpec.decode("1dlu:grow"), });
	            imageAreaBackground.setLayout(layout);
	        }
	        else {
	            layout = new FormLayout(new ColumnSpec[] {
	                    FormFactory.RELATED_GAP_COLSPEC,
	                    ColumnSpec.decode("1dlu:grow(4)"),
	                    FormFactory.RELATED_GAP_COLSPEC,},
	                new RowSpec[] {
	                    RowSpec.decode("1dlu:grow"),});
	            imageAreaBackground.setLayout(layout);
	        }
	        
	        int i = 1;
	        for (ImageData image : getImages()) {
	            if (i > 1) {
	                layout.insertColumn(i*2 - 1, ColumnSpec.decode("1dlu:grow"));
	                layout.insertColumn(i*2, ColumnSpec.decode("1dlu:grow(4)"));
	            }
	            MyImage myImage = new MyImage();
	            myImage.setImage(image);            myImage.setFocusable(true);
	            imageAreaBackground.add(myImage, (2 * i) + ", 1, fill, fill");
	            i++;
	        }   

	        addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                Jasbro.getInstance().getGui().removeLayer(MessageScreen.this);
	            }
	            
	            @Override
	            public void mousePressed(MouseEvent e) {
                    Jasbro.getInstance().getGui().removeLayer(MessageScreen.this);
	            }
	            
	            @Override
	            public void mouseEntered(MouseEvent e) {
	                requestFocus();
	            }
	        });

	        final JScrollPane scrollPane = new JScrollPane(
	                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	        scrollPane.setBackground(new Color(240, 230, 140));
	        scrollPane.getViewport().setOpaque(false);
	        scrollPane.addMouseListener(GuiUtil.DELEGATEMOUSELISTENER);
	        scrollPane.getViewport().addMouseListener(GuiUtil.DELEGATEMOUSELISTENER);
	        add(scrollPane, "2, 2, fill, fill");

	        JTextPane messageField = new JTextPane();
	        messageField.setEditable(false);
	        scrollPane.setViewportView(messageField);
	        messageField.setOpaque(false);
	        messageField.addMouseListener(GuiUtil.DELEGATEMOUSELISTENER);
	        messageField.setFont(new Font("Tahoma", Font.PLAIN, 18));
	        StyledDocument doc = messageField.getStyledDocument();

	        // Load the text pane with styled text.
	        try {
	            doc.insertString(doc.getLength(), text, null);
	        } catch (BadLocationException e2) {
	        }
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() { 
	                scrollPane.getVerticalScrollBar().setValue(0);
	            }
	         });

	        buttonPanel = new JPanel();
	        add(buttonPanel, "3, 2, fill, fill");
	        buttonPanel.setLayout(new FormLayout(new ColumnSpec[] {
	                ColumnSpec.decode("default:grow"),},
	            new RowSpec[] {
	                RowSpec.decode("min:none"),
	                RowSpec.decode("min:none"),
	                RowSpec.decode("min:none"),
	                RowSpec.decode("default:grow"),}));
	        
	        JTextArea area = GuiUtil.getDefaultTextarea();
	        area.setFocusable(false);
	        area.setText(TextUtil.t("ui.clickAnywhere"));
	        area.addMouseListener(GuiUtil.DELEGATEMOUSELISTENER);
	        area.setMinimumSize(null);
	        buttonPanel.add(area, "1, 1, fill, top");

	        JButton closeMessagesButton = new JButton(TextUtil.html(TextUtil.t("ui.closeMessages")));
	        buttonPanel.add(closeMessagesButton, "1, 2, fill, top");
	        closeMessagesButton.addActionListener(new ActionListener() {

	            @Override
	            public void actionPerformed(ActionEvent e) {
	                try {
	                    Thread.sleep(400);
	                } catch (InterruptedException e1) {
	                }
	                Jasbro.getInstance().getGui().closeAllMessages();
	            }
	        });
	        
	        JScrollPane scrollPane2 = new JScrollPane();
	        attributeModificationArea = new JLabel();
	        //attributeModificationTextarea.setLineWrap(true);
	        //attributeModificationTextarea.setWrapStyleWord(true);
	        attributeModificationArea.setOpaque(false);
	        //attributeModificationArea.setEditable(false);
	        attributeModificationArea.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
	        attributeModificationArea.setVerticalAlignment(JLabel.TOP);
	        //attributeModificationArea.setMinimumSize(new Dimension(1, 1));
	        
	        scrollPane2.setViewportView(attributeModificationArea);
	        scrollPane2.getViewport().setOpaque(false);
	        buttonPanel.add(scrollPane2, "1, 4, fill, fill");
	        
	        setMessageGroupObject(messageGroupObject);
	        
	        setFocusable(true);
	        addKeyListener(new KeyAdapter() {
	            @Override
	            public void keyPressed(KeyEvent e) {
	                handleKeyEvent(e);
	            }
	            
	            @Override
	            public void keyTyped(KeyEvent e) {
	                handleKeyEvent(e);
	            }

                private void handleKeyEvent(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE ||
                            e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        Jasbro.getInstance().getGui().removeLayer(MessageScreen.this);
                    }
                    else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        Jasbro.getInstance().getGui().restoreLastMessage();
                    }
                }
	        });
	    }
	}

	private void setModifications(final List<AttributeModification> modifications) {
    	String text = "";
    	for (AttributeModification modification : modifications) {
    	    if (modification.getRealModification() != 0) {
                float value = Math.round(modification.getRealModification() * 100) / 100.0f;
                text += modification.getTargetCharacter() + " " + modification.getAttributeType().getText() + " "
                        + value + "\n";
    	    }
    	    else if (modification.getTargetCharacter().getAttribute(modification.getAttributeType()).isMaxed()
    	            && !(modification.getAttributeType() instanceof EssentialAttributes)) {
    	        text += "<font color=\"red\">" + modification.getTargetCharacter() + " " + 
    	                modification.getAttributeType().getText() + " "
                        + 0 + "</font>\n";
    	    }
    	}
    	text = TextUtil.htmlPreformatted(text);
    	
    	attributeModificationArea.setText(text);
    	repaint();
	}
	
	@Override
	public boolean isPriorityMessage() {
		return priorityMessage;
	}

	public void setPriorityMessage(boolean priorityMessage) {
		this.priorityMessage = priorityMessage;
	}

	@Override
	public Object getMessageGroupObject() {
		return messageGroupObject;
	}

	@Override
	public void setMessageGroupObject(Object messageGroupObject) {
		this.messageGroupObject = messageGroupObject;
		if (messageGroupObject != null && buttonPanel != null) {
			Object arguments[] = {messageGroupObject};
			JButton closeMessagesButton = new JButton(TextUtil.html(TextUtil.t("ui.closeMessagesLocation", arguments)));
			buttonPanel.add(closeMessagesButton, "1, 3, fill, top");
			closeMessagesButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
					}
					Jasbro.getInstance().getGui().closeAllMessagesByLocation(getMessageGroupObject());
				}
			});			
			validate();
		}		
	}
	
	private List<ImageData> getImages() {
	    if (images == null) {
	        images = new ArrayList<ImageData>();
	    }
	    return images;
	}
	
	@Override
	public void setVisible(boolean visibility) {
	    super.setVisible(visibility);
        if (visibility == true) {
            requestFocus();
            if (messageData != null && attributeModificationArea.getText().equals("")) {
                setModifications(messageData.getAttributeModifications());
            }
        }
	}
}
