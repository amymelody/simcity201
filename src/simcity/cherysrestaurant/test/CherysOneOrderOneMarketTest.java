package simcity.cherysrestaurant.test; 

import simcity.cherysrestaurant.CherysCashierRole;
import simcity.cherysrestaurant.interfaces.CherysMarket;
import simcity.cherysrestaurant.test.mock.MockCherysMarket;

import junit.framework.*;

public class CherysOneOrderOneMarketTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CherysCashierRole cashier;
	CherysMarket market;

	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception
	{
		super.setUp();
		cashier = new CherysCashierRole("Cashier");		
		market = new MockCherysMarket("MockMarket");
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
		cashier.msgPayForDelivery(market, "Salad", 9, 0.70); //send the message from a market

		assertTrue("CashierAgent should have logged \"Received msgPayForDelivery\" but didn't. His log reads instead: "
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgPayForDelivery from market. Food = Salad. Amount = 9. Percentage = 70.0%"));
		assertEquals("Cashier should have 1 bill in it. It doesn't.",
				1, cashier.bills.size());
		assertTrue("Bills should contain a bill with state == askedFor. It doesn't.",
				cashier.bills.get(0).state == CherysCashierRole.CheckState.askedFor);
		assertTrue("Bills should contain a bill with total == $37.74. It contains something else instead: $"
				+ cashier.bills.get(0).total, cashier.bills.get(0).total == 37.74);
		assertTrue("Cashier's scheduler should have returned true (needs to react to market's msgPayForDelivery), but didn't.",
				cashier.pickAndExecuteAnAction());
		assertTrue("MockMarket should have logged \"Received msgPaymentForDelivery\" but didn't. His log reads instead: " 
				+ market.getLog().getLastLoggedEvent().toString(), market.getLog().containsString("Received msgPaymentForDelivery from cashier. Payment = 37.74"));
		assertTrue("Cashier should contain balance == $62.26. It contains something else instead: $" 
				+ cashier.balance, cashier.balance == 62.26);
		assertEquals("Cashier should have 0 bills in it. It doesn't.",
				0, cashier.bills.size());
		//Checking logs
		assertEquals("Cashier should have one event logged after the Cashier's scheduler is called for the first time. Instead, it has "
				+ cashier.log.size(), 1, cashier.log.size());
		assertEquals("MockMarket should have one event logged after the Cashier's scheduler is called for the first time. Instead, Instead, it has: "
				+ market.getLog().toString(), 1, market.getLog().size());

	//Step 2
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		//Checking logs
		assertEquals("Cashier should have one event logged after the Cashier's scheduler is called for the second time. Instead, it has "
				+ cashier.log.size(), 1, cashier.log.size());
		assertEquals("MockMarket should have one event logged after the Cashier's scheduler is called for the second time. Instead, it has: "
				+ market.getLog().toString(), 1, market.getLog().size());
	}
}
