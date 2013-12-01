package simcity.test.mock;

import simcity.interfaces.Person;
import simcity.interfaces.Bus;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;
import java.util.*;

public class MockBus extends Mock implements Bus {

	public MockBus(String name) {
		super(name);
	}
	
	public void msgComingAboard(Person person, String destination) {
		log.add(new LoggedEvent("Received msgComingAboard. Destination = " + destination));
	}
	
	public void msgHereArePassengers(List<Person> passengers) {
		log.add(new LoggedEvent("Received msgHereArePassengers"));
	}
	
	public String toString() {
		return "bus " + getName();
	}
}
