package simcity.bank.test;

import java.util.ArrayList;
import java.util.List;

import simcity.bank.BankManagerRole;
import simcity.bank.test.mock.MockBankDepositor;
import simcity.bank.test.mock.MockBankTeller;
import simcity.bank.test.mock.MockPerson;
import simcity.bank.BankManagerRole.myCustomer;
import simcity.bank.BankManagerRole.CustomerState;


import junit.framework.*;

public class BankManagerTest extends TestCase
{
	// Needed for tests
	BankManagerRole manager;
	
	MockBankTeller teller;
	MockBankDepositor customer;
	MockPerson person;
	

	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		customer = new MockBankDepositor("Customer");
		teller = new MockBankTeller("MockTeller");
		person = new MockPerson("Person");
		manager = new BankManagerRole("Manager");
		manager.unitTesting = true;
		manager.working = true;
		manager.setPerson(person);
	}


	/**
	 * This tests the first normal scenario (Customer comes in and order a list of items)
	 */
	public void testBankNormativeScenario() //tests deposit
	{
		//Test pre conditions
		manager.setDepositor(customer);
		manager.setTeller(teller);
		manager.setBankMoney(1000);
		
		
		//Manager should have no accounts
		assertEquals("Bank should have no customer accounts", 0, manager.customers.size());
		assertEquals("BankMoney should be 1000", 1000, manager.getBankMoney());
		//log
		
		
		manager.msgTransaction(customer);
		assertEquals("Bank should now have one customer in accounts", 1, manager.customers.size());
		assertTrue("Manager scheduler should return true to find a teller to help customer", manager.pickAndExecuteAnAction());
		//assertTrue("MockTeller should have logged an event for receiving \"msgHelpCustomer\" "
	      //         + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Received message from manager to work with customer"));
	                
		
		manager.msgProcessTransaction(teller, customer, 500);
		assertEquals("Bank money should now be at 1500", 1500, manager.getBankMoney());
		assertTrue("Bank scheduler should have returned true", manager.pickAndExecuteAnAction());
		assertTrue("MockTeller should have logged an event for receiving \"msgTransactionComplete\" "
                + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Received message from manager that transaction was successful"));
	
	}

		
}		
	
