package jasbro.gui;

import jasbro.game.character.activities.RunningActivity;
import jasbro.game.events.AttributeChangedEvent;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class GuiUtil {
	public static Color DEFAULTTRANSPARENTCOLOR = new Color(1.0f,1.0f,0.9f,0.7f);
	public static Border DEFAULTBORDER = new LineBorder(new Color(139, 69, 19), 1, false);
	public static Border DEFAULTEMPTYBORDER = new EmptyBorder(5, 5, 5, 5);
	public static Font DEFAULTSMALLBOLDFONT = new Font("Arial", Font.BOLD, 12);
	public static Font DEFAULTBOLDFONT = new Font("Arial", Font.BOLD, 14);
	public static Font DEFAULTLARGEBOLDFONT = new Font("Arial", Font.BOLD, 15);
	public static Font DEFAULTHEADERFONT = new Font("Arial", Font.BOLD, 17);
	public static Font DEFAULTTINYFONT = new Font("Arial", Font.PLAIN, 7);
	public static Dimension MINSIZE = new Dimension(1, 1);
	public static DelegateMouseListener DELEGATEMOUSELISTENER = new DelegateMouseListener();
	public static Color SELECTEDTRANSPARENTCOLOR = new Color(0.8f,0.8f,1.0f,0.7f);
	public static Color TRANSPARENTCOLOR = new Color(1.0f,1.0f,1.0f,0.0f);
	
	public static JTextArea getDefaultTextarea() {
		JTextArea messageField = new JTextArea();
		messageField.setLineWrap(true);
		messageField.setWrapStyleWord(true);
		messageField.setOpaque(false);
		messageField.setEditable(false);
		messageField.setFont(DEFAULTSMALLBOLDFONT);
		messageField.setMinimumSize(new Dimension(1, 1));
		return messageField;
	}
	
	public static void addMessageToEvent(MessageData messageData, MyEvent event) {
		if (event.getSource() instanceof RunningActivity) {
			RunningActivity runningActivity =  (RunningActivity) event.getSource();
			runningActivity.getMessages().add(messageData);
		}
		else if (event instanceof AttributeChangedEvent && ((AttributeChangedEvent)event).getActivity() != null) {
			RunningActivity runningActivity =  ((AttributeChangedEvent)event).getActivity();
			runningActivity.getMessages().add(messageData);
		}
		else {
			messageData.createMessageScreen();
		}
	}
}