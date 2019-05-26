package jasbro.game.world.customContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bsh.EvalError;
import bsh.Interpreter;
import jasbro.Jasbro;
import jasbro.Util;
import jasbro.Util.TypeAmounts;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.interfaces.Person;
import jasbro.game.quests.Quest;
import jasbro.game.quests.QuestStage;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import jasbro.game.world.customContent.WorldEvent.WorldEventVariables;

public class CustomQuest extends Quest implements TriggerParent {
	private final static Logger log = LogManager.getLogger(CustomQuest.class);
	
	private String customQuestId;
	private transient Interpreter interpreter;
	private transient Map<String, Object> attributeMap;
	private transient CustomQuestTemplate template;
	private transient Set<WorldEvent> triggeredEvents;
	private transient boolean inProgress = false;
	
	public CustomQuest(String customQuestId) {
		this.customQuestId = customQuestId;
	}
	
	@Override
	public List<QuestStage> getInitStages() {
		return new ArrayList<QuestStage>(getTemplate().getQuestStages());
	}
	
	@Override
	public Interpreter getInterpreter() {
		if (interpreter == null) {
			interpreter = Jasbro.getInstance().getInterpreter();
			for (Entry<String, Object> entry : getAttributeMap().entrySet()) {
				try {
					interpreter.set(entry.getKey(), entry.getValue());
				} catch (EvalError e) {
					log.error("Eval error", e);
				}
			}
		}
		return interpreter;
	}
	
	public void putAttribute(WorldEventVariables key, Object object) {
		putAttribute(key.toString(), object);
	}
	
	public void putAttribute(String key, Object object) {
		getAttributeMap().put(key, object);
		if (interpreter != null) {
			try {
				interpreter.set(key, object);
			} catch (EvalError e) {
				log.error("Eval error", e);
			}
		}
	}
	
	public Map<String, Object> getAttributeMap() {
		if (attributeMap == null) {
			attributeMap = new HashMap<String, Object>();
			putAttribute(WorldEventVariables.questInstance, this);
			putAttribute(WorldEventVariables.data, Jasbro.getInstance().getData());
		}
		return attributeMap;
	}
	
	@Override
	public void reset() {
		reset(true);
	}
	
	@Override
	public void reset(boolean resetInterpreter) {
		if (!inProgress) {
			if (triggeredEvents != null) {
				for (TriggerParent subEffect : getTriggeredEvents()) {
					subEffect.reset(false);
				}
			}
			attributeMap = null;
			triggeredEvents = null;
			if (interpreter != null && resetInterpreter) {
				Jasbro.getInstance().cleanupInterpreter();
			}
			interpreter = null;
		}
	}
	
	public CustomQuestTemplate getTemplate() {
		if (template == null) {
			template = Jasbro.getInstance().getCustomQuestTemplates().get(customQuestId);
		}
		return template;
	}
	
	public void set(String attributeName, Object value) {
		getAttributeMap().put(attributeName, value);
	}
	
	public Object get(String attributeName) {
		return getAttributeMap().get(attributeName);
	}
	
	@Override
	public boolean showInQuestLog() {
		return ((CustomQuestStage)getCurrentStage()).isShowInQuestLog();
	}
	
	public boolean isInterpreterInitialized() {
		if (interpreter != null) {
			return true;
		}
		for (TriggerParent subEffect : getTriggeredEvents()) {
			if (subEffect.isInterpreterInitialized()) {
				return true;
			}
		}
		return false;
	}
	
	
	public RunningActivity getActivity() throws EvalError {
		return (RunningActivity)getAttribute(WorldEventVariables.activity);
	}
	
	public MyEvent getEvent() throws EvalError {
		return (MyEvent)getAttribute(WorldEventVariables.event);
	}
	
	@SuppressWarnings("unchecked")
	public List<Charakter> getCharacters() throws EvalError {
		return (List<Charakter>) getAttribute(WorldEventVariables.characters);
	}
	
