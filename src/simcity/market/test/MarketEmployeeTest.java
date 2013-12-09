package simcity.market.test;

import java.util.ArrayList;
import java.util.List;

import simcity.ItemOrder;
import simcity.market.MarketEmployeeRole;
import simcity.market.MarketEmployeeRole.EmployeeState;
import simcity.market.Order;
import simcity.market.Order.OrderState;
import simcity.market.gui.MarketEmployeeGui;
import simcity.market.test.mock.MockPerson;
import simcity.test.mock.MockMarketCashier;
import simcity.test.mock.MockMarketCustomer;
import junit.framework.*;

public class MarketEmployeeTest extends TestCase
{
	// Needed for tests
	MarketEmployeeRole employee;
	MarketEmployeeGui gui;
	MockMarketCashier cashier;
	MockMarketCustomer customer1, customer2;
	MockPerson person;

	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();	
		employee = new MarketEmployeeRole();
		gui = new MarketEmployeeGui(employee);
		employee.setGui(gui);
		cashier = new MockMarketCashier("MockCashier");
		customer1 = new MockMarketCustomer("MockMarketCustomer1");
		customer2 = new MockMarketCustomer("MockMarketCustomer2");
		person = new MockPerson("MockPerson");
		employee.setPerson(person);
		employee.setUnitTest(true);
	}


	/**
	 * This tests the first normal scenario ()
	 */
	public void testOneNormalCustomerScenario2()
	{
		// Set up
		employee.setCashier(cashier);
		employee.working = true;
		List<ItemOrder> test1Orders = new ArrayList<ItemOrder>(); // orders Lasagna and Horchata
		Order test1Order = new Order(customer1, test1Orders);
		test1Orders.add(new ItemOrder("Lasagna", 2));
		test1Orders.add(new ItemOrder("Horchata", 5));
		test1Order.price = 35;

		// Check preconditions for Step 1a
		assertEquals("Employee should have no orders in List orders. It doesn't.", employee.orders.size(), 0);		
		assertEquals("EmployeeRole should have an empty event log before msgDeliverItems(...) is called. Instead, the Employee's event log reads: "
				+ employee.log.toString(), 0, employee.log.size());

		// Step 1a - Receiving order (Message)
		employee.msgGetItems(test1Order);

		// Check postconditions for Step 1a
		assertEquals("Employee should have one order in List orders. It doesn't.", employee.orders.size(), 1);
		assertTrue("Employee should have one order in List orders in which OrderState == none. It doesn't.", employee.orders.peek().getOS() == OrderState.none);
		assertTrue("Employee should have one order in List orders NOT for delivery. It doesn't", employee.orders.peek().location == null);

		// Check preconditions for Step 1b
		assertEquals("MockCashier should have an empty log befor the scheduler is called. Instead the MockCashier's event log reads: " + cashier.log.toString(), 0, cashier.log.size());
		assertEquals("MockCustomer should have an empty log befor the scheduler is called. Instead the MockRestCook's event log reads: " + customer1.log.toString(), 0, customer1.log.size());
		assertTrue("Employee's state should be nothing. It isn't.", employee.eS == EmployeeState.nothing);
		assertTrue("Current order is null. It isn't.", employee.currentOrder == null);

		// Step 1b - Retrieving items (Scheduler/Action)
		assertTrue("Employee's scheduler should have returned true, but didn't.", employee.pickAndExecuteAnAction());
		assertEquals("EmployeeState == getting. It isn't", employee.eS, EmployeeState.getting);	

		// Check postconditions for Step 1b
		assertTrue("Employee's current order should be test1Order. It isn't.", employee.currentOrder.customer == test1Order.customer);

		// Step 1c - Items are now retrieved (Message)
		employee.msgHaveItems();

		// Check postconditions for Step 1c
		assertEquals("EmployeeState == toCashier. It isn't", employee.eS, EmployeeState.toCashier);	

		// Step 2b - Going to Cashier (Scheduler/Action)
		assertTrue("Employee's scheduler should have returned true, but didn't.", employee.pickAndExecuteAnAction());

		// Check postconditions for Step 2b
		assertEquals("EmployeeState == walking. It isn't", employee.eS, EmployeeState.walking);	

		// Step 3a - Arrived at Cashier (Message)
		employee.msgAtCashier();

		// Check postconditions for Step 3a
		assertEquals("EmployeeState == handing. It isn't", employee.eS, EmployeeState.handing);	

		// Step 3b -  Going to waiting area (Scheduler/Action)
		assertTrue("Employee's scheduler should have returned true, but didn't.", employee.pickAndExecuteAnAction());

		// Check postconditions for Step 3b
		assertEquals("EmployeeState == nothing. It isn't", employee.eS, EmployeeState.nothing);
		assertTrue("Cashier should have received order from Employee. Cashier should have a log that reads: Received items. Instead it reads: " + cashier.log.toString(), cashier.log.getLastLoggedEvent().getMessage() == "Received items");
		assertTrue("Employee's currentOrder should be null. It isn't.", employee.currentOrder == null);
		assertTrue("Employee should have no orders in List orders. It doesn't.", employee.orders.size() == 0);

		// Step 3b -  Going to waiting area (Scheduler/Action)
		assertTrue("Employee's scheduler should have returned false, but didn't.", !employee.pickAndExecuteAnAction());

	} // End of Test 1	

	/**
	 * This tests the second normal scenario (Delivery order from cook) with more than one Restaurant
	 */
	public void testTwoNormalCustomerMoreThanOneScenario2()
	{
		// Set up
		employee.setCashier(cashier);
		employee.working = true;
		List<ItemOrder> test1Orders = new ArrayList<ItemOrder>(); // orders Lasagna and Horchata
		Order test1Order = new Order(customer1, test1Orders);
		test1Orders.add(new ItemOrder("Lasagna", 2));
		test1Orders.add(new ItemOrder("Horchata", 5));
		test1Order.price = 35;
		List<ItemOrder> test2Orders = new ArrayList<ItemOrder>(); // orders Lasagna and Horchata
		Order test2Order = new Order(customer2, test2Orders);
		test2Orders.add(new ItemOrder("Pizza", 2));
		test2Orders.add(new ItemOrder("Burger", 5));
		test2Order.price = 50;
		List<ItemOrder> test3Orders = new ArrayList<ItemOrder>(); // orders Lasagna and Horchata
		Order test3Order = new Order(customer2, test2Orders);
		test3Orders.add(new ItemOrder("Chicken", 5));
		test3Orders.add(new ItemOrder("Salad", 5));
		test3Order.price = 65;

		// Check preconditions for Step 1a
		assertEquals("Employee should have no orders in List orders. It doesn't.", employee.orders.size(), 0);		
		assertEquals("EmployeeRole should have an empty event log before msgDeliverItems(...) is called. Instead, the Employee's event log reads: "
				+ employee.log.toString(), 0, employee.log.size());

		// Step 1a - Receiving first Order (Message)
		employee.msgGetItems(test1Order);

		// Check postconditions for Step 1a
		assertEquals("Employee should have one order in List orders. It doesn't.", employee.orders.size(), 1);
		assertTrue("Employee should have one order in List orders in which OrderState == none. It doesn't.", employee.orders.peek().getOS() == OrderState.none);
		assertTrue("Employee should have one order in List orders NOT for delivery. It doesn't", employee.orders.peek().location == null);

		// Check preconditions for Step 1b
		assertEquals("MockCashier should have an empty log befor the scheduler is called. Instead the MockCashier's event log reads: " + cashier.log.toString(), 0, cashier.log.size());
		assertEquals("MockCustomer should have an empty log befor the scheduler is called. Instead the MockRestCook's event log reads: " + customer1.log.toString(), 0, customer1.log.size());
		assertTrue("Employee's state should be nothing. It isn't.", employee.eS == EmployeeState.nothing);
		assertTrue("Current order is null. It isn't.", employee.currentOrder == null);

		// Step 2a - Receiving second Order (Message)
		employee.msgGetItems(test2Order);

		// Check postconditions for Step 2a
		assertEquals("Employee should have two orders in List orders. It doesn't.", employee.orders.size(), 2);
		assertTrue("Employee should have two orders in List orders in which OrderState == none. It doesn't.", employee.orders.peek().getOS() == OrderState.none);
		assertTrue("Employee should have two orders in List orders NOT for delivery. It doesn't", employee.orders.peek().location == null && employee.orders.peek().location == null);

		// Step 1b - Retrieving items (Scheduler/Action)
		assertTrue("Employee's scheduler should have returned true, but didn't.", employee.pickAndExecuteAnAction());	

		// Check postconditions for Step 1b
		assertEquals("EmployeeState == getting. It isn't", employee.eS, EmployeeState.getting);
		assertTrue("Employee's current order should be test1Order. It isn't.", employee.currentOrder.customer == test1Order.customer);

		// Step 3a - Receiving third Order (Message)
		employee.msgGetItems(test3Order);

		// Check postconditions for Step 3a
		assertEquals("Employee should have two orders in List orders. It doesn't.", employee.orders.size(), 2);
		assertTrue("Employee should have two orders in List orders in which OrderState == none. It doesn't.", employee.orders.peek().getOS() == OrderState.none);
		assertTrue("Employee should have two orders in List orders NOT for delivery. It doesn't", employee.orders.peek().location == null);
		assertTrue("Employee's current order should STILL be test1Order. It isn't.", employee.currentOrder.customer == test1Order.customer);

		// Step 3b - Checking Scheduler (Scheduler)
		assertFalse("Employee's scheduler should have returned false, but didn't.", employee.pickAndExecuteAnAction());
		
		// Step 1c - Items are now retrieved (Message)
		employee.msgHaveItems();

		// Check postconditions for Step 1c
		assertEquals("EmployeeState == toCashier. It isn't", employee.eS, EmployeeState.toCashier);	

		// Step 1d - Going to Cashier (Scheduler/Action)
		assertTrue("Employee's scheduler should have returned true, but didn't.", employee.pickAndExecuteAnAction());

		// Check postconditions for Step 1d
		assertEquals("EmployeeState == walking. It isn't", employee.eS, EmployeeState.walking);	

		// Step 1e - Arrived at Cashier (Message)
		employee.msgAtCashier();
		
		// Check postconditions for Step 1e
		assertEquals("EmployeeState == handing. It isn't", employee.eS, EmployeeState.handing);	

		// Step 1f -  Going to waiting area (Scheduler/Action)
		assertTrue("Employee's scheduler should have returned true, but didn't.", employee.pickAndExecuteAnAction());

		// Check postconditions for Step 1f
		assertEquals("EmployeeState == nothing. It isn't", employee.eS, EmployeeState.nothing);
		assertTrue("Cashier should have received order from Employee. Cashier should have a log that reads: Received items. Instead it reads: " + cashier.log.toString(), cashier.log.getLastLoggedEvent().getMessage() == "Received items");
		assertTrue("Employee's currentOrder should be null. It isn't.", employee.currentOrder == null);
		assertTrue("Employee should have two orders in List orders. It doesn't.", employee.orders.size() == 2);
		
		// Step 2b - Retrieving items (Scheduler/Action)
		assertTrue("Employee's scheduler should have returned true, but didn't.", employee.pickAndExecuteAnAction());	
		
		// Check postconditions for Step 2b
		assertEquals("EmployeeState == getting. It isn't", employee.eS, EmployeeState.getting);
		assertTrue("Employee's current order should NOW be test2Order. It isn't.", employee.currentOrder.customer == test2Order.customer);

		// Step 2c - Items are now retrieved (Message)
		employee.msgHaveItems();

		// Check postconditions for Step 2c
		assertEquals("EmployeeState == toCashier. It isn't", employee.eS, EmployeeState.toCashier);	

		// Step 2d - Going to Cashier (Scheduler/Action)
		assertTrue("Employee's scheduler should have returned true, but didn't.", employee.pickAndExecuteAnAction());

		// Check postconditions for Step 2d
		assertEquals("EmployeeState == walking. It isn't", employee.eS, EmployeeState.walking);	

		// Step 2e - Arrived at Cashier (Message)
		employee.msgAtCashier();

		// Check postconditions for Step 2e
		assertEquals("EmployeeState == handing. It isn't", employee.eS, EmployeeState.handing);	

		// Step 2f -  Going to waiting area (Scheduler/Action)
		assertTrue("Employee's scheduler should have returned true, but didn't.", employee.pickAndExecuteAnAction());
		
		// Check postconditions for Step 2f
		assertEquals("EmployeeState == nothing. It isn't", employee.eS, EmployeeState.nothing);
		assertTrue("Cashier should have received order from Employee. Cashier should have a log that reads: Received items. Instead it reads: " + cashier.log.toString(), cashier.log.getLastLoggedEvent().getMessage() == "Received items");
		assertTrue("Employee's currentOrder should be null. It isn't.", employee.currentOrder == null);
		assertTrue("Employee should have one order in List orders. It doesn't.", employee.orders.size() == 1);
		
		// Step 3b - Retrieving items (Scheduler/Action)
		assertTrue("Employee's scheduler should have returned true, but didn't.", employee.pickAndExecuteAnAction());

		// Check postconditions for Step 3b
		assertEquals("EmployeeState == getting. It isn't", employee.eS, EmployeeState.getting);
		assertTrue("Employee's current order should NOW be test3Order. It isn't.", employee.currentOrder.customer == test3Order.customer);

		// Step 3c - Items are now retrieved (Message)
		employee.msgHaveItems();

		// Check postconditions for Step 1c
		assertEquals("EmployeeState == toCashier. It isn't", employee.eS, EmployeeState.toCashier);	

		// Step 3d - Going to Cashier (Scheduler/Action)
		assertTrue("Employee's scheduler should have returned true, but didn't.", employee.pickAndExecuteAnAction());

		// Check postconditions for Step 1d
		assertEquals("EmployeeState == walking. It isn't", employee.eS, EmployeeState.walking);	

		// Step 3e - Arrived at Cashier (Message)
		employee.msgAtCashier();

		// Check postconditions for Step 3e
		assertEquals("EmployeeState == handing. It isn't", employee.eS, EmployeeState.handing);	

		// Step 3f -  Going to waiting area (Scheduler/Action)
		assertTrue("Employee's scheduler should have returned true, but didn't.", employee.pickAndExecuteAnAction());

		// Check postconditions for Step 3f
		assertEquals("EmployeeState == nothing. It isn't", employee.eS, EmployeeState.nothing);
		assertTrue("Cashier should have received order from Employee. Cashier should have a log that reads: Received items. Instead it reads: " + cashier.log.toString(), cashier.log.getLastLoggedEvent().getMessage() == "Received items");
		assertTrue("Employee's currentOrder should be null. It isn't.", employee.currentOrder == null);
		assertTrue("Employee should have no orders in List orders. It doesn't.", employee.orders.size() == 0);

		// Check scheduler
		assertFalse("Employee's scheduler should have returned false, but didn't.", employee.pickAndExecuteAnAction());

	} // End of Test 2

}
