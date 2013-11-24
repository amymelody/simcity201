package simcity.market.test;

import java.util.ArrayList;
import java.util.List;

import simcity.market.MarketCashierRole;
import simcity.market.Order.OrderState;
import simcity.market.test.mock.MockMarketCustomer;
import simcity.market.test.mock.MockMarketDeliverer;
import simcity.market.test.mock.MockMarketEmployee;
import simcity.market.test.mock.MockRestCashier;
import simcity.market.test.mock.MockRestCook;
import simcity.ItemOrder;
import simcity.RestCashierRole;
import simcity.RestCookRole;
import junit.framework.*;

public class MarketCashierTest extends TestCase
{
	// Needed for tests
	MarketCashierRole cashier;
	MockMarketDeliverer deliverer1, deliverer2;
	MockMarketEmployee employee1, employee2;
	MockMarketCustomer customer1, customer2;
	MockRestCashier rCashier1, rCashier2;
	MockRestCook rCook1, rCook2;

	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new MarketCashierRole();
		customer1 = new MockMarketCustomer("MockCustomer");
		customer2 = new MockMarketCustomer("MockCustomer");
		deliverer1 = new MockMarketDeliverer("MockDeliverer1");
		deliverer2 = new MockMarketDeliverer("MockDeliverer1");
		employee1 = new MockMarketEmployee("MockEmployee1");
		employee2 = new MockMarketEmployee("MockEmployee2");
		rCook1 = new MockRestCook("MockRestCook2");
		rCook1 = new MockRestCook("MockRestCook2");
		rCashier1 = new MockRestCashier("MockRestCashier1");
		rCashier2 = new MockRestCashier("MockRestCashier2");
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
		cashier.setMarketMoney(50);
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
		assertEquals("Employee1 should now be occupied. Employee1 isn't.", cashier.employees.get(0).unoccupied, false);
		
		// Step 2a - Receiving items from Employee (Message)
		cashier.msgHereAreItems(employee1.testOrder, employee1);
		
		// Check postconditions for Step 2a
		assertTrue("Cashier should have one order in List orders in which OrderState == ready. It doesn't.", cashier.orders.get(0).getOS() == OrderState.ready);
		assertEquals("Employee1 should now be unoccupied. Employee1 isn't.", cashier.employees.get(0).unoccupied, true);
		
		// Step 2b - Letting Customer know Order is ready (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
		// Check postconditions for Step 2b
		assertTrue("Cashier should have an order in List orders in which OrderState == paying. It doesn't.", cashier.orders.get(0).getOS() == OrderState.paying);
		assertTrue("Cashier should have called customer1"
				+ " to pick up order. Customer1 should have a log that reads: Received correct items and payment. Instead it reads: " + customer1.log.toString(), customer1.log.getLastLoggedEvent().getMessage() == "Received correct items and payment");
		assertEquals("Cashier should have an order in List orders in which price == $35. It doesn't.", cashier.orders.get(0).price, 35);
		
		// Step 3a - Take Customer's payment (Message)
		cashier.msgPayment(customer1, 40);
		
		// Check postconditions for Step 3a
		assertEquals("Cashier should have one order in List orders in which amountPaid == $40. It doesn't.", cashier.orders.get(0).amountPaid, 40);
		assertEquals("Cashier should have one order in List orders in which change == $5. It doesn't.", cashier.orders.get(0).change, 5);
		