	@SuppressWarnings("unchecked")
	public List<Person> getPeople() throws EvalError {
		if (interpreter == null || interpreter.get(WorldEventVariables.people.toString()) == null) {
			if (getAttributeMap().containsKey(WorldEventVariables.people.toString())) {
				return ((List<Person>)getAttributeMap().get(WorldEventVariables.people.toString()));
			}
			else {
				List<Person> people = new ArrayList<Person>();
				people.addAll(getCharacters());
				putAttribute(WorldEventVariables.people, people);
				return people;
			}
		}
		else {
			return (List<Person>)interpreter.get(WorldEventVariables.people.toString());
		}
	}
	
	@Override
	public TypeAmounts getTypeAmounts() throws EvalError {
		if (interpreter == null || interpreter.get(WorldEventVariables.typeamounts.toString()) == null) {
			if (getAttributeMap().containsKey(WorldEventVariables.typeamounts.toString())) {
				return ((TypeAmounts)getAttributeMap().get(WorldEventVariables.typeamounts.toString()));
			}
			else {
				List<Charakter> characters = getCharacters();
				if (characters != null) {
					TypeAmounts typeAmounts = Util.getTypeAmounts(characters);
					getAttributeMap().put(WorldEventVariables.typeamounts.toString(), typeAmounts);
					return typeAmounts;
				}
				else {
					return null;
				}
			}
		}
		else {
			return (TypeAmounts)interpreter.get(WorldEventVariables.typeamounts.toString());
		}
	}
	
	@Override
	public ActivityType getActivityType() throws EvalError {
		return (ActivityType) getAttribute(WorldEventVariables.activitytype);
	}
	
	@Override
	public Charakter getCharacter() throws EvalError {
		return (Charakter)getAttribute(WorldEventVariables.character);
	}
	
	@Override
	public CustomQuest getQuest() {
		return this;
	}
	
	public Object getAttribute(WorldEventVariables key) throws EvalError {
		return getAttribute(key.toString());
	}
	
	public Object getAttribute(String key) throws EvalError {
		if (interpreter == null) {
			if (getAttributeMap().containsKey(key)) {
				return getAttributeMap().get(key);
			} else {
				return null;
			}
			
		} else {
			return interpreter.get(key);
		}
	}
	
	@Override
	public LocationTypeInterface getLocation() throws EvalError {
		LocationTypeInterface locationType = (LocationTypeInterface)getAttribute(WorldEventVariables.location);
		if (locationType == null) {
			RunningActivity activity = getActivity();
			if (activity != null) {
				locationType = activity.getPlannedActivity().getSource().getLocationType();
				putAttribute(WorldEventVariables.location, locationType);
			}
		}
		return locationType;
	}
	
	
	@Override
	public void modifyActivities(List<ActivityDetails> activityDetails, Time time, List<Charakter> characters, 
			TypeAmounts typeAmounts, CharacterLocation characterLocation) throws EvalError {
		((CustomQuestStage)getCurrentStage()).modifyActivities(activityDetails, time, characters, typeAmounts, characterLocation, this);
	}
	
	@Override
	public Map<String, Object> generateAttributeMap() throws EvalError {
		if (interpreter == null) {
			return getAttributeMap();
		}
		else {
			Map<String, Object> attributeMap = new HashMap<String, Object>();            
			for (String variable : (String[]) getInterpreter().eval("return this.variables;")) {
				attributeMap.put(variable, getInterpreter().get(variable));
			}
			return attributeMap;
		}
	}
	
	public void setInterpreter(Interpreter interpreter) {
		this.interpreter = interpreter;
	}
	
	public Set<WorldEvent> getTriggeredEvents() {
		if (triggeredEvents == null) {
			triggeredEvents = new HashSet<WorldEvent>();
		}
		return triggeredEvents;
	}
	
	@Override
	public void handleEvent(MyEvent e) {
		if (!inProgress && getCurrentStage() != null) {
			try {
				inProgress = true;
				getCurrentStage().handleEvent(e, this);
			}
			finally {
				inProgress = false;
			}
		}
	}
	
}