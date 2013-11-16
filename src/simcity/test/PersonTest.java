package simcity.test;

import simcity.PersonAgent;
import simcity.PersonAgent.LocationState;
import simcity.PersonAgent.NourishmentState;
import simcity.PersonAgent.TransportationState;
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
		customer = new MockRestCustomerRole("joshCustomer");
		person.addRole(customer, "joshCustomerRole");
		person.msgIncome(400);
	}	
	
	public void testRestaurantCustomer()
	{
		assertEquals("Person should have 1 role. It doesn't.", 1, person.roles.size());
		
		assertEquals("First role in roles should be inactive. It isn't.", false, person.roles.get(0).isActive());
		
		assertEquals("Person should have an empty event log before the first message. Instead, the Person's event log reads: "
				+ person.log.toString(), 0, person.log.size());
		
		assertEquals("Person should have $400. It doesn't.", 400, person.getMoney());
		
		assertEquals("Person's nourishment state should be normal. It isn't", NourishmentState.normal, person.state.getNState());
		
		assertEquals("Person's location state should be outside. It isn't", LocationState.outside, person.state.getLState());
		
		person.msgImHungry();
		
		assertEquals("Person's nourishment state should be gotHungry. It isn't", NourishmentState.gotHungry, person.state.getNState());
		
		assertTrue("Person should have logged \"Received msgImHungry\" but didn't. His last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgImHungry"));
		
		assertTrue("Person's scheduler should have returned true (it should call goToRestaurant, which then calls goToDestination), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person's location state should be atDestination. It isn't", LocationState.atDestination, person.state.getLState());
		
		assertTrue("Person's scheduler should have returned true (it should call goToRestaurant), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person should have 1 role. It doesn't.", person.roles.size(), 1);
		
		assertEquals("First role in roles should be active. It isn't.", true, person.roles.get(0).isActive());
		
		assertEquals("Person's nourishment state should be hungry. It isn't", NourishmentState.hungry, person.state.getNState());
		
		assertEquals("Person's location state should be restaurant. It isn't", LocationState.restaurant, person.state.getLState());
		
		assertTrue("Person's scheduler should have returned true (it should call customer role's scheduler), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person's nourishment state should be normal. It isn't", NourishmentState.normal, person.state.getNState());
		
		assertEquals("Person's location state should be outside. It isn't", LocationState.outside, person.state.getLState());
		
		assertEquals("First role in roles should be inactive. It isn't.", false, person.roles.get(0).isActive());
		
		assertEquals("Person should have $384. It doesn't.", 384, person.getMoney());
		
		assertFalse("Person's scheduler should have returned false, but didn't.", person.pickAndExecuteAnAction());
		
	}
}
