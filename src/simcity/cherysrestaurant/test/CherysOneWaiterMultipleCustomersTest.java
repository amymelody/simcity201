package simcity.cherysrestaurant.test; 

import simcity.cherysrestaurant.CherysCashierRole;
import simcity.cherysrestaurant.interfaces.CherysCustomer;
import simcity.cherysrestaurant.interfaces.CherysWaiter;
import simcity.cherysrestaurant.test.mock.MockCherysCustomer;
import simcity.cherysrestaurant.test.mock.MockCherysWaiter;

import junit.framework.*;

public class CherysOneWaiterMultipleCustomersTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CherysCashierRole cashier;
	CherysWaiter w1;
	CherysCustomer c1;
	CherysCustomer c2;

	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception
	{
		super.setUp();
		cashier = new CherysCashierRole("Cashier");
		w1 = new MockCherysWaiter("W1");
		c1 = new MockCherysCustomer("C1");
		c2 = new MockCherysCustomer("C2");
	}	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	public void testOneNormalCustomerScenario()
	{//setUp() runs first before this test!
		
	//Preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",
				0, cashier.checks.size());
		//Checking logs
		assertEquals("CashierAgent should have an empty event log before the Cashier's msgProduceCheck is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals("W1 should have an empty event log before the Cashier's scheduler is called. Instead, M1's event log reads: "
				+ w1.getLog().toString(), 0, w1.getLog().size());
		assertEquals("C1 should have an empty event log before the Cashier's scheduler is called. Instead, C1's event log reads: "
				+ c1.getLog().toString(), 0, c1.getLog().size());
		assertEquals("C2 should have an empty event log before the Cashier's scheduler is called. Instead, C2's event log reads: "
				+ c2.getLog().toString(), 0, c2.getLog().size());


	//Step 1 for C1
		cashier.msgProduceCheck(w1, "Steak", 1); //send the message from W2

		assertTrue("CashierAgent should have logged \"Received msgProduceCheck\" but didn't. His log reads instead: "
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgProduceCheck from waiter. Choice = Steak. Table = 1"));
		assertEquals("Cashier should have 1 bill in it. It doesn't.",
				1, cashier.checks.size());
		assertFalse("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.",
				cashier.pickAndExecuteAnAction());
		//Checking logs
		assertEquals("Cashier should have one event logged after the Cashier's scheduler is called for the first time. Instead, it has "
				+ cashier.log.size(), 1, cashier.log.size());
		assertEquals("W1 should have an empty event log after the Cashier's scheduler is called for the first time. Instead, W1's event log reads: "
				+ w1.getLog().toString(), 0, w1.getLog().size());
		assertEquals("C1 should have an empty event log after the Cashier's scheduler is called for the first time. Instead, C1's event log reads: "
				+ c1.getLog().toString(), 0, c1.getLog().size());
		assertEquals("C2 should have an empty event log after the Cashier's scheduler is called for the first time. Instead, C2's event log reads: "
				+ c2.getLog().toString(), 0, c2.getLog().size());

	//Step 1 for C2
		cashier.msgProduceCheck(w1, "Pizza", 2); //send the message from W2

		assertTrue("CashierAgent should have logged \"Received msgProduceCheck\" but didn't. His log reads instead: "
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgProduceCheck from waiter. Choice = Pizza. Table = 2"));
		assertEquals("Cashier should have 2 bills in it. It doesn't.",
				cashier.checks.size(), 2);
		assertFalse("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.",
				cashier.pickAndExecuteAnAction());
		//Checking logs
		assertEquals("Cashier should have two events logged after the Cashier's scheduler is called for the first time. Instead, it has "
				+ cashier.log.size(), 2, cashier.log.size());
		assertEquals("W1 should have an empty event log after the Cashier's scheduler is called for the second time. Instead, W1's event log reads: "
				+ w1.getLog().toString(), 0, w1.getLog().size());
		assertEquals("C1 should have an empty event log after the Cashier's scheduler is called for the second time. Instead, C1's event log reads: "
				+ c1.getLog().toString(), 0, c1.getLog().size());
		assertEquals("C2 should have an empty event log after the Cashier's scheduler is called for the second time. Instead, C2's event log reads: "
				+ c2.getLog().toString(), 0, c2.getLog().size());

	//Step 2 for C2
		cashier.msgGiveCheck(w1, 2); //send the message from W2

		assertTrue("CashierAgent should have logged \"Received msgGiveCheck\" but didn't. His log reads instead: "
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgGiveCheck from waiter. Table = 2"));
		assertTrue("Checks should contain a bill with state == askedFor. It doesn't.",
				cashier.checks.get(1).state == CherysCashierRole.CheckState.askedFor);
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's msgGiveCheck), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertTrue("W1 should have logged \"Received msgHereIsCheck\" but didn't. His log reads instead: " 
				+ w1.getLog().getLastLoggedEvent().toString(), w1.getLog().containsString("Received msgHereIsCheck from cashier. Order = Pizza. Total = 8.99"));
		//Checking logs
		assertEquals("Cashier should have three events logged after the Cashier's scheduler is called for the third time. Instead, it has "
				+ cashier.log.size(), 3, cashier.log.size());
		assertEquals("W1 should have an empty event log after the Cashier's scheduler is called for the third time. Instead, W1's event log reads: "
				+ w1.getLog().toString(), 1, w1.getLog().size());
		assertEquals("C1 should have an empty event log after the Cashier's scheduler is called for the third time. Instead, C1's event log reads: "
				+ c1.getLog().toString(), 0, c1.getLog().size());
		assertEquals("C2 should have an empty event log after the Cashier's scheduler is called for the third time. Instead, C2's event log reads: "
				+ c2.getLog().toString(), 0, c2.getLog().size());

	//Step 3 for C2
		cashier.msgPayment(c2, w1.getCheck(), 9.0); //send the message from a customer

		assertTrue("CashierAgent should have logged \"Received msgPayment\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgPayment from customer. Payment = 9.0"));
		assertTrue("Checks should contain a bill with state != paid. It doesn't.",
				cashier.checks.get(0).state != CherysCashierRole.CheckState.paid);
		assertTrue("Checks should contain a bill of price = $8.99. It contains something else instead: $" 
				+ cashier.checks.get(1).total, cashier.checks.get(1).total == 8.99);
		assertTrue("Checks should contain a bill with amountPaid = $9.00. Instead, amountPaid = $" 
				+ cashier.checks.get(1).amountPaid, cashier.checks.get(1).amountPaid == 9.0);
		assertTrue("Checks should contain a bill with the right customer in it. It doesn't.", 
				cashier.checks.get(1).customer == c2);
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's msgPayment), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertTrue("Cashier should contain moneyEarned == $108.99. It contains something else instead: $" 
				+ cashier.balance, cashier.balance == 108.99);
		assertEquals("Cashier should have 1 bill in it. It doesn't.",
				1, cashier.checks.size());
		assertTrue("C2 should have logged an event for receiving \"msgChange\" with the correct change, but his last event logged reads instead: " 
				+ c2.getLog().getLastLoggedEvent().toString(), c2.getLog().containsString("Received msgChange from cashier. Change = 0.01"));
		//Checking logs
		assertEquals("Cashier should have four events logged after the Cashier's scheduler is called for the fourth time. Instead, it has "
				+ cashier.log.size(), 4, cashier.log.size());
		assertEquals("W1 should have an empty event log after the Cashier's scheduler is called for the fourth time. Instead, W1's event log reads: "
				+ w1.getLog().toString(), 1, w1.getLog().size());
		assertEquals("C1 should have an empty event log after the Cashier's scheduler is called for the fourth time. Instead, C1's event log reads: "
				+ c1.getLog().toString(), 0, c1.getLog().size());
		assertEquals("C2 should have one event logged after the Cashier's scheduler is called for the fourth time. Instead, it has: "
				+ c2.getLog().toString(), 1, c2.getLog().size());

	//Step 4 for C2
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		//Checking logs
		assertEquals("Cashier should have four events logged after the Cashier's scheduler is called for the fifth time. Instead, it has "
				+ cashier.log.size(), 4, cashier.log.size());
		assertEquals("W1 should have an empty event log after the Cashier's scheduler is called for the fifth time. Instead, W1's event log reads: "
				+ w1.getLog().toString(), 1, w1.getLog().size());
		assertEquals("C1 should have an empty event log after the Cashier's scheduler is called for the fifth time. Instead, C1's event log reads: "
				+ c1.getLog().toString(), 0, c1.getLog().size());
		assertEquals("C2 should have one event logged after the Cashier's scheduler is called for the fifth time. Instead, it has: "
				+ c2.getLog().toString(), 1, c2.getLog().size());

	//Step 2 for C1
		cashier.msgGiveCheck(w1, 1); //send the message from W2

		assertTrue("CashierAgent should have logged \"Received msgGiveCheck\" but didn't. His log reads instead: "
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgGiveCheck from waiter. Table = 1"));
		assertTrue("Checks should contain a bill with state == askedFor. It doesn't.",
				cashier.checks.get(0).state == CherysCashierRole.CheckState.askedFor);
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's msgGiveCheck), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertTrue("W1 should have logged \"Received msgHereIsCheck\" but didn't. His log reads instead: " 
				+ w1.getLog().getLastLoggedEvent().toString(), w1.getLog().containsString("Received msgHereIsCheck from cashier. Order = Steak. Total = 15.99"));
		//Checking logs
		assertEquals("Cashier should have five events logged after the Cashier's scheduler is called for the sixth time. Instead, it has "
				+ cashier.log.size(), 5, cashier.log.size());
		assertEquals("W1 should have one event logged after the Cashier's scheduler is called for the sixth time. Instead, it has: "
				+ w1.getLog().toString(), 2, w1.getLog().size());
		assertEquals("C1 should have an empty event log after the Cashier's scheduler is called for the sixth time. Instead, C1's event log reads: "
				+ c1.getLog().toString(), 0, c1.getLog().size());
		assertEquals("C2 should have an empty event log after the Cashier's scheduler is called for the sixth time. Instead, C2's event log reads: "
				+ c2.getLog().toString(), 1, c2.getLog().size());

	//Step 3 for C2
		cashier.msgPayment(c1, w1.getCheck(), 16.0); //send the message from a customer

		assertTrue("CashierAgent should have logged \"Received msgPayment\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgPayment from customer. Payment = 16.0"));
		assertTrue("Checks should contain a bill with state != paid. It doesn't.",
				cashier.checks.get(0).state != CherysCashierRole.CheckState.paid);
		assertTrue("Checks should contain a bill of price = $15.99. It contains something else instead: $" 
				+ cashier.checks.get(0).total, cashier.checks.get(0).total == 15.99);
		assertTrue("Checks should contain a bill with amountPaid = $16.00. Instead, amountPaid = $" 
				+ cashier.checks.get(0).amountPaid, cashier.checks.get(0).amountPaid == 16.0);
		assertTrue("Checks should contain a bill with the right customer in it. It doesn't.", 
				cashier.checks.get(0).customer == c1);
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's msgPayment), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertTrue("Cashier should contain balance == $124.98. It contains something else instead: $" 
				+ cashier.balance, cashier.balance == 124.98);
		assertEquals("Cashier should have 0 bills in it. It doesn't.",
				0, cashier.checks.size());
		assertTrue("C1 should have logged an event for receiving \"msgChange\" with the correct change, but his last event logged reads instead: " 
				+ c1.getLog().getLastLoggedEvent().toString(), c1.getLog().containsString("Received msgChange from cashier. Change = 0.01"));
		//Checking logs
		assertEquals("Cashier should have six events logged after the Cashier's scheduler is called for the seventh time. Instead, it has "
				+ cashier.log.size(), 6, cashier.log.size());
		assertEquals("W1 should have one event logged after the Cashier's scheduler is called for the seventh time. Instead, it has: "
				+ w1.getLog().toString(), 2, w1.getLog().size());
		assertEquals("C1 should have one event logged after the Cashier's scheduler is called for the seventh time. Instead, it has: "
				+ c1.getLog().toString(), 1, c1.getLog().size());
		assertEquals("C2 should have one event logged after the Cashier's scheduler is called for the seventh time. Instead, it has: "
				+ c2.getLog().toString(), 1, c2.getLog().size());

	//Step 4 for C1
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		//Checking logs
		assertEquals("Cashier should have six events logged after the Cashier's scheduler is called for the eigth time. Instead, it has "
				+ cashier.log.size(), 6, cashier.log.size());
		assertEquals("W1 should have one event logged after the Cashier's scheduler is called for the eigth time. Instead, it has: "
				+ w1.getLog().toString(), 2, w1.getLog().size());
		assertEquals("C1 should have one event logged after the Cashier's scheduler is called for the eigth time. Instead, it has: "
				+ c1.getLog().toString(), 1, c1.getLog().size());
		assertEquals("C2 should have one event logged after the Cashier's scheduler is called for the eigth time. Instead, it has: "
				+ c2.getLog().toString(), 1, c2.getLog().size());
	}
}