		// Step 3b - Hand Customer's change (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
		// Check postconditions for Step 3b
		assertEquals("Cashier should have no orders in List orders. It doesn't.", cashier.orders.size(), 0);		
		assertTrue("Cashier should have handed customer1's"
				+ " change. Customer1 should have a log that reads: Received change. Instead it reads: " + customer1.log.toString(), customer1.log.getLastLoggedEvent().getMessage() == "Received change");
		assertEquals("marketMoney should total up to $85. It doesn't", cashier.viewMarketMoney(), 85);
		
	} // End of Test 1
	
	/**
	 * This tests the second normal scenario (Delivery order from cook)
	 */
	public void testTwoNormalCustomerScenario2()
	{
		// Set up
		customer1.cashier = cashier;
		deliverer1.cashier = cashier;
		cashier.setDeliverers(deliverer1, 10, true, true);
		cashier.setDeliverers(deliverer2, 10, true, false);
		cashier.setMarketState(true);
		cashier.setSalary(10);
		cashier.setMarketMoney(50);
		cashier.inventory.setAmount("Pizza", 10);
		cashier.inventory.setAmount("Burger", 10);
		List<ItemOrder> test2Orders = new ArrayList<ItemOrder>(); // orders Lasagna and Horchata
		test2Orders.add(new ItemOrder("Pizza", 10));
		test2Orders.add(new ItemOrder("Burger", 1));
		rCook1.items = test2Orders;
		
		// Check preconditions for Step 1a
		assertEquals("Cashier should have no orders in List orders. It doesn't.", cashier.orders.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before msgIWantDelivery(...) is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());

		// Step 1a - Receiving a delivery order (Message)
		cashier.msgIWantDelivery(rCook1, rCashier1, test2Orders, "Josh's Restaurant");
		
		// Check postconditions for Step 1a
		assertEquals("Cashier should have one order in List orders. It doesn't.", cashier.orders.size(), 1);
		assertFalse("Cashier should have one order in List orders for delivery. It doesn't", cashier.orders.get(0).location == null);
		
		// Check preconditions for Step 1b
		assertEquals("MockDeliverer should have an empty log befor the scheduler is called. Instead the MockDeliverer's event log reads: " + deliverer1.log.toString(), 0, deliverer1.log.size());
		assertEquals("MockRestCook should have an empty log befor the scheduler is called. Instead the MockRestCook's event log reads: " + rCook1.log.toString(), 0, rCook1.log.size());
		
		// Step 1b - Confirming order to a restaurant cook/Sending the order to a deliverer (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 1b
		assertTrue("Cashier should have an order in List orders in which OrderState == handing. It doesn't.", cashier.orders.get(0).getOS() == OrderState.handing);
		assertTrue("Cashier should have sent order to deliverer1. Deliverer1 should have a log that reads: Received order. Instead it reads: " + deliverer1.log.toString(), deliverer1.log.getLastLoggedEvent().getMessage() == "Received order");
		assertTrue("Cashier should have sent confirmation to rCook1. RCook1 should have a log that reads: Confirmation of (at least partial) delivery. Instead it reads: " + rCook1.log.toString(), rCook1.log.getLastLoggedEvent().getMessage() == "Confirmation of (at least partial) delivery");
		assertEquals("Pizza inventory goes down by 10. Inventory amount should be 0. It isn't.", cashier.inventory.getAmount("Pizza"), (Integer) 0);
		assertEquals("Burger inventory goes down by 1. Inventory amount should be 9. It isn't.", cashier.inventory.getAmount("Burger"), (Integer) 9);
		assertEquals("Cashier should have an order in List orders in which price == $58. It doesn't.", cashier.orders.get(0).price, 58);
		assertEquals("Deliverer1 should now be unoccupied. Deliverer1 isn't.", cashier.deliverers.get(0).unoccupied, false);
		
		// Step 2a - Receiving money from Deliverer to confirm order (Message)
		cashier.msgDelivered(deliverer1.orders.get(0), deliverer1);
		
		// Check postconditions for Step 2a
		assertTrue("Cashier should have one order in List orders in which OrderState == delivered. It doesn't.", cashier.orders.get(0).getOS() == OrderState.delivered);
		assertEquals("Deliverer1 should now be unoccupied. Deliverer1 isn't.", cashier.deliverers.get(0).unoccupied, true);
		
		// Step 2b - Letting Customer know Order is ready (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
		// Check postconditions for Step 2b
		assertEquals("Cashier should have no orders in List orders. It doesn't.", cashier.orders.size(), 0);
		assertEquals("marketMoney should total up to $108. It doesn't", cashier.viewMarketMoney(), 108);
		
	} // End of Test 2
}
