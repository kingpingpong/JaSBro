package jasbro;

import jasbro.game.GameData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class SaveAndLoadPerformer {
    private final static Logger log = Logger.getLogger(SaveAndLoadPerformer.class);

	public void save(File file, GameData gameData) {
		BufferedWriter writer = null;
		try {
			if (file.exists()) {
				file.delete();
			}
			XStream xstream = new XStream(new StaxDriver());
			xstream.autodetectAnnotations(true);
			String xml = xstream.toXML(gameData);

			writer = new BufferedWriter(new FileWriter(file));
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

	public GameData load(File selectedFile) {
		GameData gameData = null;
		BufferedReader bufferedReader = null;
		try {
            XStream xstream = new XStream(new StaxDriver());
            
            xstream.autodetectAnnotations(true);
            bufferedReader = new BufferedReader(new FileReader(selectedFile));
            String xml = "";
            String line;
            do {
            	line = bufferedReader.readLine();
            	if (line != null) {
            		xml += line + "\n";
            	}
            }
            while (line != null);   
            
            if (log.isDebugEnabled()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(new File("test.xml")));
                writer.write(xml);
                writer.flush();
                writer.close();
            }
            gameData = (GameData) xstream.fromXML(xml);
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
		return gameData;
	}

}
