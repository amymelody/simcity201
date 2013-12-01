package simcity.interfaces;

import simcity.interfaces.Person;

public interface BusStop {

	public abstract void msgWaitingForBus(Person person);
	
	public abstract void msgGetPassengers(Bus b);

}