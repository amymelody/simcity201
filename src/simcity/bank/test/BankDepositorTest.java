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
                customer.unitTesting = true;


                
        }


        //Tests basic deposit function for bank
        public void testBankNormativeScenario() 
        {
            //setup
        	person.setCurrentMoney(1000);
        	manager.setBankMoney(1000);
                // Check preconditions 
        		assertEquals("Persons money should be 1000", 1000, person.getCurrentMoney());
                assertEquals("Customer transactionAmount should be 0", 0, customer.getTransactionAmount());
                assertTrue("Customer should have customer state entered", customer.getCustomerState() == CustomerState.entered);
                assertFalse("Customer's scheduler should return false, nothing to do", customer.pickAndExecuteAnAction());               
                assertEquals("MockCustomer should have an empty event log before scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
               
                
                //Person sends message to customer to make a deposit of 500
                customer.msgMakeDeposit(500);
                assertTrue("Customer should have customerState making Transaction", 
                		customer.getCustomerState() == CustomerState.makingTransaction);                
                assertTrue("Customer's scheduler should have returned true", customer.pickAndExecuteAnAction());                               
                assertEquals("Customer transactionAmount should be 500", 500, customer.getTransactionAmount());
                
               
                           
                //Teller sends customer message to be helped
                customer.msgMakeRequest(teller);                              
                assertTrue("Customer's state should be makingRequest", customer.getCustomerState() == CustomerState.makingRequest);
                assertTrue("Customer's scheduler should have returned true", customer.pickAndExecuteAnAction());
                assertTrue("Customers state should be beingHelped", customer.getCustomerState() == CustomerState.beingHelped);
                
                //teller sends message to customer saying transaction was complete
                customer.msgTransactionComplete();                
                assertTrue("Customer's state should be leaving", customer.getCustomerState() == CustomerState.leaving);
                assertEquals("MockPerson should now have current money - 500", 500, person.getCurrentMoney());
                assertEquals("MockManager should now have current money + 500", 1500, manager.getBankMoney() );
                assertTrue("Customer's scheduler should have returned true", customer.pickAndExecuteAnAction());
                      
                
        }
        
        //Tests robbery
        public void testRobbery(){
        	person.setCurrentMoney(1000);
        	
        }

                
                
        }