package jasbro.game;

import jasbro.game.character.Gender;
import jasbro.game.events.business.AllowedServices;

public class DefaultPreferences {
	private AllowedServices allowedServicesMale;
	private AllowedServices allowedServicesFemale;
	private AllowedServices allowedServicesFuta;
	
	public DefaultPreferences() {
		allowedServicesFemale = new AllowedServices();
		allowedServicesMale = new AllowedServices();
		allowedServicesMale.setServiceMales(false);
		allowedServicesMale.setServiceFutas(false);
		allowedServicesFuta = new AllowedServices();
	}
	
	public AllowedServices getAllowedServicesMale() {
		return allowedServicesMale;
	}
	
	public void setAllowedServicesMale(AllowedServices allowedServicesMale) {
		this.allowedServicesMale = allowedServicesMale;
	}
	
	public AllowedServices getAllowedServicesFemale() {
		return allowedServicesFemale;
	}
	
	public void setAllowedServicesFemale(AllowedServices allowedServicesFemale) {
		this.allowedServicesFemale = allowedServicesFemale;
	}
	
	public AllowedServices getAllowedServicesFuta() {
		return allowedServicesFuta;
	}
	
	public void setAllowedServicesFuta(AllowedServices allowedServicesFuta) {
		this.allowedServicesFuta = allowedServicesFuta;
	}
	
	public AllowedServices getAllowedServices(Gender gender) {
		if (gender == Gender.FUTA) {
			return allowedServicesFuta;
		}
		else if (gender == Gender.MALE) {
			return allowedServicesMale;
		}
		else {
			return allowedServicesFemale;
		}
	}
	
	public void setAllowedServices(Gender gender, AllowedServices allowedServices) {
		if (gender == Gender.FUTA) {
			allowedServicesFuta = new AllowedServices(allowedServices);
		}
		else if (gender == Gender.MALE) {
			allowedServicesMale = new AllowedServices(allowedServices);
		}
		else {
			allowedServicesFemale = new AllowedServices(allowedServices);
		}
	}
	
	
}