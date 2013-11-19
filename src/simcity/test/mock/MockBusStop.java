package simcity.test.mock;

import simcity.PersonAgent;
import simcity.interfaces.BusStop;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;

public class MockBusStop extends Mock implements BusStop {

	public MockBusStop(String name) {
		super(name);
	}
	
	public void msgWaitingForBus(PersonAgent person) {
		log.add(new LoggedEvent("Received msgWaitingForBus"));
	}
	
	public String toString() {
		return "bus stop " + getName();
	}
}
