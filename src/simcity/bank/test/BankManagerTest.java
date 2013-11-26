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
	}


	/**
	 * This tests the first normal scenario (Customer comes in and order a list of items)
	 */
	public void testBankNormativeScenario() //tests deposit
	{
		
		
	}

		
		
	}
