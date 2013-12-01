package simcity;

import java.util.*;

import simcity.agent.Agent;
import simcity.interfaces.BusStop;
import simcity.interfaces.Bus;
import simcity.interfaces.Person;
import simcity.mock.LoggedEvent;

public class BusStopAgent extends Agent implements BusStop {

	private String name;
	private Bus bus = null;
	public List<Person> waitingPassengers = Collections.synchronizedList(new ArrayList<Person>());

	public BusStopAgent(String name) {
		super();
		this.name = name;
	}

	//Messages

	public void msgWaitingForBus(Person p) {
		log.add(new LoggedEvent("Received msgWaitingForBus"));
		waitingPassengers.add(p);
		stateChanged();
	}

	public void msgGetPassengers(Bus b) {
		log.add(new LoggedEvent("Received msgGetPassengers"));
		bus = b;
		stateChanged();
	}


	//Scheduler

	public boolean pickAndExecuteAnAction() {
		if (bus != null) {
			loadPassengers();
			return true;
		}
		return false;
	}


	//Actions

	public void loadPassengers() {
		bus.msgHereArePassengers(waitingPassengers);
		waitingPassengers.clear();
		bus = null;
	}
}
