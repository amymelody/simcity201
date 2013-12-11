package simcity.market.test;

import java.util.ArrayList;
import java.util.List;

import simcity.market.MarketCashierRole;
import simcity.market.MarketCashierRole.MarketState;
import simcity.market.Order.OrderState;
import simcity.market.test.mock.MockPerson;
import simcity.test.mock.MockMarketCustomer;
import simcity.test.mock.MockMarketDeliverer;
import simcity.test.mock.MockMarketEmployee;
import simcity.test.mock.MockRestCashier;
import simcity.test.mock.MockRestCook;
import simcity.ItemOrder;
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
	MockPerson person;


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
		rCook1 = new MockRestCook("rCook1");
		rCook2 = new MockRestCook("rCook2");
		rCashier1 = new MockRestCashier("MockRestCashier1");
		rCashier2 = new MockRestCashier("MockRestCashier2");
		person = new MockPerson("MockPerson");
	}


	/**
	 * This tests the first normal scenario (Customer comes in and order a list of items)
	 */
	public void testOneNormalCustomerScenario1()
	{
		// Set up
		cashier.setPerson(person);
		customer1.cashier = cashier;
		employee1.cashier = cashier;
		cashier.setEmployees(employee1, 10, true);
		cashier.setEmployees(employee2, 10, false);
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
		assertEquals("CashierRole should have an empty event log before msgIWantItems(...) is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());

		// Step 1a - Receiving an order (Message)
		cashier.msgIWantItems(customer1, test1Orders);

		// Check postconditions for Step 1a
		assertEquals("Cashier should have one order in List orders. It doesn't.", cashier.orders.size(), 1);

		// Check preconditions for Step 1b
		assertEquals("MockEmployee should have an empty log befor the scheduler is called. Instead the MockEmployee's event log reads: " + employee1.log.toString(), 0, employee1.log.size());
		assertEquals("Customer1 should have an empty log befor the scheduler is called. Instead Customer1's event log reads: " + customer1.log.toString(), 0, customer1.log.size());
		
		// Step 1b - Sending the order to an employee (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 1b
		assertTrue("Cashier should have an order in List orders in which OrderState == handing. It doesn't.", cashier.orders.get(0).getOS() == OrderState.handing);
		assertTrue("Cashier should have sent order to employee1. Employee1 should have a log that reads: Received order. Instead it reads: " + employee1.log.toString(), employee1.log.getLastLoggedEvent().getMessage() == "Received order");
		assertTrue("Cashier should have sent confirmation to customer1. Customer1 should have a log that reads: Confirmation of (at least partial) delivery. Instead it reads: " + customer1.log.toString(), customer1.log.getLastLoggedEvent().getMessage() == "Confirmation of (at least partial) delivery");
		assertEquals("Lasagna inventory goes down by 2. Inventory amount should be 8. It isn't.", cashier.inventory.getAmount("Lasagna"), (Integer) 8);
		assertEquals("Horchata inventory goes down by 5. Inventory amount should be 5. It isn't.", cashier.inventory.getAmount("Horchata"), (Integer) 5);
		
		// Step 2a - Receiving items from Employee (Message)
		cashier.msgHereAreItems(employee1.orders.get(0), employee1);

		// Check postconditions for Step 2a
		assertTrue("Cashier should have one order in List orders in which OrderState == ready. It doesn't.", cashier.orders.get(0).getOS() == OrderState.ready);

		// Step 2b - Letting Customer know Order is ready (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 2b
		assertTrue("Cashier should have an order in List orders in which OrderState == know. It doesn't.", cashier.orders.get(0).getOS() == OrderState.know);
		assertTrue("Cashier should have called customer1"
				+ " to pick up order. Customer1 should have a log that reads: My order is ready. Instead it reads: " + customer1.log.toString(), customer1.log.getLastLoggedEvent().getMessage() == "My order is ready");
		assertEquals("Cashier should have an order in List orders in which price == $35. It doesn't.", cashier.orders.get(0).price, 35);

		// Step 3a - Customer arrives at desk
		cashier.msgImHere(customer1);
		
		// Check postconditions for Step 3a
		assertTrue("Cashier should have one order in List orders in which OrderState == here. It doesn't.", cashier.orders.get(0).getOS() == OrderState.here);
		
		// Step 3b - Hand order to Customer (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
		// Check postconditions for Step 3b
		assertTrue("Cashier should have an order in List orders in which OrderState == paying. It doesn't.", cashier.orders.get(0).getOS() == OrderState.paying);
		
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
		cashier.setPerson(person);
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
		assertEquals("CashierRole should have an empty event log before msgIWantDelivery(...) is called. Instead, the Cashier's event log reads: "
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

	/**
	 * This tests the first normal scenario (Customer comes in and order a list of items) with multiple customers
	 */
	public void testThreeNormalMultipleCustomerScenario1()
	{
		// Set up
		cashier.setPerson(person);
		customer1.cashier = cashier;
		customer2.cashier = cashier;
		employee1.cashier = cashier;
		cashier.setEmployees(employee1, 10, true);
		cashier.setEmployees(employee2, 10, false);
		cashier.setMarketState(true);
		cashier.setSalary(10);
		cashier.setMarketMoney(50);
		cashier.inventory.setAmount("Lasagna", 10);
		cashier.inventory.setAmount("Horchata", 10);
		cashier.inventory.setAmount("Tacos", 10);
		cashier.inventory.setAmount("Garlic Bread", 10);
		List<ItemOrder> test3Orders1 = new ArrayList<ItemOrder>(); // orders Lasagna and Horchata
		List<ItemOrder> test3Orders2 = new ArrayList<ItemOrder>(); // orders Tacos and Garlic Bread
		test3Orders1.add(new ItemOrder("Lasagna", 2));
		test3Orders1.add(new ItemOrder("Horchata", 5));
		test3Orders2.add(new ItemOrder("Tacos", 5));
		test3Orders2.add(new ItemOrder("Garlic Bread", 10));
		customer1.items = test3Orders1;
		customer2.items = test3Orders2;

		// Check preconditions for Step 1a
		assertEquals("Cashier should have no orders in List orders. It doesn't.", cashier.orders.size(), 0);		
		assertEquals("CashierRole should have an empty event log before msgIWantItems(...) is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());

		// Step 1a - Receiving an order from first customer (Message)
		cashier.msgIWantItems(customer1, test3Orders1);

		// Check postconditions for Step 1a
		assertEquals("Cashier should have one order in List orders. It doesn't.", cashier.orders.size(), 1);

		// Check preconditions for Step 1b
		assertEquals("MockEmployee should have an empty log befor the scheduler is called. Instead the MockEmployee's event log reads: " + employee1.log.toString(), 0, employee1.log.size());
		assertEquals("Customer1 should have an empty log befor the scheduler is called. Instead Customer1's event log reads: " + customer1.log.toString(), 0, customer1.log.size());
		assertEquals("Customer2 should have an empty log befor the scheduler is called. Instead Customer2's event log reads: " + customer2.log.toString(), 0, customer2.log.size());
		
		// Step 1b - Receiving an order from second customer (Message)
		cashier.msgIWantItems(customer2, test3Orders2);

		// Check postconditions for Step 1b
		assertEquals("Cashier should have two orders in List orders. It doesn't", cashier.orders.size(), 2);

		// Step 1c - Sending first order to an employee (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 1c
		assertTrue("Cashier should have an order in List orders in which OrderState == handing. It doesn't.", cashier.orders.get(0).getOS() == OrderState.handing);
		assertTrue("Cashier should have an order in List orders in which OrderState == none. It doesn't.", cashier.orders.get(1).getOS() == OrderState.none);
		assertTrue("Cashier should have sent order to employee1. Employee1 should have a log that reads: Received order. Instead it reads: " + employee1.log.toString(), employee1.log.getLastLoggedEvent().getMessage() == "Received order");
		assertTrue("Cashier should have sent confirmation to customer1. Customer1 should have a log that reads: Confirmation of (at least partial) delivery. Instead it reads: " + customer1.log.toString(), customer1.log.getLastLoggedEvent().getMessage() == "Confirmation of (at least partial) delivery");
		assertEquals("Lasagna inventory goes down by 2. Inventory amount should be 8. It isn't.", cashier.inventory.getAmount("Lasagna"), (Integer) 8);
		assertEquals("Horchata inventory goes down by 5. Inventory amount should be 5. It isn't.", cashier.inventory.getAmount("Horchata"), (Integer) 5);

		// Step 1d - Sending second order to an employee (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 1d
		assertTrue("Cashier should have two orders in List orders in which OrderState == handing. It doesn't.", cashier.orders.get(0).getOS() == OrderState.handing && cashier.orders.get(1).getOS() == OrderState.handing);
		assertTrue("Cashier should have sent second order to employee1. Employee1 should have a log that reads: Received order. Instead it reads: " + employee1.log.toString(), employee1.log.getLastLoggedEvent().getMessage() == "Received order");
		assertTrue("Cashier should have sent confirmation to customer2. Customer2 should have a log that reads: Confirmation of (at least partial) delivery. Instead it reads: " + customer2.log.toString(), customer2.log.getLastLoggedEvent().getMessage() == "Confirmation of (at least partial) delivery");
		assertEquals("Tacos inventory goes down by 5. Inventory amount should be 5. It isn't.", cashier.inventory.getAmount("Tacos"), (Integer) 5);
		assertEquals("Garlic Bread inventory goes down by 10. Inventory amount should be 0. It isn't.", cashier.inventory.getAmount("Garlic Bread"), (Integer) 0);
		assertEquals("Employee1 should now have two pending orders. Employee1 doesn't.", employee1.orders.size(), 2);

		// Step 2a - Receiving Customer1's items from Employee (Message)
		cashier.msgHereAreItems(employee1.orders.get(0), employee1);

		// Check postconditions for Step 2a
		assertTrue("Cashier should have one order in List orders in which OrderState == ready. It doesn't.", cashier.orders.get(0).getOS() == OrderState.ready);
		assertTrue("Cashier should have one order in List orders in which OrderState == handing. It doesn't.", cashier.orders.get(1).getOS() == OrderState.handing);

		// Step 3a - Receiving Customer2's items from Employee (Message)
		cashier.msgHereAreItems(employee1.orders.get(1), employee1);

		// Check postconditions for Step 3a
		assertTrue("Cashier should have two orders in List orders in which OrderState == ready. It doesn't.", cashier.orders.get(0).getOS() == OrderState.ready && cashier.orders.get(0).getOS() == OrderState.ready);

		// Step 2b - Letting Customer1 know Order is ready (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 2b
		assertTrue("Cashier should have an order in List orders in which OrderState == know. It doesn't.", cashier.orders.get(0).getOS() == OrderState.know);
		assertTrue("Cashier should have called customer1"
				+ " to pick up order. Customer1 should have a log that reads: My order is ready. Instead it reads: " + customer1.log.toString(), customer1.log.getLastLoggedEvent().getMessage() == "My order is ready");
		assertEquals("Cashier should have an order in List orders in which price == $35. It doesn't.", cashier.orders.get(0).price, 35);

		// Step 3a - Customer arrives at desk
		cashier.msgImHere(customer1);
		
		// Check postconditions for Step 3a
		assertTrue("Cashier should have one order in List orders in which OrderState == here. It doesn't.", cashier.orders.get(0).getOS() == OrderState.here);
		
		// Step 3b - Hand order to Customer (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
		// Check postconditions for Step 3b
		assertTrue("Cashier should have an order in List orders in which OrderState == paying. It doesn't.", cashier.orders.get(0).getOS() == OrderState.paying);
		assertEquals("Cashier should have an order in List orders in which price == $35. It doesn't.", cashier.orders.get(0).price, 35);

		// Step 3c - Take Customer1's payment (Message)
		cashier.msgPayment(customer1, 40);

		// Check postconditions for Step 3c
		assertEquals("Cashier should have one order in List orders in which amountPaid == $40. It doesn't.", cashier.orders.get(0).amountPaid, 40);
		assertEquals("Cashier should have one order in List orders in which change == $5. It doesn't.", cashier.orders.get(0).change, 5);

		// Step 3d - Hand Customer1's change (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 3d
		assertEquals("Cashier should have one order in List orders. It doesn't.", cashier.orders.size(), 1);		
		assertTrue("Cashier should have handed customer1's"
				+ " change. Customer1 should have a log that reads: Received change. Instead it reads: " + customer1.log.toString(), customer1.log.getLastLoggedEvent().getMessage() == "Received change");
		assertEquals("marketMoney should total up to $85. It doesn't", cashier.viewMarketMoney(), 85);

		// Step 4b - Letting Customer2 know Order is ready (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
		// Check postconditions for Step 4b
		assertTrue("Cashier should have an order in List orders in which OrderState == know. It doesn't.", cashier.orders.get(0).getOS() == OrderState.know);
		assertTrue("Cashier should have called customer2"
				+ " to pick up order. Customer2 should have a log that reads: My order is ready. Instead it reads: " + customer2.log.toString(), customer2.log.getLastLoggedEvent().getMessage() == "My order is ready");

		// Step 5a - Customer arrives at desk
		cashier.msgImHere(customer2);
		
		// Check postconditions for Step 5a
		assertTrue("Cashier should have one order in List orders in which OrderState == here. It doesn't.", cashier.orders.get(0).getOS() == OrderState.here);
		
		// Step 5b - Hand order to Customer (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
		// Check postconditions for Step 3b
		assertTrue("Cashier should have an order in List orders in which OrderState == paying. It doesn't.", cashier.orders.get(0).getOS() == OrderState.paying);
		assertEquals("Cashier should have an order in List orders in which price == $75. It doesn't.", cashier.orders.get(0).price, 75);

		// Step 5c - Take Customer2's payment (Message)
		cashier.msgPayment(customer2, 100);

		// Check postconditions for Step 5c
		assertEquals("Cashier should have one order in List orders in which amountPaid == $100. It doesn't.", cashier.orders.get(0).amountPaid, 100);
		assertEquals("Cashier should have one order in List orders in which change == $25. It doesn't.", cashier.orders.get(0).change, 25);

		// Step 5d - Hand Customer2's change (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 5d
		assertEquals("Cashier should have no orders in List orders. It doesn't.", cashier.orders.size(), 0);		
		assertTrue("Cashier should have handed customer2's"
				+ " change. Customer2 should have a log that reads: Received change. Instead it reads: " + customer2.log.toString(), customer2.log.getLastLoggedEvent().getMessage() == "Received change");
		assertEquals("marketMoney should total up to $160. It doesn't", cashier.viewMarketMoney(), 160);
	} // End of Test 3

	/**
	 * This tests the second normal scenario (Delivery order from cook) with multiple restaurants
	 */
	public void testFourNormalMultipleCustomersScenario2()
	{
		// Set up
		cashier.setPerson(person);
		rCook1.cashier = cashier;
		rCook2.cashier = cashier;
		deliverer1.cashier = cashier;
		cashier.setDeliverers(deliverer1, 10, true);
		cashier.setDeliverers(deliverer2, 10, false);
		cashier.setMarketState(true);
		cashier.setSalary(10);
		cashier.setMarketMoney(50);
		cashier.inventory.setAmount("Pizza", 10);
		cashier.inventory.setAmount("Chicken", 10);
		cashier.inventory.setAmount("Ribs", 10);
		cashier.inventory.setAmount("Burger", 10);
		List<ItemOrder> test4Orders1 = new ArrayList<ItemOrder>(); // orders Pizza and Chicken
		List<ItemOrder> test4Orders2 = new ArrayList<ItemOrder>(); // orders Ribs and Burgers
		test4Orders1.add(new ItemOrder("Pizza", 10));
		test4Orders1.add(new ItemOrder("Chicken", 1));
		test4Orders2.add(new ItemOrder("Ribs", 7));
		test4Orders2.add(new ItemOrder("Burger", 5));
		rCook1.items = test4Orders1;
		rCook2.items = test4Orders2;


		// Check preconditions for Step 1a
		assertEquals("Cashier should have no orders in List orders. It doesn't.", cashier.orders.size(), 0);		
		assertEquals("CashierRole should have an empty event log before msgIWantDelivery(...) is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());

		// Step 1a - Receiving first delivery order (Message)
		cashier.msgIWantDelivery(rCook1, rCashier1, test4Orders1, "Restaurant1");

		// Check postconditions for Step 1a
		assertEquals("Cashier should have one order in List orders. It doesn't.", cashier.orders.size(), 1);
		assertFalse("Cashier should have one order in List orders for delivery. It doesn't", cashier.orders.get(0).location == null);

		// Step 1b - Receiving second delivery order (Message)
		cashier.msgIWantDelivery(rCook2, rCashier2, test4Orders2, "Restaurant2");

		// Check postconditions for Step 1b
		assertEquals("Cashier should have two orders in List orders. It doesn't.", cashier.orders.size(), 2);
		assertFalse("Cashier should have two orders in List orders for delivery. It doesn't", cashier.orders.get(1).location == null);

		// Check preconditions for Step 1c
		assertEquals("MockDeliverer should have an empty log befor the scheduler is called. Instead the MockDeliverer's event log reads: " + deliverer1.log.toString(), 0, deliverer1.log.size());
		assertEquals("RCook1 should have an empty log befor the scheduler is called. Instead rCook1's event log reads: " + rCook1.log.toString(), 0, rCook1.log.size());
		assertEquals("RCook2 should have an empty log befor the scheduler is called. Instead rCook2's event log reads: " + rCook2.log.toString(), 0, rCook2.log.size());

		// Step 1c - Confirming order to a restaurant cook/Sending the order to a deliverer (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 1c
		assertTrue("Cashier should have an order in List orders in which OrderState == handing. It doesn't.", cashier.orders.get(0).getOS() == OrderState.handing);
		assertTrue("Cashier should have an order in List orders in which OrderState == newDelivery. It doesn't.", cashier.orders.get(1).getOS() == OrderState.newDelivery);
		assertTrue("Cashier should have sent order to deliverer1. Deliverer1 should have a log that reads: Received order. Instead it reads: " + deliverer1.log.toString(), deliverer1.log.getLastLoggedEvent().getMessage() == "Received order");
		assertTrue("Cashier should have sent confirmation to rCook1. RCook1 should have a log that reads: Confirmation of (at least partial) delivery. Instead it reads: " + rCook1.log.toString(), rCook1.log.getLastLoggedEvent().getMessage() == "Confirmation of (at least partial) delivery");
		assertEquals("RCook2 should have an empty log befor the scheduler is called. Instead the rCook2's event log reads: " + rCook2.log.toString(), 0, rCook2.log.size());
		assertEquals("Pizza inventory goes down by 10. Inventory amount should be 0. It isn't.", cashier.inventory.getAmount("Pizza"), (Integer) 0);
		assertEquals("Chicken inventory goes down by 1. Inventory amount should be 9. It isn't.", cashier.inventory.getAmount("Chicken"), (Integer) 9);
		assertEquals("Cashier should have an order in List orders in which price == $60. It doesn't.", cashier.orders.get(0).price, 60);

		// Step 1d - Confirming order to a restaurant cook/Sending the order to a deliverer (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 1d
		assertTrue("Cashier should have an order in List orders in which OrderState == handing. It doesn't.", cashier.orders.get(0).getOS() == OrderState.handing && cashier.orders.get(0).getOS() == OrderState.handing);
		assertTrue("Cashier should have sent order to deliverer1. Deliverer1 should have a log that reads: Received order. Instead it reads: " + deliverer1.log.toString(), deliverer1.log.getLastLoggedEvent().getMessage() == "Received order");
		assertTrue("Cashier should have sent confirmation to rCook2. RCook2 should have a log that reads: Confirmation of (at least partial) delivery. Instead it reads: " + rCook1.log.toString(), rCook1.log.getLastLoggedEvent().getMessage() == "Confirmation of (at least partial) delivery");
		assertEquals("Ribs inventory goes down by 7. Inventory amount should be 3. It isn't.", cashier.inventory.getAmount("Ribs"), (Integer) 3);
		assertEquals("Burger inventory goes down by 5. Inventory amount should be 5. It isn't.", cashier.inventory.getAmount("Burger"), (Integer) 5);
		assertEquals("Cashier should have an order in List orders in which price == $110. It doesn't.", cashier.orders.get(1).price, 110);

		// Step 2a - Receiving money from Deliverer to confirm order to RCook1 (Message)
		cashier.msgDelivered(deliverer1.orders.get(0), deliverer1);

		// Check postconditions for Step 2a
		assertTrue("Cashier should have one order in List orders in which OrderState == delivered. It doesn't.", cashier.orders.get(0).getOS() == OrderState.delivered);

		// Step 3a - Receiving money from Deliverer to confirm order to RCook2 (Message)
		cashier.msgDelivered(deliverer1.orders.get(1), deliverer1);

		// Check postconditions for Step 3a
		assertTrue("Cashier should have two orders in List orders in which OrderState == delivered. It doesn't.", cashier.orders.get(0).getOS() == OrderState.delivered && cashier.orders.get(1).getOS() == OrderState.delivered);

		// Step 2b - Updating MarketMoney (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 2b
		assertEquals("Cashier should have one order in List orders. It doesn't.", cashier.orders.size(), 1);
		assertEquals("marketMoney should total up to $110. It doesn't", cashier.viewMarketMoney(), 110);

		// Step 3b - Updating MarketMoney (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 3b
		assertEquals("Cashier should have no orders in List orders. It doesn't.", cashier.orders.size(), 0);
		assertEquals("marketMoney should total up to $220. It doesn't", cashier.viewMarketMoney(), 220);

	} // End of Test 4
	
	/**
	 * This tests the first normal scenario (Customer comes in and order a list of items) with multiple customers and multiple employees
	 */
	public void testFiveNormalMultipleCustomerMultipleEmployeesScenario1()
	{
		// Set up
		cashier.setPerson(person);
		customer1.cashier = cashier;
		customer2.cashier = cashier;
		employee1.cashier = cashier;
		employee2.cashier = cashier;
		cashier.setEmployees(employee1, 10, true);
		cashier.setEmployees(employee2, 10, true);
		cashier.setMarketState(true);
		cashier.setSalary(10);
		cashier.setMarketMoney(50);
		cashier.inventory.setAmount("Lasagna", 10);
		cashier.inventory.setAmount("Horchata", 10);
		cashier.inventory.setAmount("Tacos", 10);
		cashier.inventory.setAmount("Garlic Bread", 10);
		List<ItemOrder> test5Orders1 = new ArrayList<ItemOrder>(); // orders Lasagna and Horchata
		List<ItemOrder> test5Orders2 = new ArrayList<ItemOrder>(); // orders Tacos and Garlic Bread
		test5Orders1.add(new ItemOrder("Lasagna", 2));
		test5Orders1.add(new ItemOrder("Horchata", 5));
		test5Orders2.add(new ItemOrder("Tacos", 5));
		test5Orders2.add(new ItemOrder("Garlic Bread", 10));
		customer1.items = test5Orders1;
		customer2.items = test5Orders2;

		// Check preconditions for Step 1a
		assertEquals("Cashier should have no orders in List orders. It doesn't.", cashier.orders.size(), 0);		
		assertEquals("CashierRole should have an empty event log before msgIWantItems(...) is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());

		// Step 1a - Receiving an order from first customer (Message)
		cashier.msgIWantItems(customer1, test5Orders1);

		// Check postconditions for Step 1a
		assertEquals("Cashier should have one order in List orders. It doesn't.", cashier.orders.size(), 1);
		
		// Step 1b - Receiving an order from second customer (Message)
		cashier.msgIWantItems(customer2, test5Orders2);

		// Check postconditions for Step 1b
		assertEquals("Cashier should have two orders in List orders. It doesn't", cashier.orders.size(), 2);

		// Check preconditions for Step 1c
		assertEquals("MockEmployee1 should have an empty log befor the scheduler is called. Instead the MockEmployee's event log reads: " + employee1.log.toString(), 0, employee1.log.size());
		assertEquals("MockEmployee2 should have an empty log befor the scheduler is called. Instead the MockEmployee's event log reads: " + employee2.log.toString(), 0, employee2.log.size());
		assertEquals("Customer1 should have an empty log befor the scheduler is called. Instead Customer1's event log reads: " + customer1.log.toString(), 0, customer1.log.size());
		assertEquals("Customer2 should have an empty log befor the scheduler is called. Instead Customer2's event log reads: " + customer2.log.toString(), 0, customer2.log.size());
		
		// Step 1c - Sending first order to an employee (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 1c
		assertTrue("Cashier should have an order in List orders in which OrderState == handing. It doesn't.", cashier.orders.get(0).getOS() == OrderState.handing);
		assertTrue("Cashier should have an order in List orders in which OrderState == none. It doesn't.", cashier.orders.get(1).getOS() == OrderState.none);
		assertTrue("Cashier should have sent order to employee1. Employee1 should have a log that reads: Received order. Instead it reads: " + employee1.log.toString(), employee1.log.getLastLoggedEvent().getMessage() == "Received order");
		assertTrue("Cashier should have sent confirmation to customer1. Customer1 should have a log that reads: Confirmation of (at least partial) delivery. Instead it reads: " + customer1.log.toString(), customer1.log.getLastLoggedEvent().getMessage() == "Confirmation of (at least partial) delivery");
		assertEquals("MockEmployee2 should have an empty log befor the scheduler is called. Instead the MockEmployee's event log reads: " + employee2.log.toString(), 0, employee2.log.size());
		assertEquals("Lasagna inventory goes down by 2. Inventory amount should be 8. It isn't.", cashier.inventory.getAmount("Lasagna"), (Integer) 8);
		assertEquals("Horchata inventory goes down by 5. Inventory amount should be 5. It isn't.", cashier.inventory.getAmount("Horchata"), (Integer) 5);

		// Step 1d - Sending second order to an employee (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 1d
		assertTrue("Cashier should have two orders in List orders in which OrderState == handing. It doesn't.", cashier.orders.get(0).getOS() == OrderState.handing && cashier.orders.get(1).getOS() == OrderState.handing);
		assertTrue("Cashier should have NOT sent second order to employee1. Employee1 should have a log of size one. It doesn't.", employee1.log.size() == 1);
		assertEquals("Tacos inventory goes down by 5. Inventory amount should be 5. It isn't.", cashier.inventory.getAmount("Tacos"), (Integer) 5);
		assertEquals("Garlic Bread inventory goes down by 10. Inventory amount should be 0. It isn't.", cashier.inventory.getAmount("Garlic Bread"), (Integer) 0);
		assertTrue("Cashier should have sent order to employee2. Employee2 should have a log that reads: Received order. Instead it reads: " + employee2.log.toString(), employee2.log.getLastLoggedEvent().getMessage() == "Received order");
		assertTrue("Cashier should have sent confirmation to customer2. Customer2 should have a log that reads: Confirmation of (at least partial) delivery. Instead it reads: " + customer2.log.toString(), customer2.log.getLastLoggedEvent().getMessage() == "Confirmation of (at least partial) delivery");
		
		// Step 2a - Receiving Customer1's items from Employee (Message)
		cashier.msgHereAreItems(employee1.orders.get(0), employee1);

		// Check postconditions for Step 2a
		assertTrue("Cashier should have one order in List orders in which OrderState == ready. It doesn't.", cashier.orders.get(0).getOS() == OrderState.ready);
		assertTrue("Cashier should have one order in List orders in which OrderState == handing. It doesn't.", cashier.orders.get(1).getOS() == OrderState.handing);

		// Step 3a - Receiving Customer2's items from Employee (Message)
		cashier.msgHereAreItems(employee2.orders.get(0), employee2);

		// Check postconditions for Step 3a
		assertTrue("Cashier should have two orders in List orders in which OrderState == ready. It doesn't.", cashier.orders.get(0).getOS() == OrderState.ready && cashier.orders.get(0).getOS() == OrderState.ready);

		// Step 3b - Letting Customer1 know Order is ready (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 3b
		assertTrue("Cashier should have an order in List orders in which OrderState == know. It doesn't.", cashier.orders.get(0).getOS() == OrderState.know);
		assertTrue("Cashier should have called customer1"
				+ " to pick up order. Customer1 should have a log that reads: My order is ready. Instead it reads: " + customer1.log.toString(), customer1.log.getLastLoggedEvent().getMessage() == "My order is ready");
		
		// Step 4a - Customer arrives at desk
		cashier.msgImHere(customer1);
		
		// Check postconditions for Step 4a
		assertTrue("Cashier should have one order in List orders in which OrderState == here. It doesn't.", cashier.orders.get(0).getOS() == OrderState.here);
		
		// Step 4b - Hand order to Customer (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
		// Check postconditions for Step 4b
		assertTrue("Cashier should have an order in List orders in which OrderState == paying. It doesn't.", cashier.orders.get(0).getOS() == OrderState.paying);
		assertEquals("Cashier should have an order in List orders in which price == $35. It doesn't.", cashier.orders.get(0).price, 35);

		// Step 4c - Take Customer1's payment (Message)
		cashier.msgPayment(customer1, 40);

		// Check postconditions for Step 4c
		assertEquals("Cashier should have one order in List orders in which amountPaid == $40. It doesn't.", cashier.orders.get(0).amountPaid, 40);
		assertEquals("Cashier should have one order in List orders in which change == $5. It doesn't.", cashier.orders.get(0).change, 5);

		// Step 4d - Hand Customer1's change (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 4d
		assertEquals("Cashier should have one order in List orders. It doesn't.", cashier.orders.size(), 1);		
		assertTrue("Cashier should have handed customer1's"
				+ " change. Customer1 should have a log that reads: Received change. Instead it reads: " + customer1.log.toString(), customer1.log.getLastLoggedEvent().getMessage() == "Received change");
		assertEquals("marketMoney should total up to $85. It doesn't", cashier.viewMarketMoney(), 85);

		// Step 5b - Letting Customer2 know Order is ready (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 5b
		assertTrue("Cashier should have an order in List orders in which OrderState == know. It doesn't.", cashier.orders.get(0).getOS() == OrderState.know);
		assertTrue("Cashier should have called customer2"
				+ " to pick up order. Customer2 should have a log that reads: My order is ready. Instead it reads: " + customer2.log.toString(), customer2.log.getLastLoggedEvent().getMessage() == "My order is ready");
		
		// Step 5a - Customer arrives at desk
		cashier.msgImHere(customer2);
		
		// Check postconditions for Step 5a
		assertTrue("Cashier should have one order in List orders in which OrderState == here. It doesn't.", cashier.orders.get(0).getOS() == OrderState.here);
		
		// Step 5b - Hand order to Customer (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
		// Check postconditions for Step 5b
		assertTrue("Cashier should have an order in List orders in which OrderState == paying. It doesn't.", cashier.orders.get(0).getOS() == OrderState.paying);
		assertEquals("Cashier should have an order in List orders in which price == $75. It doesn't.", cashier.orders.get(0).price, 75);

		// Step 5c - Take Customer2's payment (Message)
		cashier.msgPayment(customer2, 100);

		// Check postconditions for Step 5c
		assertEquals("Cashier should have one order in List orders in which amountPaid == $100. It doesn't.", cashier.orders.get(0).amountPaid, 100);
		assertEquals("Cashier should have one order in List orders in which change == $25. It doesn't.", cashier.orders.get(0).change, 25);

		// Step 5d - Hand Customer2's change (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 5d
		assertEquals("Cashier should have no orders in List orders. It doesn't.", cashier.orders.size(), 0);		
		assertTrue("Cashier should have handed customer2's"
				+ " change. Customer2 should have a log that reads: Received change. Instead it reads: " + customer2.log.toString(), customer2.log.getLastLoggedEvent().getMessage() == "Received change");
		assertEquals("marketMoney should total up to $160. It doesn't", cashier.viewMarketMoney(), 160);
	} // End of Test 5

	/**
	 * This tests the second normal scenario (Delivery order from cook) with multiple restaurants and multiple deliverers
	 */
	public void testSixNormalMultipleCustomersMultipleDeliverersScenario2()
	{
		// Set up
		cashier.setPerson(person);
		rCook1.cashier = cashier;
		rCook2.cashier = cashier;
		deliverer1.cashier = cashier;
		deliverer2.cashier = cashier;
		cashier.setDeliverers(deliverer1, 10, true);
		cashier.setDeliverers(deliverer2, 10, true);
		cashier.setMarketState(true);
		cashier.setSalary(10);
		cashier.setMarketMoney(50);
		cashier.inventory.setAmount("Pizza", 10);
		cashier.inventory.setAmount("Chicken", 10);
		cashier.inventory.setAmount("Ribs", 10);
		cashier.inventory.setAmount("Burger", 10);
		List<ItemOrder> test6Orders1 = new ArrayList<ItemOrder>(); // orders Pizza and Chicken
		List<ItemOrder> test6Orders2 = new ArrayList<ItemOrder>(); // orders Ribs and Burgers
		test6Orders1.add(new ItemOrder("Pizza", 10));
		test6Orders1.add(new ItemOrder("Chicken", 1));
		test6Orders2.add(new ItemOrder("Ribs", 7));
		test6Orders2.add(new ItemOrder("Burger", 5));
		rCook1.items = test6Orders1;
		rCook2.items = test6Orders2;


		// Check preconditions for Step 1a
		assertEquals("Cashier should have no orders in List orders. It doesn't.", cashier.orders.size(), 0);		
		assertEquals("CashierRole should have an empty event log before msgIWantDelivery(...) is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());

		// Step 1a - Receiving first delivery order (Message)
		cashier.msgIWantDelivery(rCook1, rCashier1, test6Orders1, "Restaurant1");

		// Check postconditions for Step 1a
		assertEquals("Cashier should have one order in List orders. It doesn't.", cashier.orders.size(), 1);
		assertFalse("Cashier should have one order in List orders for delivery. It doesn't", cashier.orders.get(0).location == null);

		// Step 1b - Receiving second delivery order (Message)
		cashier.msgIWantDelivery(rCook2, rCashier2, test6Orders2, "Restaurant2");

		// Check postconditions for Step 1b
		assertEquals("Cashier should have two orders in List orders. It doesn't.", cashier.orders.size(), 2);
		assertFalse("Cashier should have two orders in List orders for delivery. It doesn't", cashier.orders.get(1).location == null);

		// Check preconditions for Step 1c
		assertEquals("MockDeliverer1 should have an empty log befor the scheduler is called. Instead the MockDeliverer1's event log reads: " + deliverer1.log.toString(), 0, deliverer1.log.size());
		assertEquals("MockDeliverer2 should have an empty log befor the scheduler is called. Instead the MockDeliverer2's event log reads: " + deliverer2.log.toString(), 0, deliverer2.log.size());
		assertEquals("RCook1 should have an empty log befor the scheduler is called. Instead rCook1's event log reads: " + rCook1.log.toString(), 0, rCook1.log.size());
		assertEquals("RCook2 should have an empty log befor the scheduler is called. Instead rCook2's event log reads: " + rCook2.log.toString(), 0, rCook2.log.size());

		// Step 1c - Confirming order to a restaurant cook/Sending the order to a deliverer (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 1c
		assertTrue("Cashier should have an order in List orders in which OrderState == handing. It doesn't.", cashier.orders.get(0).getOS() == OrderState.handing);
		assertTrue("Cashier should have an order in List orders in which OrderState == newDelivery. It doesn't.", cashier.orders.get(1).getOS() == OrderState.newDelivery);
		assertTrue("Cashier should have sent order to deliverer1. Deliverer1 should have a log that reads: Received order. Instead it reads: " + deliverer1.log.toString(), deliverer1.log.getLastLoggedEvent().getMessage() == "Received order");
		assertTrue("Cashier should have sent confirmation to rCook1. RCook1 should have a log that reads: Confirmation of (at least partial) delivery. Instead it reads: " + rCook1.log.toString(), rCook1.log.getLastLoggedEvent().getMessage() == "Confirmation of (at least partial) delivery");
		assertEquals("RCook2 should have an empty log befor the scheduler is called. Instead the rCook2's event log reads: " + rCook2.log.toString(), 0, rCook2.log.size());
		assertEquals("Pizza inventory goes down by 10. Inventory amount should be 0. It isn't.", cashier.inventory.getAmount("Pizza"), (Integer) 0);
		assertEquals("Chicken inventory goes down by 1. Inventory amount should be 9. It isn't.", cashier.inventory.getAmount("Chicken"), (Integer) 9);
		assertEquals("Cashier should have an order in List orders in which price == $60. It doesn't.", cashier.orders.get(0).price, 60);

		// Step 1d - Confirming order to a restaurant cook/Sending the order to a deliverer (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 1d
		assertTrue("Cashier should have an order in List orders in which OrderState == handing. It doesn't.", cashier.orders.get(0).getOS() == OrderState.handing && cashier.orders.get(0).getOS() == OrderState.handing);
		assertTrue("Cashier should have NOT sent second order to deliverer1. Deliverer1 should have a log of size one. It doesn't.", deliverer1.log.size() == 1);
		assertTrue("Cashier should have sent order to deliverer2. Deliverer2 should have a log that reads: Received order. Instead it reads: " + deliverer2.log.toString(), deliverer2.log.getLastLoggedEvent().getMessage() == "Received order");
		assertTrue("Cashier should have sent confirmation to rCook2. RCook2 should have a log that reads: Confirmation of (at least partial) delivery. Instead it reads: " + rCook1.log.toString(), rCook1.log.getLastLoggedEvent().getMessage() == "Confirmation of (at least partial) delivery");
		assertEquals("Ribs inventory goes down by 7. Inventory amount should be 3. It isn't.", cashier.inventory.getAmount("Ribs"), (Integer) 3);
		assertEquals("Burger inventory goes down by 5. Inventory amount should be 5. It isn't.", cashier.inventory.getAmount("Burger"), (Integer) 5);
		assertEquals("Cashier should have an order in List orders in which price == $110. It doesn't.", cashier.orders.get(1).price, 110);

		// Step 2a - Receiving money from Deliverer to confirm order to RCook1 (Message)
		cashier.msgDelivered(deliverer1.orders.get(0), deliverer1);

		// Check postconditions for Step 2a
		assertTrue("Cashier should have one order in List orders in which OrderState == delivered. It doesn't.", cashier.orders.get(0).getOS() == OrderState.delivered);

		// Step 3a - Receiving money from Deliverer to confirm order to RCook2 (Message)
		cashier.msgDelivered(deliverer2.orders.get(0), deliverer2);

		// Check postconditions for Step 3a
		assertTrue("Cashier should have two orders in List orders in which OrderState == delivered. It doesn't.", cashier.orders.get(0).getOS() == OrderState.delivered && cashier.orders.get(1).getOS() == OrderState.delivered);

		// Step 2b - Updating MarketMoney (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 2b
		assertEquals("Cashier should have one order in List orders. It doesn't.", cashier.orders.size(), 1);
		assertEquals("marketMoney should total up to $110. It doesn't", cashier.viewMarketMoney(), 110);

		// Step 3b - Updating MarketMoney (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 3b
		assertEquals("Cashier should have no orders in List orders. It doesn't.", cashier.orders.size(), 0);
		assertEquals("marketMoney should total up to $220. It doesn't", cashier.viewMarketMoney(), 220);

	} // End of Test 6
	
	/**
	 * This tests the first normal scenario (Customer comes in and order a list of items) when out of items
	 */
	public void testSevenNormalCustomerOutOfItemsScenario1()
	{
		// Set up
		cashier.setPerson(person);
		customer1.cashier = cashier;
		employee1.cashier = cashier;
		cashier.setEmployees(employee1, 10, true);
		cashier.setEmployees(employee2, 10, false);
		cashier.setMarketState(true);
		cashier.setSalary(10);
		cashier.setMarketMoney(50);
		cashier.inventory.setAmount("Lasagna", 3);
		cashier.inventory.setAmount("Horchata", 3);
		List<ItemOrder> test7Orders = new ArrayList<ItemOrder>(); // orders Lasagna and Horchata
		test7Orders.add(new ItemOrder("Lasagna", 2));
		test7Orders.add(new ItemOrder("Horchata", 5));
		customer1.items = test7Orders;

		// Check preconditions for Step 1a
		assertEquals("Cashier should have no orders in List orders. It doesn't.", cashier.orders.size(), 0);		
		assertEquals("CashierRole should have an empty event log before msgIWantItems(...) is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());

		// Step 1a - Receiving an order (Message)
		cashier.msgIWantItems(customer1, test7Orders);

		// Check postconditions for Step 1a
		assertEquals("Cashier should have one order in List orders. It doesn't.", cashier.orders.size(), 1);

		// Check preconditions for Step 1b
		assertEquals("MockEmployee should have an empty log befor the scheduler is called. Instead the MockEmployee's event log reads: " + employee1.log.toString(), 0, employee1.log.size());

		// Step 1b - Sending the order to an employee (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 1b
		assertTrue("Cashier should have an order in List orders in which OrderState == handing. It doesn't.", cashier.orders.get(0).getOS() == OrderState.handing);
		assertTrue("Cashier should have sent order to employee1. Employee1 should have a log that reads: Received order. Instead it reads: " + employee1.log.toString(), employee1.log.getLastLoggedEvent().getMessage() == "Received order");
		assertEquals("Lasagna inventory goes down by 2. Inventory amount should be 1. It isn't.", cashier.inventory.getAmount("Lasagna"), (Integer) 1);
		assertEquals("Horchata inventory goes down by 3. Inventory amount should be 0. It isn't.", cashier.inventory.getAmount("Horchata"), (Integer) 0);

		// Step 2a - Receiving items from Employee (Message)
		cashier.msgHereAreItems(employee1.orders.get(0), employee1);

		// Check postconditions for Step 2a
		assertTrue("Cashier should have one order in List orders in which OrderState == ready. It doesn't.", cashier.orders.get(0).getOS() == OrderState.ready);

		// Step 2b - Letting Customer know Order is ready (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
		// Check postconditions for Step 2b
		assertTrue("Cashier should have an order in List orders in which OrderState == know. It doesn't.", cashier.orders.get(0).getOS() == OrderState.know);
		assertTrue("Cashier should have called customer1"
				+ " to pick up order. Customer1 should have a log that reads: My order is ready. Instead it reads: " + customer1.log.toString(), customer1.log.getLastLoggedEvent().getMessage() == "My order is ready");
		
		// Step 3a - Customer arrives at desk
		cashier.msgImHere(customer1);
		
		// Check postconditions for Step 3a
		assertTrue("Cashier should have one order in List orders in which OrderState == here. It doesn't.", cashier.orders.get(0).getOS() == OrderState.here);
		
		// Step 3b - Hand order to Customer (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());
		
		// Check postconditions for Step 3b
		assertTrue("Cashier should have an order in List orders in which OrderState == paying. It doesn't.", cashier.orders.get(0).getOS() == OrderState.paying);
		assertEquals("Cashier should have an order in List orders in which price == $29. It doesn't.", cashier.orders.get(0).price, 29);

		// Step 4a - Take Customer's payment (Message)
		cashier.msgPayment(customer1, 40);

		// Check postconditions for Step 4a
		assertEquals("Cashier should have one order in List orders in which amountPaid == $40. It doesn't.", cashier.orders.get(0).amountPaid, 40);
		assertEquals("Cashier should have one order in List orders in which change == $11. It doesn't.", cashier.orders.get(0).change, 11);

		// Step 4b - Hand Customer's change (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 4b
		assertEquals("Cashier should have no orders in List orders. It doesn't.", cashier.orders.size(), 0);		
		assertTrue("Cashier should have handed customer1's"
				+ " change. Customer1 should have a log that reads: Received change. Instead it reads: " + customer1.log.toString(), customer1.log.getLastLoggedEvent().getMessage() == "Received change");
		assertEquals("marketMoney should total up to $79. It doesn't", cashier.viewMarketMoney(), 79);

	} // End of Test 7

	/**
	 * This tests the second normal scenario (Delivery order from cook) when out of items
	 */
	public void testEightNormalCustomerOutOfItemsScenario2()
	{
		// Set up
		cashier.setPerson(person);
		rCook1.cashier = cashier;
		deliverer1.cashier = cashier;
		cashier.setDeliverers(deliverer1, 10, true);
		cashier.setDeliverers(deliverer2, 10, false);
		cashier.setMarketState(true);
		cashier.setSalary(10);
		cashier.setMarketMoney(50);
		cashier.inventory.setAmount("Pizza", 5);
		cashier.inventory.setAmount("Burger", 5);
		List<ItemOrder> test8Orders = new ArrayList<ItemOrder>(); // orders Pizza and Burgers
		test8Orders.add(new ItemOrder("Pizza", 10));
		test8Orders.add(new ItemOrder("Burger", 1));
		rCook1.items = test8Orders;

		// Check preconditions for Step 1a
		assertEquals("Cashier should have no orders in List orders. It doesn't.", cashier.orders.size(), 0);		
		assertEquals("CashierRole should have an empty event log before msgIWantDelivery(...) is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());

		// Step 1a - Receiving a delivery order (Message)
		cashier.msgIWantDelivery(rCook1, rCashier1, test8Orders, "Josh's Restaurant");

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
		assertEquals("Pizza inventory goes down by 5. Inventory amount should be 0. It isn't.", cashier.inventory.getAmount("Pizza"), (Integer) 0);
		assertEquals("Burger inventory goes down by 1. Inventory amount should be 4. It isn't.", cashier.inventory.getAmount("Burger"), (Integer) 4);
		assertEquals("Cashier should have an order in List orders in which price == $33. It doesn't.", cashier.orders.get(0).price, 33);

		// Step 2a - Receiving money from Deliverer to confirm order (Message)
		cashier.msgDelivered(deliverer1.orders.get(0), deliverer1);

		// Check postconditions for Step 2a
		assertTrue("Cashier should have one order in List orders in which OrderState == delivered. It doesn't.", cashier.orders.get(0).getOS() == OrderState.delivered);

		// Step 2b - Updating MarketMoney (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 2b
		assertEquals("Cashier should have no orders in List orders. It doesn't.", cashier.orders.size(), 0);
		assertEquals("marketMoney should total up to $83. It doesn't", cashier.viewMarketMoney(), 83);

	} // End of Test 8

	/**
	 * This tests the first normal scenario (Customer comes in and order a list of items) when out of ALL items wanted
	 */
	public void testNineNormalCustomerOutOfItemsScenario1()
	{
		// Set up
		cashier.setPerson(person);
		customer1.cashier = cashier;
		employee1.cashier = cashier;
		cashier.setEmployees(employee1, 10, true);
		cashier.setEmployees(employee2, 10, false);
		cashier.setMarketState(true);
		cashier.setSalary(10);
		cashier.setMarketMoney(50);
		cashier.inventory.setAmount("Lasagna", 0);
		cashier.inventory.setAmount("Horchata", 0);
		List<ItemOrder> test9Orders = new ArrayList<ItemOrder>(); // orders Lasagna and Horchata
		test9Orders.add(new ItemOrder("Lasagna", 2));
		test9Orders.add(new ItemOrder("Horchata", 5));
		customer1.items = test9Orders;

		// Check preconditions for Step 1a
		assertEquals("Cashier should have no orders in List orders. It doesn't.", cashier.orders.size(), 0);		
		assertEquals("CashierRole should have an empty event log before msgIWantItems(...) is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());

		// Step 1a - Receiving an order (Message)
		cashier.msgIWantItems(customer1, test9Orders);

		// Check postconditions for Step 1a
		assertEquals("Cashier should have one order in List orders. It doesn't.", cashier.orders.size(), 1);

		// Check preconditions for Step 1b
		assertEquals("MockEmployee should have an empty log befor the scheduler is called. Instead the MockEmployee's event log reads: " + employee1.log.toString(), 0, employee1.log.size());

		// Step 1b - Sending the order to an employee (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 1b
		assertEquals("Cashier should have no orders in List orders. It doesn't.", cashier.orders.size(), 0);		
		assertTrue("Cashier should have NOT sent order to employee1. Employee1 should have an empty log. It doesn't.", employee1.log.size() == 0);
		assertTrue("Cashier should have sent confirmation to customer1. Customer1 should have a log that reads: Doesn't have items requested. Instead it reads: " + customer1.log.toString(), customer1.log.getLastLoggedEvent().getMessage() == "Doesn't have items requested");
		assertEquals("marketMoney should total up to $50. It doesn't", cashier.viewMarketMoney(), 50);
		
	} // End of Test 9

	/**
	 * This tests the second normal scenario (Delivery order from cook) when out of ALL items wanted
	 */
	public void testTenNormalCustomerOutOfItemsScenario2()
	{
		// Set up
		cashier.setPerson(person);
		rCook1.cashier = cashier;
		deliverer1.cashier = cashier;
		cashier.setDeliverers(deliverer1, 10, true);
		cashier.setDeliverers(deliverer2, 10, false);
		cashier.setMarketState(true);
		cashier.setSalary(10);
		cashier.setMarketMoney(50);
		cashier.inventory.setAmount("Pizza", 0);
		cashier.inventory.setAmount("Burger", 0);
		List<ItemOrder> test10Orders = new ArrayList<ItemOrder>(); // orders Pizza and Burgers
		test10Orders.add(new ItemOrder("Pizza", 10));
		test10Orders.add(new ItemOrder("Burger", 1));
		rCook1.items = test10Orders;

		// Check preconditions for Step 1a
		assertEquals("Cashier should have no orders in List orders. It doesn't.", cashier.orders.size(), 0);		
		assertEquals("CashierRole should have an empty event log before msgIWantDelivery(...) is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());

		// Step 1a - Receiving a delivery order (Message)
		cashier.msgIWantDelivery(rCook1, rCashier1, test10Orders, "Josh's Restaurant");

		// Check postconditions for Step 1a
		assertEquals("Cashier should have one order in List orders. It doesn't.", cashier.orders.size(), 1);
		assertFalse("Cashier should have one order in List orders for delivery. It doesn't", cashier.orders.get(0).location == null);

		// Check preconditions for Step 1b
		assertEquals("MockDeliverer should have an empty log befor the scheduler is called. Instead the MockDeliverer's event log reads: " + deliverer1.log.toString(), 0, deliverer1.log.size());
		assertEquals("RCook1 should have an empty log befor the scheduler is called. Instead the rCook1 event log reads: " + rCook1.log.toString(), 0, rCook1.log.size());

		// Step 1b - Confirming order to a restaurant cook/Sending the order to a deliverer (Scheduler/Action)
		assertTrue("Cashier's scheduler should have returned true, but didn't.", cashier.pickAndExecuteAnAction());

		// Check postconditions for Step 1b
		assertEquals("Cashier should have no orders in List orders. It doesn't.", cashier.orders.size(), 0);		
		assertTrue("Cashier should have NOT sent order to deliverer1. Deliverer1 should have an empty log. It doesn't.", deliverer1.log.size() == 0);
		assertTrue("Cashier should have sent confirmation to rCook1. RCook1 should have a log that reads: Doesn't have items requested. Instead it reads: " + rCook1.log.toString(), rCook1.log.getLastLoggedEvent().getMessage() == "Doesn't have items requested");
		assertEquals("marketMoney should total up to $50. It doesn't", cashier.viewMarketMoney(), 50);

	} // End of Test 10

}
