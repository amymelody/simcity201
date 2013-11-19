package simcity.cherysrestaurant.test;

import simcity.cherysrestaurant.CherysCashierRole;
import simcity.cherysrestaurant.interfaces.CherysMarket;
import simcity.cherysrestaurant.test.mock.MockCherysMarket;

import junit.framework.*;

public class CherysOneOrderTwoMarketsTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CherysCashierRole cashier;
	CherysMarket m1;
	CherysMarket m2;

	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception
	{
		super.setUp();
		cashier = new CherysCashierRole("Cashier");
		m1 = new MockCherysMarket("MockMarket");
		m2 = new MockCherysMarket("MockMarket");
	}
	/**
	 * This tests the cashier under very simple terms: one market is ready to pay the exact bill.
	 */
	public void testOneNormalmarketScenario()
	{//setUp() runs first before this test!
		
	//Preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",
				0, cashier.bills.size());
		//Checking logs
		assertEquals("CashierAgent should have an empty event log before the Cashier's scheduler is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals("M1 should have an empty event log before the Cashier's scheduler is called. Instead, M1's event log reads: "
				+ m1.getLog().toString(), 0, m1.getLog().size());
		assertEquals("M2 should have an empty event log before the Cashier's scheduler is called. Instead, M2's event log reads: "
				+ m2.getLog().toString(), 0, m2.getLog().size());

	//Step 1
		cashier.msgPayForDelivery(m1, "Pizza", 2, 0.70); //send the message from a market

		assertTrue("CashierAgent should have logged \"Received msgPayForDelivery\" but didn't. His log reads instead: "
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgPayForDelivery from market. Food = Pizza. Amount = 2. Percentage = 70.0%"));
		assertEquals("Cashier should have 1 bill in it. It doesn't.",
				1, cashier.bills.size());
		assertTrue("Bills should contain a bill with state == askedFor. It doesn't.",
				cashier.bills.get(0).state == CherysCashierRole.CheckState.askedFor);
		assertTrue("Bills should contain a bill with total == $12.59. It contains something else instead: $"
				+ cashier.bills.get(0).total, cashier.bills.get(0).total == 12.59);
		assertTrue("Cashier's scheduler should have returned true (needs to react to market's msgPayForDelivery), but didn't.",
				cashier.pickAndExecuteAnAction());
		assertTrue("M1 should have logged \"Received msgPaymentForDelivery\" but didn't. His log reads instead: " 
				+ m1.getLog().getLastLoggedEvent().toString(), m1.getLog().containsString("Received msgPaymentForDelivery from cashier. Payment = 12.59"));
		assertTrue("Cashier should contain balance == $87.41. It contains something else instead: $" 
				+ cashier.balance, cashier.balance == 87.41);
		assertEquals("Cashier should have 0 bills in it. It doesn't.",
				0, cashier.bills.size());
		//Checking logs
		assertEquals("Cashier should have one event logged after the Cashier's scheduler is called for the first time. Instead, it has "
				+ cashier.log.size(), 1, cashier.log.size());
		assertEquals("M1 should have one event logged after the Cashier's scheduler is called for the first time. Instead, Instead, it has: "
				+ m1.getLog().toString(), 1, m1.getLog().size());
		assertEquals("M2 should have an empty event log after the Cashier's scheduler is called for the first time. Instead, M2's event log reads: "
				+ m2.getLog().toString(), 0, m2.getLog().size());

	//Step 2
		cashier.msgPayForDelivery(m2, "Pizza", 6, 0.70); //send the message from a market

		assertTrue("CashierAgent should have logged \"Received msgPayForDelivery\" but didn't. His log reads instead: "
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgPayForDelivery from market. Food = Pizza. Amount = 6. Percentage = 70.0%"));
		assertEquals("Cashier should have 1 bill in it. It doesn't.",
				1, cashier.bills.size());
		assertTrue("Bills should contain a bill with state == askedFor. It doesn't.",
				cashier.bills.get(0).state == CherysCashierRole.CheckState.askedFor);
		assertTrue("Bills should contain a bill with total == $37.76. It contains something else instead: $"
				+ cashier.bills.get(0).total, cashier.bills.get(0).total == 37.76);
		assertTrue("Cashier's scheduler should have returned true (needs to react to market's msgPayForDelivery), but didn't.",
				cashier.pickAndExecuteAnAction());
		assertTrue("M2 should have logged \"Received msgPaymentForDelivery\" but didn't. His log reads instead: " 
				+ m2.getLog().getLastLoggedEvent().toString(), m2.getLog().containsString("Received msgPaymentForDelivery from cashier. Payment = 37.76"));
		assertTrue("Cashier should contain balance == $49.65. It contains something else instead: $" 
				+ cashier.balance, cashier.balance == 49.65);
		assertEquals("Cashier should have 0 bills in it. It doesn't.",
				0, cashier.bills.size());
		//Checking logs
		assertEquals("Cashier should have two events logged after the Cashier's scheduler is called for the second time. Instead, it has "
				+ cashier.log.size(), 2, cashier.log.size());
		assertEquals("M1 should have one event logged after the Cashier's scheduler is called for the second time. Instead, Instead, it has: "
				+ m1.getLog().toString(), 1, m1.getLog().size());
		assertEquals("M2 should have one event logged after the Cashier's scheduler is called for the second time. Instead, Instead, it has: "
				+ m2.getLog().toString(), 1, m2.getLog().size());


	//Step 2
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		//Checking logs
		assertEquals("Cashier should have two events logged after the Cashier's scheduler is called for the second time. Instead, it has "
				+ cashier.log.size(), 2, cashier.log.size());
		assertEquals("M1 should have one event logged after the Cashier's scheduler is called for the third time. Instead, Instead, it has: "
				+ m1.getLog().toString(), 1, m1.getLog().size());
		assertEquals("M2 should have one event logged after the Cashier's scheduler is called for the third time. Instead, Instead, it has: "
				+ m2.getLog().toString(), 1, m2.getLog().size());
	}
}
