package simcity.test.mock;

import simcity.interfaces.BusStop;
import simcity.interfaces.Person;
import simcity.interfaces.Bus;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;

public class MockBusStop extends Mock implements BusStop {

	public MockBusStop(String name) {
		super(name);
	}
	
	public void msgWaitingForBus(Person person) {
		log.add(new LoggedEvent("Received msgWaitingForBus"));
	}
	
	public void msgGetPassengers(Bus b) {
		log.add(new LoggedEvent("Received msgGetPassengers"));
	}
	
	public String toString() {
		return "bus stop " + getName();
	}
}
