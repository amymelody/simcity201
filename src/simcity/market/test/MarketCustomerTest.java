package simcity.market.test;

import java.util.ArrayList;
import java.util.List;

import simcity.market.MarketCustomerRole;
import simcity.market.MarketCustomerRole.CustomerState;
import simcity.market.gui.MarketCustomerGui;
import simcity.test.mock.MockMarketCashier;
import simcity.ItemOrder;
import junit.framework.*;

public class MarketCustomerTest extends TestCase
{
	// Needed for tests
	MarketCustomerRole customer;
	MarketCustomerGui gui;
	MockMarketCashier cashier;

	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();	
		customer = new MarketCustomerRole();
		gui = new MarketCustomerGui(customer);
		customer.setGui(gui);
		cashier = new MockMarketCashier("MockCashier");
	}


	/**
	 * This tests the first normal scenario (Customer comes in and order a list of items)
	 */
	public void testOneNormalCustomerScenario1()
	{
		// Set up
		customer.setCashier(cashier);
		List<ItemOrder> test1Orders = new ArrayList<ItemOrder>(); // orders Lasagna and Horchata
		test1Orders.add(new ItemOrder("Lasagna", 2));
		test1Orders.add(new ItemOrder("Horchata", 5));

		// Check preconditions for Step 1a
		assertEquals("CustomerState == nothing. It isn't", customer.cS, CustomerState.nothing);		
		assertEquals("CustomerRole should have an empty event log before msgIWantItems(...) is called. Instead, CustomerRole's event log reads: "
				+ customer.log.toString(), 0, customer.log.size());

		// Step 1a - Going to order (Message)
		customer.msgOrderItems(test1Orders);

		// Check postconditions for Step 1a
		assertEquals("CustomerState == arrived. It isn't", customer.cS, CustomerState.arrived);	
		assertFalse("CustomerRole's items should NOT be null. It isn't", customer.items == null);		

		// Check preconditions for Step 1b
		assertEquals("MockCashier should have an empty log befor the scheduler is called. Instead the MockCashier's event log reads: " + cashier.log.toString(), 0, cashier.log.size());

		// Step 1b - Walking to order (Scheduler/Action)
		assertTrue("Customer's scheduler should have returned true, but didn't.", customer.pickAndExecuteAnAction());

		// Check postconditions for Step 1b
		assertEquals("CustomerState == walking. It isn't", customer.cS, CustomerState.walking);	

		// Step 1c - Arrived at cashier's desk (Message)
		customer.msgAtCashier();

		// Check postconditions for Step 1c
		assertEquals("CustomerState == atCashier. It isn't", customer.cS, CustomerState.atCashier);	

		// Step 2b - Giving order to cashier (Scheduler/Action)
		assertTrue("Customer's scheduler should have returned true, but didn't.", customer.pickAndExecuteAnAction());

		// Check postconditions for Step 2b
		assertEquals("CustomerState == confirming. It isn't", customer.cS, CustomerState.confirming);
		assertTrue("Customer should have ordered to cashier. Cashier should have a log that reads: Received order. Instead it reads: " + cashier.log.toString(), cashier.log.getLastLoggedEvent().getMessage() == "Received order");

		// Step 3a - Receive Cashier's confirmation (Message)
		customer.msgHereIsWhatICanFulfill(test1Orders, true);

		// Check postconditions for Step 3a
		assertEquals("CustomerState == waiting. It isn't", customer.cS, CustomerState.waiting);

		// Step 3b -  Going to waiting area (Scheduler/Action)
		assertTrue("Customer's scheduler should have returned true, but didn't.", customer.pickAndExecuteAnAction());

		// Check postconditions for Step 3b
		assertEquals("CustomerState == walking. It isn't", customer.cS, CustomerState.walking);
	
		// Step 4a - Order Ready (Message)
		customer.msgHereAreItemsandPrice(test1Orders, 40);

		// Check postconditions for Step 4a
		assertEquals("CustomerState == getting. It isn't", customer.cS, CustomerState.getting);
		assertEquals("Customer should have cost == 40. It doesn't.", customer.cost, 40);

		// Step 4b - Give payment to Cashier (Scheduler/Action)
		assertTrue("Customer's scheduler should have returned true, but didn't.", customer.pickAndExecuteAnAction());

		// Check postconditions for Step 4b
		assertEquals("CustomerState == paying. It isn't", customer.cS, CustomerState.paying);
		assertTrue("Customer should have ordered to cashier. Cashier should have a log that reads: Received payment. Instead it reads: " + cashier.log.toString(), cashier.log.getLastLoggedEvent().getMessage() == "Received payment");
		assertEquals("Customer should have cost == 0. It doesn't.", customer.cost, 0);

		// Step 5a - Receive change from Cashier (Message)
		customer.msgThankYou(0);

		// Check postconditions for Step 5a
		assertEquals("CustomerState == leaving. It isn't", customer.cS, CustomerState.leaving);

		// Step 5b - Leave Market (Scheduler/Action)
		assertTrue("Customer's scheduler should have returned true, but didn't.", customer.pickAndExecuteAnAction());

		// Check postconditions for Step 5b
		assertEquals("CustomerState == done. It isn't", customer.cS, CustomerState.done);

	} // End of Test 1

	/**
	 * This tests the first normal scenario (Customer comes in and order a list of items) when out of ALL items wanted
	 */
	public void testTwoNormalCustomerOutOfItemsScenario1()
	{
		// Set up
		customer.setCashier(cashier);
		List<ItemOrder> test2Orders = new ArrayList<ItemOrder>(); // orders Lasagna and Horchata
		test2Orders.add(new ItemOrder("Lasagna", 2));
		test2Orders.add(new ItemOrder("Horchata", 5));

		// Check preconditions for Step 1a
		assertEquals("CustomerState == nothing. It isn't", customer.cS, CustomerState.nothing);		
		assertEquals("CustomerRole should have an empty event log before msgIWantItems(...) is called. Instead, CustomerRole's event log reads: "
				+ customer.log.toString(), 0, customer.log.size());

		// Step 1a - Going to order (Message)
		customer.msgOrderItems(test2Orders);

		// Check postconditions for Step 1a
		assertEquals("CustomerState == arrived. It isn't", customer.cS, CustomerState.arrived);	
		assertFalse("CustomerRole's items should NOT be null. It isn't", customer.items == null);		

		// Check preconditions for Step 1b
		assertEquals("MockCashier should have an empty log befor the scheduler is called. Instead the MockCashier's event log reads: " + cashier.log.toString(), 0, cashier.log.size());

		// Step 1b - Walking to order (Scheduler/Action)
		assertTrue("Customer's scheduler should have returned true, but didn't.", customer.pickAndExecuteAnAction());

		// Check postconditions for Step 1b
		assertEquals("CustomerState == walking. It isn't", customer.cS, CustomerState.walking);	

		// Step 1c - Arrived at cashier's desk (Message)
		customer.msgAtCashier();

		// Check postconditions for Step 1c
		assertEquals("CustomerState == atCashier. It isn't", customer.cS, CustomerState.atCashier);	

		// Step 2b - Giving order to cashier (Scheduler/Action)
		assertTrue("Customer's scheduler should have returned true, but didn't.", customer.pickAndExecuteAnAction());

		// Check postconditions for Step 2b
		assertEquals("CustomerState == confirming. It isn't", customer.cS, CustomerState.confirming);
		assertTrue("Customer should have ordered to cashier. Cashier should have a log that reads: Received order. Instead it reads: " + cashier.log.toString(), cashier.log.getLastLoggedEvent().getMessage() == "Received order");

		// Step 3a - Receive Cashier's confirmation (Message)
		customer.msgHereIsWhatICanFulfill(test2Orders, false);

		// Check postconditions for Step 3a
		assertEquals("CustomerState == leaving. It isn't", customer.cS, CustomerState.leaving);

		// Step 3b - Leave Market (Scheduler/Action)
		assertTrue("Customer's scheduler should have returned true, but didn't.", customer.pickAndExecuteAnAction());

		// Check postconditions for Step 3b
		assertEquals("CustomerState == done. It isn't", customer.cS, CustomerState.done);
		
	} // End of Test 2

}
