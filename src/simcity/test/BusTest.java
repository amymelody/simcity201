package simcity.test;

import simcity.BusAgent;
import simcity.BusAgent.BusState;
import simcity.BusAgent.PassengerState;
import simcity.test.mock.MockBusStop;
import simcity.test.mock.MockPerson;
import simcity.interfaces.Person;
import simcity.joshrestaurant.JoshCashierRole.CheckState;
import junit.framework.*;

import java.util.*;

public class BusTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	BusAgent bus;
	MockBusStop busStop1;
	MockBusStop busStop2;
	MockBusStop busStop3;
	MockBusStop busStop4;
	MockPerson person1;
	
	public void setUp() throws Exception{
		super.setUp();	
		bus = new BusAgent("bus");
		busStop1 = new MockBusStop("busStop1");
		busStop2 = new MockBusStop("busStop2");
		busStop3 = new MockBusStop("busStop3");
		busStop4 = new MockBusStop("busStop4");
		person1 = new MockPerson("person1");
		bus.unitTesting = true;
		bus.addBusStop(busStop1, true);
		bus.addBusStop(busStop2, false);
		bus.addBusStop(busStop3, false);
		bus.addBusStop(busStop4, false);
	}	
	
	public void testOnePassenger() {
		
		List<Person> people = new ArrayList<Person>();
		
		assertEquals("Bus's passengers should be empty. It isn't.", 0, bus.passengers.size());
		
		assertEquals("Bus should have an empty event log. Instead, the Bus's event log reads: "
				+ bus.log.toString(), 0, bus.log.size());
		
		assertEquals("BusStop1 should have an empty event log. Instead, the BusStop's event log reads: "
				+ busStop1.log.toString(), 0, busStop1.log.size());
		
		assertEquals("BusStop2 should have an empty event log. Instead, the BusStop's event log reads: "
				+ busStop2.log.toString(), 0, busStop2.log.size());
		
		assertEquals("BusStop3 should have an empty event log. Instead, the BusStop's event log reads: "
				+ busStop3.log.toString(), 0, busStop3.log.size());
		
		assertEquals("BusStop4 should have an empty event log. Instead, the BusStop's event log reads: "
				+ busStop4.log.toString(), 0, busStop4.log.size());
		
		assertEquals("Person1 should have an empty event log. Instead, the Person's event log reads: "
				+ person1.log.toString(), 0, person1.log.size());
		
		bus.msgHereArePassengers(people);
		
		assertTrue("Bus should have logged \"Received msgHereArePassengers\" but didn't. His last event logged reads instead: " 
				+ bus.log.getLastLoggedEvent().toString(), bus.log.containsString("Received msgHereArePassengers"));
		
		assertTrue("Bus's state should be Leaving. It isn't.", bus.getState() == BusState.Leaving);
		
		assertTrue("Bus's scheduler should have returned true (it should call goToNextStop(), but didn't.", bus.pickAndExecuteAnAction());
		
		assertTrue("Bus's state should be AtStop. It isn't.", bus.getState() == BusState.AtStop);
		
		assertTrue("BusStop2 should have logged \"Received msgGetPassengers\" but didn't. His last event logged reads instead: " 
				+ busStop2.log.getLastLoggedEvent().toString(), busStop2.log.containsString("Received msgGetPassengers"));
		
		people.add(person1);
		
		bus.msgHereArePassengers(people);
		
		assertTrue("Bus should have logged \"Received msgHereArePassengers\" but didn't. His last event logged reads instead: " 
				+ bus.log.getLastLoggedEvent().toString(), bus.log.containsString("Received msgHereArePassengers"));
		
		assertEquals("Bus should have one passenger in passengers. It doesn't.", 1, bus.passengers.size());
		
		assertTrue("First passenger in passengers should have state == Waiting. It doesn't.", bus.passengers.get(0).getState() == PassengerState.Waiting);
		
		assertTrue("Bus's scheduler should have returned true (it should call openDoors(), but didn't.", bus.pickAndExecuteAnAction());
		
		assertTrue("Person1 should have logged \"Received msgBusIsHere\" but didn't. His last event logged reads instead: " 
				+ person1.log.getLastLoggedEvent().toString(), person1.log.containsString("Received msgBusIsHere"));
		
		assertTrue("First passenger in passengers should have state == Boarding. It doesn't.", bus.passengers.get(0).getState() == PassengerState.Boarding);
		
		bus.msgComingAboard(person1, "joshRestaurant");
		
		assertTrue("Bus should have logged \"Received msgComingAboard\" but didn't. His last event logged reads instead: " 
				+ bus.log.getLastLoggedEvent().toString(), bus.log.containsString("Received msgComingAboard"));
		
		assertTrue("First passenger in passengers should have state == OnBus. It doesn't.", bus.passengers.get(0).getState() == PassengerState.OnBus);
		
		assertTrue("First passenger in passengers should have destination == joshRestaurant. It doesn't.", bus.passengers.get(0).getDestination().equals("joshRestaurant"));
		
		assertTrue("Bus's state should be Leaving. It isn't.", bus.getState() == BusState.Leaving);
		
		assertTrue("Bus's scheduler should have returned true (it should call goToNextStop(), but didn't.", bus.pickAndExecuteAnAction());
		
		assertTrue("Bus's state should be AtStop. It isn't.", bus.getState() == BusState.AtStop);
		
		assertTrue("BusStop3 should have logged \"Received msgGetPassengers\" but didn't. His last event logged reads instead: " 
				+ busStop3.log.getLastLoggedEvent().toString(), busStop3.log.containsString("Received msgGetPassengers"));
		
		assertTrue("Person1 should have logged \"Received msgAtDestination. Destination = joshRestaurant\" but didn't. His last event logged reads instead: " 
				+ person1.log.getLastLoggedEvent().toString(), person1.log.containsString("Received msgAtDestination. Destination = joshRestaurant"));
		
		assertEquals("Bus's passengers should be empty. It isn't.", 0, bus.passengers.size());
		
		
		
//		busStop.msgWaitingForBus(person1);
//		
//		assertTrue("BusStop should have logged \"Received msgWaitingForBus\" but didn't. His last event logged reads instead: " 
//				+ busStop.log.getLastLoggedEvent().toString(), busStop.log.containsString("Received msgWaitingForBus"));
//		
//		assertEquals("BusStop should have one passenger in waitingPassengers. It doesn't.", 1, busStop.waitingPassengers.size());
//	
//		assertFalse("BusStop's scheduler should have returned false, but didn't.", busStop.pickAndExecuteAnAction());
//		
//		busStop.msgGetPassengers(bus);
//		
//		assertTrue("BusStop should have logged \"Received msgGetPassengers\" but didn't. His last event logged reads instead: " 
//				+ busStop.log.getLastLoggedEvent().toString(), busStop.log.containsString("Received msgGetPassengers"));
//		
//		assertTrue("BusStop's scheduler should have returned true (it should call loadPassengers(), but didn't.", busStop.pickAndExecuteAnAction());
//	
//		assertTrue("Bus should have logged \"Received msgHereArePassengers\" but didn't. His last event logged reads instead: " 
//				+ bus.log.getLastLoggedEvent().toString(), bus.log.containsString("Received msgHereArePassengers"));
//		
//		assertEquals("BusStop's waitingPassengers should be empty. It isn't.", 0, busStop.waitingPassengers.size());
//		
//		assertFalse("BusStop's scheduler should have returned false, but didn't.", busStop.pickAndExecuteAnAction());
	}
}
