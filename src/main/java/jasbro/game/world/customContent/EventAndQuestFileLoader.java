package jasbro.game.world.customContent;

import jasbro.Jasbro;
import jasbro.MyException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileReader;
import net.java.truevfs.access.TFileWriter;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class EventAndQuestFileLoader {
	private final static Logger log = Logger.getLogger(EventAndQuestFileLoader.class);
    
    public final static String QUESTPATH = "quests";
    public final static String QUESTFILEREGEX = ".+\\.xml";
    public final static String EVENTPATH = "events";
    public final static String EVENTFILEREGEX = ".+\\.xml";
    
    private static EventAndQuestFileLoader instance;

    
    private EventAndQuestFileLoader() {
    }
    
    public static synchronized EventAndQuestFileLoader getInstance() {
        if (instance == null) {
            instance = new EventAndQuestFileLoader();
        }
        return instance;
    }


    public synchronized List<CustomQuestTemplate> loadAllCustomQuests() {
        List<CustomQuestTemplate> customQuests;
    	customQuests=AccessController.doPrivileged(new PrivilegedAction<List<CustomQuestTemplate>>() {
            public List<CustomQuestTemplate> run() {
                List<CustomQuestTemplate> customQuests = new ArrayList<CustomQuestTemplate>();
            	TFile questFolder = new TFile(QUESTPATH);
                addQuests(questFolder, customQuests, 5);
                return customQuests;
            }
        });
        return customQuests;
    }

    private void addQuests(TFile questFolder, List<CustomQuestTemplate> quests, int depth) {
        if (questFolder.isDirectory()) {
            for (TFile file : questFolder.listFiles()) {
            	if (file.isFile() && file.getName().matches(QUESTFILEREGEX)) {
            		try {
                		quests.add(loadQuest(file));
            		}
            		catch (Exception e) {
            			log.error("Error loading quest file: " + file.getName(), e);
            		}
            	}
            	else if (depth > 0 && file.isDirectory()) {
            		addQuests(file, quests, depth - 1);
            	}
            }
        }
	}

	private CustomQuestTemplate loadQuest(TFile file) {
	    CustomQuestTemplate quest = null;
		BufferedReader bufferedReader = null;
		try {
            XStream xstream = new XStream(new StaxDriver());
            xstream.autodetectAnnotations(true);
            bufferedReader = new BufferedReader(new TFileReader(file));
            String xml = "";
            String line;
            do {
            	line = bufferedReader.readLine();
            	if (line != null) {
            		xml += line + "\n";
            	}
            }
            while (line != null);
            
            quest = (CustomQuestTemplate) xstream.fromXML(xml);
            quest.setFile(file);
            quest.setId(file.getName().substring(0, file.getName().length()-4));
		} catch (Exception e) {
			log.error("File does not exist or can not be read", e);
			throw new MyException("File does not exist or can not be read", e);
		}
		finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
				}
			}
		}
		return quest;
	}

	public void save(CustomQuestTemplate quest) {
		BufferedWriter writer = null;
		TFile file = quest.getFile();
		
		if (file == null) {
			file = new TFile(QUESTPATH + "/"+quest.getId()+".xml");
			quest.setFile(file);
		}
		try {
			if (file.exists()) {
				file.rm();
			}

			XStream xstream = new XStream(new StaxDriver());
			xstream.autodetectAnnotations(true);
			String xml = xstream.toXML(quest);

			writer = new BufferedWriter(new TFileWriter(file));
			writer.write(xml);
			writer.flush();
			
		} catch (Exception e) {
			log.error("Error on saving data", e);
		}
		finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					log.error("Error on closing writer", e);
				}				
			}
		}
	}

	public void delete(CustomQuestTemplate quest) {
		try {
			if (quest.getFile() != null) {
				quest.getFile().rm();
			}
			Jasbro.getInstance().getCustomQuestTemplates().remove(quest.getId());
		} catch (IOException e) {
			log.error("Error on deleting quest", e);
		}
	}
	
	
	
	
	
	
	
    public synchronized List<WorldEvent> loadAllCustomEvents() {
        List<WorldEvent> customEvents;
    	customEvents=AccessController.doPrivileged(new PrivilegedAction<List<WorldEvent>>() {
            public List<WorldEvent> run() {
                TFile eventFolder = new TFile(EVENTPATH);
                List<WorldEvent> customEvents = new ArrayList<WorldEvent>();
                addEvents(eventFolder, customEvents, 5);
                return customEvents;
            }
        });
        return customEvents;
    }

    private void addEvents(TFile eventFolder, List<WorldEvent> events, int depth) {
        if (eventFolder.isDirectory()) {
            for (TFile file : eventFolder.listFiles()) {
                if (file.isFile() && file.getName().matches(EVENTFILEREGEX)) {
                    try {
                        events.add(loadEvent(file));
                    }
                    catch (Exception e) {
                        log.error("Error loading event file: " + file.getName(), e);
                    }
                }
                else if (depth > 0 && file.isDirectory()) {
                    addEvents(file, events, depth - 1);
                }
            }
        }
    }

    private WorldEvent loadEvent(TFile file) {
        WorldEvent event = null;
        BufferedReader bufferedReader = null;
        try {
            XStream xstream = new XStream(new StaxDriver());
            xstream.autodetectAnnotations(true);
            bufferedReader = new BufferedReader(new TFileReader(file));
            String xml = "";
            String line;
            do {
                line = bufferedReader.readLine();
                if (line != null) {
                    xml += line + "\n";
                }
            }
            while (line != null);
            
            event = (WorldEvent) xstream.fromXML(xml);
            event.setFile(file);
            event.setId(file.getName().substring(0, file.getName().length()-4));
        } catch (Exception e) {
            log.error("File does not exist or can not be read", e);
            throw new MyException("File does not exist or can not be read", e);
        }
        finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                }
            }
        }
        return event;
    }

    public void save(WorldEvent event) {
        BufferedWriter writer = null;
        TFile file = event.getFile();
        
        if (file == null) {
            file = new TFile(EVENTPATH + "/" + event.getId()+".xml");
            event.setFile(file);
        }
        try {
            if (file.exists()) {
                file.rm();
            }

            XStream xstream = new XStream(new StaxDriver());
            xstream.autodetectAnnotations(true);
            String xml = xstream.toXML(event);

            writer = new BufferedWriter(new TFileWriter(file));
            writer.write(xml);
            writer.flush();
            
        } catch (Exception e) {
            log.error("Error on saving data", e);
        }
        finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    log.error("Error on closing writer", e);
                }               
            }
        }
    }

    public void delete(WorldEvent event) {
        try {
            if (event.getFile() != null) {
                event.getFile().rm();
            }
            Jasbro.getInstance().getWorldEvents().remove(event.getId());
        } catch (IOException e) {
            log.error("Error on deleting event", e);
        }
    }
}
