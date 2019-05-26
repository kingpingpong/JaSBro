package jasbro.game.events.business;

import jasbro.game.character.attributes.Sextype;

import java.util.HashMap;
import java.util.Map;

public class AllowedServices {
	private Map<Sextype, Boolean> allowedServicesMap;
	private boolean serviceMales = true;
	private boolean serviceFemales = true;
	private boolean serviceFutas = true;
	
	public AllowedServices() {
		allowedServicesMap = new HashMap<Sextype, Boolean>();
	}
	
	public AllowedServices(AllowedServices allowedServices) {
		serviceFemales = allowedServices.serviceFemales;
		serviceFutas = allowedServices.serviceFutas;
		serviceMales = allowedServices.serviceMales;
		allowedServicesMap = new HashMap<Sextype, Boolean>(allowedServices.allowedServicesMap);
	}
	
	public boolean isAllowed(Sextype sextype) {
		if (!allowedServicesMap.containsKey(sextype)) {
			allowedServicesMap.put(sextype, true);
		}
		return allowedServicesMap.get(sextype);
	}
	
	public void setAllowed(Sextype sextype, boolean allowed) {
		allowedServicesMap.put(sextype, allowed);
	}
	
	public boolean isServiceMales() {
		return serviceMales;
	}
	
	public void setServiceMales(boolean serviceMales) {
		this.serviceMales = serviceMales;
	}
	
	public boolean isServiceFemales() {
		return serviceFemales;
	}
	
	public void setServiceFemales(boolean serviceFemales) {
		this.serviceFemales = serviceFemales;
	}
	
	public boolean isServiceFutas() {
		return serviceFutas;
	}
	
	public void setServiceFutas(boolean serviceFutas) {
		this.serviceFutas = serviceFutas;
	}
}