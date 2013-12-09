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
                
                customer.setManager(manager);
                customer.setPerson(person);
                
                person.setCurrentMoney(1000);
                
        }


        //Tests basic deposit function for bank
        public void testBankNormativeScenario() 
        {
                // Set up
               

                // Check preconditions 
                assertEquals("Customer transactionAmount should be 0", 0, customer.getTransactionAmount());
                //assertTrue("Customer should have customer state entered", customer.getCustomerState() == CustomerState.entered);
                assertFalse("Customer's scheduler should return false, nothing to do", customer.pickAndExecuteAnAction());
                
                assertEquals("MockCustomer should have an empty event log before scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
               
                
                //Person sends message to customer to make a deposit of 500
                customer.msgMakeDeposit(500);
                assertTrue("Customer's scheduler should have returned true", customer.pickAndExecuteAnAction());

                assertTrue("MockCustomer should have recieved logged event to take on role of customer"
                        + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received message from person agent to take on role of depositor"));
                assertTrue("Customer should have customerState making Transaction", 
                		customer.getCustomerState() == CustomerState.makingTransaction);
                assertEquals("Customer transactionAmount should be 500", 500, customer.getTransactionAmount());
                
                assertTrue("Customer's state should be waiting", customer.getCustomerState() == CustomerState.waiting);
                assertTrue("MockManager should have recieved logged event to take on customer"
                        + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Received message from customer that he wants to make a transaction"));
                
                
                
                //Teller sends customer message to be helped
                customer.msgMakeRequest(teller);
                
                //postcondition
                assertTrue("MockCustomer should have recieved logged event to make request to teller"
                        + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received message from teller to make a request"));
                assertTrue("Customer's state should be makingRequest", customer.getCustomerState() == CustomerState.makingRequest);
                assertTrue("Customer's scheduler should have returned true", customer.pickAndExecuteAnAction());
                assertTrue("MockTeller should have recieved logged event with customers request"
                        + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received message from customer with his request"));
                assertTrue("Customers state should be beingHelped", customer.getCustomerState() == CustomerState.beingHelped);
                
                //teller sends message to customer saying transaction was complete
                customer.msgTransactionComplete();
                
                //postconditions
                assertTrue("MockCustomer should have recieved logged event that his transaction was successful"
                        + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received message from teller that transaction was successful."));
                assertTrue("Customer's state should be leaving", customer.getCustomerState() == CustomerState.leaving);
                assertTrue("MockPerson should have recieved logged event his expense has increased"
                        + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received msgExpense from bankCustomer"));
               
                assertEquals("MockPerson should now have current money + 500", 1500, person.getCurrentMoney());
                assertTrue("Customer's scheduler should have returned true", customer.pickAndExecuteAnAction());
                
                
                assertTrue("Customers state should be beingHelped", customer.getCustomerState() == CustomerState.beingHelped);
                
                
                
                
               

                
                
        }

                
                
        }