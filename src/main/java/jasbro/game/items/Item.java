package jasbro.game.items;

import jasbro.game.world.customContent.ImageSelection;
import jasbro.gui.pictures.ImageData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.java.truevfs.access.TFile;

public abstract class Item implements Serializable {    
	private transient String id;
	private int value = 0;
	private String name;
	private String description;
	private ItemType type;
	private List<ItemSpawnData> spawnData = new ArrayList<ItemSpawnData>();
	private ImageSelection imageSelection;
	private String authorDescription;
	private transient TFile file;

	public Item(String id, ItemType type) {
		super();
		this.id = id;
		this.type = type;
		spawnData.add(new ItemSpawnData());
	}
	
	public Item(Item item) {
		super();
		this.id = item.id;
		this.value = item.value;
		this.name = item.name;
		this.description = item.description;
		this.type = item.type;
		this.spawnData = item.spawnData;
		this.imageSelection = item.imageSelection;
		this.file = item.file;
	}

    public abstract String getText();

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<ItemSpawnData> getSpawnData() {
		return spawnData;
	}

	public void setSpawnData(List<ItemSpawnData> spawnData) {
		this.spawnData = spawnData;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TFile getFile() {
		return file;
	}

	public void setFile(TFile file) {
		this.file = file;
	}
	
	@Override
	public String toString() {
		return getId();
	}

    public String getAuthorDescription() {
        return authorDescription;
    }

    public void setAuthorDescription(String authorDescription) {
        this.authorDescription = authorDescription;
    }
	
    public ImageData getIcon() {
        if (imageSelection == null) {
            if (this instanceof UsableItem || this instanceof UnlockItem) {
                return new ImageData("images/icons/items/Default Icons/icon_use_scroll.png");
            }
            else if (this instanceof Equipment) {
                Equipment equipment = (Equipment) this;
                if (equipment.getEquipmentType() == EquipmentType.HEAD) {
                    return new ImageData("images/icons/items/Default Icons/icon_bdy_hat.png");
                }
                else if (equipment.getEquipmentType() == EquipmentType.DRESS) {
                    return new ImageData("images/icons/items/Default Icons/icon_bdy_robe.png");
                }
                else if (equipment.getEquipmentType() == EquipmentType.SHOES) {
                    return new ImageData("images/icons/items/Default Icons/icon_bdy_boots.png");
                }
                else if (equipment.getEquipmentType() == EquipmentType.ACCESSORY) {
                    if (equipment.getAccessoryType() != null) {
                        switch (equipment.getAccessoryType()) {
                            case NECKLACE: return new ImageData("images/icons/items/Default Icons/icon_acc_amulet.png");
                            case GLOVES: return new ImageData("images/icons/items/Default Icons/icon_acc_ring.png");
                            case ONEHANDED: return new ImageData("images/icons/items/Default Icons/icon_wpn_sword.png");
                            case TWOHANDED: return new ImageData("images/icons/items/Default Icons/icon_wpn_axe.png");
                            default:
                        }
                    }
                    else {
                        return new ImageData("images/icons/items/General Items/Scrolls and Books/Ebook-icon.png");
                    }
                }
            }
            return new ImageData("images/icons/items/Default Icons/icon_use_scroll.png");
        }
        else {
            return imageSelection.getImageData(this);
        }
    }

    public ImageSelection getImageSelection() {
        return this.imageSelection;
    }
    
    public void setImageSelection(ImageSelection icon) {
        this.imageSelection = icon;
    }
	
    
}
