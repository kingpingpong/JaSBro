package jasbro;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import jasbro.game.GameData;

public class SaveAndLoadPerformer {
	private final static Logger log = LogManager.getLogger(SaveAndLoadPerformer.class);

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
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					log.error("Error on closing writer", e);
				}
			}
		}
	}

	public GameData load(File selectedFile) throws IOException {
		GameData gameData = null;
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(selectedFile))) {
			XStream xstream = new XStream(new StaxDriver());

			xstream.autodetectAnnotations(true);
			String xml = "";
			String line;
			do {
				line = bufferedReader.readLine();
				if (line != null) {
					xml += line + "\n";
				}
			} while (line != null);

			if (log.isDebugEnabled()) {
				BufferedWriter writer = new BufferedWriter(new FileWriter(new File("test.xml")));
				writer.write(xml);
				writer.flush();
				writer.close();
			}
			gameData = (GameData) xstream.fromXML(xml);
		} catch (final IOException e) {
			IOException le = new IOException("Failed to open or read file '" + selectedFile + "'", e);
			log.error(le);
			throw le;
		}

		return gameData;
	}

}