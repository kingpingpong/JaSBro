package jasbro.util;

import jasbro.game.character.CharacterBase;
import jasbro.game.character.CharacterType;
import jasbro.gui.pictures.ImageData;

import java.util.Comparator;

public class Comparators {
	public static class CharacterTypeComparator implements Comparator<CharacterBase> {
		@Override
		public int compare(CharacterBase o1, CharacterBase o2) {
			if (o1.getType() == o2.getType()) {
				return 0;
			}
			else if (o1.getType() == CharacterType.TRAINER) {
				return -1;
			}
			else if (o2.getType() == CharacterType.TRAINER) {
				return 1;
			}
			else {
				return 0;
			}
		}		
	}
	
	public static class CharacterBaseNameComparator implements Comparator<CharacterBase> {
		@Override
		public int compare(CharacterBase o1, CharacterBase o2) {
			if (o1 == o2) {
				return 0;
			}
			else if (o1 == null) {
				return -1;
			}
			else {
				return o1.getName().compareTo(o2.getName());
			}
		}
	}
	
	public static class CharacterBaseFolderComparator implements Comparator<CharacterBase> {
		@Override
		public int compare(CharacterBase o1, CharacterBase o2) {
			if (o1 == o2) {
				return 0;
			}
			else if (o1 == null) {
				return -1;
			}
			else {
				return o1.getId().compareTo(o2.getId());
			}
		}
	}
	
	public static class ImageDataComparator implements Comparator<ImageData> {
		@Override
		public int compare(ImageData o1, ImageData o2) {
			if (o1 == o2) {
				return 0;
			}
			else if (o1 == null) {
				return -1;
			}
			else {
				return o1.getFilename().compareTo(o2.getFilename());
			}
		}
	}
}
