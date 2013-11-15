package simcity.market.test;

import simcity.market.CashierRole;
import junit.framework.*;

public class CashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CashierAgent cashier;
	MockDeliverer waiter;
	MockCustomer customer;
	MockEmployee market1, market2;


	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new CashierAgent("Cashier");		
		customer = new MockCustomer("MockCustomer");		
		waiter = new MockDeliverer("MockWaiter");
		market1 = new MockEmployee("MockMarket1");
		market2 = new MockEmployee("MockMarket2");
	}	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 * Interleaved with "One market bills cashier" test
	 */
	public void testOneNormalCustomerScenario()
	{
		//setUp() runs first before this test!

		customer.cashier = cashier;//You can do almost anything in a unit test.			
		market1.cashier = cashier;
		market2.cashier = cashier;
		//check preconditions
		assertEquals("Cashier should have 0 checks in it. It doesn't.",cashier.checks.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's ComputeCheck is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());

		//step 1a of the test
		//Computing Check
		cashier.msgComputeCheck(waiter, customer, "Steak", customer.name);//send the message from a waiter

		//step 1b
		//Market Bill
		cashier.msgHereIsBill(market1, 50, 0);//send the message from a market

		//check postconditions for step 1b and preconditions for step 2

		assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
				+ market1.log.toString(), 0, market1.log.size());

		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);

		//check postconditions for step 1a and preconditions for step 2

		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());

		assertEquals("Cashier should have 1 check in it. It doesn't.", cashier.checks.size(), 1);

		assertFalse("Cashier's scheduler should have returned true, but didn't.", !cashier.pickAndExecuteAnAction());

		assertEquals(
				"MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());

		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());

		//step 2 of the test
		cashier.msgCustomerPayment(customer, 15, customer.name);

		//check postconditions for step 2 / preconditions for step 3
		assertTrue("Cashier's checks list should contain a check with state == paying. It doesn't.",
				cashier.checks.get(0).cS == CheckState.paying);

		assertTrue("Cashier's checks list should contain a check of price = $15. It contains something else instead: $" 
				+ cashier.checks.get(0).amount, cashier.checks.get(0).amount == 15);

		assertTrue("Cashier's checks list should contain a check with the right customer in it. It doesn't.", 
				cashier.checks.get(0).name.equals(customer.name));


		//step 3
		//NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());

		//check postconditions for step 3 / preconditions for step 4

		assertTrue("Cashier should contain a check with amount == 0. It contains something else instead: $" 
				+ cashier.checks.get(0).amount, cashier.checks.get(0).amount == 0);

		//check postconditions for step 4		
		assertTrue("Cashier should contain a check with state == done. It doesn't.",
				cashier.checks.get(0).cS == CheckState.paid);

		//step 5 (Market Bill)
		assertTrue("CashierBill should contain a bill with state == none. It doesn't.",
				cashier.bills.get(0).bS == BillState.none);

		assertTrue("CashierBill should contain a bill of price = $50. It contains something else instead: $" 
				+ cashier.bills.get(0).amountDue, cashier.bills.get(0).amountDue == 50);

		assertTrue("CashierBill should contain a bill with the right market in it. It doesn't.", 
				cashier.bills.get(0).market.equals(market1));

		assertFalse("Cashier's scheduler should have returned true, but didn't.", !cashier.pickAndExecuteAnAction());

		assertEquals(
				"MockMarket should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ market1.log.toString(), 0, market1.log.size());

		//check postconditions for step 6 / preconditions for step 7

		assertTrue("CashierBill should contain amountPaid= $50. It contains something else instead: $" 
				+ cashier.bills.get(0).amountPaid, cashier.bills.get(0).amountPaid == 50);

		//check postconditions for step 4		
		assertTrue("CashierBill should contain a bill with state == paid. It doesn't.",
				cashier.bills.get(0).bS == BillState.paid);

		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());

	}//end one normal customer scenario

	/**
	 * This tests when customer does not have enough money to pay. The cashier will reassure him that he can pay next time.
	 */
	public void testTwoCustomerNoMoney() {		

		//setUp() runs first before this test!
		customer.cashier = cashier;//You can do almost anything in a unit test.			
		market1.cashier = cashier;
		market2.cashier = cashier;

		//check preconditions
		assertEquals("Cashier should have 0 checks in it. It doesn't.",cashier.checks.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());

		//step 1 of the test
		//Computing Check
		cashier.msgComputeCheck(waiter, customer, "Steak", customer.name);//send the message from a waiter

		//check postconditions for step 1 and preconditions for step 2

		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());

		assertEquals("Cashier's checks list should contain a check in it. It doesn't.", cashier.checks.size(), 1);

		assertFalse("Cashier's scheduler should have returned true, but didn't.", !cashier.pickAndExecuteAnAction());

		assertEquals(
				"MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());

		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());

		//step 2 of the test
		cashier.msgCustomerPayment(customer, 10, customer.name);

		//check postconditions for step 2 / preconditions for step 3
		assertTrue("Cashier's checks list should contain a check with state == paying. It doesn't.",
				cashier.checks.get(0).cS == CheckState.paying);

		assertTrue("Cashier's checks list should contain a check with price = $15. It contains something else instead: $" 
				+ cashier.checks.get(0).amount, cashier.checks.get(0).amount == 15);

		assertTrue("Cashier's checks list should contain a check with the right customer in it. It doesn't.", 
				cashier.checks.get(0).name.equals(customer.name));


		//step 3
		//NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());

		//check postconditions for step 3 / preconditions for step 4

		assertTrue("Cashier's checks list should contain a check with amount greater than 0. It contains something else instead: $" 
				+ cashier.checks.get(0).amount, cashier.checks.get(0).amount > 0);

		//check postconditions for step 4		
		assertTrue("Cashier's checks list should contain a check with state == owe. It doesn't.",
				cashier.checks.get(0).cS == CheckState.owe);

		assertTrue("MockCustomer's event log should not be empty. It is.",
				customer.log.size()>0);

		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	} // end of test two

	/**
	 * This tests when a customer pays with a surplus amount of money and needs to receive change.
	 */
	public void testThreeCustomerPaymentWithChange() {			

		//setUp() runs first before this test!

		customer.cashier = cashier;//You can do almost anything in a unit test.			
		market1.cashier = cashier;
		market2.cashier = cashier;
		//check preconditions
		assertEquals("Cashier should have 0 checks in it. It doesn't.",cashier.checks.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's ComputeCheck is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());

		//step 1 of the test
		//Computing Check
		cashier.msgComputeCheck(waiter, customer, "Steak", customer.name);//send the message from a waiter

		//check postconditions for step 1 and preconditions for step 2

		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());

		assertEquals("Cashier should have 1 check in it. It doesn't.", cashier.checks.size(), 1);

		assertFalse("Cashier's scheduler should have returned true, but didn't.", !cashier.pickAndExecuteAnAction());

		assertEquals(
				"MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());

		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());

		//step 2 of the test
		cashier.msgCustomerPayment(customer, 20, customer.name);

		//check postconditions for step 2 / preconditions for step 3
		assertTrue("Cashier's checks list should contain a check with state == paying. It doesn't.",
				cashier.checks.get(0).cS == CheckState.paying);

		assertTrue("Cashier's checks list should contain a check of price = $15. It contains something else instead: $" 
				+ cashier.checks.get(0).amount, cashier.checks.get(0).amount == 15);

		assertTrue("Cashier's checks list should contain a check with the right customer in it. It doesn't.", 
				cashier.checks.get(0).name.equals(customer.name));


		//step 3
		//NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());

		//check postconditions for step 3 / preconditions for step 4

		assertTrue("Cashier should contain a check with amount == -$5 (change of $5). It contains something else instead: $" 
				+ cashier.checks.get(0).amount, cashier.checks.get(0).amount == -5);

		//check postconditions for step 4		
		assertTrue("Cashier should contain a check with state == done. It doesn't.",
				cashier.checks.get(0).cS == CheckState.paid);

		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());

	} // end of test three

	/**
	 * This tests the first normal scenario when one market bills the cashier.
	 */
	public void testFourNormalMarketPayment() {

		//setUp() runs first before this test!
		customer.cashier = cashier;//You can do almost anything in a unit test.			
		market1.cashier = cashier;
		market2.cashier = cashier;
		cashier.money = 100.00;

		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bills.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's message is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());

		//step 1 of the test
		//Receiving bill
		cashier.msgHereIsBill(market1, 50, 0);//send the message from a market

		//check postconditions for step 1 and preconditions for step 2

		assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
				+ market1.log.toString(), 0, market1.log.size());

		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);

		//step 2
		assertTrue("CashierBill should contain a bill with state == none. It doesn't.",
				cashier.bills.get(0).bS == BillState.none);

		assertTrue("CashierBill should contain a bill of price = $50. It contains something else instead: $" 
				+ cashier.bills.get(0).amountDue, cashier.bills.get(0).amountDue == 50);

		assertTrue("CashierBill should contain a bill with the right market in it. It doesn't.", 
				cashier.bills.get(0).market.equals(market1));

		assertFalse("Cashier's scheduler should have returned true, but didn't.", !cashier.pickAndExecuteAnAction());

		assertEquals(
				"MockMarket should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ market1.log.toString(), 0, market1.log.size());

		//check postconditions for step 3 / preconditions for step 4

		assertTrue("CashierBill should contain amountPaid= $50. It contains something else instead: $" 
				+ cashier.bills.get(0).amountPaid, cashier.bills.get(0).amountPaid == 50);

		//check postconditions for step 4		
		assertTrue("CashierBill should contain a bill with state == paid. It doesn't.",
				cashier.bills.get(0).bS == BillState.paid);

		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	} // end of test four

	/**
	 * This tests market billing from two separate markets
	 */
	public void testFivePayTwoMarkets() {

		//setUp() runs first before this test!
		customer.cashier = cashier;//You can do almost anything in a unit test.			
		market1.cashier = cashier;
		market2.cashier = cashier;
		cashier.money = 150.00;

		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bills.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's message is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());

		//step 1a of the test
		//Receiving bill 1 from market1
		cashier.msgHereIsBill(market1, 50, 0);//send the message from a market

		//check postconditions for step 1a and preconditions for step 2

		assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
				+ market1.log.toString(), 0, market1.log.size());

		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);

		//step 1b of the test
		//Receiving bill 2 from market2
		cashier.msgHereIsBill(market2, 70, 1);//send the message from a market

		//check postconditions for step 1b and preconditions for step 2

		assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
				+ market2.log.toString(), 0, market2.log.size());

		assertEquals("Cashier should have 2 bills in it. It doesn't.", cashier.bills.size(), 2);

		//step 2
		assertTrue("Cashier's Bill 0 should have state == none. It doesn't.",
				cashier.getBill(0).bS == BillState.none);

		assertTrue("Cashier's Bill 1 should have state == none. It doesn't.",
				cashier.getBill(1).bS == BillState.none);

		assertTrue("Cashier's Bill 0 should contain price = $50. It contains something else instead: $" 
				+ cashier.getBill(0).amountDue, cashier.getBill(0).amountDue == 50);

		assertTrue("Cashier's Bill 1 should contain price = $70. It contains something else instead: $" 
				+ cashier.getBill(1).amountDue, cashier.getBill(1).amountDue == 70);

		assertTrue("Cashier's Bill 0 should contain a bill with market1. It doesn't.", 
				cashier.getBill(0).market.equals(market1));

		assertTrue("Cashier's Bill 1 should contain a bill with market2. It doesn't.", 
				cashier.getBill(1).market.equals(market2));

		assertFalse("Cashier's scheduler should have returned true, but didn't.", !cashier.pickAndExecuteAnAction());

		assertEquals(
				"MockMarket should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ market1.log.toString(), 0, market1.log.size());

		//check postconditions for step 3 / preconditions for step 4

		assertTrue("CashierBill should contain amountPaid= $50. It contains something else instead: $" 
				+ cashier.getBill(0).amountPaid, cashier.getBill(0).amountPaid == 50);

		//check postconditions for step 4		
		assertTrue("CashierBill should contain a bill with state == paid. It doesn't.",
				cashier.getBill(0).bS == BillState.paid);

		assertFalse("Cashier's scheduler should have returned true, but didn't.", !cashier.pickAndExecuteAnAction());

		assertEquals(
				"MockMarket should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ market2.log.toString(), 0, market2.log.size());

		//check postconditions for step 3 / preconditions for step 4

		assertTrue("CashierBill should contain amountPaid= $70. It contains something else instead: $" 
				+ cashier.getBill(1).amountPaid, cashier.getBill(1).amountPaid == 70);

		//check postconditions for step 4		
		assertTrue("CashierBill should contain a bill with state == paid. It doesn't.",
				cashier.getBill(1).bS == BillState.paid);

		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	} // end of test five

	/**
	 * This tests when the cashier cannot fulfill all payments.
	 */
	public void testSixCannotFulfillPayment() {

		//setUp() runs first before this test!
		customer.cashier = cashier;//You can do almost anything in a unit test.			
		market1.cashier = cashier;
		market2.cashier = cashier;
		cashier.money = 40.00;

		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.bills.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's message is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());

		//step 1 of the test
		//Receiving bill
		cashier.msgHereIsBill(market1, 100, 0);//send the message from a market

		//check postconditions for step 1 and preconditions for step 2

		assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
				+ market1.log.toString(), 0, market1.log.size());

		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);

		//step 2
		assertTrue("CashierBill should contain a bill with state == none. It doesn't.",
				cashier.bills.get(0).bS == BillState.none);

		assertTrue("CashierBill should contain a bill of price = $100. It contains something else instead: $" 
				+ cashier.bills.get(0).amountDue, cashier.bills.get(0).amountDue == 100);

		assertTrue("CashierBill should contain a bill with the right market in it. It doesn't.", 
				cashier.bills.get(0).market.equals(market1));

		assertFalse("Cashier's scheduler should have returned true, but didn't.", !cashier.pickAndExecuteAnAction());

		assertEquals(
				"MockMarket should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ market1.log.toString(), 0, market1.log.size());

		//check postconditions for step 3 / preconditions for step 4

		assertTrue("CashierBill should contain amountPaid= $40. It contains something else instead: $" 
				+ cashier.bills.get(0).amountPaid, cashier.bills.get(0).amountPaid == 40);

		assertTrue("CashierBill should contain amountDue= $60. It contains something else instead: $" 
				+ cashier.bills.get(0).amountDue, cashier.bills.get(0).amountDue == 60);

		//check postconditions for step 4		
		assertTrue("CashierBill should contain a bill with state == owe. It doesn't.",
				cashier.bills.get(0).bS == BillState.owe);

		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	} // end of test six
}
