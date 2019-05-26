package jasbro.game.events;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.gui.objects.div.MessageInterface;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;

public class MessageData {
	private final static Logger log = LogManager.getLogger(MessageData.class);
	private List<ImageData> images = new ArrayList<ImageData>();
	private String message = "";
	private ImageData background;
	private boolean priorityMessage;
	private List<AttributeModification> attributeModifications = new ArrayList<AttributeModification>();
	private Object messageGroupObject;
	private List<Future<MessageData>> futures;
	private List<Integer> futureTextPosition;
	
	public MessageData() {
	}
	
	public MessageData(String message, ImageData image, ImageData background) {
		super();
		if (image != null) {
			images.add(image);
		}
		addToMessage(message);
		this.background = background;
	}
	
	public MessageData(String message, ImageTag tag, Charakter character) {
		this(message, ImageUtil.getInstance().getImageDataByTag(tag, character), character.getBackground());
	}
	
	public MessageData(String message, ImageTag tag, Charakter character, boolean priorityMessage) {
		this(message, tag, character);
		this.priorityMessage = priorityMessage;
	}
	
	public MessageData(String message, ImageData image, ImageData background, boolean priorityMessage) {
		this(message, image, background);
		this.priorityMessage = priorityMessage;
	}
	
	public MessageData(String message, ImageData image, ImageData image2, ImageData background) {
		this(message, image, background);
		images.add(image2);
	}
	
	public ImageData getImage() {
		if (images.size() > 0) {
			return images.get(0);
		}
		else {
			return null;
		}
	}
	public void setImage(ImageData image) {
		if (images.size() == 1) {
			images.clear();
		}
		images.add(0, image);
	}
	public String getMessage() {
		getCallableData();
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
		addToMessage("");
	}
	public ImageData getBackground() {
		return background;
	}
	public void setBackground(ImageData background) {
		this.background = background;
	}
	
	public void addToMessage(String message) {
		this.message += message;
		if (this.message.length() > 0) {
			if (this.message.charAt(this.message.length()-1) != '\n' && this.message.charAt(this.message.length()-1) != ' ') {
				this.message += " ";
			}
		}
	}
	
	public void addToMessage(Integer location, String message) {
		String tmpMessage = this.message.substring(location, this.message.length());
		this.message = this.message.substring(0, location);
		addToMessage(message);
		addToMessage(tmpMessage);
	}
	
	public ImageData getImage2() {
		if (images.size() > 1) {
			return images.get(1);
		}
		else {
			return null;
		}
	}
	
	public void setImage2(ImageData image2) {
		if (images.size() > 0) {
			images.add(1, image2);
		}
		else {
			images.add(image2);
		}
	}
	
	public boolean isPriorityMessage() {
		return priorityMessage;
	}
	
	public void setPriorityMessage(boolean priorityMessage) {
		this.priorityMessage = priorityMessage;
	}
	
	public List<AttributeModification> getAttributeModifications() {
		return attributeModifications;
	}
	
	public void setAttributeModifications(List<AttributeModification> attributeModifications) {
		this.attributeModifications = attributeModifications;
	}
	
	public MessageInterface createMessageScreen() {
		MessageInterface screen;
		MessageScreen messageScreen = new MessageScreen(this);
		screen = messageScreen;
		return screen;
	}
	
	public Object getMessageGroupObject() {
		return messageGroupObject;
	}
	
	public void setMessageGroupObject(Object messageGroupObject) {
		this.messageGroupObject = messageGroupObject;
	}
	
	public void addFuture(Future<MessageData> future) {
		if (futures == null) {
			futures = new ArrayList<Future<MessageData>>();
			futureTextPosition = new ArrayList<Integer>();
		}
		futures.add(future);
		futureTextPosition.add(message.length());
	}
	
	public void getCallableData() {
		if (futures != null) {
			for (int i = 0; i < futures.size(); i++) {
				Future<MessageData> future = futures.get(i);
				try {
					MessageData changedData = future.get();
					if (changedData != null) {
						if (changedData.getMessage() != null) {
							addToMessage(futureTextPosition.get(i), changedData.getMessage());
						}
						images.addAll(changedData.images);
						if (changedData.getBackground() != null) {
							background = changedData.getBackground();
						}
					}
				} catch (InterruptedException e) {
				} catch (ExecutionException e) {
					log.error("Error while collecting messagedata", e);
				}
			}
			futures = null;
		}
		
	}
	
	public List<ImageData> getImages() {
		while (images.contains(null)) {
			images.remove(null);
		}
		return images;
	}
	
	public void addImage(ImageData image) {
		getImages().add(image);
	}
}