package simcity.test;

import simcity.BusStopAgent;
import simcity.test.mock.MockBus;
import simcity.test.mock.MockPerson;
import junit.framework.*;

import java.util.*;

public class BusStopTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	BusStopAgent busStop;
	MockBus bus;
	MockPerson person1;
	
	public void setUp() throws Exception{
		super.setUp();	
		busStop = new BusStopAgent("busStop");
		bus  = new MockBus("bus");
		person1 = new MockPerson("person1");
	}	
	
	public void testOnePassenger() {
		assertEquals("BusStop's waitingPassengers should be empty. It isn't.", 0, busStop.waitingPassengers.size());
		
		assertEquals("BusStop should have an empty event log. Instead, the BusStop's event log reads: "
				+ busStop.log.toString(), 0, busStop.log.size());
		
		assertEquals("Bus should have an empty event log. Instead, the Bus's event log reads: "
				+ bus.log.toString(), 0, bus.log.size());
		
		busStop.msgWaitingForBus(person1);
		
		assertTrue("BusStop should have logged \"Received msgWaitingForBus\" but didn't. Its last event logged reads instead: " 
				+ busStop.log.getLastLoggedEvent().toString(), busStop.log.containsString("Received msgWaitingForBus"));
		
		assertEquals("BusStop should have one passenger in waitingPassengers. It doesn't.", 1, busStop.waitingPassengers.size());
	
		assertFalse("BusStop's scheduler should have returned false, but didn't.", busStop.pickAndExecuteAnAction());
		
		busStop.msgGetPassengers(bus);
		
		assertTrue("BusStop should have logged \"Received msgGetPassengers\" but didn't. Its last event logged reads instead: " 
				+ busStop.log.getLastLoggedEvent().toString(), busStop.log.containsString("Received msgGetPassengers"));
		
		assertTrue("BusStop's scheduler should have returned true (it should call loadPassengers(), but didn't.", busStop.pickAndExecuteAnAction());
	
		assertTrue("Bus should have logged \"Received msgHereArePassengers\" but didn't. Its last event logged reads instead: " 
				+ bus.log.getLastLoggedEvent().toString(), bus.log.containsString("Received msgHereArePassengers"));
		
		assertEquals("BusStop's waitingPassengers should be empty. It isn't.", 0, busStop.waitingPassengers.size());
		
		assertFalse("BusStop's scheduler should have returned false, but didn't.", busStop.pickAndExecuteAnAction());
	}
}
