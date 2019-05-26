package jasbro.game.housing;

import jasbro.texts.TextUtil;

public enum SecurityState {

	CRIMINALDEN,
	DANGEROUS,
	UNRULY,
	UNCIVIL,
	CIVIL,
	SECURE,
	HEAVEN;
	
	public String getText() {
		return TextUtil.t(this.toString());
	}
	
	public static boolean isImplemented() {
		return false;
	}
	
	public static SecurityState calcState(House house) {
		int security = house.getSecurity();
		if (security == 0) {
			return CRIMINALDEN;
		} else if (security > 0 && security <= 20) {
			return DANGEROUS;
		} else if (security > 20 && security <= 40) {
			return UNRULY;
		} else if (security > 40 && security <= 60) {
			return UNCIVIL;
		} else if (security > 60 && security <= 80) {
			return CIVIL;
		} else if (security > 80 && security < 100) {
			return SECURE;
		} else { // security >= 100
			return HEAVEN;
		}
	}
	
	
}
