package simcity.market.test.mock;

import simcity.market.interfaces.Customer;

public class MockCashier extends Mock implements Customer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public String name;
	public EventLog log = new EventLog();

	public MockCashier(String n) {
		super(n);

		name = n;
	}

	@Override
	public void msgSitAtTable(Waiter w, int tNum, Menu menu) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent ("Sitting"));
	}

	@Override
	public void msgTakeOrder() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent ("Taking order"));
	}

	@Override
	public void msgRetakeOrder(Menu m) {
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
