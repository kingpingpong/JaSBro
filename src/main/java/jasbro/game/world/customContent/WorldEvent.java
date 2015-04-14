package jasbro.game.world.customContent;

import jasbro.Jasbro;
import jasbro.MyException;
import jasbro.Util;
import jasbro.Util.TypeAmounts;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.events.AttributeChangedEvent;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.interfaces.Person;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import jasbro.game.world.customContent.effects.WorldEventEffectContainer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.java.truevfs.access.TFile;

import org.apache.log4j.Logger;

import bsh.EvalError;
import bsh.Interpreter;

public class WorldEvent implements TriggerParent, Serializable {
    private final static Logger log = Logger.getLogger(WorldEvent.class);
	private String id;
	private List<Trigger> triggers;
	private List<WorldEventEffect> effects;
	
	private transient Interpreter interpreter;
	private transient Map<String, Object> attributeMap;
	private transient TFile file;
	private transient Set<WorldEvent> triggeredEvents;
	private transient Set<Charakter> protectedCharacters;
	private transient boolean inProgress = false;
	
	public WorldEvent(String id) {
	    this.id = id;
	    getEffects().add(new WorldEventEffectContainer());
	}
	
	public void handleEvent(MyEvent e) {
	    if (!inProgress && getTriggers().size() > 0) {
	        try {
	            putAttribute(WorldEventVariables.worldEventInstance, this);
	            putAttribute(WorldEventVariables.event, e);
                if (e.getSource() instanceof RunningActivity) {
                    RunningActivity activity = (RunningActivity) e.getSource();
                    putAttribute(WorldEventVariables.activity, activity);
                    putAttribute(WorldEventVariables.activitytype, activity.getType());
                    putAttribute(WorldEventVariables.character, activity.getCharacters().get(0));
                    putAttribute(WorldEventVariables.characters, activity.getCharacters());
                }
                else if (e.getSource() instanceof Charakter) {
                    putAttribute(WorldEventVariables.character, (Charakter) e.getSource());
                }
	            for (Trigger trigger : getTriggers()) {
	                if (trigger.isTriggered(this)) {
	                    execute();
	                    break;
	                }
	            }
	        } catch (EvalError ex) {
	            log.error(getId() + ": Error during world Event", ex);
	            throw new MyException("World event failed", ex);
	        }
	    }
	    
        if (e.getType() == EventType.HEALTHZERO && getProtectedCharacters().contains(
                ((AttributeChangedEvent) e).getAttribute().getCharacter())) {
            e.setCancelled(true);
        }
	}
	
    public void modifyActivities(List<ActivityDetails> activityDetails, Time time, List<Charakter> characters, 
            TypeAmounts typeAmounts, CharacterLocation characterLocation) {
        if (getTriggers().size() > 0) {
            try {
                putAttribute(WorldEventVariables.worldEventInstance, this);
                putAttribute(WorldEventVariables.activitytype, ActivityType.CUSTOMEVENT);
                putAttribute(WorldEventVariables.typeamounts, typeAmounts);
                putAttribute(WorldEventVariables.location, characterLocation.getLocationType());
                if (characters != null) {
                    putAttribute(WorldEventVariables.characters, characters);
                    if (characters.size() > 0) {
                        putAttribute(WorldEventVariables.character, characters.get(0));
                    }
                }
                for (Trigger trigger : getTriggers()) {
                    if (trigger.isTriggered(this)) {
                        ActivityDetails activityDetail = new ActivityDetails(ActivityType.CUSTOMEVENT, getId(), 
                                trigger.getActivityLabel(), trigger.getActivityDescription());
                        activityDetails.add(activityDetail);
                    }
                }
            } catch (EvalError ex) {
                log.error("Error, while evaluating custom code", ex);
                throw new MyException("Custom code failed", ex);
            }
        }
    }

    public void execute(Interpreter interpreter) {
        this.interpreter = interpreter;
        execute();
    }
	
    public void execute() {
        if (!inProgress) {
            try {
                inProgress = true;
                putAttribute(WorldEventVariables.worldEventInstance, this);
                for (WorldEventEffect effect : effects) {
                    effect.perform(this);
                }
            } catch (EvalError ex) {
                log.error("Error, while evaluating custom code", ex);
                throw new MyException("Custom code failed", ex);
            }
            finally {
                inProgress = false;
            }
        }
	}
	
	public List<Trigger> getTriggers() {
	    if (triggers == null) {
	        triggers = new ArrayList<Trigger>();
	    }
        return triggers;
    }

    public List<WorldEventEffect> getEffects() {
        if (effects == null) {
            effects = new ArrayList<WorldEventEffect>();
        }
        return effects;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public Interpreter getInterpreter() throws EvalError {
        if (interpreter == null) {
            interpreter = Jasbro.getInstance().getInterpreter();
            for (Entry<String, Object> entry : getAttributeMap().entrySet()) {
                try {
                    interpreter.set(entry.getKey(), entry.getValue());
                } catch (EvalError e) {
                    log.error("Eval error", e);
                }
            }
            if (getQuest() != null) {
                getQuest().setInterpreter(interpreter);
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
            protectedCharacters = null;
            triggeredEvents = null;
            if (interpreter != null && resetInterpreter) {
                Jasbro.getInstance().cleanupInterpreter();
            }
            interpreter = null;
        }
    }
    
    public TFile getFile() {
        return file;
    }
    public void setFile(TFile file) {
        this.file = file;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WorldEvent other = (WorldEvent) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return getId();
    }    

    public Map<String, Object> getAttributeMap() {
        if (attributeMap == null) {
            attributeMap = new HashMap<String, Object>();
            attributeMap.put("data", Jasbro.getInstance().getData());
        }
        return attributeMap;
    }

    public void setAttributeMap(Map<String, Object> attributeMap) {
        this.attributeMap = attributeMap;
    }

    public void setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
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
    
    public boolean isInterpreterInitialized() {
        return interpreter != null;
    }
    
    public RunningActivity getActivity() throws EvalError {
        return (RunningActivity)getAttribute(WorldEventVariables.activity);
    }
    
    public MyEvent getEvent() throws EvalError {
        return (MyEvent)getAttribute(WorldEventVariables.event);
    }
    
    public CustomQuest getQuest() throws EvalError {
        return (CustomQuest)getAttribute(WorldEventVariables.questInstance);
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
                List<Charakter> characters = getCharacters();
                if (characters != null) {
                    people.addAll(getCharacters());
                }
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
    
    public Set<WorldEvent> getTriggeredEvents() {
        if (triggeredEvents == null) {
            triggeredEvents = new HashSet<WorldEvent>();
        }
        return triggeredEvents;
    }
    public Set<Charakter> getProtectedCharacters() {
        if (protectedCharacters == null) {
            protectedCharacters = new HashSet<Charakter>();
        }
        return protectedCharacters;
    }

    public void setTriggeredEvents(Set<WorldEvent> effects) {
        this.triggeredEvents = effects;
    }

    public void setProtectedCharacters(Set<Charakter> protectedCharacters) {
        this.protectedCharacters = protectedCharacters;
    }



    public static enum WorldEventVariables {
        data, activity, questInstance, event, character, characters, people,
        typeamounts, activitytype, attributemodifications, location,
        worldEventInstance
    }
}
