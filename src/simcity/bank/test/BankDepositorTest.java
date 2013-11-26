package simcity.bank.test;

import java.util.ArrayList;
import java.util.List;

import simcity.bank.BankDepositorRole;
import simcity.bank.test.mock.MockBankManager;
import simcity.bank.test.mock.MockBankTeller;
import simcity.bank.test.mock.MockPerson;
import simcity.bank.BankManagerRole.myCustomer;
import simcity.bank.BankDepositorRole.CustomerState;
import junit.framework.*;

public class BankDepositorTest extends TestCase
{
	// Needed for tests
	BankDepositorRole customer;
	
	MockBankTeller teller;
	MockBankManager manager;
	MockPerson person;
	

	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		customer = new BankDepositorRole();
		teller = new MockBankTeller("MockTeller");
		manager = new MockBankManager("MockManager");
		person = new MockPerson("Person");
		
	}


	/**
	 * This tests the first normal scenario (Customer comes in and order a list of items)
	 */
	public void testBankNormativeScenario() //tests deposit
	{
		// Set up
		customer.setManager(manager);
		customer.setPerson(person);

		// Check preconditions for Step 1a
		assertEquals("Customer transactionAmount should be 0", 0, customer.getTransactionAmount());
		
		customer.msgMakeDeposit(500);
		assertTrue("Customer should have customerState making Transaction", customer.getCustomerState() == CustomerState.makingTransaction);
		assertEquals("Customer transactionAmount should be 500", 500, customer.getTransactionAmount());
		
		assertTrue("Customer's scheduler should have returned true", customer.pickAndExecuteAnAction());
		
		assertTrue("MockManager should have logged an event for receiving \"msgMakeTransaction\" "
                 + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Received message from customer that he wants to make a transaction"));

		
		customer.msgMakeRequest(teller);
		assertTrue("Customer should have customer state making deposit", customer.getCustomerState() == CustomerState.makingDeposit);
		assertTrue("Customer's scheduler should return true", customer.pickAndExecuteAnAction());
		assertTrue("MockTeller should have logged an event for receiving \"msgMakeDeposit\" "
                + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Received message from Bank customer to make a deposit"));
		assertTrue("Customer should have customerState of being Helped", customer.getCustomerState() == CustomerState.beingHelped);
		
		
		customer.msgTransactionComplete();
		assertTrue("Customer should have customerState leaving", customer.getCustomerState() == CustomerState.leaving);
		assertTrue("Customer's scheduler should return true", customer.pickAndExecuteAnAction());
		//assertTrue()
		
	}

		
		
	}
