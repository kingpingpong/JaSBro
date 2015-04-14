package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.texts.TextUtil;

public class EquipmentChangeCustomerTypeSatisfaction extends EquipmentEffect {
	private CustomerType customerType;
	private int amount;

	@Override
	public EquipmentEffectType getType() {
		return EquipmentEffectType.CHANGECUSTOMERTYPESATISFACTION;
	}

	@Override
	public void handleEvent(MyEvent e, Charakter character) {
		if (e.getType() == EventType.ACTIVITY && customerType != null) {
			RunningActivity activity = (RunningActivity) e.getSource();
			if (activity instanceof BusinessMainActivity) {
				BusinessMainActivity businessMainActivity = (BusinessMainActivity) activity;
				if (businessMainActivity.getMainCustomers().size() == 1) {
					Customer customer = activity.getMainCustomer();
					if (customer.getType() == customerType) {
						customer.addToSatisfaction(amount, "Equipment");
					}
				}
			}
		}
	}

	public CustomerType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(CustomerType customerType) {
		this.customerType = customerType;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
    @Override
    public String getDescription() {
        if (customerType == null) {
            return "";
        }
        else if (amount < 0) {
            return TextUtil.t("equipment.customerSatisfactionMinus", new Object[]{customerType.getText(), 
                    amount});
        }
        else {
            return TextUtil.t("equipment.customerSatisfactionPlus", new Object[]{customerType.getText(), 
                    amount});
        }
    }
    
    
    @Override
    public double getValue() {
        if (customerType == null) {
            return 0;
        }
        else {
            if (customerType != CustomerType.GROUP) {
                return CustomerType.generateCustomer(customerType).getImportance() * 10;
            }
            else {
                return 20 * 10;
            }
        }
    }
    
    @Override
    public int getAmountEffects() {
        return amount;
    }
}
