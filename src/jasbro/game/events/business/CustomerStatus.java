package jasbro.game.events.business;

import jasbro.Util;
import jasbro.game.character.Gender;
import jasbro.game.interfaces.Person;
import jasbro.texts.TextUtil;

public enum CustomerStatus {
	 HORNYSTATUS, VERYHORNY, DRUNK, VERYDRUNK, HAPPY, SAD, HYPED, TIRED, LIVELY, PISSED, SHYSTATUS, STRONGSTATUS;
	
	
	private CustomerStatus() {
	}
	

	
	public String getText(final Customer customer) {
		return TextUtil.t(this.toString(), new Person() {
            @Override
            public Gender getGender() {
                return customer.getGender();
            }
            @Override
            public String getName() {
            	
                return CustomerStatus.this.toString();
            	
            }		    
		});
	}


}
