package simcity.JesusRestaurant.test.mock;


import restaurant.Menu;
import simcity.cherysrestaurant.interfaces.Cashier;
import simcity.cherysrestaurant.interfaces.Customer;
import simcity.cherysrestaurant.interfaces.Waiter;

public class MockJesusCustomer extends Mock implements JesusCustomer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public JesusCashier cashier;
	public String name;
	public EventLog log = new EventLog();

	public MockJesusCustomer(String n) {
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
