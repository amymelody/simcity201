package simcity.test.mock;

import simcity.PersonAgent;
import simcity.interfaces.Bus;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;

public class MockBus extends Mock implements Bus {

	public MockBus(String name) {
		super(name);
	}
	
	public void msgComingAboard(PersonAgent person, String destination) {
		log.add(new LoggedEvent("Received msgComingAboard. Destination = " + destination));
	}
	
	public String toString() {
		return "bus " + getName();
	}
}
