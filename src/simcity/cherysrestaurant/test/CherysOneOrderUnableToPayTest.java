package simcity.cherysrestaurant.test;

import simcity.cherysrestaurant.CherysCashierRole;
import simcity.cherysrestaurant.interfaces.CherysCustomer;
import simcity.cherysrestaurant.interfaces.CherysMarket;
import simcity.cherysrestaurant.interfaces.CherysWaiter;
import simcity.cherysrestaurant.test.mock.MockCherysCustomer;
import simcity.cherysrestaurant.test.mock.MockCherysMarket;
import simcity.cherysrestaurant.test.mock.MockCherysWaiter;

import junit.framework.*;

public class CherysOneOrderUnableToPayTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CherysCashierRole cashier;
	CherysMarket market;
	CherysWaiter waiter;
	CherysCustomer customer;

	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception
	{
		super.setUp();
		cashier = new CherysCashierRole("Cashier");		
		market = new MockCherysMarket("MockMarket");
		waiter = new MockCherysWaiter("mockwaiter");
		customer = new MockCherysCustomer("mockcustomer");
	}	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	public void testOneNormalCustomerScenario()
	{//setUp() runs first before this test!
		
	//Preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",
				0, cashier.bills.size());
		//Checking logs
		assertEquals("CashierAgent should have an empty event log before the Cashier's scheduler is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
				+ market.getLog().toString(), 0, market.getLog().size());

	//Step 1
		cashier.msgPayForDelivery(market, "Steak", 9, 0.70); //send the message from a market

		assertTrue("CashierAgent should have logged \"Received msgPayForDelivery\" but didn't. His log reads instead: "
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgPayForDelivery from market. Food = Steak. Amount = 9. Percentage = 70.0%"));
		assertEquals("Cashier should have 1 bill in it. It doesn't.",
				1, cashier.bills.size());
		assertTrue("Bills should contain a bill with state == askedFor. It doesn't.",
				cashier.bills.get(0).state == CherysCashierRole.CheckState.askedFor);
		assertTrue("Bills should contain a bill with total == $100.74. It contains something else instead: $"
				+ cashier.bills.get(0).total, cashier.bills.get(0).total == 100.74);
		assertTrue("Cashier should contain balance == $100. It contains something else instead: $" 
						+ cashier.balance, cashier.balance == 100.0);
		assertTrue("Cashier's scheduler should have returned true (needs to react to market's msgPayForDelivery), but didn't.",
				cashier.pickAndExecuteAnAction());
		assertTrue("Bills should contain a bill with state == unPaid. It doesn't.",
				cashier.bills.get(0).state == CherysCashierRole.CheckState.unpaid);
		//Checking logs
		assertEquals("Cashier should have one event logged after the Cashier's scheduler is called for the first time. Instead, it has "
				+ cashier.log.size(), 1, cashier.log.size());
		assertEquals("MockMarket should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockMarket's event log reads: "
				+ market.getLog().toString(), 0, market.getLog().size());
		
		//Step 2
		cashier.msgProduceCheck(waiter, "Pizza", 2); //code to simulate customer buying something. Already tested in OneWaiterOneCustomerTest
		assertFalse("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.",
				cashier.pickAndExecuteAnAction());
		cashier.msgGiveCheck(waiter, 2);
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's msgGiveCheck), but didn't.", 
				cashier.pickAndExecuteAnAction());
		cashier.msgPayment(customer, waiter.getCheck(), 9.0);
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's msgPayment), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("Cashier should contain balance == $108.99. It contains something else instead: $" 
				+ cashier.balance, cashier.balance == 108.99);
		assertTrue("Bills should contain a bill with state == askedFor. It doesn't.",
				cashier.bills.get(0).state == CherysCashierRole.CheckState.askedFor);
		assertTrue("Cashier's scheduler should have returned true (needs to react to the bill's stateChanged), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertTrue("MockMarket should have logged \"Received msgPaymentForDelivery\" but didn't. His log reads instead: " 
				+ market.getLog().getLastLoggedEvent().toString(), market.getLog().containsString("Received msgPaymentForDelivery from cashier. Payment = 100.74"));
		assertTrue("Cashier should contain balance == $8.25. It contains something else instead: $" 
				+ cashier.balance, cashier.balance == 8.25);
		assertEquals("Cashier should have 0 bills in it. It doesn't.",
				0, cashier.bills.size());
		//Checking logs
		assertEquals("Cashier should have four events logged after the Cashier's scheduler is called for the fifth time. Instead, it has "
				+ cashier.log.size(), 4, cashier.log.size());
		assertEquals("MockMarket should have one event logged after the Cashier's scheduler is called for the fifth time. Instead, Instead, it has: "
				+ market.getLog().toString(), 1, market.getLog().size());

	//Step 2
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		//Checking logs
		assertEquals("Cashier should have four events logged after the Cashier's scheduler is called for the sixth time. Instead, it has "
				+ cashier.log.size(), 4, cashier.log.size());
		assertEquals("MockMarket should have one event logged after the Cashier's scheduler is called for the sixth time. Instead, Instead, it has: "
				+ market.getLog().toString(), 1, market.getLog().size());
	}
}
