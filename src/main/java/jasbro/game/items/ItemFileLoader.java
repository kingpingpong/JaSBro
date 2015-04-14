package jasbro.game.items;

import jasbro.Jasbro;
import jasbro.MyException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileReader;
import net.java.truevfs.access.TFileWriter;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class ItemFileLoader {
	private final static Logger log = Logger.getLogger(ItemFileLoader.class);
    
    public final static String ITEMPATH = "items";
    public final static String ITEMFILEREGEX = ".+\\.xml";
    
    private static ItemFileLoader instance;

    
    private ItemFileLoader() {
    }
    
    public static synchronized ItemFileLoader getInstance() {
        if (instance == null) {
            instance = new ItemFileLoader();
        }
        return instance;
    }


    public synchronized List<Item> loadAllItems() {
    	TFile itemFolder = new TFile(ITEMPATH);
        List<Item> items = new ArrayList<Item>();
        addItems(itemFolder, items, 5);
        return items;
    }

    private void addItems(TFile itemFolder, List<Item> items, int depth) {
        if (itemFolder.isDirectory()) {
            for (TFile file : itemFolder.listFiles()) {
            	if (file.isFile() && file.getName().endsWith(".xml")) {
            		try {
                		items.add(loadItem(file));
            		}
            		catch (Exception e) {
            			log.error("Error loading item file: " + file.getName(), e);
            		}
            	}
            	else if (depth > 0 && file.isDirectory()) {
            		addItems(file, items, depth - 1);
            	}
            }
        }
	}

	private Item loadItem(TFile file) {
		Item item = null;
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
            
            item = (Item) xstream.fromXML(xml);
            item.setFile(file);
            item.setId(file.getName().substring(0, file.getName().length()-4));
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
		return item;
	}

	public void save(Item item) {
		BufferedWriter writer = null;
		TFile file = item.getFile();
		
		if (file == null) {
			file = new TFile("items/"+item.getId()+".xml");
			item.setFile(file);
		}
		try {
			if (file.exists()) {
				file.rm();
			}

			XStream xstream = new XStream(new StaxDriver());
			xstream.autodetectAnnotations(true);
			String xml = xstream.toXML(item);

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

	public void delete(Item item) {
		try {
			if (item.getFile() != null) {
				item.getFile().rm();
			}
			Jasbro.getInstance().getItems().remove(item.getId());
		} catch (IOException e) {
			log.error("Error on deleting item", e);
		}
	}
}
