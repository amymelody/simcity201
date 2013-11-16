package simcity.test;

import simcity.PersonAgent;
import simcity.test.mock.MockRestCustomerRole;
import junit.framework.*;

public class PersonTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	PersonAgent person;
	MockRestCustomerRole customer;
	
	public void setUp() throws Exception{
		super.setUp();		
		person = new PersonAgent("person");
	}	
	
	public void testRestaurantCustomer()
	{
		assertEquals("Person should have an empty event log before the first message. Instead, the Person's event log reads: "
				+ person.log.toString(), 0, person.log.size());
		
		assertEquals("Person should have $400. It doesn't.", 400, person.getMoney());
		
		person.msgImHungry();
		
		assertTrue("Person should have logged \"Received msgImHungry\" but didn't. His last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgImHungry"));
		
		
		
		customer.person = person;
		
		//check preconditions
		assertEquals("Person should have 0 checks in it. It doesn't.", 0, person.checks.size());		
		
		assertEquals("Person should have $200. It doesn't.", 200, person.getCash());	
		
		assertEquals("Person should have an empty event log before the Person's msgProduceCheck is called. Instead, the Person's event log reads: "
						+ person.log.toString(), 0, person.log.size());
		
		//step 1: Receive message to produce check
		person.msgProduceCheck(waiter, customer, "steak");

		//check postconditions for step 1 and preconditions for step 2
		assertTrue("Person should have logged \"Received msgProduceCheck\" but didn't. His last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgProduceCheck"));
		
		assertEquals("MockWaiter should have an empty event log before the Person's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals("Person should have 1 check in it. It doesn't.", person.checks.size(), 1);
		
		assertTrue("First Check in checks should have state == Created. It doesn't.", person.checks.get(0).getState() == CheckState.Created);
		
		assertTrue("First Check in checks should have charge == $16. Instead, the charge is: $" + person.checks.get(0).getCharge(), person.checks.get(0).getCharge() == 16);
		
		assertTrue("First Check in checks should have the right customer. It doesn't.", person.checks.get(0).getCust() == customer);
		
		assertTrue("First Check in checks should have the right waiter. It doesn't.", person.checks.get(0).getWaiter() == waiter);
		
		//step 2: Run the scheduler
		assertTrue("Person's scheduler should have returned true (it should call giveToWaiter), but didn't.", person.pickAndExecuteAnAction());
		
		//check postconditions for step 2 and preconditions for step 3
		assertTrue("First Check in checks should have state == GivenToWaiter. It doesn't.", person.checks.get(0).getState() == CheckState.GivenToWaiter);
		
		assertTrue("MockWaiter should have logged an event for receiving \"msgHereIsCheck\" with the correct customer and charge. His last event logged reads instead: "
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received msgHereIsCheck from person. Customer = mockcustomer. Charge = $16"));
		
		assertEquals("MockCustomer should have an empty event log after the Person's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
		
		//step 3: Receive message Payment from customer
		person.msgPayment(customer, 20);
		
		//check postconditions for step 3 and preconditions for step 4
		assertTrue("Person should have logged \"Received msgPayment\" but didn't. His last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgPayment"));
		
		assertEquals("MockCustomer should have an empty event log before the Person's scheduler is called. Instead, the MockCustomer's event log reads: "
				+ customer.log.toString(), 0, customer.log.size());
		
		assertTrue("First Check in checks should have state == Paid. It doesn't.", person.checks.get(0).getState() == CheckState.Paid);
		
		assertTrue("First Check in checks should have payment == $20. Instead, the payment is: $" + person.checks.get(0).getPayment(), person.checks.get(0).getPayment() == 20);
		
		//step 4: Run the scheduler again
		assertTrue("Person's scheduler should have returned true (it should call giveCustomerChange), but didn't.", person.pickAndExecuteAnAction());
		
		//check postconditions for step 4
		assertTrue("First Check in checks should have state == Done. It doesn't.", person.checks.get(0).getState() == CheckState.Done);
		
		assertEquals("Person should have $216. It doesn't.", 216, person.getCash());	
		
		assertTrue("MockCustomer should have logged an event for receiving \"msgChange\" with the correct change. His last event logged reads instead: "
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received msgChange from person. Change = $4"));
		
		assertFalse("Person's scheduler should have returned false (nothing left to do), but didn't.", person.pickAndExecuteAnAction());
		
	}
}
