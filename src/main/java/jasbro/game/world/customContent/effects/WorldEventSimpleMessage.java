package jasbro.game.world.customContent.effects;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bsh.EvalError;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.world.customContent.ImageSelection;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEvent.WorldEventVariables;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import jasbro.texts.TextUtil;


public class WorldEventSimpleMessage extends WorldEventEffect {
	private final static Logger log = LogManager.getLogger(WorldEventSimpleMessage.class);
	private String message;
	
	private List<ImageSelection> images;
	private ImageSelection background;
	private boolean importantMessage = true;
	
	public WorldEventSimpleMessage() {
		images = new ArrayList<ImageSelection>();
		
		background = new ImageSelection();
		background.setBackground(true);
	}
	
	@Override
	public void perform(WorldEvent worldEvent) throws EvalError {
		MessageData messageData = new MessageData();
		
		if (message != null) {
			messageData.addToMessage(TextUtil.getInstance().applyTemplates(message, worldEvent.getPeople(), 
					worldEvent.generateAttributeMap()));
		}
		
		for (ImageSelection imageSelection : images) {
			messageData.addImage(imageSelection.getImageData(worldEvent));
		}
		messageData.setBackground(background.getImageData(worldEvent));
		messageData.setPriorityMessage(importantMessage);
		
		@SuppressWarnings("unchecked")
		List<AttributeModification> modifications = 
		(List<AttributeModification>) worldEvent.getAttribute(WorldEventVariables.attributemodifications);
		if (modifications != null) {
			messageData.setAttributeModifications(modifications);
			worldEvent.putAttribute(WorldEventVariables.attributemodifications, null);
		}
		
		log.debug(messageData.getMessage());
		
		if (worldEvent.getActivity() == null || worldEvent.getActivity().isAbort() || 
				(worldEvent.getEvent() != null &&worldEvent.getEvent().getType() == EventType.ACTIVITYFINISHED)) {
			messageData.createMessageScreen();
		}
		else {
			worldEvent.getActivity().getMessages().add(messageData);
		}
		
	}
	
	@Override
	public WorldEventEffectType getType() {
		return WorldEventEffectType.MESSAGE;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public List<ImageSelection> getImages() {
		return images;
	}
	
	public void setImages(List<ImageSelection> images) {
		this.images = images;
	}
	
	public ImageSelection getBackground() {
		return background;
	}
	
	public void setBackground(ImageSelection background) {
		this.background = background;
	}
	
	public boolean isImportantMessage() {
		return importantMessage;
	}
	
	public void setImportantMessage(boolean importantMessage) {
		this.importantMessage = importantMessage;
	}
	
	
	
}