package simcity.market.test;

import java.util.ArrayList;
import java.util.List;

import simcity.ItemOrder;
import simcity.market.MarketDelivererRole;
import simcity.market.MarketDelivererRole.DelivererState;
import simcity.market.Order;
import simcity.market.Order.OrderState;
import simcity.market.gui.MarketDelivererGui;
import simcity.market.test.mock.MockPerson;
import simcity.test.mock.MockMarketCashier;
import simcity.test.mock.MockRestCashier;
import simcity.test.mock.MockRestCook;
import junit.framework.*;

public class MarketDelivererTest extends TestCase
{
	// Needed for tests
	MarketDelivererRole deliverer;
	MarketDelivererGui gui;
	MockMarketCashier cashier;
	MockRestCook rCook1, rCook2;
	MockRestCashier rCashier1, rCashier2;
	MockPerson person;

	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();	
		deliverer = new MarketDelivererRole();
		gui = new MarketDelivererGui(deliverer);
		deliverer.setGui(gui);
		cashier = new MockMarketCashier("MockCashier");
		rCook1 = new MockRestCook("MockRestCook1");
		rCook2 = new MockRestCook("MockRestCook2");
		rCashier1 = new MockRestCashier("MockRestCashier1");
		rCashier2 = new MockRestCashier("MockRestCashier2");
		person = new MockPerson("MockPerson");
		deliverer.setPerson(person);
		deliverer.setUnitTest(true);
	}


	/**
	 * This tests the second normal scenario (Delivery order from cook)
	 */
	public void testOneNormalCustomerScenario2()
	{
		// Set up
		deliverer.setCashier(cashier);
		deliverer.working = true;
		List<ItemOrder> test1Orders = new ArrayList<ItemOrder>(); // orders Lasagna and Horchata
		Order test1Order = new Order(rCook1, rCashier1, test1Orders, "Josh's Restaurant");
		test1Orders.add(new ItemOrder("Lasagna", 2));
		test1Orders.add(new ItemOrder("Horchata", 5));
		test1Order.price = 35;

		// Check preconditions for Step 1a
		assertEquals("Deliverer should have no orders in List orders. It doesn't.", deliverer.orders.size(), 0);		
		assertEquals("DelivererRole should have an empty event log before msgDeliverItems(...) is called. Instead, the Deliverer's event log reads: "
				+ deliverer.log.toString(), 0, deliverer.log.size());
		
		// Step 1a - Going to order (Message)
		deliverer.msgDeliverItems(test1Order);

		// Check postconditions for Step 1a
		assertEquals("Deliverer should have one order in List orders. It doesn't.", deliverer.orders.size(), 1);
		assertTrue("Deliverer should have one order in List orders in which OrderState == newDelivery. It doesn't.", deliverer.orders.get(0).getOS() == OrderState.newDelivery);
		assertFalse("Deliverer should have one order in List orders for delivery. It doesn't", deliverer.orders.get(0).location == null);
		
		// Check preconditions for Step 1b
		assertEquals("MockCashier should have an empty log befor the scheduler is called. Instead the MockCashier's event log reads: " + cashier.log.toString(), 0, cashier.log.size());
		assertEquals("MockRestCook should have an empty log befor the scheduler is called. Instead the MockRestCook's event log reads: " + rCook1.log.toString(), 0, rCook1.log.size());
		assertEquals("MockRestCashier should have an empty log befor the scheduler is called. Instead the MockRestCashier's event log reads: " + rCashier1.log.toString(), 0, rCashier1.log.size());

		// Step 1b - Walking to order (Scheduler/Action)
		assertTrue("Deliverer's scheduler should have returned true, but didn't.", deliverer.pickAndExecuteAnAction());

		// Check postconditions for Step 1b
		assertTrue("Deliverer's current order should be test1Order. It isn't.", deliverer.currentOrder == deliverer.orders.get(0));
				
		// Step 1c - Arrived at cashier's desk (Message)
		deliverer.msgArrived();

		// Check postconditions for Step 1c
		assertEquals("DelivererState == arrived. It isn't", deliverer.dS, DelivererState.arrived);	
		
		// Step 2b - Giving order to cashier (Scheduler/Action)
		assertTrue("Deliverer's scheduler should have returned true, but didn't.", deliverer.pickAndExecuteAnAction());

		// Check postconditions for Step 2b
		assertEquals("Deliverer has one order in List orders in which OrderState == ready. It doesn't.", deliverer.orders.get(0).getOS(), OrderState.ready);
		assertEquals("Deliverer's currentOrder has OrderState == ready. It doesn't.", deliverer.currentOrder.getOS(), OrderState.ready);
		assertTrue("RCook1 should have received order from Deliverer. RCook1 should have a log that reads: Received delivery. Instead it reads: " + rCook1.log.toString(), rCook1.log.getLastLoggedEvent().getMessage() == "Received delivery");
		assertTrue("RCashier1 should have received bill from Deliverer. RCashier1 should have a log that reads: Received market bill. Instead it reads: " + rCashier1.log.toString(), rCashier1.log.getLastLoggedEvent().getMessage() == "Received market bill");

		// Step 3a - Receive Cashier's confirmation (Message)
		deliverer.msgPayment(rCashier1, 40);
		
		// Check postconditions for Step 3a
		assertEquals("Deliverer has one order in List orders in which OrderState == paying. It doesn't.", deliverer.orders.get(0).getOS(), OrderState.paying);
		assertEquals("Deliverer's currentOrder has OrderState == paying. It doesn't.", deliverer.currentOrder.getOS(), OrderState.paying);
		assertEquals("Deliverer has one order in List orders in which amountPaid == 40. It doesn't.", deliverer.currentOrder.amountPaid, 40);
		
		// Step 3b -  Going to waiting area (Scheduler/Action)
		assertTrue("Deliverer's scheduler should have returned true, but didn't.", deliverer.pickAndExecuteAnAction());

		// Check postconditions for Step 3b
		assertEquals("Deliverer has one order in List orders in which OrderState == paid. It doesn't.", deliverer.orders.get(0).getOS(), OrderState.paid);
		assertEquals("Deliverer's currentOrder has OrderState == paid. It doesn't.", deliverer.currentOrder.getOS(), OrderState.paid);
		assertEquals("Deliverer has one order in List orders in which change == 5. It doesn't.", deliverer.currentOrder.change, 5);
		assertTrue("RCashier1 should have received change from Deliverer. RCashier1 should have a log that reads: Received change. Instead it reads: " + rCashier1.log.toString(), rCashier1.log.getLastLoggedEvent().getMessage() == "Received change");
		
		// Step 4a - Order Ready (Message)
		deliverer.msgArrivedBack();

		// Check postconditions for Step 4a
		assertEquals("DelivererState == arrivedBack. It isn't", deliverer.dS, DelivererState.arrivedBack);

		// Step 4b - Give payment to Cashier (Scheduler/Action)
		assertTrue("Deliverer's scheduler should have returned true, but didn't.", deliverer.pickAndExecuteAnAction());

		// Check postconditions for Step 4b
		assertTrue("Cashier should have received change from Deliverer. Cashier should have a log that reads: Received deliver order to finish. Instead it reads: " + cashier.log.toString(), cashier.log.getLastLoggedEvent().getMessage() == "Received deliver order to finish");
		assertTrue("Deliverer's currentOrder should be null. It isn't.", deliverer.currentOrder == null);
		assertTrue("Deliverer should have no orders in List orders. It doesn't.", deliverer.orders.size() == 0);
		
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
