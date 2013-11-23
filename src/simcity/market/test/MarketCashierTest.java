package simcity.market.test;

import java.util.ArrayList;
import java.util.List;

import simcity.market.MarketCashierRole;
import simcity.market.Order.OrderState;
import simcity.market.test.mock.MockMarketCustomer;
import simcity.market.test.mock.MockMarketDeliverer;
import simcity.market.test.mock.MockMarketEmployee;
import simcity.ItemOrder;
import junit.framework.*;

public class MarketCashierTest extends TestCase
{
	// Needed for tests
	MarketCashierRole cashier;
	MockMarketDeliverer deliverer1, deliverer2;
	MockMarketEmployee employee1, employee2;
	MockMarketCustomer customer1, customer2;

	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new MarketCashierRole("Cashier");
		customer1 = new MockMarketCustomer("MockCustomer");
		customer1 = new MockMarketCustomer("MockCustomer");
		deliverer1 = new MockMarketDeliverer("MockDeliverer1");
		deliverer2 = new MockMarketDeliverer("MockDeliverer1");
		employee1 = new MockMarketEmployee("MockEmployee1");
		employee2 = new MockMarketEmployee("MockEmployee2");
	}
	
	
	/**
	 * This tests the first normal scenario (Customer comes in and order a list of items)
	 */
	public void testOneNormalCustomerScenario1()
	{
		// Set up
		customer1.cashier = cashier;
		employee1.cashier = cashier;
		cashier.setEmployees(employee1, 10, true, true);
		cashier.setEmployees(employee2, 10, true, false);
		cashier.setMarketState(true);
		cashier.setSalary(10);
		cashier.inventory.setAmount("Lasagna", 10);
		cashier.inventory.setAmount("Horchata", 10);
		List<ItemOrder> test1Orders = new ArrayList<ItemOrder>(); // orders Lasagna and Horchata
		test1Orders.add(new ItemOrder("Lasagna", 2));
		test1Orders.add(new ItemOrder("Horchata", 5));
		customer1.items = test1Orders;
		
		// Check preconditions for Step 1a
		assertEquals("Cashier should have no orders in List orders. It doesn't.", cashier.orders.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before msgIWantItems(...) is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());

		// Step 1a - Receiving an order (Message)
		cashier.msgIWantItems(customer1, test1Orders);
		
		// Check postconditions for Step 1a
		assertEquals("Cashier should have one order in List orders. It doesn't.", cashier.orders.size(), 1);
		
		// Check preconditions for Step 1b
		assertEquals("MockEmployee should have an empty log befor the scheduler is called. Instead the MockEmployee's event log reads: " + employee1.log.toString(), 0, employee1.log.size());

		// Step 1b - Sending the order to an employee (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 1b
		assertTrue("Cashier should have an order in List orders in which OrderState == handing. It doesn't.", cashier.orders.get(0).getOS() == OrderState.handing);
		assertTrue("Cashier should have sent order to employee1. Employee1 should have a log that reads: Received order. Instead it reads: " + employee1.log.toString(), employee1.log.getLastLoggedEvent().getMessage() == "Received order.");
		assertEquals("Lasagna inventory goes down by 2. Inventory amount should be 8. It isn't.", cashier.inventory.getAmount("Lasagna"), (Integer) 8);
		assertEquals("Horchata inventory goes down by 5. Inventory amount should be 5. It isn't.", cashier.inventory.getAmount("Horchata"), (Integer) 5);
		
		// Step 2a - Receiving items from Employee (Message)
		cashier.msgHereAreItems(employee1.testOrder, employee1);
		
		// Check postconditions for Step 2a
		assertEquals("Cashier should have one order in List orders. It doesn't.", cashier.orders.size(), 1);
		
		
	} // End of Test 1
}
