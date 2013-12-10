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
                assertTrue("Customer's scheduler should have returned true", customer.pickAndExecuteAnAction());
                      
                
        }
        
        //Tests robbery
        public void testRobbery(){
        	person.setCurrentMoney(1000);
        	assertEquals("Persons money should be 1000", 1000, person.getCurrentMoney());
            assertEquals("Customer transactionAmount should be 0", 0, customer.getTransactionAmount());
            assertFalse("Customer's scheduler should return false, nothing to do", customer.pickAndExecuteAnAction());               
            assertEquals("MockCustomer should have an empty event log before scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
					+ customer.log.toString(), 0, customer.log.size());
            assertFalse("Customer's scheduler should have returned false", customer.pickAndExecuteAnAction());                               

            
        	customer.msgImARobber();
        	
        	assertEquals("Customer transactionAmount should be 0", 0, customer.getTransactionAmount());
        	assertTrue("person should have bool robber is true", customer.getRobberStatus());
        	assertTrue("Customer state should be robber entered", customer.getCustomerState() == CustomerState.robberEntered);
        	assertTrue("Customers scheduler should have returned true", customer.pickAndExecuteAnAction());
        	
        	//post conditions
        	assertTrue("Manager should have logged \"Manager is being robbed\" but didn't",
    				manager.log.containsString("Manager is being robbed")); 
        	assertEquals("Customer should now have 300 more after action robBank", 1300, person.getCurrentMoney()); 
        	
        	
        	customer.msgYoureDead();

        	assertEquals("Customer transactionAmount should be 0", 0, customer.getTransactionAmount());
        	assertTrue("customer should have customer state robber killed", customer.getCustomerState() == CustomerState.robberKilled);
        	assertTrue("Customers scheduler should have returned true", customer.pickAndExecuteAnAction());
        	assertEquals("Customer should have given money back to manager, should now have 1000 again", 1000, person.getCurrentMoney());
        	assertTrue("Manager should have logged \"Got my money back\" but didn't",
    				manager.log.containsString("Got my money back")); 	
        	
        	customer.msgLeaveMyBank();
        	
        	assertTrue("Customer should have customer state leaving", customer.getCustomerState() == CustomerState.leaving);
        	assertTrue("Customers scheduler should have returned true", customer.pickAndExecuteAnAction());
        	assertFalse("Robber status should now be false", customer.getRobberStatus());
        	assertTrue("Person should have logged \"goodGuyAgain\" but didn't",
    				person.log.containsString("goodGuyAgain"));    
        	assertTrue("Person should have logged \"left destination\" but didn't",
    				person.log.containsString("left destination")); 
        	}
        	
        
        
        public void testLoan(){
        	person.setCurrentMoney(0);
        	manager.setBankMoney(1000);
            // Check preconditions 
    		assertEquals("Persons money should be 0", 0, person.getCurrentMoney());
            assertEquals("Customer transactionAmount should be 0", 0, customer.getTransactionAmount());
            assertTrue("Customer should have customer state entered", customer.getCustomerState() == CustomerState.entered);
            assertFalse("Customer's scheduler should return false, nothing to do", customer.pickAndExecuteAnAction());               
            assertEquals("MockCustomer should have an empty event log before scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
					+ customer.log.toString(), 0, customer.log.size());
           
        	//Person tries to make a withdrawal
            customer.msgMakeWithdrawal(300);
            assertTrue("Customer should have customerState making Transaction", 
            		customer.getCustomerState() == CustomerState.makingTransaction);                
            assertTrue("Customer's scheduler should have returned true", customer.pickAndExecuteAnAction());                               
            assertEquals("Customer transactionAmount should be -300", -300, customer.getTransactionAmount());
            assertTrue("Manager should have logged \"starting transaction\" but didn't",
    				manager.log.containsString("starting transaction"));
            
            customer.msgMakeRequest(teller);
            assertTrue("Customer should have customerState making request", 
            		customer.getCustomerState() == CustomerState.makingRequest);                
            assertTrue("Customer's scheduler should have returned true", customer.pickAndExecuteAnAction());               
            assertTrue("Teller should have logged \"Received message from customer with his request\" but didn't",
    				teller.log.containsString("Received message from customer with his request"));
            assertTrue("Customer should have customerState beingHelped", 
            		customer.getCustomerState() == CustomerState.beingHelped);               
        
            customer.msgCannotMakeTransaction();
            assertTrue("Customer should have customerState making loan", 
            		customer.getCustomerState() == CustomerState.makingLoan); 
            assertTrue("Customer's scheduler should have returned true", customer.pickAndExecuteAnAction());               
            assertTrue("Teller should have logged \"msg make loan request\" but didn't",
    				teller.log.containsString("msg make loan request"));
            
            
            customer.msgTransactionComplete();
            assertTrue("Customer should have customerState leaving", 
            		customer.getCustomerState() == CustomerState.leaving); 
            assertTrue("Customer's scheduler should have returned true", customer.pickAndExecuteAnAction());               
            assertEquals("Person should have 300 now", 300, person.getCurrentMoney());
            assertTrue("Person should have logged \"left destination\" but didn't",
    				person.log.containsString("left destination")); 
        
        }
        
        
        	
        }

                
                
        