package jasbro.game.world;

import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.housing.HouseType;
import jasbro.game.housing.RoomType;
import jasbro.game.interfaces.UnlockObject;
import jasbro.game.world.locations.LocationType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class FameUnlockLoader {
    private final static Logger log = Logger.getLogger(FameUnlockLoader.class);
    private final static String UNLOCKFILENAME = "fameUnlocks.xml";
    private final static String LISTNODENAME = "unlock";
    private final static String TYPENODENAME = "type";
    private final static String ENTRYNODENAME = "name";
    private final static String FAMENODENAME = "fame";
    
    public void loadUnlocks(Unlocks unlocks) {
        FileInputStream fileInputStream = null;
        try {
            File file = new File(UNLOCKFILENAME);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            fileInputStream = new FileInputStream(file);
            Document doc = dBuilder.parse(fileInputStream);
            doc.getDocumentElement().normalize();
            NodeList elements = doc.getElementsByTagName(LISTNODENAME);
            for (int i = 0; i < elements.getLength(); i++) {
                Element element = (Element)elements.item(i);
                try {
                    String type = element.getElementsByTagName(TYPENODENAME).item(0).getTextContent().toUpperCase();
                    String name = element.getElementsByTagName(ENTRYNODENAME).item(0).getTextContent().toUpperCase();
                    long fame = Long.parseLong(element.getElementsByTagName(FAMENODENAME).item(0).getTextContent());
                    UnlockObject unlockObject = null;                    
                    if (type.equals("SPECIALIZATION")) {
                        unlockObject = SpecializationType.valueOf(name);
                    }
                    else if (type.equals("HOUSE")) {
                        unlockObject = HouseType.valueOf(name);
                    }
                    else if (type.equals("ROOM")) {
                        unlockObject = RoomType.valueOf(name);
                    }
                    else if (type.equals("LOCATION")) {
                        unlockObject = LocationType.valueOf(name);
                    }
                    if (unlockObject != null && fame > 0) {
                        unlocks.addFameUnlock(unlockObject, fame);
                    }
                }
                catch (Exception e) {
                    //Ignore single faulty entries
                    log.error("Error while loading unlock entry " + element.getTextContent(), e);
                }
            }
        }
        catch (Exception e) {
            log.error("Error while loading unlocks", e);
        }
        finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
