package simcity.cherysrestaurant.test;

import simcity.cherysrestaurant.CherysCashierRole;
import simcity.cherysrestaurant.interfaces.CherysCustomer;
import simcity.cherysrestaurant.interfaces.CherysWaiter;
import simcity.cherysrestaurant.test.mock.MockCherysCustomer;
import simcity.cherysrestaurant.test.mock.MockCherysWaiter;

import junit.framework.*;

public class CherysOneWaiterOneCustomerTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CherysCashierRole cashier;
	CherysWaiter waiter;
	CherysCustomer customer;

	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception
	{
		super.setUp();
		cashier = new CherysCashierRole("cashier");
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
				0, cashier.checks.size());
		//Checking logs
		assertEquals("CashierAgent should have an empty event log before the Cashier's msgProduceCheck is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
				+ waiter.getLog().toString(), 0, waiter.getLog().size());
		assertEquals("MockCustomer should have an empty event log before the Cashier's scheduler is called. Instead, the MockCustomer's event log reads: "
				+ customer.getLog().toString(), 0, customer.getLog().size());

	//Step 1
		cashier.msgProduceCheck(waiter, "Pizza", 2); //send the message from a waiter

		assertTrue("CashierAgent should have logged \"Received msgProduceCheck\" but didn't. His log reads instead: "
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgProduceCheck from waiter. Choice = Pizza. Table = 2"));
		assertEquals("Cashier should have 1 bill in it. It doesn't.",
				1, cashier.checks.size());
		assertFalse("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.",
				cashier.pickAndExecuteAnAction());
		//Checking logs
		assertEquals("Cashier should have one event logged after the Cashier's scheduler is called for the first time. Instead, it has "
				+ cashier.log.size(), 1, cashier.log.size());
		assertEquals("MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
				+ waiter.getLog().toString(), 0, waiter.getLog().size());
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
				+ customer.getLog().toString(), 0, customer.getLog().size());
		
	//Step 2
		cashier.msgGiveCheck(waiter, 2); //send the message from a waiter

		assertTrue("CashierAgent should have logged \"Received msgGiveCheck\" but didn't. His log reads instead: "
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgGiveCheck from waiter. Table = 2"));
		assertTrue("Checks should contain a bill with state == askedFor. It doesn't.",
				cashier.checks.get(0).state == CherysCashierRole.CheckState.askedFor);
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's msgGiveCheck), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertTrue("MockWaiter should have logged \"Received msgHereIsCheck\" but didn't. His log reads instead: " 
				+ waiter.getLog().getLastLoggedEvent().toString(), waiter.getLog().containsString("Received msgHereIsCheck from cashier. Order = Pizza. Total = 8.99"));
		//Checking logs
		assertEquals("Cashier should have two events logged after the Cashier's scheduler is called for the second time. Instead, it has "
				+ cashier.log.size(), 2, cashier.log.size());
		assertEquals("MockWaiter should have one event logged after the Cashier's scheduler is called for the second time. Instead, it has: "
				+ waiter.getLog().toString(), 1, waiter.getLog().size());
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the second time. Instead, the MockCustomer's event log reads: "
				+ customer.getLog().toString(), 0, customer.getLog().size());

	//Step 3
		cashier.msgPayment(customer, waiter.getCheck(), 9.0); //send the message from a customer

		assertTrue("CashierAgent should have logged \"Received msgPayment\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgPayment from customer. Payment = 9.0"));
		assertTrue("Checks should contain a bill with state != paid. It doesn't.",
				cashier.checks.get(0).state != CherysCashierRole.CheckState.paid);
		assertTrue("Checks should contain a bill of price = $8.99. It contains something else instead: $" 
				+ cashier.checks.get(0).total, cashier.checks.get(0).total == 8.99);
		assertTrue("Checks should contain a bill with amountPaid = $9.00. Instead, amountPaid = $" 
				+ cashier.checks.get(0).amountPaid, cashier.checks.get(0).amountPaid == 9.0);
		assertTrue("Checks should contain a bill with the right customer in it. It doesn't.", 
				cashier.checks.get(0).customer == customer);
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's msgPayment), but didn't.", 
				cashier.pickAndExecuteAnAction());
		assertTrue("Cashier should contain balance == $108.99. It contains something else instead: $" 
				+ cashier.balance, cashier.balance == 108.99);
		assertEquals("Cashier should have 0 bills in it. It doesn't.",
				0, cashier.checks.size());
		assertTrue("MockCustomer should have logged an event for receiving \"msgChange\" with the correct change, but his last event logged reads instead: " 
				+ customer.getLog().getLastLoggedEvent().toString(), customer.getLog().containsString("Received msgChange from cashier. Change = 0.01"));
		//Checking logs
		assertEquals("Cashier should have three events logged after the Cashier's scheduler is called for the third time. Instead, it has "
				+ cashier.log.size(), 3, cashier.log.size());
		assertEquals("MockWaiter should have one event logged after the Cashier's scheduler is called for the third time. Instead, it has: "
				+ waiter.getLog().toString(), 1, waiter.getLog().size());
		assertEquals("MockCustomer should have one event logged after the Cashier's scheduler is called for the third time. Instead, it has: "
				+ customer.getLog().toString(), 1, customer.getLog().size());

	//Step 4
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		//Checking logs
		assertEquals("Cashier should have three events logged after the Cashier's scheduler is called for the fourth time. Instead, it has "
				+ cashier.log.size(), 3, cashier.log.size());
		assertEquals("MockWaiter should have one event logged after the Cashier's scheduler is called for the fourth time. Instead, it has: "
				+ waiter.getLog().toString(), 1, waiter.getLog().size());
		assertEquals("MockCustomer should have one event logged after the Cashier's scheduler is called for the fourth time. Instead, it has: "
				+ customer.getLog().toString(), 1, customer.getLog().size());
	}
}
