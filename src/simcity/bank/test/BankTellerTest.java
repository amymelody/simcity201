package simcity.bank.test;

import java.util.ArrayList;
import java.util.List;

import simcity.bank.BankDepositorRole;
import simcity.bank.test.mock.MockBankDepositor;
import simcity.bank.test.mock.MockBankManager;
import simcity.bank.test.mock.MockBankTeller;
import simcity.bank.test.mock.MockPerson;
import simcity.bank.BankManagerRole.myCustomer;
import simcity.bank.BankDepositorRole.CustomerState;
import simcity.bank.BankTellerRole;
import junit.framework.*;

public class BankTellerTest extends TestCase
{
        // Needed for tests
        BankTellerRole teller;
        
        MockBankDepositor customer;
        MockBankManager manager;
        MockPerson person;
        

        /**
         * This method is run before each test. You can use it to instantiate the class variables
         * for your agent and mocks, etc.
         */
        public void setUp() throws Exception{
                super.setUp();                
                teller = new BankTellerRole();
                customer = new MockBankDepositor("MockDepositor");
                manager = new MockBankManager("MockManager");
                person = new MockPerson("Person");
                
        }


        /**
         * This tests the first normal scenario (Customer comes in and order a list of items)
         */
        public void testBankNormativeScenario() //tests deposit
        {
                // Set up
                teller.setManager(manager);
                teller.setDepositor(customer);
                // Check preconditions for Step 1a

                assertEquals("Teller should have no customers in its list of customers" ,0, teller.customers.size());
                teller.msgHelpCustomer(customer, 400);
                assertEquals("There should be one customer in the list of tellers customers", 1, teller.customers.size());
                assertTrue("Tellers scheduler should have returned true to help the customer", teller.pickAndExecuteAnAction());
                
                assertTrue("MockCustomer should have logged an event for receiving \"msgMakeRequest\" "
                 + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received message from teller to make a request"));
       
                
               teller.msgMakeDeposit(customer, 400);
               assertTrue("Tellers scheduler should have returned true", teller.pickAndExecuteAnAction());
               
                
        }

                
                
        }