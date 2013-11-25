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
		
		// Step 3b - Hand Customer's change (Scheduler/Action)

		// Check postconditions for Step 3b

	} // End of Test 1

	/**
	 * This tests the first normal scenario (Customer comes in and order a list of items) when out of ALL items wanted
	 */
	public void testTwoNormalCustomerOutOfItemsScenario1()
	{
		// Set up
		rCook1.cashier = cashier;
		deliverer1.cashier = cashier;
		cashier.setDeliverers(deliverer1, 10, true);
		cashier.setDeliverers(deliverer2, 10, false);
		cashier.setMarketState(true);
		cashier.setSalary(10);
		cashier.setMarketMoney(50);
		cashier.inventory.setAmount("Pizza", 10);
		cashier.inventory.setAmount("Burger", 10);
		List<ItemOrder> test2Orders = new ArrayList<ItemOrder>(); // orders Pizza and Burgers
		test2Orders.add(new ItemOrder("Pizza", 10));
		test2Orders.add(new ItemOrder("Burger", 1));
		rCook1.items = test2Orders;

		// Check preconditions for Step 1a
		assertEquals("Cashier should have no orders in List orders. It doesn't.", cashier.orders.size(), 0);		
		assertEquals("CustomerRole should have an empty event log before msgIWantDelivery(...) is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());

		// Step 1a - Receiving a delivery order (Message)
		cashier.msgIWantDelivery(rCook1, rCashier1, test2Orders, "Josh's Restaurant");

		// Check postconditions for Step 1a
		assertEquals("Cashier should have one order in List orders. It doesn't.", cashier.orders.size(), 1);
		assertFalse("Cashier should have one order in List orders for delivery. It doesn't", cashier.orders.get(0).location == null);

		// Check preconditions for Step 1b
		assertEquals("MockDeliverer should have an empty log befor the scheduler is called. Instead the MockDeliverer's event log reads: " + deliverer1.log.toString(), 0, deliverer1.log.size());
		assertEquals("RCook1 should have an empty log befor the scheduler is called. Instead the rCook1 event log reads: " + rCook1.log.toString(), 0, rCook1.log.size());

		// Step 1b - Confirming order to a restaurant cook/Sending the order to a deliverer (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 1b
		assertTrue("Cashier should have an order in List orders in which OrderState == handing. It doesn't.", cashier.orders.get(0).getOS() == OrderState.handing);
		assertTrue("Cashier should have sent order to deliverer1. Deliverer1 should have a log that reads: Received order. Instead it reads: " + deliverer1.log.toString(), deliverer1.log.getLastLoggedEvent().getMessage() == "Received order");
		assertTrue("Cashier should have sent confirmation to rCook1. RCook1 should have a log that reads: Confirmation of (at least partial) delivery. Instead it reads: " + rCook1.log.toString(), rCook1.log.getLastLoggedEvent().getMessage() == "Confirmation of (at least partial) delivery");
		assertEquals("Pizza inventory goes down by 10. Inventory amount should be 0. It isn't.", cashier.inventory.getAmount("Pizza"), (Integer) 0);
		assertEquals("Burger inventory goes down by 1. Inventory amount should be 9. It isn't.", cashier.inventory.getAmount("Burger"), (Integer) 9);
		assertEquals("Cashier should have an order in List orders in which price == $58. It doesn't.", cashier.orders.get(0).price, 58);

		// Step 2a - Receiving money from Deliverer to confirm order (Message)
		cashier.msgDelivered(deliverer1.orders.get(0), deliverer1);

		// Check postconditions for Step 2a
		assertTrue("Cashier should have one order in List orders in which OrderState == delivered. It doesn't.", cashier.orders.get(0).getOS() == OrderState.delivered);

		// Step 2b - Updating MarketMoney (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 2b
		assertEquals("Cashier should have no orders in List orders. It doesn't.", cashier.orders.size(), 0);
		assertEquals("marketMoney should total up to $108. It doesn't", cashier.viewMarketMoney(), 108);

	} // End of Test 2

}
