package jasbro.game.character;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.util.Comparators.ImageDataComparator;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileInputStream;
import net.java.truevfs.access.TPath;

public class CharacterFileLoader {
	private final static Logger log = LogManager.getLogger(CharacterFileLoader.class);
	
	public final static String CHARACTERPATH = "characters";
	public final static String PROPERTYFILENAME = "properties.xml";
	public final static String IMAGENODENAME = "image";
	public final static String TRAITNODENAME = "trait";
	
	private static CharacterFileLoader instance;
	
	
	private CharacterFileLoader() {
	}
	
	public static synchronized CharacterFileLoader getInstance() {
		if (instance == null) {
			instance = new CharacterFileLoader();
		}
		return instance;
	}
	
	
	public synchronized List<CharacterBase> loadAllCharacters() {
		return loadAllCharacters(false);
	}
	
	public synchronized List<CharacterBase> loadAllCharacters(boolean optimized) {
		TFile characterFolder = new TFile(CHARACTERPATH);
		List<CharacterBase> characters = new ArrayList<CharacterBase>();
		addCharacters(characterFolder, characters, 5, optimized);
		return characters;
	}
	
	private void addCharacters(TFile characterFolder, List<CharacterBase> characters, int depth, boolean optimized) {
		if (characterFolder.isDirectory()) {
			
			for (final TFile file : characterFolder.listFiles()) {
				if ((file.isFile() && file.getName().endsWith(".zip")) || file.isDirectory()) {
					if (!file.getName().equals("template.zip") && ! file.getName().equals("template")) {
						String files[] = file.list();
						if (files != null) {
							if (Arrays.asList(files).contains("properties.xml")) {
								try {
									CharacterBase base;
									if (optimized) {
										base = loadCharacterOptimized(file);
										if (base.getImages().size() == 0) {
											log.error("Error: character has no Images: {}", base.getFolder().getNormalizedPath());
											base = null;
										}
									}
									else {
										base = loadCharacter(file);
									}
									if (base != null) {
										characters.add(base);
									}
								}
								catch (Exception e) {
									log.error("Error: character could not be loaded: {}", file.getName());
									log.throwing(e);
								}
							}
							else if (depth > 0) {
								addCharacters(file, characters, depth - 1, optimized);
							}
						}
					}
				}
			}
		}
	}
	
	public CharacterBase loadCharacter(TFile file) throws IOException {
		TFile properties = null;
		
		if (file.exists()) {
			TFile files[] = file.listFiles();
			List<TFile> imageList = new ArrayList<TFile>();
			
			for (TFile curFile : files) {
				if (curFile.getName().equals("properties.xml")) {
					properties = curFile;
				} else {
					if (ImageUtil.getInstance().isImage(curFile)) {
						imageList.add(curFile);
					} 
					else if (curFile.isDirectory()) {
						addImagesFromFolder(curFile, imageList, 4);
					}
					else {
						// not implemented
					}
				}
			}
			
			if(properties != null) {
				try (InputStream tFileInputStream = new TFileInputStream(properties)){
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(tFileInputStream);
					
					doc.getDocumentElement().normalize();
					
					CharacterBase character = new CharacterBase();
					
					load(character, doc, file);
					
					//Assign xml data to images
					Map<String, Element> imageDataMap = new HashMap<String, Element>();
					NodeList elements = doc.getElementsByTagName(IMAGENODENAME);
					for (int i = 0; i < elements.getLength(); i++) {
						Element element = (Element)elements.item(i);
						String name = element.getAttribute("name");
						name = name.replace('\\', '/');
						imageDataMap.put(name, element);
					}
					
					for (TFile imageFile : imageList) {
						String relativePath = new TPath(character.getFolder()).relativize(new TPath(imageFile)).toString();
						
						String imageId = character.getFolder().getPath() + File.separator + relativePath;
						ImageData imageData = new ImageData();
						imageData.setKey(imageId);
						relativePath = relativePath.replace('\\', '/');
						imageData.setFilename(relativePath);
						character.getImages().add(imageData);
						
						if (imageDataMap.containsKey(imageData.getFilename())) {
							Element element = imageDataMap.get(imageData.getFilename());
							NodeList attributes = element.getElementsByTagName("tag");
							for (int i = 0; i < attributes.getLength(); i++) {
								try {
									//TODO remove fix to accomodate to old wording
									if (attributes.item(i).getTextContent().equals("ASS")) {
										imageData.getTags().add(ImageTag.ANAL);
									}
									else if (attributes.item(i).getTextContent().equals("CLOTHING")) {
										imageData.getTags().add(ImageTag.CLOTHED);
									}
									else if (attributes.item(i).getTextContent().equals("STRAIGHT")) {
										imageData.getTags().add(ImageTag.VAGINAL);
									}
									else if (attributes.item(i).getTextContent().equals("EATOUT")) {
										imageData.getTags().add(ImageTag.CUNNILINGUS);
									}
									else {
										imageData.getTags().add(ImageTag.valueOf(attributes.item(i).getTextContent()));
									}
								}
								catch (Exception e) {
									log.error("Error on loading image data", e);
								}
							}
							elements = element.getElementsByTagName("customtext");
							if (elements != null && elements.getLength() > 0) {
								imageData.setCustomText(elements.item(0).getTextContent());
							}
						}
					}
					Collections.sort(character.getImages(), new ImageDataComparator());
					
					return character;
					} catch (final ParserConfigurationException | IOException | SAXException e) {
						IOException ioe = new IOException("Failed to load or parse file '" + properties.getPath() + "'", e);
						throw log.throwing(ioe);
					}
				}
			}
		return null;
	}
	
