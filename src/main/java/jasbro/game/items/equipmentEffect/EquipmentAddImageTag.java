package jasbro.game.items.equipmentEffect;

import jasbro.gui.pictures.ImageTag;

import java.util.List;

public class EquipmentAddImageTag extends EquipmentEffect {
	private ImageTag imageTag;
	
	@Override
	public void modifyImageTags(List<ImageTag> imageTags) {
		if (imageTag != null && !imageTags.contains(imageTag)) {
			imageTags.add(imageTag);
		}
	}
	
	public ImageTag getImageTag() {
		return imageTag;
	}
	
	public void setImageTag(ImageTag imageTag) {
		this.imageTag = imageTag;
	}
	
	@Override
	public EquipmentEffectType getType() {
		return EquipmentEffectType.ADDIMAGETAG;
	}
	
	@Override
	public double getValue() {
		return 0;
	}
	
	@Override
	public int getAmountEffects() {
	    return 0;
	}
}