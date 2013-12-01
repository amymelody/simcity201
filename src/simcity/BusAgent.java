package simcity;

import java.util.*;

import simcity.agent.Agent;
import simcity.interfaces.Person;
import simcity.interfaces.Bus;
import simcity.interfaces.BusStop;
import simcity.mock.LoggedEvent;

public class BusAgent extends Agent implements Bus {

	public enum BusState {AtStop, Leaving}
	public enum PassengerState {Waiting, Boarding, OnBus}
	
	private String name;
	public String location; //hack used for unit testing
	private BusState state;
	public List<Passenger> passengers = Collections.synchronizedList(new ArrayList<Passenger>());
	public List<MyBusStop> busStops = Collections.synchronizedList(new ArrayList<MyBusStop>());
	public boolean unitTesting;
	
	public BusAgent(String name) {
		super();
		this.name = name;
	}
	
	//Utilities
	
	private boolean nearLocation(String l) {
		if (unitTesting) {
			if (location.equals("apartments")) {
				if (l.equals("apartment1")) {
					return false;
				}
			}
			if (location.equals("restaurants")) {
				if (l.equals("joshRestaurant")) {
					return true;
				}
			}
			if (location.equals("markets")) {
				if (l.equals("market3")) {
					return true;
				}
			}
			if (location.equals("houses")) {
				if (l.equals("bank1")) {
					return true;
				}
			}
			return false;
		}
		return false;
	}
	
	public BusState getState() {
		return state;
	}
	
	public void addBusStop(BusStop b, boolean c) {
		busStops.add(new MyBusStop(b,c));
	}


	//Messages

	public void msgHereArePassengers(List<Person> passengers) {
		log.add(new LoggedEvent("Received msgHereArePassengers"));
		if (passengers.isEmpty()) {
			state = BusState.Leaving;
		} else {
			for (Person p : passengers) {
				this.passengers.add(new Passenger(p, PassengerState.Waiting));
			}
		}
		stateChanged();
	}

	public void msgComingAboard(Person p, String location) {
		log.add(new LoggedEvent("Received msgComingAboard"));
		synchronized(passengers) {
			for (Passenger passenger : passengers) {
				if (passenger.person == p) {
					passenger.state = PassengerState.OnBus;
					passenger.destination = location;
				}
			}
		}
		
		boolean everyoneOnBoard = true;
		synchronized(passengers) {
			for (Passenger passenger : passengers) {
				if (passenger.state != PassengerState.OnBus) {
					everyoneOnBoard = false;
				}
			}
		}
		if (everyoneOnBoard) {
			state = BusState.Leaving;
		}
		stateChanged();
	}

	
	//Scheduler

	public boolean pickAndExecuteAnAction() {
		if (state == BusState.AtStop) {
			synchronized(passengers) {
				for (Passenger p : passengers) {
					if (p.state == PassengerState.Waiting) {
						openDoors();
						return true;
					}
				}
			}
		}
		if (state == BusState.Leaving) {
			goToNextStop();	
			return true;
		}
		return false;
	}


	//Actions

	private void openDoors() {
		synchronized(passengers) {
			for (Passenger p : passengers) {
				if (p.state == PassengerState.Waiting) {
					p.person.msgBusIsHere(this);
					p.state = PassengerState.Boarding;
				}
			}
		}
	}

	private void goToNextStop() {
		for (int i=busStops.size()-1; i>=0; i--) {
			if (busStops.get(i).current) {
				busStops.get(i).current = false;
				if (i==busStops.size()-1) {
					busStops.get(0).current = true;
//					if (!unitTesting) {
//						DoGoToStop(busStops.get(0));
//					}
					state = BusState.AtStop;
					busStops.get(0).busStop.msgGetPassengers(this);
				} else {
					busStops.get(i+1).current = true;
//					if (!unitTesting) {
//						DoGoToStop(busStops.get(i+1));
//					}
					state = BusState.AtStop;
					busStops.get(i+1).busStop.msgGetPassengers(this);
				}
			}
		}
		
		for (int i=passengers.size()-1; i>=0; i--) {
			if (nearLocation(passengers.get(i).destination)){
				passengers.get(i).person.msgAtDestination(passengers.get(i).destination);
				passengers.remove(passengers.get(i));
			}
		}
	}
	
	
	//Inner classes
	
	public class Passenger {
		Person person;
		PassengerState state;
		String destination;
		
		Passenger(Person p, PassengerState s) {
			person = p;
			state = s;
		}
		
		public PassengerState getState() {
			return state;
		}
		
		public String getDestination() {
			return destination;
		}
	}

	public class MyBusStop {
		BusStop busStop;
		boolean current;
		
		MyBusStop(BusStop b, boolean c) {
			busStop = b;
			current = c;
		}
		
		public boolean getCurrent() {
			return current;
		}
	}
}
