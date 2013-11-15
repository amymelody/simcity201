package simcity.joshrestaurant.test.mock;

import mock.Mock;
import mock.LoggedEvent;
import simcity.joshrestaurant.interfaces.JoshCashier;
import simcity.joshrestaurant.interfaces.JoshCustomer;

public class MockJoshCustomer extends Mock implements JoshCustomer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public JoshCashier cashier;
	private int charge;

	public MockJoshCustomer(String name) {
		super(name);

	}
	
	public int getCharge() {
		return charge;
	}
	
	public void setCharge(int c) {
		charge = c;
	}
	
	@Override
	public void msgChange(int change) {
		log.add(new LoggedEvent("Received msgChange from cashier. Change = $" + change));
	}
	
	public String toString() {
		return "customer " + getName();
	}

	/*@Override
	public void HereIsYourTotal(double total) {
		log.add(new LoggedEvent("Received HereIsYourTotal from cashier. Total = "+ total));

		if(this.name.toLowerCase().contains("thief")){
			//test the non-normative scenario where the customer has no money if their name contains the string "theif"
			cashier.IAmShort(this, 0);

		}else if (this.name.toLowerCase().contains("rich")){
			//test the non-normative scenario where the customer overpays if their name contains the string "rich"
			cashier.HereIsMyPayment(this, Math.ceil(total));

		}else{
			//test the normative scenario
			cashier.HereIsMyPayment(this, total);
		}
	}

	@Override
	public void HereIsYourChange(double total) {
		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ total));
	}

	@Override
	public void YouOweUs(double remaining_cost) {
		log.add(new LoggedEvent("Received YouOweUs from cashier. Debt = "+ remaining_cost));
	}*/

}