	private void addImagesFromFolder(TFile folder, List<TFile> imageList, int depth) {
		TFile files[] = folder.listFiles();
		
		for (TFile curFile : files) {
			if (ImageUtil.getInstance().isImage(curFile)) {
				imageList.add(curFile);
			}
			else if (curFile.isDirectory() && depth > 0) {
				addImagesFromFolder(curFile, imageList, depth-1);
			}
			else {
				// not implemented
			}
		}
	}
	
	
	private CharacterBase loadCharacterOptimized(TFile file) {
		TFile properties = null;

		if (file.exists()) {
			TFile files[] = file.listFiles();	
			for (TFile curFile : files) {
				if (curFile.getName().equals("properties.xml")) {
					properties = curFile;
				}
			}
			
			if (properties != null) {
				try (InputStream tFileInputStream = new TFileInputStream(properties)) {
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(tFileInputStream);
					
					doc.getDocumentElement().normalize();
					CharacterBase character = new CharacterBase();
					
					load(character, doc, file);
					
					//Assign xml data to images
					NodeList elements = doc.getElementsByTagName(IMAGENODENAME);
					for (int i = 0; i < elements.getLength(); i++) {
						Element element = (Element)elements.item(i);
						String name = element.getAttribute("name");
						name = name.replace('\\', '/');
						
						String imageId = character.getFolder().getPath() + File.separator + name;
						ImageData imageData = new ImageData();
						imageData.setKey(imageId);
						imageData.setFilename(name);
						character.getImages().add(imageData);
						
						NodeList attributes = element.getElementsByTagName("tag");
						for (int j = 0; j < attributes.getLength(); j++) {
							try {
								//TODO remove fix to accomodate to old wording
								if (attributes.item(j).getTextContent().equals("ASS")) {
									imageData.getTags().add(ImageTag.ANAL);
								}
								else if (attributes.item(j).getTextContent().equals("CLOTHING")) {
									imageData.getTags().add(ImageTag.CLOTHED);
								}
								else if (attributes.item(j).getTextContent().equals("STRAIGHT")) {
									imageData.getTags().add(ImageTag.VAGINAL);
								}
								else if (attributes.item(j).getTextContent().equals("EATOUT")) {
									imageData.getTags().add(ImageTag.CUNNILINGUS);
								}
								else {
									imageData.getTags().add(ImageTag.valueOf(attributes.item(j).getTextContent()));
								}
							}
							catch (Exception e) {
								log.error("Error on loading image data", e);
							}
							NodeList elements2 = element.getElementsByTagName("customtext");
							if (elements2 != null && elements2.getLength() > 0) {
								imageData.setCustomText(elements2.item(0).getTextContent());
							}
						}
					}
					
					
					Collections.sort(character.getImages(), new ImageDataComparator());
					
					return character;
				} catch(ArrayIndexOutOfBoundsException e) {
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParserConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SAXException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			}
		}
		return null;
	}
	
	private void load(CharacterBase character, Document doc, TFile file) {
		if (doc.getElementsByTagName("type").getLength() != 0 && 
				!doc.getElementsByTagName("type").item(0).getTextContent().equals("")) {
			String type = doc.getElementsByTagName("type").item(0).getTextContent();
			character.setType(CharacterType.valueOf(type.toUpperCase()));
		}
		else {
			character.setType(null);
		}
		character.setName(doc.getElementsByTagName("name").item(0).getTextContent());
		character.setId(file.getName().split("\\.zip")[0]);//ID is the folder name / name of zip file without .zip
		character.setFolder(file);
		
		NodeList nodes = doc.getElementsByTagName("gender");
		if (nodes.getLength() > 0) {
			String gender = nodes.item(0).getTextContent();
			if (gender != null && !gender.equals("")) {
				character.setGender(Gender.valueOf(gender.toUpperCase()));
			}
		}
		
		for (BaseAttributeTypes attribute : BaseAttributeTypes.values()) {
			if (attribute != BaseAttributeTypes.COMMAND) { // add command later, replacing obedience for trainers
				NodeList elements = doc.getElementsByTagName(attribute.toString());
				if (elements == null || elements.getLength() == 0) {
					elements = doc.getElementsByTagName(attribute.getText());//TODO remove fix to accomodate to old wording
				}
				if (elements != null && elements.getLength() > 0) {
					String content = elements.item(0).getTextContent();
					if (content != null) {
						try {
							int value = Integer.parseInt(content);
							if (value > 0) {
								character.setAttribute(attribute, value);
							}
						}
						catch (Exception e) {
						}
					}
				}
			}
		}
		
		//Load traits
		NodeList elements = doc.getElementsByTagName(TRAITNODENAME);
		for (int i = 0; i < elements.getLength(); i++) {
			try {
				character.addTrait(Trait.valueOf(elements.item(i).getTextContent()));
			}
			catch (Exception e) {
				log.error("Error on loading trait", e);
			}
		}
		
		//Add description
		elements = doc.getElementsByTagName("description");
		if (elements != null && elements.getLength() > 0) {
			character.setDescription(elements.item(0).getTextContent());
		}
		
		//Add initial specialization
		nodes = doc.getElementsByTagName("initialSpecialization");
		if (nodes.getLength() > 0) {
			String specialization = nodes.item(0).getTextContent();
			if (specialization != null && !specialization.equals("")) {
				character.setInitialSpecialization(SpecializationType.valueOf(specialization.toUpperCase()));
			}
		}
		
		//Add younger / older base links
		elements = doc.getElementsByTagName("youngerBase");
		if (elements != null && elements.getLength() > 0) {
			character.setYoungerBase(elements.item(0).getTextContent());
		}
		elements = doc.getElementsByTagName("olderBase");
		if (elements != null && elements.getLength() > 0) {
			character.setOlderBase(elements.item(0).getTextContent());
		}
	}
	
	
	
	
	
	public synchronized void saveCharacter(CharacterBase character) throws IOException {
		InputStream is = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			
			Element rootElement = doc.createElement("character");
			doc.appendChild(rootElement);
			
			Element element;
			if (character.getType() != null) {
				element = doc.createElement("type");
				rootElement.appendChild(element);
				element.appendChild(doc.createTextNode(character.getType().toString()));
			}
			
			element = doc.createElement("name");
			rootElement.appendChild(element);
			element.appendChild(doc.createTextNode(character.getName()));
			
			element = doc.createElement("gender");
			rootElement.appendChild(element);
			element.appendChild(doc.createTextNode(character.getGender().toString()));
			
			//save attributes
			for (BaseAttributeTypes attribute : BaseAttributeTypes.values()) {
				if (attribute != BaseAttributeTypes.COMMAND) { // add command later replacing obedience for trainers
					element = doc.createElement(attribute.toString());
					rootElement.appendChild(element);
					element.appendChild(doc.createTextNode(character.getAttribute(attribute)+""));
				}
			}
			
			//save Traits
			for (Trait trait : character.getTraits()) {
				element = doc.createElement(TRAITNODENAME);
				rootElement.appendChild(element);
				element.appendChild(doc.createTextNode(trait.toString()));
			}
			
			//save Description
			if (character.getDescription() != null && !character.getDescription().equals("")) {
				element = doc.createElement("description");
				rootElement.appendChild(element);
				element.appendChild(doc.createTextNode(character.getDescription().toString()));
			}
			
			//save inital specialization
			if (character.getInitialSpecialization() != null) {
				element = doc.createElement("initialSpecialization");
				rootElement.appendChild(element);
				element.appendChild(doc.createTextNode(character.getInitialSpecialization().toString()));
			}
			
			//save younger / older base
			if (character.getYoungerBase() != null && !character.getYoungerBase().equals("")) {
				element = doc.createElement("youngerBase");
				rootElement.appendChild(element);
				element.appendChild(doc.createTextNode(character.getYoungerBase().toString()));
			}
			if (character.getOlderBase() != null && !character.getOlderBase().equals("")) {
				element = doc.createElement("olderBase");
				rootElement.appendChild(element);
				element.appendChild(doc.createTextNode(character.getOlderBase().toString()));
			}
			
			//save image data
			for (ImageData image : character.getImages()) {
				element = doc.createElement(IMAGENODENAME);
				rootElement.appendChild(element);
				element.setAttribute("name", image.getFilename());
				for (ImageTag tag : image.getTags()) {
					Element tagElement = doc.createElement("tag");
					element.appendChild(tagElement);
					tagElement.appendChild(doc.createTextNode(tag.toString()));
				}
				if (image.getCustomText() != null && !image.getCustomText().equals("")) {
					Element textElement = doc.createElement("customtext");
					element.appendChild(textElement);
					textElement.appendChild(doc.createTextNode(image.getCustomText()));
				}
			}
			
			doc.normalizeDocument();
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			Source xmlSource = new DOMSource(doc);
			Result outputTarget = new StreamResult(outputStream);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.transform(xmlSource, outputTarget);
			is = new ByteArrayInputStream(outputStream.toByteArray());
			TFile propertiesFile =  new TFile(character.getFolder().getCanonicalPath()+File.separator+PROPERTYFILENAME);
			boolean finished = false;
			int tries = 5;
			do {
				try {
					if (propertiesFile.exists()) {
						propertiesFile.rm();
					}
					TFile.cp(is, propertiesFile);
					finished = true;
				}
				catch (final IOException e) {
					if (tries <= 0) {
						throw e;
					}
					else {
						tries--;
						log.debug("Write failed, try again");
						Thread.sleep(2000);
					}
				}
			}
			while(!finished);
			log.debug("Character saved {}", character.getName());
		} catch (Exception e) {
			log.error("Error on writing changes into XML file",e);
			throw new IOException("Error on writing changes into XML file",e);
		}
		finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.error("Error on closing input stream",e);
				}
			}
		}
	}
	
	public void addImages(CharacterBase character, File[] selectedFiles) {
		try {
			for (File f : selectedFiles) {
				if (ImageUtil.getInstance().isImage(f.toPath())) {
					String newKey = character.getFolder().getCanonicalPath()+File.separator+f.getName();
					deletePicture(newKey);
					TFile newFile = new TFile(newKey);
					TFile.cp(f, newFile);
					ImageData imageData = new ImageData();
					imageData.setKey(newKey);
					imageData.setFilename(f.getName());
					character.getImages().add(imageData);
				}
			}
		}
		catch (Exception e) {
			log.error("Error on adding images",e);
		}
	}
	
	public synchronized void deletePicture(ImageData image) {
		deletePicture(image.getKey());
	}
	
	private synchronized void deletePicture(String imageKey) {
		try {
			TFile file = new TFile(imageKey);
			if (file.exists()) {
				file.rm();
			}
		} catch (IOException e) {
			log.error("Failed to delete image with key '{}'", imageKey);
			log.throwing(e);
			// Eat the exception
		}
	}
	
	public synchronized CharacterBase createCharacter(String id, List<CharacterBase> characters) {
		try {
			if (id != null && !id.equals("")) {
				for (CharacterBase character : characters) {
					if (id.equals(character.getId())) {
						return null;
					}
				}
				TFile source = new TFile(CHARACTERPATH + File.separator + "template");
				if (!source.exists()) {
					source = new TFile(CHARACTERPATH + File.separator + "template.zip");
				}
				TFile destination = new TFile(CHARACTERPATH + File.separator + id);                
				TFile.cp_r(source, destination, source.getArchiveDetector(), destination.getArchiveDetector());
				
				CharacterBase character = new CharacterBase();
				character.setId(id);
				character.setName("Template");
				character.setFolder(destination);
				character.getImages().add(new ImageData(CHARACTERPATH+File.separator+character.getId()+File.separator+"template.jpg"));
				characters.add(character);
				return character;
			}
			else {
				return null;
			}
		} catch (Exception e) {
			log.error("Error on creating character",e);
			return null;
		}
	}
}