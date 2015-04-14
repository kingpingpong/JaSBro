package jasbro.gui.pictures;

import jasbro.texts.TextUtil;

public enum ImageTagGroup {
	GENERAL, SEX, FOREPLAY, ACTIVITY, CLOTHING(15), GROUP, FILTERTAGS,
	CHARACTERGENDER(false, 5), AMOUNTPEOPLE(false, 5), OTHERGENDER(false, 5), DOMINANCE(false, 5),
	MONSTERTYPE(false),
	HIDDEN(false)
	;	
	
	private int groupValue = 30;
	private boolean standardGroup = true;
	
	private ImageTagGroup() {
	}
	
	private ImageTagGroup(int groupValue) {
		this.groupValue = groupValue;
	}
	
	private ImageTagGroup(boolean standardGroup) {
		this.standardGroup = standardGroup;
	}
	
	private ImageTagGroup(boolean standardGroup, int groupValue) {
		this.standardGroup = standardGroup;
		this.groupValue = groupValue;
	}
		
	public boolean isStandardGroup() {
		return standardGroup;
	}

	public String getText() {
		String text = TextUtil.tNoCheck(this.toString());
		if (text == null) {
			text = this.toString();
			text = text.charAt(0) + text.substring(1).toLowerCase();
		}
		return text;
	}

	public int getGroupValue() {
		return groupValue;
	}
}
