package simcity.test.mock;

import simcity.PersonAgent;
import simcity.interfaces.Car;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;

public class MockCar extends Mock implements Car {

	public MockCar(String name) {
		super(name);
	}
	
	public void msgGoToDestination(PersonAgent person, String destination) {
		log.add(new LoggedEvent("Received msgGoToDestination. Destination = " + destination));
	}
	
	public String toString() {
		return "car " + getName();
	}
}
