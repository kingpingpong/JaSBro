package jasbro.game.world.customContent;

import jasbro.Util.TypeAmounts;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.interfaces.Person;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import jasbro.game.world.customContent.WorldEvent.WorldEventVariables;

import java.util.List;
import java.util.Map;
import java.util.Set;

import bsh.EvalError;
import bsh.Interpreter;

public interface TriggerParent {
    public void putAttribute(WorldEventVariables key, Object object);
    public Interpreter getInterpreter() throws EvalError;
    public boolean isInterpreterInitialized();
    public Map<String, Object> generateAttributeMap() throws EvalError;
    public Object getAttribute(String key) throws EvalError;    
    public RunningActivity getActivity() throws EvalError;
    public Charakter getCharacter() throws EvalError;
    public List<Charakter> getCharacters() throws EvalError;
    public TypeAmounts getTypeAmounts() throws EvalError;
    public ActivityType getActivityType() throws EvalError;
    public CustomQuest getQuest() throws EvalError;
    public MyEvent getEvent() throws EvalError;
    public List<Person> getPeople() throws EvalError;
    public LocationTypeInterface getLocation() throws EvalError;
    public void reset(boolean resetInterpreter);
    public void reset();
    public void modifyActivities(List<ActivityDetails> activityDetails, Time time, List<Charakter> characters, 
            TypeAmounts typeAmounts, CharacterLocation characterLocation) throws EvalError;
    public Set<WorldEvent> getTriggeredEvents();
}
