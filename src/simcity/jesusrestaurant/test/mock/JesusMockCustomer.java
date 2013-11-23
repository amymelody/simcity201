package simcity.jesusrestaurant.test.mock;

import simcity.jesusrestaurant.JesusMenu;
import simcity.jesusrestaurant.interfaces.JesusCashier;
import simcity.jesusrestaurant.interfaces.JesusCustomer;
import simcity.jesusrestaurant.interfaces.JesusWaiter;
import simcity.mock.EventLog;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;

public class JesusMockCustomer extends Mock implements JesusCustomer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public JesusCashier cashier;
	public String name;
	public EventLog log = new EventLog();

	public JesusMockCustomer(String n) {
		super(n);

		name = n;
	}

	@Override
	public void msgSitAtTable(JesusWaiter w, int tNum, JesusMenu menu) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent ("Sitting"));
	}

	@Override
	public void msgTakeOrder() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent ("Taking order"));
	}

	@Override
	public void msgRetakeOrder(JesusMenu m) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent ("Order retook"));
	}

	@Override
	public void msgNoFood() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent ("No food"));
	}

	@Override
	public void msgEnjoyOrder(String choice) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent ("Eating"));
	}

	@Override
	public void msgHereIsCheck(double aDue) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent ("Received check"));
	}

	@Override
	public void msgChange(double change) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent ("Change received"));
	}

	@Override
	public void msgPayNextTime(double amount) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent ("Not enough money"));
	}

	

}
